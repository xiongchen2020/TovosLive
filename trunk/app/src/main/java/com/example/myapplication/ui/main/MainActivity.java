package com.example.myapplication.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.commonlib.utils.LogUtil;
import com.example.commonlib.utils.ToastUtils;
import com.example.filgthhublibrary.flighthub.CustomFlightHubManager2;
import com.example.filgthhublibrary.listener.HubTeamListener;
import com.example.filgthhublibrary.network.bean.TeamGetModel;
import com.example.filgthhublibrary.view.listener.OnTeamItemClickListener;
import com.example.myapplication.R;
import com.example.myapplication.listener.TeamTextViewListener;
import com.example.myapplication.ui.main.live.LiveFragment;
import com.example.myapplication.ui.main.record.RecordFragment;
import com.example.myapplication.ui.main.set.SetFragment;
import com.example.myapplication.ui.main.team.TeamFragment;
import com.example.myapplication.view.TeamTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnTeamItemClickListener,
        HubTeamListener, SwipeRefreshLayout.OnRefreshListener, TeamTextViewListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.live_list_rb)
    RadioButton liveListRb;
    @BindView(R.id.data_all_rb)
    RadioButton dataAllRb;
    @BindView(R.id.team_rb)
    RadioButton teamRb;
    @BindView(R.id.rt_set)
    RadioButton rtSet;
    @BindView(R.id.hub_rg)
    RadioGroup hubRg;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.teamtitle)
    LinearLayout teamtitle;
    @BindView(R.id.horizontalScrollView)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.team_list_view)
    RecyclerView teamListView;
    @BindView(R.id.team_refresh)
    SwipeRefreshLayout teamRefresh;
    @BindView(R.id.activity_na)
    DrawerLayout activityNa;


    private long exitTime = 0;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private List<TeamGetModel> mData = new ArrayList<>();
    private TeamAdapter teamAdapter;
    private CustomFlightHubManager2 customFlightHubManager;
    private ActionBarDrawerToggle drawerToggle;
    private TeamGetModel nowTeamGetModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);
        customFlightHubManager = CustomFlightHubManager2.getInstance();
        customFlightHubManager.setHubTeamListener(this);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, new LiveFragment());
        transaction.commit();

        ((RadioButton) findViewById(R.id.live_list_rb)).setChecked(true);
        teamtitle = findViewById(R.id.teamtitle);
        hubRg = findViewById(R.id.hub_rg);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                activityNa.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        toolbar = findViewById(R.id.toolbar);

        //RadioGroup的check事件，来实现Fragment的切换
        hubRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //根据RadioButton不同的Id来选中不同的Fragment。
                if (checkedId == R.id.live_list_rb) {

                    transaction.replace(R.id.fl_content, new LiveFragment());
                } else if (checkedId == R.id.data_all_rb) {
                    transaction.replace(R.id.fl_content, new RecordFragment());
                } else if (checkedId == R.id.team_rb) {
                    transaction.replace(R.id.fl_content, new TeamFragment());
                } else if (checkedId == R.id.rt_set) {
                    transaction.replace(R.id.fl_content, new SetFragment());
                }
                transaction.commit();
            }
        });

        activityNa = (DrawerLayout) findViewById(R.id.activity_na);
        initToolbar("Tovos Live", "live");
        teamRefresh = findViewById(R.id.team_refresh);
        teamListView = findViewById(R.id.team_list_view);
        teamRefresh.setOnRefreshListener(this);
        teamAdapter = new TeamAdapter(this, mData, this);
        teamListView.setLayoutManager(new LinearLayoutManager(this));
        teamListView.setAdapter(teamAdapter);

        customFlightHubManager.teams();
        LogUtil.d("MainActivity 开始获取团队");
    }

    @Override
    public void getTeams(List<TeamGetModel> list) {
        LogUtil.d("MainActivity 获取团队结束");
        teamRefresh.setRefreshing(false);
        mData.clear();
        mData.addAll(list);
        teamAdapter.notifyDataSetChanged();

        teamtitle.removeAllViews();
        addTextView(null);
    }

    @Override
    public void onTaskItemClick(int i) {
        updataListView(mData.get(i));
    }

    @Override
    public void onTaskItemLongClick(int i) {

    }

    @Override
    public void onRefresh() {
        customFlightHubManager.teams();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void OnTeamTextClick(List<TeamGetModel> list) {

    }

    private void addTextView(TeamGetModel teamGetModel) {
        TeamTextView teamTextView = new TeamTextView(this);
        teamTextView.setTeamGetModel(teamGetModel);
        if (teamGetModel != null) {
            teamTextView.setText(teamGetModel.getTeamName() + "/ ");
        } else {
            teamTextView.setText("Tovos/ ");
        }
        teamTextView.setTextColor(Color.WHITE);
        teamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = teamtitle.indexOfChild(v);
                int max = teamtitle.getChildCount();
                for (int i = max - 1; i > index; i--) {
                    LogUtil.d("总view数:" + max + "删除子View：" + i);
                    teamtitle.removeViewAt(i);
                }

                nowTeamGetModel = ((TeamTextView) teamtitle.getChildAt(index)).getTeamGetModel();
                if (nowTeamGetModel != null) {
                    mData.clear();
                    if (nowTeamGetModel.getSubTeams() != null) {
                        mData.addAll(nowTeamGetModel.getSubTeams());
                    }
                    teamAdapter.notifyDataSetChanged();
                } else {
                    customFlightHubManager.teams();
                }
            }
        });

        teamtitle.addView(teamTextView);
    }

    private void updataListView(TeamGetModel teamGetModel) {
        nowTeamGetModel = teamGetModel;
        addTextView(teamGetModel);
        mData.clear();
        if (nowTeamGetModel.getSubTeams() != null) {
            mData.addAll(nowTeamGetModel.getSubTeams());
        }
        teamAdapter.notifyDataSetChanged();
    }

    public TeamGetModel getNowTeamGetModel() {
        return nowTeamGetModel;
    }

    public Toolbar initToolbar(String title, String Sub) {
        //标题+颜色
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        //子标题+颜色
        toolbar.setSubtitle(Sub);
        toolbar.setSubtitleTextColor(Color.GRAY);

        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, activityNa, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {//完全打开时触发
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {//完全关闭时触发
                super.onDrawerClosed(drawerView);
            }

            /**
             * 当抽屉被滑动的时候调用此方法
             * slideOffset表示 滑动的幅度（0-1）
             */
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             *具体状态可以慢慢调试
             */
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        //通过下面这句实现toolbar和Drawer的联动：如果没有这行代码，箭头是不会随着侧滑菜单的开关而变换的（或者没有箭头），
        // 可以尝试一下，不影响正常侧滑
        drawerToggle.syncState();

        activityNa.setDrawerListener(drawerToggle);

        //去掉侧滑的默认图标（动画箭头图标），也可以选择不去，
        //不去的话把这一行注释掉或者改成true，然后把toolbar.setNavigationIcon注释掉就行了
        drawerToggle.setDrawerIndicatorEnabled(true);

        //可在toolbar上设置图标来作为侧滑的默认图标
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        //toolbar设置的图标控制drawerlayout的侧滑
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityNa.isDrawerOpen(GravityCompat.START)) {
                    activityNa.closeDrawer(GravityCompat.START);
                } else {
                    activityNa.openDrawer(GravityCompat.START);
                }
            }
        });

        return toolbar;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;

        }
        return super.onKeyDown(keyCode, event);

    }

    public void exit() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.setResultToToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();

        } else {
            finish();
            System.exit(0);
        }
    }
}
