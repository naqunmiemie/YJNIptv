package com.yjn.yjniptv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.owen.focus.AbsFocusBorder;
import com.owen.focus.FocusBorder;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.yjn.common.Utils.L;
import com.yjn.common.Utils.T;
import com.yjn.yjniptv.adapter.ProgramAdapter;
import com.yjn.yjniptv.data.ProgramList;

import com.yjn.yjniptv.R;
import com.yjn.yjniptv.widget.EmptyControlVideo;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.videoPlayer)
    EmptyControlVideo videoPlayer;
    @BindView(R.id.rv_program)
    TvRecyclerView recyclerView;

    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private boolean cache = false;
    private boolean isShowProgramList = false;
    private boolean isMoveProgramList = false;
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

    FocusBorder mColorFocusBorder;

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
        initBorder();
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
                hideProgramList();
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

    private void initBorder() {
        /** 颜色焦点框 */
        mColorFocusBorder = new FocusBorder.Builder().asColor()
                //阴影宽度(方法shadowWidth(18f)也可以设置阴影宽度)
                .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 10f)
                //阴影颜色
                .shadowColor(Color.parseColor("#00FFFFFF"))
                //边框宽度(方法borderWidth(2f)也可以设置边框宽度)
                .borderWidth(4f)
                //边框颜色
                .borderColor(Color.parseColor("#00FF00"))
                //padding值
                .padding(1f)
                //动画时长
                .animDuration(300)
                //不要闪光效果动画
//                .noShimmer()
                //闪光颜色
                .shimmerColor(Color.parseColor("#66FFFFFF"))
                //闪光动画时长
                .shimmerDuration(1000)
                //不要呼吸灯效果
//                .noBreathing()
                //呼吸灯效果时长
                .breathingDuration(3000)
                //边框动画模式
                .animMode(AbsFocusBorder.Mode.TOGETHER)
                .build(this);

        //焦点监听 方式一:绑定整个页面的焦点监听事件
        mColorFocusBorder.boundGlobalFocusListener(new FocusBorder.OnFocusCallback() {
            @Override
            public FocusBorder.Options onFocus(View oldFocus, View newFocus) {
                if(null != newFocus) {
                    switch (newFocus.getId()) {
                        case R.id.ll_program:
                            return createColorBorderOptions(6);
                        default:
                            break;
                    }
                }
                //返回null表示不使用焦点框框架
                return null;
            }
        });
    }

    private FocusBorder.Options createColorBorderOptions(int radius) {
        float scale = 1f;
        return FocusBorder.OptionsFactory.get(scale, scale, dp2px(radius) * scale);
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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
        if (isMoveProgramList){
            return;
        }
        ViewCompat.animate(recyclerView)
                .translationXBy(PROGRAM_RECYCLERVIEW_WIDTH)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        isMoveProgramList = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isShowProgramList = true;
                        isMoveProgramList = false;
                        recyclerView.setClickable(true);
                        recyclerView.setFocusable(true);
                        recyclerView.requestFocus();
                        mColorFocusBorder.setVisible(true);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isMoveProgramList = false;
                    }
                });
    }

    private void hideProgramList(){
        if (isMoveProgramList){
            return;
        }
        ViewCompat.animate(recyclerView)
                .translationXBy(0-PROGRAM_RECYCLERVIEW_WIDTH)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        isMoveProgramList = true;
                        mColorFocusBorder.setVisible(false);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isShowProgramList = false;
                        isMoveProgramList = false;
                        recyclerView.setClickable(false);
                        recyclerView.setFocusable(false);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isMoveProgramList = false;
                    }
                });


    }

    private void hideProgramListSlow(){
        if (isMoveProgramList){
            return;
        }
        ViewCompat.animate(recyclerView)
                .translationXBy(0-PROGRAM_RECYCLERVIEW_WIDTH)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        isMoveProgramList = true;
                        mColorFocusBorder.setVisible(false);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isShowProgramList = false;
                        isMoveProgramList = false;
                        recyclerView.setClickable(false);
                        recyclerView.setFocusable(false);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isMoveProgramList = false;
                    }
                });
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
        if(ev.getAction() == MotionEvent.ACTION_UP) {//当手指离开的时候
            x2 = ev.getX();
            y2 = ev.getY();
            if (isShowProgramList){
                if (x2 >= (PROGRAM_RECYCLERVIEW_WIDTH)){
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
        if (event.getAction() == ACTION_DOWN){
            if (isShowProgramList){
                switch (event.getKeyCode()){
                    case KEYCODE_DPAD_LEFT:
                        hideProgramList();
                        return true;
                }
            }else {
                switch (event.getKeyCode()){
                    case KEYCODE_DPAD_CENTER:
                        showProgramList();
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
            }

        }
        return super.dispatchKeyEvent(event);
    }

}