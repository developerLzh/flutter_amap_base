package me.yohom.amapbase.map.marker;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.List;

public class MySmoothMarker {

    private MoveUtils moveUtils;

    private Marker marker;

    private AMap aMap;

    public MySmoothMarker(AMap aMap, final Marker marker) {
        this.aMap = aMap;
        moveUtils = new MoveUtils();

        this.marker = marker;

        moveUtils.setCallBack(new MoveUtils.OnCallBack() {
            @Override
            public void onSetLatLng(LatLng latLng, float rotate) {
                if (!marker.isRemoved()) {
                    marker.setPosition(latLng);
                    //车辆方向
//                    float carDirection = 360.0F - rotate + getAMap().getCameraPosition().bearing;
//                    marker.setRotateAngle(carDirection);
                }


            }
        });
    }


    public void startMove(LatLng latLng, int time, boolean isContinue) {
        moveUtils.startMove(latLng, time, isContinue);

    }

    public void startMove(List<LatLng> list, int time, boolean isContinue) {
        moveUtils.startMove(list, time, isContinue);

    }


    public void stop() {
        moveUtils.stop();

    }


    public void destory() {
        moveUtils.destory();
        if (null != marker) {
            marker.remove();

        }
    }

    public Marker getMarker() {
        return marker;
    }

    public AMap getAMap() {
        return aMap;
    }

}
