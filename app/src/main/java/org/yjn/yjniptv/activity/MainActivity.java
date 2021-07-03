package org.yjn.yjniptv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.yjn.common.Utils.L;
import org.yjn.yjniptv.R;
import org.yjn.yjniptv.adapter.ProgramAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.yjn.yjniptv.data.ProgramList.programHashMap;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.videoPlayer)
    StandardGSYVideoPlayer videoPlayer;
    @BindView(R.id.rv_program)
    TvRecyclerView recyclerView;

    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private boolean cache = false;
    private int PROGRAM_RECYCLERVIEW_WIDTH = (int) getResources().getDimension(R.dimen.program_recyclerview_width);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initView();
        initVideoPlayer();
    }

    private void initView() {
        ProgramAdapter programAdapter = new ProgramAdapter();
        recyclerView.setAdapter(programAdapter);
        programAdapter.programListOnclick(new ProgramAdapter.IListSwitchChannel() {
            @Override
            public void onClick(View view, int position) {
                L.i(String.valueOf(programHashMap.get(position).getUrl()));
                switchChannel(position);
            }
        });


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

        switchChannel(1);
        gsyVideoOptionBuilder.build(videoPlayer);
    }

    private void switchChannel(int num) {
        String url = String.valueOf(programHashMap.get(num).getUrl());
        String title = String.valueOf(programHashMap.get(num).getTitle());
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

    private void hideProgramList(){
        ViewCompat.animate(recyclerView)
                .translationXBy(PROGRAM_RECYCLERVIEW_WIDTH)
                .setInterpolator(new OvershootInterpolator());
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
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}