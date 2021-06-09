package com.example.myapplication.ui.main.record.flightrecord;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.commonlib.utils.CustomNumberUtils;
import com.example.filgthhublibrary.network.bean.ResFlightRecords;
import com.example.myapplication.R;
import com.example.myapplication.ui.recycleadapter.BaseViewHolder;
import com.example.myapplication.ui.recycleadapter.XMBaseAdapter;

import androidx.annotation.LayoutRes;

public class FlightRecordAdapter extends XMBaseAdapter<ResFlightRecords.FlightGetModel> {


    private OnRecordItemClick onRecordItemClick;

    public FlightRecordAdapter(Context context,OnRecordItemClick onRecordItemClick) {
        super(context);
        this.onRecordItemClick = onRecordItemClick;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_record_layout);
    }

    private class ViewHolder extends BaseViewHolder<ResFlightRecords.FlightGetModel> {
        private TextView tv_team;
        private TextView tv_user;
        private TextView tv_type;
        private TextView tv_sn;
        private TextView tv_time;
        private TextView tv_height;
        private TextView tv_flight_time;
        private TextView tv_flight_distance;
        private TextView tv_flight_lng_lat;
        private LinearLayout record_rl;


        ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            tv_team = itemView.findViewById(R.id.tv_team);
            tv_user = itemView.findViewById(R.id.tv_user);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_sn = itemView.findViewById(R.id.tv_sn);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_height = itemView.findViewById(R.id.tv_height);
            tv_flight_time = itemView.findViewById(R.id.tv_flight_time);
            tv_flight_distance = itemView.findViewById(R.id.tv_flight_distance);
            tv_flight_lng_lat = itemView.findViewById(R.id.tv_flight_lng_lat);
            record_rl = itemView.findViewById(R.id.record_rl);


        }


        @Override
        public void setData(ResFlightRecords.FlightGetModel data) {
            tv_team.setText("团队名称: " + data.getTeamId());
            tv_user.setText("操作人员: " + data.getAccount());
            tv_type.setText("设备类型: " + data.getDroneType());
            tv_sn.setText("飞机编码: " + data.getDroneSN() + "");
            tv_time.setText("起飞时间: " + (int)Math.floor(data.getDuration()/60)+ "分钟"+(int)data.getDuration()%60+"秒");
            tv_height.setText("最大高度: " + data.getMaxHeight() + "米");
            tv_flight_time.setText("飞行时长: " + (int)Math.floor(data.getDuration()/60)+ "分钟"+(int)data.getDuration()%60+"秒");
            tv_flight_distance.setText("飞行距离: " + CustomNumberUtils.format4(data.getDistance()) + "米");
            tv_flight_lng_lat.setText("起飞地址: "+data.getTakeoffLatitude()+","+data.getTakeoffLongitude());
            record_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecordItemClick.onRecordItemClick(data);
                }
            });
        }
    }
}


