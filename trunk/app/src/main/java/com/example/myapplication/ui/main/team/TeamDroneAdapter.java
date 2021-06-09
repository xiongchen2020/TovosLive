package com.example.myapplication.ui.main.team;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.filgthhublibrary.network.bean.ResDrones;
import com.example.filgthhublibrary.network.bean.ResTeamMember;
import com.example.myapplication.R;
import com.example.myapplication.ui.main.record.flightrecord.OnRecordItemClick;
import com.example.myapplication.ui.recycleadapter.BaseViewHolder;
import com.example.myapplication.ui.recycleadapter.XMBaseAdapter;

import androidx.annotation.LayoutRes;

public class TeamDroneAdapter  extends XMBaseAdapter<ResDrones.DroneGetModel> {


    private OnRecordItemClick onRecordItemClick;

    public TeamDroneAdapter(Context context, OnRecordItemClick onRecordItemClick) {
        super(context);
        this.onRecordItemClick = onRecordItemClick;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_team_drone_layout);
    }

    private class ViewHolder extends BaseViewHolder<ResDrones.DroneGetModel> {
        private TextView drone_type;
        private TextView drone_name;
        private TextView drone_sn;


        ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            drone_type = itemView.findViewById(R.id.drone_type);
            drone_name = itemView.findViewById(R.id.drone_name);
            drone_sn = itemView.findViewById(R.id.drone_sn);

        }


        @Override
        public void setData(ResDrones.DroneGetModel data) {
            drone_type.setText("飞机类别: " + data.getDroneType());
            if (data.getName()!=null&&!"".equals(data.getName())&&!"null".equals(data.getName())){
                drone_name.setText("飞机名称: "+data.getName());
            }else{
                drone_name.setText("飞机名称: ");
            }
            drone_sn.setText("飞机编码: " + data.getSn());

        }
    }
}
