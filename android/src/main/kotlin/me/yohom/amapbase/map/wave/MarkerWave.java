package me.yohom.amapbase.map.wave;

import android.animation.ValueAnimator;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MarkerWave {
    public MarkerWave() {

    }

    private List<Circle> circleList;//圆集合

    private ValueAnimator valueAnimator;//动画工具

    private int fillColor;
    private int strokeColor;

    /**
     * 添加水波纹效果
     *
     * @param latLng 要展示扩散效果的点经纬度
     *               AMap aMap：高德地图
     *               fillColor: #ffffff
     */
    public void addWaveAnimation(LatLng latLng, AMap aMap, String fill, String stroke) {
        this.fillColor = Color.parseColor(fill);
        this.strokeColor = Color.parseColor(stroke);

        circleList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int radius = 50 * (i + 1);
            circleList.add(addCircle(latLng, radius, aMap, fill, stroke));
        }
        valueAnimator = AnimatorUtil.getValueAnimator(0, 50, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                for (int i = 0; i < circleList.size(); i++) {
                    int nowradius = 50 + 50 * i;
                    Circle circle = circleList.get(i);
                    double radius1 = value + nowradius;
                    circle.setRadius(radius1);
                    int strokePercent;
                    int fillPercent;
                    if (value < 25) {
                        strokePercent = value * 8;
                        fillPercent = value * 20 / 50;
                    } else {
                        strokePercent = 200 - value * 4;
                        fillPercent = 20 - value * 20 / 50;
                    }
                    circle.setStrokeColor(setArgb(strokePercent, strokeColor));
                    circle.setFillColor(setArgb(fillPercent, fillColor));
                }
            }
        });
    }

    public int setArgb(int alpha, int color) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * 移除水波纹动画
     */
    public void removeCircleWave() {
        if (null != valueAnimator) {
            valueAnimator.cancel();
        }
        if (circleList != null) {
            for (Circle circle : circleList) {
                circle.remove();
            }
            circleList.clear();
        }
    }

    public Circle addCircle(LatLng latlng, double radius, AMap aMap, String fillColor, String strokeColor) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(Color.parseColor(fillColor));
        options.strokeColor(Color.parseColor(strokeColor));
        options.center(latlng);
        options.radius(radius);
        return aMap.addCircle(options);
    }
}
