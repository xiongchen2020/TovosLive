package com.example.myapplication.ui.main;

import android.os.Bundle;


import com.example.filgthhublibrary.flighthub.CustomFlightHubManager2;
import com.example.myapplication.MApplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    protected CustomFlightHubManager2 customFlightHubManager;
    MainActivity mainActivity;
    protected int teamId;
    @Override
    public void onRefresh() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customFlightHubManager = CustomFlightHubManager2.getInstance();
        mainActivity = (MainActivity)getActivity();

    }
    public void setLayoutView(RecyclerView recyclerView){
        if (MApplication.isTabletDevice(getContext())) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    public  int  getTeamId(){
        if (mainActivity.getNowTeamGetModel()!=null){
            teamId = mainActivity.getNowTeamGetModel().getId();
        }else{
            teamId = 1;
        }
        return teamId;
    }
}
