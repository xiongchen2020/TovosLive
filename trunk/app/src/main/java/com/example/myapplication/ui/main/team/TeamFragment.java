package com.example.myapplication.ui.main.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.filgthhublibrary.listener.DroneListener;
import com.example.filgthhublibrary.listener.HubLoginListener;
import com.example.filgthhublibrary.network.bean.ResDrones;
import com.example.filgthhublibrary.network.bean.ResTeamMember;
import com.example.filgthhublibrary.network.bean.ResUserInfo;
import com.example.filgthhublibrary.network.bean.TeamGetModel;
import com.example.filgthhublibrary.view.listener.OnTeamItemClickListener;
import com.example.myapplication.R;
import com.example.myapplication.ui.main.BaseFragment;
import com.example.myapplication.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamFragment extends BaseFragment implements OnTeamItemClickListener, HubLoginListener, DroneListener {

   // protected SwipeRefreshLayout swipeRefreshLayout;

  //  protected RecyclerView team_user_view;
    @BindView(R.id.team_user_view)
    RecyclerView teamUserView;
    @BindView(R.id.team_drone_view)
    RecyclerView teamDroneView;
    @BindView(R.id.team_refresh)
    SwipeRefreshLayout teamRefresh;
    private TeamUserAdapter teamUserAdapter;
    private List<TeamGetModel> mData = new ArrayList<>();
   // private RecyclerView team_drone_view;
    private TeamDroneAdapter teamDroneAdapter;
    private List<ResDrones.DroneGetModel> droneGetModelList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_layout, container, false);
        ButterKnife.bind(this,view);
        Toolbar toolbar = ((MainActivity) getActivity()).initToolbar("团队管理", "团队成员");//findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.light_toobar_bg));
        setHasOptionsMenu(true);
       // teamRefresh = view.findViewById(R.id.team_refresh);

       // teamUserView = view.findViewById(R.id.team_user_view);
       // teamDroneView = view.findViewById(R.id.team_drone_view);
        teamUserView.setVisibility(View.VISIBLE);
        teamDroneView.setVisibility(View.GONE);

        initView();

        return view;
    }

    private void initView() {

        teamRefresh.setOnRefreshListener(this);
        customFlightHubManager.setLoginListener(this);
        customFlightHubManager.setDroneListener(this);
        teamUserAdapter = new TeamUserAdapter(getContext(), null);
        setLayoutView(teamUserView);
        //  team_user_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        teamUserView.setAdapter(teamUserAdapter);

        teamDroneAdapter = new TeamDroneAdapter(getContext(), null);
        setLayoutView(teamDroneView);
        //  team_drone_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        teamDroneView.setAdapter(teamDroneAdapter);


        teamRefresh.setRefreshing(true);
        customFlightHubManager.drones();
        customFlightHubManager.teamMembers(getTeamId());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_team_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //当点击一个条目时，不显示另外一个
            case R.id.menu_user:
                teamUserView.setVisibility(View.VISIBLE);
                teamDroneView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).initToolbar("团队管理", "团队成员");//findViewById(R.id.toolbar);
                break;
            case R.id.menu_drone:
                teamUserView.setVisibility(View.GONE);
                teamDroneView.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).initToolbar("团队管理", "团队设备");//findViewById(R.id.toolbar);
                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        customFlightHubManager.drones();
        customFlightHubManager.teamMembers(getTeamId());
    }

    @Override
    public void onTaskItemClick(int i) {
        mData.get(i).getId();

    }

    @Override
    public void onTaskItemLongClick(int i) {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFail() {

    }

    @Override
    public void setAllUser(List<ResUserInfo> list) {

    }

    @Override
    public void setAllUserThrowable(Throwable throwable) {

    }

    @Override
    public void getTeamMember(List<ResTeamMember> list) {
        teamRefresh.setRefreshing(false);
        teamUserAdapter.setData(list);
        teamUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void getTeamMembersThrowable(Throwable throwable) {
        teamUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDroneList(ResDrones resDrones) {
        teamRefresh.setRefreshing(false);
        droneGetModelList.clear();
        for (int i = 0; i < resDrones.getDroneGetModels().size(); i++) {
            if (getTeamId() == resDrones.getDroneGetModels().get(i).getTeamId()) {
                droneGetModelList.add(resDrones.getDroneGetModels().get(i));
            }
        }
        teamDroneAdapter.setData(droneGetModelList);
        teamDroneAdapter.notifyDataSetChanged();
    }


}
