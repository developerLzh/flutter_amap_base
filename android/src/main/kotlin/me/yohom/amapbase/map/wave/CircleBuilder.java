package me.yohom.amapbase.map.wave;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;

public class CircleBuilder {

    public static Circle addCircle(LatLng latlng, double radius, AMap aMap, String fillColor, String strokeColor) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(Color.parseColor(fillColor));
        options.strokeColor(Color.parseColor(strokeColor));
        options.center(latlng);
        options.radius(radius);
        return aMap.addCircle(options);
    }

}
