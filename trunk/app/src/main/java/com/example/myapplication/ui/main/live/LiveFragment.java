package com.example.myapplication.ui.main.live;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.commonlib.utils.LogUtil;

import com.example.filgthhublibrary.listener.DroneOnlineListener;
import com.example.filgthhublibrary.listener.StreanDronesListener;
import com.example.filgthhublibrary.network.bean.ResDrones;
import com.example.filgthhublibrary.network.bean.ResOnLineDrone;
import com.example.myapplication.MApplication;
import com.example.myapplication.R;
import com.example.myapplication.ui.liveplay.LiveViewActivity;
import com.example.myapplication.ui.main.BaseFragment;
import com.example.myapplication.ui.main.MainActivity;
import com.example.myapplication.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class LiveFragment extends BaseFragment implements OnLiveItemClick, DroneOnlineListener {

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected EmptyRecyclerView recyclerView;
    private List<ResOnLineDrone.DroneOnlineModel> list = new ArrayList<>();
    private OnLineDroneAdapter onLineDroneAdapter;
    private TextView nodata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_list_layout, container, false);
        Toolbar toolbar = ((MainActivity) getActivity()).initToolbar("在线飞机", "live");//findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.light_toobar_bg));
        setHasOptionsMenu(true);
        swipeRefreshLayout = view.findViewById(R.id.main_refresh);
        recyclerView = view.findViewById(R.id.live_list_view);
        swipeRefreshLayout.setOnRefreshListener(this);
        nodata = view.findViewById(R.id.nodata);
        onLineDroneAdapter = new OnLineDroneAdapter(getActivity(), list, this);
        setLayoutView(recyclerView);
//        if (MApplication.isTabletDevice(getContext())) {
//            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
//        } else {
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        }

        recyclerView.setAdapter(onLineDroneAdapter);
        customFlightHubManager.setDroneOnlineListener(this);
        swipeRefreshLayout.setRefreshing(true);
        customFlightHubManager.onLineDrones();
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onRefresh() {
        customFlightHubManager.onLineDrones();
    }




    @Override
    public void getOnLineDroneList(ResOnLineDrone resOnLineDrone) {
        // LogUtil.d("online",resOnLineDrone.toString());
        swipeRefreshLayout.setRefreshing(false);
        list.clear();
        list.addAll(resOnLineDrone.getList());
        LogUtil.d("tovos live stream baseResponse:" + resOnLineDrone.getList());
        onLineDroneAdapter.notifyDataSetChanged();

        if (list.size() > 0) {
            nodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            nodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void getOnLineDroneListThrowable(Throwable throwable) {
        onLineDroneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLiveItemClick(ResOnLineDrone.DroneOnlineModel item) {
        if (item.getLiveStatus()==1){
            Intent intent = new Intent(getActivity(), LiveViewActivity.class);
            intent.putExtra("sn", item.getSn());
            startActivity(intent);
        }

    }
}
