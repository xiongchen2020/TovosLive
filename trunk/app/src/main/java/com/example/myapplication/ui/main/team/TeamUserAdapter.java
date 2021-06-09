package com.example.myapplication.ui.main.team;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.commonlib.utils.CustomNumberUtils;
import com.example.filgthhublibrary.network.bean.ResFlightRecords;
import com.example.filgthhublibrary.network.bean.ResTeamMember;
import com.example.myapplication.R;
import com.example.myapplication.ui.main.record.flightrecord.FlightRecordAdapter;
import com.example.myapplication.ui.main.record.flightrecord.OnRecordItemClick;
import com.example.myapplication.ui.recycleadapter.BaseViewHolder;
import com.example.myapplication.ui.recycleadapter.XMBaseAdapter;

import androidx.annotation.LayoutRes;

public class TeamUserAdapter extends XMBaseAdapter<ResTeamMember> {


    private OnRecordItemClick onRecordItemClick;

    public TeamUserAdapter(Context context, OnRecordItemClick onRecordItemClick) {
        super(context);
        this.onRecordItemClick = onRecordItemClick;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_team_user_layout);
    }

    private class ViewHolder extends BaseViewHolder<ResTeamMember> {
        private TextView user_name;
        private TextView user_account;
        private TextView user_role;


        ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            user_name = itemView.findViewById(R.id.user_name);
            user_account = itemView.findViewById(R.id.user_account);
            user_role = itemView.findViewById(R.id.user_role);

        }


        @Override
        public void setData(ResTeamMember data) {
            user_name.setText("昵称: "+data.getName());
            user_account.setText("账户: "+data.getAccount());
//            if (data.getRole()==0){
//                user_role.setText("角色: "+"管理员");
//            }else if (data.getRole()==1){
//                user_role.setText("角色: "+"队长");
//            }else if (data.getRole()==2){
//                user_role.setText("角色: "+"飞手");
//            }


        }
    }
}