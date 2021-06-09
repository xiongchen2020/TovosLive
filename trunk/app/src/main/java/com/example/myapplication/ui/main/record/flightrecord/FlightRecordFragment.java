package com.example.myapplication.ui.main.record.flightrecord;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlib.utils.CustomNumberUtils;
import com.example.commonlib.utils.CustomTimeUtils;
import com.example.filgthhublibrary.listener.HubRecordsListener;
import com.example.filgthhublibrary.network.bean.ResFlightRecords;
import com.example.filgthhublibrary.network.bean.ResMediaList;
import com.example.filgthhublibrary.network.bean.ResStatisticsRecords;
import com.example.myapplication.MApplication;
import com.example.myapplication.R;
import com.example.myapplication.ui.main.BaseFragment;
import com.example.myapplication.ui.main.MainActivity;
import com.example.myapplication.ui.main.RecordActivity;
import com.example.myapplication.ui.recycleadapter.XMBaseAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightRecordFragment extends BaseFragment implements OnRecordItemClick,
        HubRecordsListener, XMBaseAdapter.OnLoadMoreListener {

    //    RecyclerView record_list_view;
    FlightRecordAdapter flightRecordAdapter;
    List<ResFlightRecords.FlightGetModel> list = new ArrayList<>();
    long startTime;
    long endTime;
    int page = 0;
    int count = 20;
    int totalPage = 0;
    //  protected SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    FlightRecordHeader header;
    @BindView(R.id.record_list_view)
    RecyclerView recordListView;
    @BindView(R.id.media_refresh)
    SwipeRefreshLayout mediaRefresh;
    @BindView(R.id.nac_layout)
    LinearLayout nacLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flight_record_layout, container, false);
        ButterKnife.bind(this, view);
        toolbar = ((MainActivity) getActivity()).initToolbar("数据统计", "飞行记录");//findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.argb(1, 1, 1, 1));
        setHasOptionsMenu(true);
        initView();

        return view;
    }

    private void initView() {
        endTime = System.currentTimeMillis();
        startTime = CustomTimeUtils.getWeekStartTime(endTime);

        flightRecordAdapter = new FlightRecordAdapter(getContext(), this);
        flightRecordAdapter.setMore(R.layout.view_recyclerview_more, this);
        flightRecordAdapter.setNoMore(R.layout.view_recyclerview_nomore);
        flightRecordAdapter.setError(R.layout.view_recyclerview_error);
        header = new FlightRecordHeader();
        flightRecordAdapter.addHeader(header);
        // record_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        setLayoutView(recordListView);
        recordListView.setAdapter(flightRecordAdapter);
        mediaRefresh.setOnRefreshListener(this);
        customFlightHubManager.setHubRecordsListener(this);
        mediaRefresh.setRefreshing(true);
        customFlightHubManager.getStatisticsRecords(getTeamId(), 0, "", startTime, endTime);
        customFlightHubManager.getFlightRecords(getTeamId(), 0, "", startTime, endTime, page, count);
        //滑动变色

        recordListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position;
                if (MApplication.isTabletDevice(getContext())) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    int[] array = null;
                    position = staggeredGridLayoutManager.findFirstVisibleItemPositions(array)[0];
                } else {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    position = layoutManager.findFirstVisibleItemPosition();
                }
                if (position > 0) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.light_toobar_bg));
                } else {
                    toolbar.setBackgroundColor(Color.argb(1, 1, 1, 1));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 0;
        endTime = System.currentTimeMillis();
        startTime = CustomTimeUtils.getWeekStartTime(endTime);
        try {
            header.tvStartTime.setText(CustomTimeUtils.longToString(startTime, "yyyy-MM-dd"));
            header.tvEndTime.setText(CustomTimeUtils.longToString(endTime, "yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // customFlightHubManager.getMediaList(type[0], getTeamId(), "", "", startTime, endTime, page, count);
        customFlightHubManager.getStatisticsRecords(getTeamId(), 0, "", startTime, endTime);
        customFlightHubManager.getFlightRecords(getTeamId(), 0, "", startTime, endTime, page, count);
    }


    private void setBarChartData(List<String> names, List<Double> values) {

        ArrayList<BarEntry> barValues = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            if (values.size() > i) {
                barValues.add(new BarEntry(i, new Float(values.get(i))));
            }
        }

        header.xAxis.setLabelCount(names.size());
        header.xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return names.get((int) value) + "分钟";
            }
        });


        BarDataSet set1 = new BarDataSet(barValues, "");
        set1.setDrawIcons(false);
        set1.setColors(header.colorList);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);

        data.setValueTextColors(header.colorList);
        data.setBarWidth(0.5f);
        header.chartBar.setData(data);

        header.chartBar.invalidate();
    }

    @Override
    public void setStatisticsRecords(ResStatisticsRecords resStatisticsRecords) {
        int count = 0;
        mediaRefresh.setRefreshing(false);
        for (int i = 0; i < resStatisticsRecords.getValues().size(); i++) {
            count += resStatisticsRecords.getValues().get(i).intValue();
        }
        header.tvCount.setText("飞行次数: " + count + "次");
        header.tvTotalTime.setText("总时长: " + CustomNumberUtils.format4(resStatisticsRecords.getTotalDuration() / 60.0) + "分钟");
        header.tvAvgTime.setText("平均时长: " + CustomNumberUtils.format4(resStatisticsRecords.getAvgDuration() / 60.0) + "分钟");
        setBarChartData(resStatisticsRecords.getNames(), resStatisticsRecords.getValues());
    }

    @Override
    public void setStatisticsRecordsThrowable(Throwable throwable) {

    }

    @Override
    public void setMediaList(int i, ResMediaList resMediaList) {

    }

    @Override
    public void setMediaListThrowable(Throwable throwable) {

    }


    @Override
    public void setFlightRecords(ResFlightRecords resFlightRecords) {
        mediaRefresh.setRefreshing(false);
        totalPage = resFlightRecords.getPages();
        if (page == 0) {
            flightRecordAdapter.clear();
            list.clear();
        }

        list.addAll(resFlightRecords.getFlightGetModelList());
        flightRecordAdapter.addAll(resFlightRecords.getFlightGetModelList());


    }

    @Override
    public void setFlightRecordsThrowable(Throwable throwable) {
        flightRecordAdapter.notifyDataSetChanged();
    }


    public void showDateDialog(TextView tv, long time, boolean isStart) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long timeInMillis = calendar.getTime().getTime();
                if (isStart) {
                    startTime = timeInMillis;
                } else {
                    endTime = timeInMillis;
                }
                try {
                    tv.setText(CustomTimeUtils.longToString(timeInMillis, "yyyy-MM-dd"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                page = 0;
                customFlightHubManager.getStatisticsRecords(getTeamId(), 0, "", startTime, endTime);
                customFlightHubManager.getFlightRecords(getTeamId(), 0, "", startTime, endTime, page, count);
            }
        }, Integer.valueOf(CustomTimeUtils.getYearFromTime(time)), Integer.valueOf(CustomTimeUtils.getMonthFromTime(time)) - 1, Integer.valueOf(CustomTimeUtils.getDayFromTime(time)));
        datePickerDialog.show();
    }

    @Override
    public void onLoadMore() {

        page++;
        if (totalPage > page) {
            customFlightHubManager.getFlightRecords(getTeamId(), 0, "", startTime, endTime, page * count, count);
        } else {
            flightRecordAdapter.stopMore();
        }

    }

    @Override
    public void onRecordItemClick(ResFlightRecords.FlightGetModel data) {

        //customFlightHubManager.getRecordById(list.get(Position).getId());
        Intent intent = new Intent(getActivity(), RecordActivity.class);
        intent.putExtra("id",data.getId());
        startActivity(intent);
    }


    class FlightRecordHeader implements XMBaseAdapter.ItemView, View.OnClickListener {
       // BarChart chart_bar;
       // TextView tv_count;
     //   TextView tv_total_time;
      //  TextView tv_avg_time;
      //  TextView tv_start_time;
      //  TextView tv_end_time;
        XAxis xAxis;
        List<Integer> colorList = new ArrayList<>();
        @BindView(R.id.tv_start_time)
        TextView tvStartTime;
        @BindView(R.id.tv_end_time)
        TextView tvEndTime;
        @BindView(R.id.chart_bar)
        BarChart chartBar;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_total_time)
        TextView tvTotalTime;
        @BindView(R.id.tv_avg_time)
        TextView tvAvgTime;

        @Override
        public View onCreateView(ViewGroup parent) {
            View header = LayoutInflater.from(getActivity()).inflate(R.layout.flight_record_recyle_header, null);
            ButterKnife.bind(this,header);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            header.setLayoutParams(params);
          //  chart_bar = header.findViewById(R.id.chart_bar);
          //  tv_count = header.findViewById(R.id.tv_count);
           // tv_total_time = header.findViewById(R.id.tv_total_time);
           // tv_avg_time = header.findViewById(R.id.tv_avg_time);
            // = header.findViewById(R.id.tv_start_time);
           // tvEndTime = header.findViewById(R.id.tv_end_time);

            tvStartTime.setOnClickListener(this);
            tvEndTime.setOnClickListener(this);

            try {
                tvStartTime.setText(CustomTimeUtils.longToString(startTime, "yyyy-MM-dd"));
                tvEndTime.setText(CustomTimeUtils.longToString(endTime, "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            initBarChart();
            return header;
        }

        @Override
        public void onBindView(View headerView) {


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_start_time:
                    showDateDialog(tvStartTime, startTime, true);
                    break;
                case R.id.tv_end_time:
                    showDateDialog(tvEndTime, endTime, false);
                    break;
            }
        }

        private void initBarChart() {
            chartBar.setDrawBarShadow(false);
            chartBar.setDrawValueAboveBar(true);
            chartBar.getDescription().setEnabled(false);
            chartBar.setMaxVisibleValueCount(60);
            chartBar.setPinchZoom(false);
            chartBar.setDrawGridBackground(false);
            chartBar.setSelected(false);
            chartBar.setClickable(false);


            xAxis = chartBar.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);

            ValueFormatter custom = new DefaultAxisValueFormatter(5);
            YAxis leftAxis = chartBar.getAxisLeft();
            leftAxis.setLabelCount(8, false);
            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setSpaceTop(15f);
            leftAxis.setTextColor(Color.BLACK);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


            chartBar.getAxisRight().setEnabled(false);
            chartBar.getLegend().setEnabled(false);



            colorList.add(Color.rgb(0, 197, 250));
            colorList.add(Color.rgb(124, 234, 138));
            colorList.add(Color.rgb(254, 122, 139));
            colorList.add(Color.rgb(78, 99, 168));
            colorList.add(Color.rgb(215, 123, 240));

        }
    }


}
