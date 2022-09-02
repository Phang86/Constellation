package com.yyzy.constellation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.AlertDialogUtils;

public class IntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isAvailable()){
//            Toast toast = Toast.makeText(context,"网络连接成功",Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP,0,0);
//            toast.show();
        }else {
            try {
                Dialog(context);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private final void Dialog(final Context context) {
        AlertDialogUtils dialogUtils = AlertDialogUtils.getInstance();
        AlertDialogUtils.showConfirmDialog(context,"网络安全设置","后台检测到手机网络未连接，请您正确设置网络连接!","前往设置","取消");
        dialogUtils.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(AlertDialog dialog) {
                context.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
                dialog.dismiss();
            }

            @Override
            public void onNegativeButtonClick(AlertDialog dialog) {
                //context.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
                dialog.dismiss();
            }
        });
    }
}