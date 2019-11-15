package me.yohom.amapbase.map.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

import me.yohom.amapbase.R;

/**
 * Created by developerLzh on 2017/12/25 0025.
 * 司机剩余时长、距离infoWindow
 */
public class DisTimeWindowAdapter implements AMap.InfoWindowAdapter {

    private Context context;
    //    private Setting setting;
    private String des;

    public DisTimeWindowAdapter(Context context, String des) {
        this.context = context;
        this.des = des;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        String leftDis = marker.getSnippet();//剩余时长
        String leftTime = marker.getTitle();//剩余时间

        View view = LayoutInflater.from(context).inflate(R.layout.zhuanche_left_info_window, null, false);

        TextView left_dis = view.findViewById(R.id.left_dis);
        TextView left_time = view.findViewById(R.id.left_time);
        TextView desText = view.findViewById(R.id.des);

        left_dis.setText(leftDis);
        left_time.setText(leftTime);
        desText.setText(des);

        return view;
    }
}
