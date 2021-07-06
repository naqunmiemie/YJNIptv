package com.yjn.yjniptv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

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

import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;

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
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    int move=100;//移动距离

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

        hideProgramListSlow();
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
        if (num >= ProgramList.programHashMap.size()){
            num = 0;
        }else if (num < 0){
            num = ProgramList.programHashMap.size()-1;
        }
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
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        isShowProgramList = true;
    }

    private void hideProgramList(){
        ViewCompat.animate(recyclerView)
                .translationXBy(0-PROGRAM_RECYCLERVIEW_WIDTH)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        isShowProgramList = false;
    }

    private void hideProgramListSlow(){
        ViewCompat.animate(recyclerView)
                .translationXBy(0-PROGRAM_RECYCLERVIEW_WIDTH)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator());
        isShowProgramList = false;
    }

    private void changeProgramList(){
        if (isShowProgramList){
            hideProgramList();
        }else {
            showProgramList();
        }
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
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {//当手指按下的时候
            x1 = ev.getX();
            y1 = ev.getY();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            //屏蔽触摸事件
            if (!isShowProgramList){
                return true;
            }
        }
        if(ev.getAction() == MotionEvent.ACTION_UP) {//当手指离开的时候
            x2 = ev.getX();
            y2 = ev.getY();
            if (isShowProgramList){
                if (x2 >= (mWidth/2)){
                hideProgramList();
                return true;
                }
            }else {
                if (y1 - y2 > move && (y1 - y2) > (x1 - x2) && (y1 - y2) > (x2 - x1)) {
                    L.i("向上滑");
                    switchChannel(currentChannel+1);
                    return true;
                } else if (y2 - y1 > move && (y2 - y1) > (x1 - x2) && (y2 - y1) > (x2 - x1)) {
                    L.i("向下滑");
                    switchChannel(currentChannel-1);
                    return true;
                } else if (x1 - x2 > move) {
                    L.i("向左滑");
                    switchChannel(lastChannel);
                    return true;
                } else if (x2 - x1 > move) {
                    L.i("向右滑");
                    showProgramList();
                    return true;
                }
                if (x2 < (mWidth/2)){
                    showProgramList();
                    return true;
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()){
            case KEYCODE_DPAD_CENTER:
                if (isShowProgramList){
                    hideProgramList();
                }else {
                    showProgramList();
                }
                return true;
            case KEYCODE_DPAD_LEFT:
                switchChannel(lastChannel);
                return true;
            case KEYCODE_DPAD_UP:
                switchChannel(currentChannel+1);
                return true;
            case KEYCODE_DPAD_DOWN:
                switchChannel(currentChannel-1);
                return true;
        }
        return super.dispatchKeyEvent(event);
    }
}