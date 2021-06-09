package com.example.myapplication.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import com.amap.api.maps.model.LatLng;
import com.example.amaplibrary.MovePointActivity;
import com.example.filgthhublibrary.flighthub.CustomFlightHubManager2;
import com.example.filgthhublibrary.listener.HubRecordByIdListener;
import com.example.filgthhublibrary.network.bean.ResRecordDetail;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends MovePointActivity implements HubRecordByIdListener {

    private CustomFlightHubManager2 customFlightHubManager2;
    private String id = "";
    private List<LatLng> latLngList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMoveView(savedInstanceState);

        if (getIntent().getStringExtra("id")!=null){
            id = getIntent().getStringExtra("id");
        }
        customFlightHubManager2 = CustomFlightHubManager2.getInstance();
        customFlightHubManager2.setHubRecordByIdListener(this);
        customFlightHubManager2.getRecordById(id);
    }

    @Override
    public void getFlightRecordsById(ResRecordDetail resRecordDetail) {
        for (int i = 0;i<resRecordDetail.getRecordPoints().size();i++){
            LatLng latLng = new LatLng(resRecordDetail.getRecordPoints().get(i).getLat(),resRecordDetail.getRecordPoints().get(i).getLng());
            latLngList.add(latLng);
        }
        customAMap.initMove(latLngList,new Double(resRecordDetail.getSummary().getDuration()).intValue());
        customAMap.setPolyline(latLngList,Color.RED);
        customAMap.moveCamera(latLngList.get(0),20.0f,customAMap.titl,customAMap.bearing);
    }

    @Override
    public void getFlightRecordsByIdThrowable(Throwable throwable) {

    }
}
