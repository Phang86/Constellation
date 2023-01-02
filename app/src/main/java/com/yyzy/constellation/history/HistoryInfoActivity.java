package com.yyzy.constellation.history;

import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.history.bean.HistoryInfoEntity;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HistoryInfoActivity extends BaseActivity implements View.OnClickListener{


    private TextView tvTitle,tvContent,tvInfoTitle;
    private LinearLayout tvWu;
    private ImageView imgBack,imgTitle;
    private CardView cardView;
    private ScrollView sv;
    private Bitmap bitmap;
    //底部弹窗
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_history_info;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.details_title);
        imgBack = findViewById(R.id.details_back);
        tvContent = findViewById(R.id.history_info_tv_content);
        tvInfoTitle = findViewById(R.id.history_info_tv_title);
        imgTitle = findViewById(R.id.history_info_imgTitle);
        cardView = findViewById(R.id.history_info_carView);
        sv = findViewById(R.id.history_info_sv);
        tvWu = findViewById(R.id.history_info_tv_wu);
        //titleLayout.setBackgroundColor();
        tvTitle.setText("历史详情");
        imgBack.setOnClickListener(this);


        Intent intent = getIntent();
        String info_id = intent.getStringExtra("info_id");
        String descURL = URLContent.getHistoryDescURL("1.0", info_id);
        loadDatas(descURL);

    }

    @Override
    protected void initData() {
        imgTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeAvatar();
                return true;
            }
        });
    }

    @Override
    public void onSuccess(String result) {
        try {
            sv.setVisibility(View.VISIBLE);
            HistoryInfoEntity entity = new Gson().fromJson(result, HistoryInfoEntity.class);
            List<HistoryInfoEntity.ResultBean> beans = entity.getResult();
            HistoryInfoEntity.ResultBean bean = beans.get(0);
            String title = bean.getTitle();
            String content = bean.getContent();
            String pic = bean.getPic();
            tvInfoTitle.setText(title+"。");
            tvContent.setText(content);
            if (!TextUtils.isEmpty(pic)) {
                imgTitle.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                Glide.with(this).load(pic).into(imgTitle);
                
            }else{
                imgTitle.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
            sv.setVisibility(View.GONE);
            tvWu.setVisibility(View.VISIBLE);
        }
    }

    public void changeAvatar() {
        bitmap = ((BitmapDrawable) imgTitle.getDrawable()).getBitmap();
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomView = getLayoutInflater().inflate(R.layout.dialog_select_photo, null);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        TextView tvEnjoy = bottomView.findViewById(R.id.tv_take_pictures);
        TextView tvLoad = bottomView.findViewById(R.id.tv_open_album);
        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);
        tvEnjoy.setText("分享图片");
        tvLoad.setText("保存图片");

        //分享图片
        tvEnjoy.setOnClickListener(v -> {
            shareSingleImage();
            bottomSheetDialog.cancel();
        });

        //保存图片
        tvLoad.setOnClickListener(v -> {
            saveImage(bitmap);
            bottomSheetDialog.cancel();
        });

        //取消
        tvCancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });
        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.history_info_imgTitle:
                bigImageLoader(bitmap);
                break;
        }
    }

    private void bigImageLoader(Bitmap bitmap) {
        Dialog dialog = new Dialog(this);
        ImageView image = new ImageView(this);
        image.setImageBitmap(bitmap);
        dialog.setContentView(image);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.cancel();
            }
        });
    }

    //保存图片
    private void saveImage(Bitmap bitmap) {
        boolean isSaveSuccess = saveImageToGallery(this, bitmap);
        if (isSaveSuccess) {
            MyToast.showText(this, "保存图片成功", Toast.LENGTH_SHORT);
        } else {
            MyToast.showText(this, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT);
        }
    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

////设置分享列表的标题，并且每次都显示分享列表
//    public void shareText() {
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
//        shareIntent.setType("text/plain");
//        startActivity(Intent.createChooser(shareIntent, "分享到"));
//    }

    //分享单张图片
    public void shareSingleImage() {
        //由文件得到uri
        Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));

    }

    /**
     * 异步线程下载图片
     */

    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */

    public Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    class Task extends AsyncTask {
        protected Void doInBackground(String... params) {
            bitmap = GetImageInputStream((String) params[0]);
            return null;

        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Message message = new Message();
            message.what = 0x123;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}