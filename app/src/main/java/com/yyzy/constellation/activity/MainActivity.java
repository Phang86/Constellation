package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yyzy.constellation.R;
import com.yyzy.constellation.dict.activity.DictActivity;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.fragment.LuckFragment;
import com.yyzy.constellation.fragment.MeFragment;
import com.yyzy.constellation.fragment.PartnershipFragment;
import com.yyzy.constellation.fragment.StarFragment;
import com.yyzy.constellation.receiver.IntentReceiver;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.BitmapUtils;
import com.yyzy.constellation.utils.CameraUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.SPUtils;
import com.yyzy.constellation.utils.StringUtils;
import com.yyzy.constellation.weather.activity.WeatherActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import skin.support.SkinCompatManager;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{
    private DrawerLayout drawerLayout;
    private RadioGroup radioGroup;
    private FragmentManager manager;
    private Bundle bundle;
    private Fragment starFragment,partnershipFragment,luckFragment,meFragment;
    private TextView title,tvName;
    private NavigationView nv;
    private ImageView imgClose,imgMore;
    private NotificationManager NotiManager;
    //权限请求
    private RxPermissions rxPermissions;
    //是否拥有权限
    private boolean hasPermissions = false;
    //底部弹窗
    private BottomSheetDialog bottomSheetDialog;
    //弹窗视图
    private View bottomView;
    //存储拍完照后的图片
    private File outputImagePath;
    //启动相机标识
    public static final int TAKE_PHOTO = 1;
    //启动相册标识
    public static final int SELECT_PHOTO = 2;
    //图片控件
    private CircleImageView cirImg;
    //Base64
    private String base64Pic;
    //拍照和相册获取图片的Bitmap
    private Bitmap orc_bitmap;
    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)  //不做磁盘缓存
            .skipMemoryCache(true);         //不做内存缓存

    private BroadcastReceiver receivers = new IntentReceiver();

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        StarInfoEntity starInfoEntity = loadData();
        bundle = new Bundle();
        bundle.putSerializable("info",starInfoEntity);

        radioGroup = findViewById(R.id.main_rb);
        //为RadioGroup控件设置监听，判断点击哪个
        radioGroup.setOnCheckedChangeListener(this);
        //分别实例化对应的Fragment
        starFragment = new StarFragment();
        starFragment.setArguments(bundle);
        partnershipFragment = new PartnershipFragment();
        partnershipFragment.setArguments(bundle);
        luckFragment = new LuckFragment();
        luckFragment.setArguments(bundle);
        meFragment = new MeFragment();
        meFragment.setArguments(bundle);
        title = findViewById(R.id.main_tv_title);
        imgMore = findViewById(R.id.main_img_more);
        //实现左侧滑动菜单
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //滑动后的菜单
        nv = findViewById(R.id.nav_view);
        showDrawerMenu();
        View view = nv.getHeaderView(0);
        cirImg = view.findViewById(R.id.header_layout_cirImg);
        tvName = view.findViewById(R.id.header_layout_tv_name);
        imgClose = view.findViewById(R.id.header_close);
        cirImg.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        imgMore.setOnClickListener(this);
        SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        String name = sp.getString("name", "");
        tvName.setText(name);
        nv.setItemIconTintList(null);
        checkVersion();
        //取出缓存
        String imageUrl = SPUtils.getString("imageUrl",null,this);
        if(imageUrl != null){
            Glide.with(this).load(imageUrl).apply(requestOptions).into(cirImg);
        }

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receivers,filter);
    }



    protected String findByKey(String key) {
        SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    protected void insertVal( String key, String val) {
        SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    private void showDrawerMenu() {
        //给左侧滑动的菜单item设置监听
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    case R.id.skin:
                        String skin = findByKey("skin");
                        if (skin.equals("night")){
                            // 恢复应用默认皮肤
                            SkinCompatManager.getInstance().restoreDefaultTheme();
                            insertVal("skin","defualt");
                        }else {
                            SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                            insertVal("skin","night");
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.gongneng:
                        MeFragment.showDialogSure(MainActivity.this,"功能介绍", StringUtils.setContent());
                        break;
                    case R.id.user_info:
                        intent.setClass(MainActivity.this, AppInfoActivity.class);
                        startActivity(intent);
                    break;
                    case R.id.weather:
                        intent.setClass(MainActivity.this, WeatherActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.xingshi:
                        intent.setClass(MainActivity.this, DictActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.exit:
                        //dialogShow();
                        showDefaultDialog();
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        //drawerLayout.closeDrawers();
                        break;
                }
                //item.setCheckable(true);//设置可选项
                //item.setChecked(true);//设置被选中
                //drawerLayout.closeDrawers();//关闭菜单栏
                return false;
            }
        });
    }

    private void showDefaultDialog() {
        AlertDialogUtils dialogUtils = AlertDialogUtils.getInstance();
        AlertDialogUtils.showConfirmDialog(MainActivity.this,"温馨提示","您确定退出吗？","确定","取消");
        dialogUtils.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(androidx.appcompat.app.AlertDialog dialog) {
                //点击确认按钮要做的事情
                dialog.dismiss();
                //获取存储在sp里面的用户名和密码以及两个复选框状态
                SharedPreferences sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                //清空所有
                //editor.remove("username");
                //editor.remove("password");
                //提交
                DiyProgressDialog dialog1 = new DiyProgressDialog(MainActivity.this,"退出登录中...");
                dialog1.setCancelable(false);//设置不能通过后退键取消
                dialog1.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_MAIN);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        editor.clear();
                        ed.clear();
                        editor.commit();
                        ed.commit();
                        SPUtils.remove("imageUrl",MainActivity.this);
                        finish();
                        startActivity(intent);
                        //取消登录通知
                        NotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        NotiManager.cancel(1);
                        //取消加载
                        dialog1.cancel();
                        Toast.makeText(MainActivity.this, "您已成功退出星座缘！", Toast.LENGTH_SHORT).show();
                    }
                },1200);
            }

            @Override
            public void onNegativeButtonClick(androidx.appcompat.app.AlertDialog dialog) {
                //点击取消按钮关闭弹框
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void initData() {
        //为布局管理器添加相应的Fragment
        //创建碎片管理者对象
        manager = getSupportFragmentManager();
        //创建碎片处理事务的对象
        FragmentTransaction transaction = manager.beginTransaction();
        //将四个Fragment添加到布局中
        transaction.add(R.id.main_layout_linear, starFragment);
        transaction.add(R.id.main_layout_linear, partnershipFragment);
        transaction.add(R.id.main_layout_linear, luckFragment);
        //transaction.add(R.id.main_layout_linear, meFragment);
        transaction.hide(partnershipFragment);
        transaction.hide(luckFragment);
        //transaction.hide(meFragment);
        //提交事务
        transaction.commit();

    }

    //加载数据  读取Assets文件夹下的xzcontent.json文件
    private StarInfoEntity loadData() {
        String json = AssetsUtils.getJsonFromAssets(this, "xzcontent/xzcontent.json");
        Gson gson = new Gson();
        StarInfoEntity infoEntity = gson.fromJson(json, StarInfoEntity.class);
        AssetsUtils.saveBitmapFromAssets(this,infoEntity);
        return infoEntity;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = manager.beginTransaction();
        switch (checkedId){
            case R.id.main_rb_star:
                transaction.hide(partnershipFragment);
                transaction.hide(luckFragment);
                transaction.hide(meFragment);
                transaction.show(starFragment);
                title.setText("星座缘");
                break;
            case R.id.main_rb_partnership:
                transaction.hide(starFragment);
                transaction.hide(luckFragment);
                transaction.hide(meFragment);
                transaction.show(partnershipFragment);
                title.setText("星座配对");
                break;
            case R.id.main_rb_luck:
                transaction.hide(partnershipFragment);
                transaction.hide(starFragment);
                transaction.hide(meFragment);
                transaction.show(luckFragment);
                title.setText("星座运势");
                break;
//            case R.id.main_rb_me:
//                transaction.hide(partnershipFragment);
//                transaction.hide(luckFragment);
//                transaction.hide(starFragment);
//                transaction.show(meFragment);
//                title.setText("我的");
//                break;
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receivers);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_layout_cirImg:
                changeAvatar(v);
                break;
            case R.id.header_close:
                drawerLayout.closeDrawers();
                break;
            case R.id.main_img_more:
                drawerLayout.openDrawer(nv);
                break;
            default:
                break;
        }

    }


    /**
     * 检查版本
     */
    private void checkVersion() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果你是在Fragment中，则把this换成getActivity()
            rxPermissions = new RxPermissions(this);
            //权限请求
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {//申请成功
                            //showToast("已获取相机权限");
                            hasPermissions = true;
                        } else {//申请失败
                            showToast("相机权限未开启");
                            hasPermissions = false;
                        }
                    });
        } else {
            //Android6.0以下
            showToast("无需请求动态权限");
        }
    }

    /**
     * 更换头像
     *
     * @param view
     */
    public void changeAvatar(View view) {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomView = getLayoutInflater().inflate(R.layout.dialog_select_photo, null);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);

        //拍照
        tvTakePictures.setOnClickListener(v -> {
            takePhoto();
            //showToast("拍照");
            bottomSheetDialog.cancel();
        });
        //打开相册
        tvOpenAlbum.setOnClickListener(v -> {
            openAlbum();
            //showToast("打开相册");
            bottomSheetDialog.cancel();
        });
        //取消
        tvCancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });
        bottomSheetDialog.show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (!hasPermissions) {
            showToast("未获取到权限");
            checkVersion();
            return;
        }
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getExternalCacheDir(),
                filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath);
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        if (!hasPermissions) {
            showToast("未获取到权限");
            checkVersion();
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }

    /**
     * 返回到Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照后返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //显示图片
                    displayImage(outputImagePath.getAbsolutePath());
                }
                break;
            //打开相册后返回
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4及以上系统使用这个方法处理图片
                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
                    }
                    //显示图片
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 通过图片路径显示图片
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(cirImg);
            //放入缓存
            SPUtils.putString("imageUrl",imagePath,this);
            //压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);

        } else {
            showToast("图片获取失败");
        }
    }

    //退出弹出框
//    public void dialogShow() {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View v = inflater.inflate(R.layout.diy_alert_dialog, null);
//        TextView content = (TextView) v.findViewById(R.id.dialog_content);
//        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
//        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
//        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
//        final Dialog dialog = builder.create();
//        dialog.show();
//        dialog.setCancelable(false);
//        dialog.getWindow().getDecorView().setBackground(null);
//        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
//        //设置隐藏dialog默认的背景
//        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
//        content.setText("确定退出吗？");
//        btn_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                dialog.dismiss();
//            }
//        });
//    }
}