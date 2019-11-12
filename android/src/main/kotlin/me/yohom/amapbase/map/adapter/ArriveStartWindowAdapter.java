package me.yohom.amapbase.map.adapter;

import android.content.Context;
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
public class ArriveStartWindowAdapter implements AMap.InfoWindowAdapter {

    private Context context;

    public ArriveStartWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        String leftTime = marker.getTitle();//剩余时间

        View view = LayoutInflater.from(context).inflate(R.layout.zhuanche_arrive_start_wait_info_window, null, false);

        TextView count_down = view.findViewById(R.id.count_down);
        TextView des = view.findViewById(R.id.des);
        count_down.setText(leftTime);
        if (leftTime.equals("00:00")) {
            des.setText("等候已超时");
        } else {
            des.setText("等候倒计时");
        }

        return view;
    }
}
