package com.yyzy.constellation.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.yyzy.constellation.R;
import com.yyzy.constellation.basic.BasicActivity;
import com.yyzy.constellation.databinding.ActivityLocalMusicOutBinding;
import com.yyzy.constellation.music.adapter.MusicListAdapter;
import com.yyzy.constellation.music.bean.Song;
import com.yyzy.constellation.music.utils.BLog;
import com.yyzy.constellation.music.utils.Constant;
import com.yyzy.constellation.music.utils.MusicUtils;
import com.yyzy.constellation.music.view.MusicRoundProgressView;
import com.yyzy.constellation.utils.SPUtils;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalMusicOutActivity extends BasicActivity implements MediaPlayer.OnCompletionListener {


    private final String TAG = "LocalMusicOutActivity";
    private Toolbar toolbar;
    /**
     * 歌曲列表
     */
    private RecyclerView rvMusic;
    /**
     * 定位当前音乐按钮
     */
    private MaterialButton btnLocationPlayMusic;
    /**
     * 扫描歌曲布局
     */
    private LinearLayout layScanMusic;
    /**
     * 歌曲适配器
     */
    private MusicListAdapter mAdapter;
    /**
     * 歌曲列表
     */
    private List<Song> mList = new ArrayList<>();

    /**
     * 上一次点击的位置
     */
    private int oldPosition = -1;
    /**
     * 初始化控件级页面的业务逻辑
     */

    /**
     * 底部logo图标，点击之后弹出当前播放歌曲详情页
     */
    private ShapeableImageView ivLogo;
    /**
     * 底部当前播放歌名
     */
    private MaterialTextView tvSongName;
    /**
     * 底部当前歌曲控制按钮, 播放和暂停
     */
    private MaterialButton btnPlay;
    /**
     * 音频播放器
     */
    private MediaPlayer mediaPlayer;
    /**
     * 记录当前播放歌曲的位置
     */
    public int mCurrentPosition = -1;

    /**
     * 自定义进度条
     */
    private MusicRoundProgressView musicProgress;

    /**
     * 音乐进度间隔时间
     */
    private static final int INTERNAL_TIME = 1000;

    /**
     * 图片动画
     */
    private ObjectAnimator logoAnimation;
    /**
     * 本地音乐数据  不是缓存
     */
    private boolean localMusicData = false;

    private void initView() {
        ActivityLocalMusicOutBinding binding = DataBindingUtil.setContentView(context, R.layout.activity_local_music_out);
        toolbar = binding.toolbar;
        rvMusic = binding.rvMusic;
        layScanMusic = binding.layScanMusic;
        btnLocationPlayMusic = binding.btnLocationPlayMusic;

        ivLogo = binding.ivLogo;
        tvSongName = binding.tvSongName;
        btnPlay = binding.btnPlay;
        musicProgress = binding.musicProgress;

        Back(toolbar);

        //当进入页面时发现有缓存数据时，则隐藏扫描布局，直接获取本地数据。
        if (SPUtils.getBoolean(Constant.LOCAL_MUSIC_DATA, false, context)) {
            //省去一个点击扫描的步骤
            layScanMusic.setVisibility(View.GONE);
            permissionsRequest();
        }

        rvMusic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //暂停
                    showLocationMusic(false);
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //滑动
                    showLocationMusic(true);
                }
            }
        });

        initAnimation();
    }

    /**
     * 显示定位当前音乐图标
     */
    private void showLocationMusic(boolean isScroll) {
        //先判断是否存在播放音乐
        if (oldPosition != -1) {
            if (isScroll) {
                //滑动
                btnLocationPlayMusic.setVisibility(View.VISIBLE);
            } else {
                //延时隐藏  Android 11（即API 30:Android R）弃用了Handler默认的无参构造方法,所以传入了Looper.myLooper()
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnLocationPlayMusic.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        }
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initView();

        List<Song> list = LitePal.findAll(Song.class);
        BLog.d(TAG,list.size() + "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_local_music_out;
    }

    /**
     * 动态权限请求
     */
    private void permissionsRequest() {
        PermissionX.init(this).permissions(
                //写入文件
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                    }
                })
                .setDialogTintColor(R.color.white, R.color.app_color)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            //通过后的业务逻辑
                            getMusicList();
                        } else {
                            show("您拒绝了如下权限：" + deniedList);
                        }
                    }
                });
    }

    /**
     * 获取音乐列表
     */
    private void getMusicList() {
        localMusicData = SPUtils.getBoolean(Constant.LOCAL_MUSIC_DB, false, context);

        //清除列表数据
        mList.clear();
        if (localMusicData) {
            //有数据则读取本地数据库的数据
            BLog.d(TAG, "读取本地数据库 ====>");
            mList = LitePal.findAll(Song.class);
        } else {
            //没有数据则扫描本地文件夹获取音乐数据
            BLog.d(TAG, "扫描本地文件夹 ====>");
            mList = MusicUtils.getMusicData(this);
        }

        if (mList != null && mList.size() > 0) {
            //显示本地音乐
            showLocalMusicData();

            if (!localMusicData) {
                //添加到本地数据库中
                addLocalDB();
            }

        } else {
            show("兄嘚，你是一无所有啊~");
        }

    }

    /**
     * 添加到本地数据库
     */
    private void addLocalDB() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mList.size(); i++) {
                    Song song = new Song();
                    song.setSinger(mList.get(i).getSinger());
                    song.setSong(mList.get(i).getSong());
                    song.setAlbumId(mList.get(i).getAlbumId());
                    song.setAlbum(mList.get(i).getAlbum());
                    song.setPath(mList.get(i).getPath());
                    song.setDuration(mList.get(i).getDuration());
                    song.setSize(mList.get(i).getSize());
                    song.setCheck(mList.get(i).isCheck());
                    song.save();
                }
                List<Song> list = LitePal.findAll(Song.class);
                if (list.size() > 0) {
                    SPUtils.putBoolean(Constant.LOCAL_MUSIC_DB, true, context);
                    BLog.d("TAG", "添加到本地数据库的音乐：" + list.size() + "首");
                }

            }
        });
    }

    /**
     * 显示本地音乐数据
     */
    private void showLocalMusicData() {
        //指定适配器的布局和数据源
        mAdapter = new MusicListAdapter(R.layout.item_music_rv_list, mList);
        //线性布局管理器，可以设置横向还是纵向，RecyclerView默认是纵向的，所以不用处理,如果不需要设置方向，代码还可以更加的精简如下
        rvMusic.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        rvMusic.setAdapter(mAdapter);
        //是否有缓存歌曲
        SPUtils.putBoolean(Constant.LOCAL_MUSIC_DATA, true, context);
        layScanMusic.setVisibility(View.GONE);


        //item的点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_music) {
                    //控制当前播放位置
                    playPositionControl(position);

                    mCurrentPosition = position;
                    changeSong(mCurrentPosition);
                }
            }
        });

        //item的点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_music) {
                    //控制当前播放位置
                    playPositionControl(position);
                    mCurrentPosition = position;
                    //播放当前歌曲
                    changeSong(position);
                }
            }
        });
    }

    /**
     * 控制播放位置
     *
     * @param position
     */
    private void playPositionControl(int position) {
        if (oldPosition == -1) {
            //未点击过 第一次点击
            oldPosition = position;
            mList.get(position).setCheck(true);
        } else {
            //大于 1次
            if (oldPosition != position) {
                mList.get(oldPosition).setCheck(false);
                mList.get(position).setCheck(true);
                //重新设置位置，当下一次点击时position又会和oldPosition不一样
                oldPosition = position;
            }
        }
        mAdapter.changeState();
    }

    /**
     * 切换歌曲
     */
    private void changeSong(int position) {

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            //监听音乐播放完毕事件，自动下一曲
            mediaPlayer.setOnCompletionListener(this);
        }

        try {
            //切歌前先重置，释放掉之前的资源
            mediaPlayer.reset();
            BLog.i("TAG", mList.get(position).path);
            //设置播放音频的资源路径
            mediaPlayer.setDataSource(mList.get(position).path);
            //设置播放的歌名和歌手
            tvSongName.setText(mList.get(position).song + " - " + mList.get(position).singer);
            //如果内容超过控件，则启用跑马灯效果
            tvSongName.setSelected(true);
            //开始播放前的准备工作，加载多媒体资源，获取相关信息
            mediaPlayer.prepare();
            //开始播放音频
            mediaPlayer.start();
            //设置歌曲所在专辑的封面图片
            ivLogo.setImageBitmap(MusicUtils.getAlbumPicture(context,mList.get(position).getPath(),1));

            musicProgress.setProgress(0,mediaPlayer.getDuration());
            //更新进度
            updateProgress();

            //播放按钮控制
            if (mediaPlayer.isPlaying()) {
                btnPlay.setIcon(getDrawable(R.mipmap.icon_pause));
                btnPlay.setIconTint(getResources().getColorStateList(R.color.gold_color));
                logoAnimation.resume();
            } else {

                btnPlay.setIcon(getDrawable(R.mipmap.icon_play));
                btnPlay.setIconTint(getResources().getColorStateList(R.color.white));
                logoAnimation.pause();
            }
            //图片旋转动画
            logoAnimation.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 播放完成之后自动下一曲
     *
     * @param mp
     */
    public void onCompletion(MediaPlayer mp) {
        //停止旋转并重置
        logoAnimation.end();
        int position = -1;
        if (mList != null) {
            if (mCurrentPosition == mList.size() - 1) {
                //当前为最后一首歌时,则切换到列表的第一首歌
                position = mCurrentPosition = 0;
            } else {
                position = ++mCurrentPosition;
            }
        }

        //移动播放位置
        playPositionControl(position);
        //切歌
        changeSong(position);

    }


    /**
     * 页面点击事件
     * @param view 控件
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan_local_music:
                //扫描本地音乐
                permissionsRequest();
                break;
            case R.id.btn_location_play_music:
                //定位当前播放歌曲
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvMusic.getLayoutManager();
                linearLayoutManager.scrollToPositionWithOffset(oldPosition, 0);
                break;
            case R.id.btn_play:
                //控制音乐 播放、暂停
                if (mediaPlayer == null) {
                    if (mList.size() == 0) {
                        show("没有可播放的音乐");
                        return;
                    }
                    //没有播放过音乐 ,点击之后播放第一首
                    oldPosition = 0;
                    mCurrentPosition = 0;
                    mList.get(mCurrentPosition).setCheck(true);
                    mAdapter.changeState();
                    changeSong(mCurrentPosition);
                } else {
                    //播放过音乐  暂停或者播放
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        btnPlay.setIcon(getDrawable(R.mipmap.icon_play));
                        btnPlay.setIconTint(getResources().getColorStateList(R.color.white));
                        logoAnimation.pause();
                    } else {
                        mediaPlayer.start();
                        btnPlay.setIcon(getDrawable(R.mipmap.icon_pause));
                        btnPlay.setIconTint(getResources().getColorStateList(R.color.gold_color));
                        logoAnimation.resume();
                    }
                }
                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            // 展示给进度条和当前时间
            int progress = mediaPlayer.getCurrentPosition();
            musicProgress.setProgress(progress, mediaPlayer.getDuration());

            //更新进度
            updateProgress();
            return true;
        }
    });

    /**
     * 更新进度
     */
    private void updateProgress() {
        // 使用Handler每间隔1s发送一次空消息，通知进度条更新
        // 获取一个现成的消息
        Message msg = Message.obtain();
        // 使用MediaPlayer获取当前播放时间除以总时间的进度
        int progress = mediaPlayer.getCurrentPosition();
        msg.arg1 = progress;
        mHandler.sendMessageDelayed(msg, INTERNAL_TIME);
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        logoAnimation = ObjectAnimator.ofFloat(ivLogo, "rotation", 0.0f, 360.0f);
        logoAnimation.setDuration(6000);
        logoAnimation.setInterpolator(new LinearInterpolator());
        logoAnimation.setRepeatCount(-1);
        logoAnimation.setRepeatMode(ObjectAnimator.RESTART);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止播放
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }
    }
}