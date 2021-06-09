package com.example.myapplication.ui.main.record;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;


import com.example.myapplication.R;
import com.example.myapplication.ui.main.BaseFragment;
import com.example.myapplication.ui.main.MainActivity;
import com.example.myapplication.ui.main.record.flightrecord.FlightRecordFragment;
import com.example.myapplication.ui.main.record.mediarecord.MediaRecordFeagment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

public class RecordFragment extends BaseFragment{
    RadioGroup rg_record;
    FrameLayout fl_frag;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.record_layout,container,false);
        rg_record = view.findViewById(R.id.rg_record);
        fl_frag = view.findViewById(R.id.fl_frag);


        Toolbar toolbar= ((MainActivity)getActivity()).initToolbar("数据统计","data");//findViewById(R.id.toolbar);

        setHasOptionsMenu(true);
        //((MainActivity)getActivity()).setSupportActionBar(toolbar);
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_frag, new FlightRecordFragment());
        transaction.commit();

        rg_record.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //根据RadioButton不同的Id来选中不同的Fragment。
                if (checkedId == R.id.live_list_rb) {
                    transaction.replace(R.id.fl_frag, new FlightRecordFragment());
                } else if (checkedId == R.id.data_all_rb) {
                    transaction.replace(R.id.fl_frag, new MediaRecordFeagment());
                }
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (item.getItemId()){
            //当点击一个条目时，不显示另外一个

            case R.id.menu_edit:

                transaction.replace(R.id.fl_frag, new FlightRecordFragment());
                transaction.commit();
                break;
            case R.id.menu_cancel:
                transaction.replace(R.id.fl_frag, new MediaRecordFeagment());
                transaction.commit();
                break;
        }
        return true;
    }
}
