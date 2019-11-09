package me.yohom.amapbase.map.wave;

import android.animation.ValueAnimator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MarkerWave {
    public MarkerWave() {

    }

    private List<Circle> circleList;//圆集合

    private ValueAnimator valueAnimator;//动画工具

    /**
     * 添加水波纹效果
     *
     * @param latLng 要展示扩散效果的点经纬度
     *               AMap aMap：高德地图
     *               fillColor: #ffffff
     */
    public void addWaveAnimation(LatLng latLng, AMap aMap, String fillColor,String strokeColor) {
        circleList = new ArrayList<>();
        int radius = 0;
        for (int i = 0; i < 5; i++) {
            radius = radius + 50 * i;
            circleList.add(CircleBuilder.addCircle(latLng, radius, aMap, fillColor,strokeColor));
        }
        valueAnimator = AnimatorUtil.getValueAnimator(0, 50, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Collections.sort(circleList, new Comparator<Circle>() {
                    @Override
                    public int compare(Circle o1, Circle o2) {
                        if (o1.getRadius() < o2.getRadius()) {
                            return -1;
                        } else if (o1.getRadius() > o2.getRadius()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                for (int i = 0; i < circleList.size(); i++) {
                    Circle circle = circleList.get(i);
                    int nowradius = (int) circle.getRadius();
                    double radius1 = nowradius + 1;
                    if (radius1 >= 250) {
                        radius1 = 0;
                    }
                    circle.setRadius(radius1);
                }
            }
        });
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
}
