package com.yyzy.constellation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.yyzy.constellation.R;
import com.yyzy.constellation.fragment.MeFragment;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.Mydialog;

public class IntentReceiver extends BroadcastReceiver {

    private static IntentReceiver mInstance;

    public IntentReceiver() {

    }

    public static IntentReceiver getInstance() {
        if (mInstance == null) {
            synchronized (IntentReceiver.class) {
                if (mInstance == null) {
                    mInstance = new IntentReceiver();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("TAG", "onReceive: " + "网络连接成功！");
        } else {
            try {
                Dialog(context);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private final void Dialog(final Context context) {
        Mydialog mydialog = new Mydialog(context, "网络安全设置", "后台检测到手机网络未连接，请您正确设置网络连接!");
        mydialog.show();
        mydialog.setCancelable(false);
        mydialog.setDialogSize();
        mydialog.setClickSure(new Mydialog.ClickSure() {
            @Override
            public void onClickSure() {
                context.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
                mydialog.dismiss();
            }
        });

//        AlertDialogUtils dialogUtils = AlertDialogUtils.getInstance();
//        AlertDialogUtils.showConfirmDialog(context,"网络安全设置","后台检测到手机网络未连接，请您正确设置网络连接!","前往设置","取消");
//        dialogUtils.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
//            @Override
//            public void onPositiveButtonClick(AlertDialog dialog) {
//                context.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onNegativeButtonClick(AlertDialog dialog) {
//                //context.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
//                dialog.cancel();
//            }
//        });
    }
}