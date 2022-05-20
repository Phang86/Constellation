package com.yyzy.constellation.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.yyzy.constellation.activity.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

public class DialogUtils {
//    public static void setDialog(Context context, String titleMsg, String contentMsg, String btnCancel, String btnConfirm){
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
//                .setTitle(titleMsg)
//                .setMessage(contentMsg)
//                .setPositiveButton(btnConfirm, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //获取存储在sp里面的用户名和密码以及两个复选框状态
//                        SharedPreferences sharedPreferences = context.getSharedPreferences("busApp", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        //清空所有
//                        editor.clear();
//                        //提交
//                        editor.commit();
//                    }
//                })
//                .setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //取消按钮
//                        showToast(context,""+btnCancel);
//                    }
//                });
//        dialog.create().show();
//
//    }

    public static void showToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
