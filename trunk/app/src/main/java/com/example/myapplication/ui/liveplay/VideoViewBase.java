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


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.commonlib.utils.LogUtil;
import com.example.commonlib.utils.ToastUtils;
import com.example.filgthhublibrary.flighthub.CustomFlightHubManager2;
import com.example.filgthhublibrary.listener.HubStreamListener;
import com.example.filgthhublibrary.network.bean.ResPlay;
import com.example.myapplication.R;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoViewBase extends Activity implements MediaPlayer.OnBufferingUpdateListener,
		MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener , MediaPlayer.OnErrorListener,
		MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnTimedTextListener, MediaPlayer.OnInfoListener {

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media file
	 * path.
	 */
	protected MediaController mController;
	protected VideoView mVideoView;
	protected ProgressBar progressBar;
	protected LinearLayout ll;
	protected String path="http://gslb.miaopai.com/stream/3D~8BM-7CZqjZscVBEYr5g__.mp4";

	protected RelativeLayout mFlVideoGroup;
	//?????????????????????
	protected Boolean mIsFullScreen = false;
	/*?????????????????????View*/
	protected ArrayList<View> mViews;
	protected Toolbar toolbar;
	protected RelativeLayout rl;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

	}


	protected void initVitamio(){
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		progressBar = findViewById(R.id.task_pg);
		mFlVideoGroup = findViewById(R.id.fl_video_group);
		ll = findViewById(R.id.ll);

		toolbar= findViewById(R.id.toolbar);
		//??????+??????
		toolbar.setTitle("Tovos Live");
		toolbar.setTitleTextColor(Color.YELLOW);
		//?????????+??????
		toolbar.setSubtitle("");
		toolbar.setSubtitleTextColor(Color.GREEN);

		mVideoView.setMediaController(new MediaController(this));
		mVideoView.setOnBufferingUpdateListener(this);  //??????????????????
		mVideoView.setOnCompletionListener(this);  // ????????????????????????
		mVideoView.setOnErrorListener(this);   //??????????????????
		mVideoView.setOnInfoListener(this);  //??????????????????
		mVideoView.setOnPreparedListener(this);  //??????????????????
		mVideoView.setOnSeekCompleteListener(this);  //??????????????????
		mVideoView.setOnTimedTextListener(this);  //????????????????????????


		initVideo();
	}

	protected void initVideo(){
		//customFlightHubManager.play(sn);
		controller = new MediaController(VideoViewBase.this,true,mFlVideoGroup);
		mVideoView.setMediaController(controller);
		/*
		 * and set its position on screen
		 */
		controller.setAnchorView(mVideoView);
		controller.onFinishInflate();

		controller.setVisibility(View.GONE);
		mVideoView.requestFocus();
		mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				// optional need Vitamio 4.0
				mediaPlayer.setPlaybackSpeed(1.0f);
			}
		});

		controller.hide();
	}

	protected void playfunction(String path){

		if (path == "") {
			// Tell the user to provide a media file URL/path.
			//Toast.makeText(VideoViewBase.this, "Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
			return;
		} else {
			/*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */
			mVideoView.setVideoPath(path);

		}
	}

	protected void smallScreen(){
		mIsFullScreen = false;
		/*??????flag,???????????????????????????*/
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		toolbar.setVisibility(View.VISIBLE);
		hideViews(false);
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int deviceWidth = displayMetrics.widthPixels;
		LogUtil.d("deviceWidth:"+deviceWidth);
		int video_height = deviceWidth*9/16;
		LogUtil.d("???????????????video_height:"+video_height);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
				.LayoutParams.MATCH_PARENT,video_height);
				//DensityUtil.dip2px(this,video_height));
		mFlVideoGroup.setLayoutParams(params);
	}

	protected void fullScreen(){
		//??????
		mIsFullScreen = true;
		toolbar.setVisibility(View.GONE);
		//?????????????????????
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		hideViews(true);
		//??????mFlVideoGroup????????????
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
				.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mFlVideoGroup.setLayoutParams(params);
		//???????????????
//            public static final int VIDEO_LAYOUT_ORIGIN = 0;
		//?????????????????????????????????????????????????????????????????????????????????????????????View????????????????????????????????????
//            public static final int VIDEO_LAYOUT_SCALE = 1;
		//???????????????????????????
//            public static final int VIDEO_LAYOUT_STRETCH = 2;
		//???????????????????????????
//            public static final int VIDEO_LAYOUT_ZOOM = 3;
		//???????????????????????????????????????????????????View???
//            public static final int VIDEO_LAYOUT_FIT_PARENT = 4;
		mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
	}

	//?????????activity?????????
	// android:screenOrientation="portrait" ????????????????????????????????????????????????
	//android:configChanges="orientation|keyboardHidden|screenLayout|screenSize"????????????????????????
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			fullScreen();
		} else {
			smallScreen();
		}
	}

	public void hideViews(boolean hide) {
//		if (hide) {
//			for (int i = 0; i < mViews.size(); i++) {
//				mViews.get(i).setVisibility(View.GONE);
//			}
//		} else {
//			for (int i = 0; i < mViews.size(); i++) {
//				mViews.get(i).setVisibility(View.VISIBLE);
//			}
//		}
	}

	//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (mIsFullScreen) {
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				//.setFullScreenIconState(false);
				return true;
			}else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

		LogUtil.d("Tovos onBufferingUpdate  i:"+i);
	}

	MediaController controller;

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		LogUtil.d("Tovos onPrepared");
		mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				/*
				 * add media controller
				 */

			}
		});


	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		LogUtil.d("Tovos onCompletion");
		mVideoView.stopPlayback();
		initVideo();
		ToastUtils.setResultToToast("???????????????");
	}

	@Override
	public boolean onError(MediaPlayer mediaPlayer, int what, int i1) {
//		mediaPlayer.stop();
//		LogUtil.d("Tovos onError  i:"+i);
//		LogUtil.d("Tovos onError  i1:"+i1);
		mVideoView.stopPlayback();
		switch (what){
			case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				ToastUtils.setResultToToast("????????????");
				break;

			case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
				ToastUtils.setResultToToast("MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:");
				break;

			case MediaPlayer.MEDIA_ERROR_IO:
				ToastUtils.setResultToToast("?????????IO??????");
				break;

			case MediaPlayer.MEDIA_ERROR_MALFORMED:
				ToastUtils.setResultToToast("?????????????????????");
				break;

			case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
				ToastUtils.setResultToToast("????????????????????????");
				break;

			case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
				ToastUtils.setResultToToast("?????????????????????");
				break;
		}
		return true;
	}

	@Override
	public void onSeekComplete(MediaPlayer mediaPlayer) {
		LogUtil.d("Tovos onSeekComplete");
	}

	@Override
	public void onTimedText(String s) {
		LogUtil.d("Tovos onTimedText  s:"+s);
	}

	@Override
	public void onTimedTextUpdate(byte[] bytes, int i, int i1) {
		LogUtil.d("Tovos onTimedTextUpdate  bytes:"+bytes);
		LogUtil.d("Tovos onTimedTextUpdate  i:"+i);
		LogUtil.d("Tovos onTimedTextUpdate  i1:"+i1);
	}

	@Override
	public boolean onInfo(MediaPlayer mediaPlayer, int what, int i1) {

		switch (what) {
			//????????????
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				LogUtil.d("onInfo: ????????????");
				mVideoView.pause();
				ll .setVisibility(View.VISIBLE);
				break;
			//????????????
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				LogUtil.d("onInfo: ????????????");
				mVideoView.start();
				ll .setVisibility(View.GONE);
				break;
			//????????????????????????
			case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
				LogUtil.d("onInfo: ????????????:"+i1+ "kb/s");
				break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		mVideoView.stopPlayback();
		super.onDestroy();
	}
}
