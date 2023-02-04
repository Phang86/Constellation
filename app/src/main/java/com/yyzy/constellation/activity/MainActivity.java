package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yyzy.constellation.R;
import com.yyzy.constellation.api.OnClickThree;
import com.yyzy.constellation.dict.activity.DictActivity;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.fragment.LuckFragment;
import com.yyzy.constellation.fragment.MeFragment;
import com.yyzy.constellation.fragment.PartnershipFragment;
import com.yyzy.constellation.fragment.StarFragment;
import com.yyzy.constellation.news.NewsActivity;
import com.yyzy.constellation.tally.TallyActivity;
import com.yyzy.constellation.tally.util.OnClickSure;
import com.yyzy.constellation.user.AppInfoActivity;
import com.yyzy.constellation.user.CallBackOkhttp;
import com.yyzy.constellation.user.GexingActivity;
import com.yyzy.constellation.user.UserLoginInfoActivity;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.BitmapUtils;
import com.yyzy.constellation.utils.CameraUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.SPUtils;
import com.yyzy.constellation.utils.StatusBarUtils;
import com.yyzy.constellation.utils.StringUtils;
import com.yyzy.constellation.utils.URLContent;
import com.yyzy.constellation.weather.activity.WeatherActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private DrawerLayout drawerLayout;
    private RadioGroup radioGroup;
    private FragmentManager manager;
    private Bundle bundle;
    private Fragment starFragment, partnershipFragment, luckFragment, meFragment;
    private TextView title, tvName,tvIp;
    private NavigationView nv;
    private ImageView imgClose, imgMore,ivRight;
    private TextView tvRight;
    private CardView cvSex,cvGexing;
    private TextView tvSex,tvGexing;
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

    private long exitTime = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    @Override
    protected void initView() {
        StarInfoEntity starInfoEntity = loadData();
        bundle = new Bundle();
        bundle.putSerializable("info", starInfoEntity);

        radioGroup = findViewById(R.id.main_rb);
        ivRight = findViewById(R.id.main_title_iv_right);
        tvRight = findViewById(R.id.main_title_tv_right);
        ivRight.setOnClickListener(this);
        Log.e("TAG", "initView: "+base_login_info);
        if (base_login_info){
            tvRight.setVisibility(View.VISIBLE);
        }else{
            tvRight.setVisibility(View.GONE);
        }
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
        tvSex = view.findViewById(R.id.tv_sex);
        cvSex = view.findViewById(R.id.cv_sex);
        tvIp = view.findViewById(R.id.tv_ip);
        cvGexing = view.findViewById(R.id.cv_gexing);
        tvGexing = view.findViewById(R.id.tv_gexing);
        cirImg.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        imgMore.setOnClickListener(this);
        cvSex.setOnClickListener(this);
        cvGexing.setOnClickListener(this);
        tvName.setText(base_user_names);
        setIpInfo();
        nv.setItemIconTintList(null);
        checkVersion();
        //取出缓存
        String imageUrl = SPUtils.getString("imageUrl", null, this);
        Log.e("TAG", "initView: "+base_imgheader);
        if (!TextUtils.isEmpty(base_imgheader)) {
            Glide.with(this).load(URLContent.BASE_URL+base_imgheader).apply(requestOptions).into(cirImg);
        }else{
            Glide.with(this).load(imageUrl).apply(requestOptions).into(cirImg);
        }
    }

    private void setIpInfo() {
        if (!TextUtils.isEmpty(base_signature)){
            tvGexing.setText(base_signature);
        }else{
            tvGexing.setText("这个人很懒，暂无个性签名......");
        }
        if (!TextUtils.isEmpty(base_ip)) {
            tvIp.setText("IP属地 : " + base_ip);
        } else {
            tvIp.setText(" IP : ----");
        }
        if (base_sex == 0){
            tvSex.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            tvSex.setTextColor(getResources().getColor(R.color.zhuBlue));
            tvSex.setText("♂");
            tvSex.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            return;
        }
        if (base_sex == 1){
            tvSex.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            tvSex.setTextColor(getResources().getColor(R.color.lightpink));
            tvSex.setText("♀");
            tvSex.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        }else{
            tvSex.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            tvSex.setTextColor(getResources().getColor(R.color.gray_600));
            tvSex.setText("性别_未知");
            tvSex.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        }
        Log.e("TAG", "setIpInfo: "+base_signature);

    }

    private void showDrawerMenu() {
        //给左侧滑动的菜单item设置监听
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.gongneng:
                        MeFragment.showDialogSure(MainActivity.this, StringUtils.setContent());
                        break;
                    case R.id.user_info:
                        intentJump(AppInfoActivity.class);
                        break;
                    case R.id.weather:
                        intentJump(WeatherActivity.class);
                        break;
                    case R.id.xingshi:
                        intentJump(DictActivity.class);
                        break;
                    case R.id.exit:
                        showExitDialog();
                        break;
                    case R.id.news:
                        intentJump(NewsActivity.class);
                        break;
                    case R.id.tally:
                        intentJump(TallyActivity.class);
                        break;
                    default:
                        //drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {
                // 主页内容
                View contentView = drawerLayout.getChildAt(0);
                // 侧边栏
                View menuView = drawerView;
                // slideOffset 值默认是0~1
                contentView.setTranslationX(menuView.getMeasuredWidth() * slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void showExitDialog() {
        openBottomDialog("确定退出星缘吗？", "确定退出", "取消",false, new OnClickThree() {
            @Override
            public void one() {
                loading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        clearUserInfo();
                        clearNotificationManger();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        finish();
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "您已成功退出星缘！", Toast.LENGTH_SHORT).show();
                    }
                }, 800);
            }

            @Override
            public void two() {

            }

            @Override
            public void three() {

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

        ViewGroup.LayoutParams mLayoutParams = nv.getLayoutParams();
        int width = getResources().getDisplayMetrics().widthPixels;
        mLayoutParams.width = width;
        nv.setLayoutParams(mLayoutParams);
    }

    //加载数据  读取Assets文件夹下的xzcontent.json文件
    private StarInfoEntity loadData() {
        String json = AssetsUtils.getJsonFromAssets(this, "xzcontent/xzcontent.json");
        StarInfoEntity infoEntity = new Gson().fromJson(json, StarInfoEntity.class);
        AssetsUtils.saveBitmapFromAssets(this, infoEntity);
        return infoEntity;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.main_rb_star:
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.hide(partnershipFragment);
                transaction.hide(luckFragment);
                transaction.hide(meFragment);
                transaction.show(starFragment);
                title.setText(getResources().getString(R.string.app_name));
                transaction.commit();
                break;
            case R.id.main_rb_partnership:
                FragmentTransaction transaction2 = manager.beginTransaction();
                transaction2.hide(starFragment);
                transaction2.hide(luckFragment);
                transaction2.hide(meFragment);
                transaction2.show(partnershipFragment);
                title.setText(getResources().getString(R.string.label_star) + "" + getResources().getString(R.string.label_partnership));
                transaction2.commit();
                break;
            case R.id.main_rb_luck:
                FragmentTransaction transaction3 = manager.beginTransaction();
                transaction3.hide(partnershipFragment);
                transaction3.hide(starFragment);
                transaction3.hide(meFragment);
                transaction3.show(luckFragment);
                title.setText(getResources().getString(R.string.label_star) + "" + getResources().getString(R.string.label_luck));
                transaction3.commit();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK  && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            MyToast.showText(getApplicationContext(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//            System.exit(0);
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_layout_cirImg:
                openBottomDialog("头像选择", "打开相机", "打开图库",false, new OnClickThree() {
                    @Override
                    public void one() {
                        takePhoto();
                    }

                    @Override
                    public void two() {
                        openAlbum();
                    }

                    @Override
                    public void three() {

                    }
                });
                break;
            case R.id.header_close:
                drawerLayout.closeDrawers();
                break;
            case R.id.main_img_more:
                drawerLayout.openDrawer(nv);
                break;
            case R.id.main_title_iv_right:
                getUserInfo();
                if (base_login_info){
                    tvRight.setVisibility(View.VISIBLE);
                    updateSpfUserInfo("login_info",false);
                    tvRight.setVisibility(View.GONE);
                }else{
                    tvRight.setVisibility(View.GONE);
                }
                intentJump(UserLoginInfoActivity.class);
                break;
            case R.id.cv_sex:
                openBottomDialog("性别选择", "男", "女",true, new OnClickThree() {
                    @Override
                    public void one() {
                        //选择男
                        requestNetUpdateSex("0");
                    }

                    @Override
                    public void two() {
                        //选择女
                        requestNetUpdateSex("1");
                    }

                    @Override
                    public void three() {
                        //选择保密
                        requestNetUpdateSex("2");
                    }
                });
                break;
            case R.id.cv_gexing:
                intentJump(GexingActivity.class);
                break;
            default:
                break;
        }

    }

    private void requestNetUpdateSex(String sex){
        int intSex = Integer.valueOf(sex);
        requestOkhttpLoadFeedback(sex, "","user", base_user_names, "/user/update/sex", new CallBackOkhttp() {
            @Override
            public void onSuccess(String resultStr) {
                if (resultStr.equals("success")) {
                    MyToast.showText(getApplicationContext(),"性别设置成功");
                    updateSpfUserInfo("sex",intSex);
                    getUserInfo();
                    setIpInfo();
                    return;
                }
                MyToast.showText(getApplicationContext(),"性别设置失败");
            }

            @Override
            public void onError(Exception e) {
                MyToast.showText(getApplicationContext(),"性别设置失败，服务器连接超时！");
            }
        });
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
     *
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
            loading();
            upload(imagePath);
        } else {
            showToast("图片获取失败");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setIpInfo();
    }

    public void upload(String imagePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                File file = new File("/storage/emulated/0/Tencent/QQ_Images/-1a45eb02c89a3895.jpg");
                File file = new File(imagePath);
                System.out.println("测试返回结果imagePath:"+imagePath);
                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(), MultipartBody.create(MediaType.parse("multipart/form-data"), new File(imagePath)))
                        .build();

                String url = URLContent.BASE_URL+"/update/user/img";
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("user",base_user_names)
                        .post(body)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("TAG", "onFailure: " );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stopLoading();
                                MyToast.showText(MainActivity.this,"服务器连接超时，头像上传失败！");

                            }
                        });

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stopLoading();
                                if (result.equals("error")) {
                                    MyToast.showText(MainActivity.this,"用户名为空，头像上传失败！");
                                    return;
                                }
                                if (result.equals("s_success")) {
                                    MyToast.showText(MainActivity.this,"头像上传成功！");
                                    updateSpfUserInfo("imgheader","");
                                    //显示图片
                                    Glide.with(MainActivity.this).load(imagePath).apply(requestOptions).into(cirImg);
                                    //放入缓存
                                    SPUtils.putString("imageUrl", imagePath, MainActivity.this);
                                    Log.e("TAG", "上传成功run: "+imagePath);
                                    //压缩图片
                                    orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
                                    //转Base64
                                    base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);
                                }else{
                                    MyToast.showText(MainActivity.this,"头像上传失败！");
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }
}