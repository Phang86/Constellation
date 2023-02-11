package com.yyzy.constellation.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.basic.BasicActivity;
import com.yyzy.constellation.databinding.ActivityMusicBinding;
import com.yyzy.constellation.music.bean.Song;
import com.yyzy.constellation.music.utils.BLog;

import org.litepal.LitePal;

import java.util.List;

public class MusicActivity extends BasicActivity {

    private String TAG = "MusicActivity";
    private Toolbar toolbar;
    private List<Song> mList;
    /**
     * 本地音乐数量
     */
    private TextView tvLocalMusicNum;



    @Override
    public int getLayoutId() {
        return R.layout.activity_music;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        ActivityMusicBinding binding = DataBindingUtil.setContentView(context, R.layout.activity_music);
        toolbar = binding.toolbarMusic;
        tvLocalMusicNum = binding.tvLocalMusicNum;
        Back(toolbar);

    }

    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(),LocalMusicOutActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        BLog.d(TAG, "onResume");
        mList = LitePal.findAll(Song.class);
        tvLocalMusicNum.setText(String.valueOf(mList.size()));
    }

}