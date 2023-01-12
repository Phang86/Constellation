package com.yyzy.constellation.utils;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.yyzy.constellation.R;
import com.yyzy.constellation.receiver.IntentReceiver;

public class AlertDialogUtils {
    private static View view_custom;
    public static AlertDialog.Builder builder;
    public static AlertDialog alert;
    public static TextView tv_dialog_content;
    public static Button dialog_cancelBtn, dialog_confirmBtn;
    private static AlertDialogUtils mInstance;

    public AlertDialogUtils(){

    }

    public static AlertDialogUtils getInstance() {
        if (mInstance == null) {
            synchronized (AlertDialogUtils.class) {
                if (mInstance == null) {
                    mInstance = new AlertDialogUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * todo 带有确认取消按钮的自定义dialog
     *
     * @param context 上下文对象
//     * @param title   标题
     * @param content 内容
     */
    public static void showConfirmDialog(Context context, String content,String config,String cancel) {
        builder = new AlertDialog.Builder(context);
        alert = builder.create();
        alert.show();
        alert.getWindow().getDecorView().setBackground(null);
        setWindowSize();


        //引入布局
        view_custom = LayoutInflater.from(context).inflate(R.layout.alert_dialog_defaut, null, false);
//        tv_dialog_title = view_custom.findViewById(R.id.title);
//        tv_dialog_title.setText(title);
        tv_dialog_content = view_custom.findViewById(R.id.content);
        tv_dialog_content.setText(content);
        dialog_cancelBtn = view_custom.findViewById(R.id.cancel_btn);
        dialog_confirmBtn = view_custom.findViewById(R.id.config_btn);
        dialog_confirmBtn.setText(config);
        dialog_cancelBtn.setText(cancel);
        alert.setCancelable(false); //点击空白处不关闭弹窗

        //为取消按钮设置点击监听
        dialog_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDialogButtonClickListener != null) {
                    mOnDialogButtonClickListener.onNegativeButtonClick(alert);
                }
            }
        });
        //为确认按钮设置点击监听
        dialog_confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDialogButtonClickListener != null) {
                    mOnDialogButtonClickListener.onPositiveButtonClick(alert);
                }
            }
        });
        //使用布局
        alert.getWindow().setContentView(view_custom);
    }

    public static void setWindowSize(){
        //获取窗口对象
        Window window = alert.getWindow();
        //获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕尺寸
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = d.getWidth();
        wlp.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }

    //todo 按钮点击回调接口
    public static OnDialogButtonClickListener mOnDialogButtonClickListener;

    public void setMonDialogButtonClickListener(OnDialogButtonClickListener listener) {
        this.mOnDialogButtonClickListener = listener;
    }

    public interface OnDialogButtonClickListener {
        void onPositiveButtonClick(AlertDialog dialog); //确认

        void onNegativeButtonClick(AlertDialog dialog); //取消
    }
}
