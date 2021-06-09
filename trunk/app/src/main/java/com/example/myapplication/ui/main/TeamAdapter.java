package com.example.myapplication.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.filgthhublibrary.flighthub.CustomFlightHubManager2;
import com.example.filgthhublibrary.network.bean.TeamGetModel;
import com.example.filgthhublibrary.view.listener.OnTeamItemClickListener;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyTVHolder>   {

    private LayoutInflater mLayoutInflater;
//    private Context mContext;
    private List<TeamGetModel>  mData = new ArrayList<>();
    private OnTeamItemClickListener onRecyclerItemClickListener;
//    private boolean checkModel = false;
//    private boolean is_all_selected = false;
    private CustomFlightHubManager2 customFlightHubManager;
    public TeamAdapter(Context context, List<TeamGetModel> mData, OnTeamItemClickListener onRecyclerItemClickListener) {
        mLayoutInflater = LayoutInflater.from(context);
//        mContext = context;
        this.mData = mData;
        customFlightHubManager = CustomFlightHubManager2.getInstance();
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    @Override
    public TeamAdapter.MyTVHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        return new TeamAdapter.MyTVHolder(mLayoutInflater.inflate(R.layout.item_team_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final TeamAdapter.MyTVHolder holder, final int pos)  {
         holder.team_name.setText(mData.get(pos).getTeamName());
         customFlightHubManager.teamMembers(mData.get(pos).getId());
         holder.ll.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 if(holder.ll_detail.getVisibility() == View.VISIBLE){
//                     holder.ll_detail.setVisibility(View.GONE);
//                     holder.iv_selector.setSelected(false);
//                 }else {
//                     holder.ll_detail.setVisibility(View.VISIBLE);
//                     holder.iv_selector.setSelected(true);
//                 }

                 onRecyclerItemClickListener.onTaskItemClick(pos);
             }
         });

    }



    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }



    class MyTVHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        TextView team_name;
        ImageView iv_selector;
        MyTVHolder(View itemView) {
            super(itemView);
            team_name = (TextView) itemView.findViewById(R.id.team_name);
            //ll_detail = itemView.findViewById(R.id.ll_detail);
            iv_selector = itemView.findViewById(R.id.iv_selector);
            ll= itemView.findViewById(R.id.ll);
        }
    }

}

