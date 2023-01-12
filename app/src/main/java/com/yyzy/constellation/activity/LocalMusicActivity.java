package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.emergency.EmergencyNumber;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.adapter.LocalMusicAdapter;
import com.yyzy.constellation.entity.LocalMusicEntity;
import com.yyzy.constellation.utils.DiyProgressDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class LocalMusicActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout linLayout;
    private TextView tv;
    private TextView title, tvGeShou, tvGeMing;
    private ImageView iv, ivLast, ivPlay, ivNext;
    private RecyclerView musicRv;
    private List<LocalMusicEntity> mDatas = new ArrayList<>();
    private LocalMusicAdapter adapter;
    private int currentPos = -1;   //记录播放状态
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int currentPausePos = 0;   //记录暂停时音乐的状态位置

    @Override
    protected int initLayout() {
        return R.layout.activity_local_music;
    }

    @Override
    protected void initView() {
        iv = findViewById(R.id.details_back);
        title = findViewById(R.id.details_title);
        tvGeShou = findViewById(R.id.tvGeShou);
        tvGeMing = findViewById(R.id.tvGeMing);
        ivLast = findViewById(R.id.ivLast);
        ivPlay = findViewById(R.id.ivPlay);
        ivNext = findViewById(R.id.ivNext);
        musicRv = findViewById(R.id.music_recycler);
        linLayout = findViewById(R.id.local_music_layout);
        tv = findViewById(R.id.local_music_tv);
    }

    @Override
    protected void initData() {
        title.setText("本地音乐");
        iv.setOnClickListener(this);
        ivLast.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        musicRv.setOnClickListener(this);
        //创建适配器
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(manager);
        //加载本地数据源
        try {
            loadLocalMusicData();
        }catch (Exception e){
            e.printStackTrace();
        }
        //设置recycleView的item每一项的点击事件
        setEventListener();
    }

    //设置recycleView的item每一项的点击事件
    private void setEventListener() {
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentPos = position;
                LocalMusicEntity musicEntity = mDatas.get(position);
                playMusicInMusicEntity(musicEntity);
            }
        });
    }

    //当切换音乐时，当前的歌曲相关信息也及时发生改变
    public void playMusicInMusicEntity(LocalMusicEntity musicEntity){
        tvGeShou.setText(musicEntity.getGeShou());
        tvGeMing.setText(musicEntity.getGeMing());
        //停止音乐
        stopMusic();
        //重置多媒体播放器
        mediaPlayer.reset();
        try {
            //设置新的路径
            mediaPlayer.setDataSource(musicEntity.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //点击播放按钮播放音乐或者暂停重新播放
    //两种情况：
    // 1.从暂停到播放
    // 2.从停止到播放
    private void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if (currentPausePos == 0) {
                //从头开始播放
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //从暂停到播放
                mediaPlayer.seekTo(currentPausePos);
                mediaPlayer.start();
            }
            ivPlay.setImageResource(R.mipmap.icon_pause);
        }
    }

    //停止音乐
    private void stopMusic() {
        //停止音乐函数
        if (mediaPlayer != null) {
            currentPausePos = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(currentPausePos);
            mediaPlayer.stop();
            ivPlay.setImageResource(R.mipmap.icon_play);
        }
    }

    //暂停音乐
    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentPausePos = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            ivPlay.setImageResource(R.mipmap.icon_play);
        }
    }

    //加载本地音乐文件
    private void loadLocalMusicData() {
        //加载本地存储在本地的mp3文件
        //1.获取内容提供者对象
        ContentResolver resolver = getContentResolver();
        //2.获取本地音乐存储的URL地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //3.查找本地存储的mp3文件
        Cursor cursor = resolver.query(uri, null, null, null, null);
        //4.遍历cursor
        int id = 0;
        LocalMusicEntity musicEntity;
        while (cursor.moveToNext()) {
            String geMing = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String geShou = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            id++;
            String idNum = String.valueOf(id);
            String dvd = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));
            musicEntity = new LocalMusicEntity(idNum, geShou, geMing, time, dvd, path);
            mDatas.add(musicEntity);
        }
        if (id > 0){
            showOrHide(true);
        }else{
            showOrHide(false);
        }
        //数据源发生变化，提示适配器更新
        adapter.notifyDataSetChanged();

    }

    private void showOrHide(boolean isShow){
        if (isShow){
            musicRv.setVisibility(View.VISIBLE);
        }else{
            linLayout.setVisibility(View.VISIBLE);
            tv.setText("本地暂无音乐！");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                stopMusic();
                break;
            case R.id.ivLast:
                try {
                    if (currentPos == 0) {
                        showToast("当前已是第一首！");
                        return;
                    }
                    currentPos = currentPos - 1;
                    LocalMusicEntity lastMusic = mDatas.get(currentPos);
                    playMusicInMusicEntity(lastMusic);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.ivPlay:
                try {
                    if (currentPos == -1) {
                        showToast("请选择需要播放的音乐！");
                        return;
                    }
                    if (mediaPlayer.isPlaying()) {
                        //此时音乐正在播放，需要暂停音乐
                        pauseMusic();
                    }else{
                        //未播放音乐，需要播放音乐
                        playMusic();
                    }
                }catch (Exception e){

                }
                break;
            case R.id.ivNext:
                try {
                    if (currentPos == mDatas.size()-1) {
                        showToast("当前已是最后一首！");
                        return;
                    }
                    currentPos = currentPos + 1;
                    LocalMusicEntity nextMusic = mDatas.get(currentPos);
                    playMusicInMusicEntity(nextMusic);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
}