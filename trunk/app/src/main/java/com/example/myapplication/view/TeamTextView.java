package com.example.myapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.filgthhublibrary.network.bean.TeamGetModel;
import com.example.myapplication.listener.TeamTextViewListener;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class TeamTextView extends TextView {

    private TeamGetModel teamGetModel;

    public TeamTextView(Context context) {
        super(context);
    }

    public TeamTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TeamTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TeamGetModel getTeamGetModel() {
        return teamGetModel;
    }

    public void setTeamGetModel(TeamGetModel teamGetModel) {
        this.teamGetModel = teamGetModel;
    }

}
