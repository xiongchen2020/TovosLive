package com.example.myapplication.ui.main.record.mediarecord;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.filgthhublibrary.network.bean.ResMediaList;
import com.example.myapplication.R;
import com.example.myapplication.ui.recycleadapter.BaseViewHolder;
import com.example.myapplication.ui.recycleadapter.XMBaseAdapter;

import androidx.annotation.LayoutRes;

public class MediaRecordAdapter  extends XMBaseAdapter<ResMediaList.FileListModel> {
    Context context;
    VideoItemClickListener videoItemClickListener;
    public MediaRecordAdapter(Context context, VideoItemClickListener onVideoItemClick) {
        super(context);
        this.context = context;
        this.videoItemClickListener = onVideoItemClick;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_media_layout);
    }

    private class ViewHolder extends BaseViewHolder<ResMediaList.FileListModel> {
        private ImageView pic;

        private TextView name;


        ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);


        }


        @Override
        public void setData(ResMediaList.FileListModel data) {


            String[]  decode = data.getThumbnail().split(",");
            String picData = "";
            if (decode.length==2){
                picData = decode[1];
            }
             Glide.with(context).load(Base64.decode(picData, Base64.DEFAULT)).asBitmap().into(pic);

            //将字符串转换成Bitmap类型
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoItemClickListener.onVideoItemClick(data);
                }
            });



            name.setText(data.getName());

        }


        }
    }
