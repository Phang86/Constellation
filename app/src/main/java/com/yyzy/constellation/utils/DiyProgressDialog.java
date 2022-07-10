package com.yyzy.constellation.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyzy.constellation.R;

public class DiyProgressDialog extends Dialog  {

    public TextView messageTv;

    public DiyProgressDialog(Context context) {
        this(context, R.style.MyDialogStyle, "");
    }

    public DiyProgressDialog(Context context, String string) {
        this(context, R.style.MyDialogStyle, string);
    }

    public DiyProgressDialog(Context context, int theme, String s) {
        super(context, theme);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.diy_progress_dialog);
        messageTv = (TextView) findViewById(R.id.tv_message);
        messageTv.setText(s);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().getAttributes().dimAmount = 0f;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
