package com.example.myapplication.ui.main.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.filgthhublibrary.network.bean.ResDrones;
import com.example.filgthhublibrary.network.bean.ResOnLineDrone;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnLineDroneAdapter extends RecyclerView.Adapter<OnLineDroneAdapter.ViewHolder> {

    private Context context;
    private List<ResOnLineDrone.DroneOnlineModel> list = new ArrayList<>();
    private OnLiveItemClick onLiveItemClick;

    public OnLineDroneAdapter(Context context, List<ResOnLineDrone.DroneOnlineModel> data, OnLiveItemClick onLiveItemClick){
        this.context = context;
        this.list = data;
        this.onLiveItemClick = onLiveItemClick;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ResOnLineDrone.DroneOnlineModel item = list.get(position);
        if (item.getName()!=null&&!"".equals(item.getName())&&!"null".equals(item.getName())){
            holder.name.setText("飞机名称: "+item.getName());
        }else{
            holder.name.setText("飞机名称: ");
        }

        holder.type.setText("飞机类型: "+item.getType());
        holder.sn.setText("飞机编码: "+item.getSn());
        holder.team.setText("所在分组: "+item.getTeamId()+"");
        holder.user.setText("操作人员: "+item.getAccount());
        if (item.getLiveStatus()==1){
            holder.live_status.setText("直播状态: 直播中");
        }else{
            holder.live_status.setText("直播状态: 未直播");
        }
        holder.live_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLiveItemClick.onLiveItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView type;
        private TextView sn;
        private TextView team;
        private TextView user;
        private TextView live_status;

        private LinearLayout live_item_ll;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.drone_name);
            type = itemView.findViewById(R.id.drone_type);
            sn = itemView.findViewById(R.id.drone_sn);
            team = itemView.findViewById(R.id.drone_team);
            live_item_ll = itemView.findViewById(R.id.live_item_ll);
            user = itemView.findViewById(R.id.tv_user);
            live_status = itemView.findViewById(R.id.live_status);



        }
    }
}

