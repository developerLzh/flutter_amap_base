package me.yohom.amapbase.map.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

import me.yohom.amapbase.R;
import me.yohom.amapbase.common.LogExKt;

import static me.yohom.amapbase.common.LogExKt.log;

/**
 * Created by developerLzh on 2017/12/25 0025.
 * 司机剩余时长、距离infoWindow
 */
public class WaitAcceptAdapter implements AMap.InfoWindowAdapter {

    private Context context;
//    private Setting setting;

    public WaitAcceptAdapter(Context context) {
        this.context = context;
//        setting = Setting.findOne();
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        String title = marker.getTitle();//等待时长

        View view = LayoutInflater.from(context).inflate(R.layout.zhuanche_wait_accept_info_window, null, false);

        TextView wait_accept_time = view.findViewById(R.id.wait_accept_time);
        wait_accept_time.setText(title);

        return view;
    }
}
