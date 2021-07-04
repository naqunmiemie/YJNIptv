package com.yjn.yjniptv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yjn.common.Utils.L;
import com.yjn.common.Utils.T;
import com.yjn.yjniptv.adapter.ProgramAdapter;
import com.yjn.yjniptv.data.ProgramList;

import com.yjn.yjniptv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.videoPlayer)
    StandardGSYVideoPlayer videoPlayer;
    @BindView(R.id.rv_program)
    TvRecyclerView recyclerView;

    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private boolean cache = false;
    private boolean isShowProgramList = false;
    private int PROGRAM_RECYCLERVIEW_WIDTH;
    private float mWidth;
    private float mHeight;
    private int currentChannel;
    private int lastChannel;
    private long firstTime = 0;
    private long secondTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initParam();
        initView();
        initVideoPlayer();
    }

    private void initParam() {
        PROGRAM_RECYCLERVIEW_WIDTH = getResources().getDimensionPixelOffset(R.dimen.program_recyclerview_width);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;
        currentChannel = 1;
        lastChannel = 1;
    }

    private void initView() {
        ProgramAdapter programAdapter = new ProgramAdapter();
        recyclerView.setAdapter(programAdapter);
        programAdapter.programListOnclick(new ProgramAdapter.IListSwitchChannel() {
            @Override
            public void onClick(View view, int position) {
                L.i(String.valueOf(ProgramList.programHashMap.get(position).getUrl()));
                switchChannel(position);
            }
        });

        hideProgramList();
    }

    private void initVideoPlayer() {
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder()
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setCacheWithPlay(cache);

        switchChannel(currentChannel);
        gsyVideoOptionBuilder.build(videoPlayer);
    }

    private void switchChannel(int num) {
        lastChannel = currentChannel;
        currentChannel = num;
        String url = String.valueOf(ProgramList.programHashMap.get(num).getUrl());
        String title = String.valueOf(ProgramList.programHashMap.get(num).getTitle());
        L.i(title+":"+url);
        videoPlayer.release();
        gsyVideoOptionBuilder.setUrl(url)
                .setCacheWithPlay(cache)
                .setVideoTitle(title)
                .build(videoPlayer);
        gsyVideoOptionBuilder.build(videoPlayer);
        videoPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoPlayer.startPlayLogic();
            }
        }, 1000);
    }

    private void showProgramList(){
        ViewCompat.animate(recyclerView)
                .translationXBy(PROGRAM_RECYCLERVIEW_WIDTH)
                .setInterpolator(new AccelerateDecelerateInterpolator());

        isShowProgramList = true;
    }

    private void hideProgramList(){
        ViewCompat.animate(recyclerView)
                .translationXBy(0-PROGRAM_RECYCLERVIEW_WIDTH)
                .setInterpolator(new AccelerateDecelerateInterpolator());

        isShowProgramList = false;
    }



    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (isShowProgramList){
            hideProgramList();
            return;
        }

        secondTime = System.currentTimeMillis();
        L.d(firstTime+","+secondTime);
        if (secondTime - firstTime > 2000) {
            T.show("再按一次返回键退出,点击⬅️或者左滑为回看");
            L.d("再按一次返回键退出,点击⬅️或者左滑为回看");
            firstTime = System.currentTimeMillis();
            return;
        }

        L.d("退出");

        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isShowProgramList){
            if (ev.getX()>=(mWidth/2)){
                hideProgramList();
                return true;
            }
        }else {
            if (ev.getX()<(mWidth/2)){
                showProgramList();
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}