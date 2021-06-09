/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication.ui.liveplay;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.example.amaplibrary.view.CustomAMap;
import com.example.commonlib.utils.LogUtil;

import com.example.filgthhublibrary.flighthub.CustomFlightHubManager2;
import com.example.filgthhublibrary.listener.HubStreamListener;
import com.example.filgthhublibrary.network.bean.ResPlay;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.Vitamio;

public class LiveViewActivity extends VideoViewBase implements HubStreamListener{

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media file
	 * path.
	 */

	private CustomFlightHubManager2 customFlightHubManager;
	private String sn = "";


	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Vitamio.isInitialized(getApplicationContext());
		setContentView(R.layout.videoview);
		initVitamio();

		if (getIntent().getStringExtra("sn")!=null){
			sn = getIntent().getStringExtra("sn");
		}
		customFlightHubManager = CustomFlightHubManager2.getInstance();
		customFlightHubManager.setStreamListener(this);
		customFlightHubManager.play(sn);
		ll.setVisibility(View.VISIBLE);

	}


	@Override
	public void getPublishUrlSuccess(String s) {

	}

	@Override
	public void getPublishUrlFail() {

	}

	@Override
	public void updateVideoInfo() {

	}



	@Override
	public void playUrl(ResPlay resPlay) {
		ll.setVisibility(View.GONE);
		LogUtil.d("tovos stream video:"+resPlay.getHlv().getPlayUrl());
		playfunction(resPlay.getHlv().getPlayUrl());
	}

}
