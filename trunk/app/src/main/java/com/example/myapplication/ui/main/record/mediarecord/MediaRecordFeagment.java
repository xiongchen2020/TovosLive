package com.example.myapplication.ui.main.record.mediarecord;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.commonlib.utils.CustomTimeUtils;
import com.example.commonlib.utils.DialogManager;
import com.example.commonlib.utils.FileManager;
import com.example.commonlib.utils.LogUtil;
import com.example.commonlib.utils.NetWorkUtil;
import com.example.filgthhublibrary.listener.HubRecordsListener;
import com.example.filgthhublibrary.listener.HubVideoListener;
import com.example.filgthhublibrary.network.bean.ResFlightRecords;
import com.example.filgthhublibrary.network.bean.ResMediaList;
import com.example.filgthhublibrary.network.bean.ResStatisticsRecords;
import com.example.myapplication.R;
import com.example.myapplication.ui.liveplay.VideoViewActivity;
import com.example.myapplication.ui.main.BaseFragment;
import com.example.myapplication.ui.main.MainActivity;
import com.example.myapplication.ui.recycleadapter.XMBaseAdapter;
import com.example.myapplication.view.EmptyRecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class MediaRecordFeagment extends BaseFragment implements HubRecordsListener, View.OnClickListener, XMBaseAdapter.OnLoadMoreListener, VideoItemClickListener, HubVideoListener {
   // TextView tv_start_time;
  //  TextView tv_end_time;
   // Spinner spinner;
  //  protected SwipeRefreshLayout swipeRefreshLayout;
  //  protected EmptyRecyclerView media_list_view;
    long startTime;
    long endTime;
    int page = 0;
    int count = 20;
    int totalPage = 0;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.media_list_view)
    EmptyRecyclerView mediaListView;
    @BindView(R.id.media_refresh)
    SwipeRefreshLayout mediaRefresh;
    private MediaRecordAdapter mediaRecordAdapter;
    private String[] type = new String[]{"flightMedia", "flightPic", "flightVideo"};
    private String[] typeName = new String[]{"全部", "图片", "视频"};
    ResMediaList.FileListModel data;
    ProgressDialog progressDialog;
    String path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_record_layout, container, false);
        ButterKnife.bind(this,view);
      //  tvStartTime = view.findViewById(R.id.tv_start_time);
     //   tvEndTime = view.findViewById(R.id.tv_end_time);
      //  swipeRefreshLayout = view.findViewById(R.id.media_refresh);
      //  media_list_view = view.findViewById(R.id.media_list_view);
      //  spinner = view.findViewById(R.id.spinner);
        Toolbar toolbar = ((MainActivity) getActivity()).initToolbar("数据统计", "媒体文件");//findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.light_toobar_bg));
        FileManager fileManager = new FileManager();
        path = Environment.getExternalStorageDirectory() + "/tovoslive/";
        fileManager.createDirectory(path);
        setHasOptionsMenu(true);
        initView();
        return view;
    }

    private void initView() {

        endTime = System.currentTimeMillis();
        startTime = CustomTimeUtils.getWeekStartTime(endTime);
        try {
            tvStartTime.setText(CustomTimeUtils.longToString(startTime, "yyyy-MM-dd"));
            tvEndTime.setText(CustomTimeUtils.longToString(endTime, "yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mediaRefresh.setOnRefreshListener(this);

        mediaListView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));


        mediaRecordAdapter = new MediaRecordAdapter(getContext(), this);
        mediaRecordAdapter.setMore(R.layout.view_recyclerview_more, this);
        mediaRecordAdapter.setNoMore(R.layout.view_recyclerview_nomore);
        mediaRecordAdapter.setError(R.layout.view_recyclerview_error);
        mediaListView.setAdapter(mediaRecordAdapter);
        customFlightHubManager.setHubRecordsListener(this);
        customFlightHubManager.setHubVideoListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        mediaRefresh.setRefreshing(true);
        spinner.setVisibility(View.VISIBLE);

        // customFlightHubManager.getMediaList(type[0], getTeamId(), "", "", startTime, endTime, page, count);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, typeName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(Color.WHITE);
                page = 0;
                customFlightHubManager.getMediaList(type[position], getTeamId(), "", "", startTime, endTime, page, count);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onRefresh() {
        page = 0;
        customFlightHubManager.getMediaList(type[0], getTeamId(), "", "", startTime, endTime, page, count);
    }

    @Override
    public void setStatisticsRecords(ResStatisticsRecords resStatisticsRecords) {

    }

    @Override
    public void setStatisticsRecordsThrowable(Throwable throwable) {

    }

    @Override
    public void setMediaList(int i, ResMediaList resMediaList) {
        if (i == 0) {
            mediaRefresh.setRefreshing(false);
            totalPage = resMediaList.getPages();
            if (page == 0) {
                mediaRecordAdapter.clear();
            }
            mediaRecordAdapter.addAll(resMediaList.getFlightGetModelList());
        }

    }

    @Override
    public void setMediaListThrowable(Throwable throwable) {
        mediaRecordAdapter.notifyDataSetChanged();
    }


    @Override
    public void setFlightRecords(ResFlightRecords resFlightRecords) {

    }

    @Override
    public void setFlightRecordsThrowable(Throwable throwable) {

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
                // customFlightHubManager.getStatisticsRecords(getTeamId(),0,"",startTime,endTime);
                customFlightHubManager.getMediaList(type[0], getTeamId(), "", "", startTime, endTime, page, count);
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
            mediaRecordAdapter.stopMore();
        }
    }


    @Override
    public void onVideoItemClick(ResMediaList.FileListModel data) {
        this.data = data;
        FileManager fileManager = new FileManager();


        if (!fileManager.fileIsExists(path + data.getName())) {
            if (NetWorkUtil.getNetWorkType(getContext()) == NetWorkUtil.NETWORKTYPE_WIFI) {
                downloadVideo();
            } else {
                DialogManager.showSelectDialog(getContext(), "提示", "播放视频需要下载，是否下载？", "确定", "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ToDo: 你想做的事情
                        downloadVideo();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ToDo: 你想做的事情
                        dialog.dismiss();
                    }
                });
            }


        } else {
            startVideoActivity();
        }
    }

    private void downloadVideo() {
        String[] d = data.getPath().split("/");
        customFlightHubManager.downloadMedia(data.getCategory(), d[d.length - 1]);
        progressDialog = DialogManager.showProgressDialog(getContext(), "下载", false, "", null);
    }

    private void startVideoActivity() {
        Intent intent = new Intent(getContext(), VideoViewActivity.class);
        String url = path + data.getName();
        intent.putExtra("url", url);
        //intent.putExtra("name",data.getPath());
        LogUtil.d("MediaRecordFeagment", url);
        getActivity().startActivity(intent);
    }


    @Override
    public void getVideoUrl(ResponseBody responseBody) {
        writeResponseBodyToDisk(responseBody);
        LogUtil.d("MediaRecordFeagment", responseBody.toString());

    }


    private boolean writeResponseBodyToDisk(ResponseBody body) {
        if (body != null) {
            try {
                // todo change the file location/name according to your needs
                File futureStudioIconFile = new File(path + data.getName());
                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    byte[] fileReader = new byte[4096];

                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(futureStudioIconFile);

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);
                        fileSizeDownloaded += read;
                        int pregress = (int) (fileSizeDownloaded / Double.valueOf(fileSize) * 100);
                        progressDialog.setProgress(pregress);
                        if (fileSizeDownloaded == fileSize) {
                            progressDialog.dismiss();
                            startVideoActivity();
                        }
                        LogUtil.d("MediaRecordFeagment", fileSizeDownloaded + " of " + fileSize);
                        // Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    }

                    outputStream.flush();

                    return true;
                } catch (IOException e) {
                    progressDialog.dismiss();
                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                    progressDialog.dismiss();
                }
            } catch (IOException e) {
                progressDialog.dismiss();
                return false;

            }
        } else {
            progressDialog.dismiss();
            return false;
        }

    }
}
