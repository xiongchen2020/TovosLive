package com.example.myapplication.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.commonlib.utils.LogUtil;
import com.example.commonlib.utils.SPUtils;

import com.example.filgthhublibrary.flighthub.CustomFlightHubManager2;

import com.example.filgthhublibrary.listener.HubLoginListener;
import com.example.filgthhublibrary.network.bean.ResTeamMember;
import com.example.filgthhublibrary.network.bean.ResUserInfo;
import com.example.myapplication.R;
import com.example.myapplication.ui.liveplay.LiveViewActivity;
import com.example.myapplication.ui.main.MainActivity;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class LoginActivity extends Activity implements HubLoginListener {

    public CustomFlightHubManager2 customFlightHubManager;
    private EditText usernameEditText ;
    private EditText passwordEditText;
    private Button loginButton ;
    private ProgressBar loadingProgressBar;
    private String account,password;
    private boolean isLogin;
    private Toolbar toolbar;
    TitanicTextView tv;
    private static final int REQUEST_PERMISSION_CODE = 12345;
    private List<String> missingPermission = new ArrayList<>();
    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.VIBRATE, // Gimbal rotation
            Manifest.permission.INTERNET, // API requests
            Manifest.permission.ACCESS_WIFI_STATE, // WIFI connected products
            Manifest.permission.ACCESS_COARSE_LOCATION, // Maps
            Manifest.permission.ACCESS_NETWORK_STATE, // WIFI connected products
            Manifest.permission.ACCESS_FINE_LOCATION, // Maps
            Manifest.permission.CHANGE_WIFI_STATE, // Changing between WIFI and USB connection
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // Log files
            Manifest.permission.BLUETOOTH, // Bluetooth connected products
            Manifest.permission.BLUETOOTH_ADMIN, // Bluetooth connected products
            Manifest.permission.READ_EXTERNAL_STORAGE, // Log files
            Manifest.permission.READ_PHONE_STATE, // Device UUID accessed upon registration
            Manifest.permission.RECORD_AUDIO, // Speaker accessory
            Manifest.permission.KILL_BACKGROUND_PROCESSES
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        checkAndRequestPermissions();

        toolbar= findViewById(R.id.toolbar);
        //标题+颜色
        toolbar.setTitle("Tovos Live");

        toolbar.setTitleTextColor(Color.YELLOW);

        //子标题+颜色
        toolbar.setSubtitle("登录");
        toolbar.setSubtitleTextColor(Color.GREEN);
        setLoginInfo();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgressBar.setVisibility(View.VISIBLE);
                LogUtil.d("Tovos Stream customFlightHubManager:"+customFlightHubManager);
                LogUtil.d("Tovos Stream user:"+usernameEditText.getText().toString());
                LogUtil.d("Tovos Stream pwd:"+passwordEditText.getText().toString());
                account = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                SPUtils.put("hub_account", account);
                SPUtils.put("hub_password", password);
                customFlightHubManager.Login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        tv = (TitanicTextView) findViewById(R.id.my_text_view);
        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        // start animation
        new Titanic().start(tv);
    }

    public void setLoginInfo() {
        account = (String) SPUtils.get("hub_account", "");
        password = (String) SPUtils.get("hub_password", "");
        isLogin = (boolean) SPUtils.get("is_login", false);
        if (isLogin && account != "" && password != "") {
            //  hubSet.setVisibility(VISIBLE);
            usernameEditText.setText(account);

            passwordEditText.setText(password);
            if (customFlightHubManager != null) {
                customFlightHubManager.Login(account, password);
            }

            //
        }
    }


    @Override
    public void loginSuccess() {
        Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_LONG).show();
        loadingProgressBar.setVisibility(View.GONE);
        SPUtils.put("is_login", true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFail() {
        Toast.makeText(getApplicationContext(), "登录失败！", Toast.LENGTH_LONG).show();
        loadingProgressBar.setVisibility(View.GONE);
        SPUtils.put("is_login", false);
    }

    @Override
    public void setAllUser(List<ResUserInfo> list) {

    }

    @Override
    public void setAllUserThrowable(Throwable throwable) {

    }


    @Override
    public void getTeamMember(List<ResTeamMember> list) {

    }

    @Override
    public void getTeamMembersThrowable(Throwable throwable) {

    }


    /**
     * Checks if there is any missing permissions, and
     * requests runtime permission if needed.
     */
    private void checkAndRequestPermissions() {
        // Check for permissions
        for (String eachPermission : REQUIRED_PERMISSION_LIST) {
            if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
                missingPermission.add(eachPermission);
            }
        }
        // Request for missing permissions
        if (missingPermission.isEmpty()) {

            customFlightHubManager = CustomFlightHubManager2.getInstance();
            customFlightHubManager.setLoginListener(this);
//
        } else {
            ActivityCompat.requestPermissions(this,
                    missingPermission.toArray(new String[missingPermission.size()]),
                    REQUEST_PERMISSION_CODE);
        }
    }


    /**
     * Result of runtime permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check for granted permission and remove from missing list
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = grantResults.length - 1; i >= 0; i--) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    missingPermission.remove(permissions[i]);
                }
            }
        }
        // If there is enough permission, we will start the registration
        if (missingPermission.isEmpty()) {
            customFlightHubManager = CustomFlightHubManager2.getInstance();
            customFlightHubManager.setLoginListener(this);
//
        } else {
            finish();
        }
    }

    private void initData(){

        account = (String) SPUtils.get("hub_account", "");
        password = (String) SPUtils.get("hub_password", "");
        isLogin = (boolean) SPUtils.get("is_login", false);
        usernameEditText.setText(account);
        passwordEditText.setText(password);
    }
}