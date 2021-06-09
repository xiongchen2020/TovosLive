package com.example.myapplication.ui.main.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.commonlib.utils.SPUtils;
import com.example.myapplication.R;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.ui.main.BaseFragment;
import com.example.myapplication.ui.main.MainActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetFragment extends BaseFragment {
   // RelativeLayout rl_unlogin;
   // TextView tvUnLogin;
  //  TextView tv_login;
    @BindView(R.id.headimg)
    ImageView headimg;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_unlogin)
    TextView tvUnlogin;
    @BindView(R.id.rl_unlogin)
    RelativeLayout rlUnlogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_layout, container, false);
        ButterKnife.bind(this,view);
        Toolbar toolbar = ((MainActivity) getActivity()).initToolbar("设置", "");//findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.light_toobar_bg));
        setHasOptionsMenu(true);
      //  rl_unlogin = view.findViewById(R.id.rl_unlogin);
     //   tvUnLogin = view.findViewById(R.id.tv_unlogin);
      //  tv_login = view.findViewById(R.id.tv_login);
        String account = (String) SPUtils.get("hub_account", "");
        if (!"".equals(account)) {
            tvLogin.setText(account);
        }
        rlUnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.put("is_login", false);
                customFlightHubManager.removeLoginInfo();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        return view;
    }


}
