package com.yyzy.constellation.tally.util;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.MainActivity;
import com.yyzy.constellation.tally.TallyJiluActivity;
import com.yyzy.constellation.tally.TallySearchActivity;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;
import com.yyzy.constellation.tally.db.TallyManger;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.MyToast;

import java.util.List;

public class TallyMoreDialog extends BottomSheetDialog implements View.OnClickListener {

    private ImageView imgClose;
    private CardView cvSet,cvTallyInfo,cvTallyJilu,cvSearch;
    private RefreshListener refresh;

    public void setRefresh(RefreshListener refresh) {
        this.refresh = refresh;
    }

    public interface RefreshListener{
        void refreshData();
    }

    public TallyMoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tally_more_dialog);
        initView();
    }

    private void initView() {
        imgClose = findViewById(R.id.tally_more_dialog_img);
        cvSearch = findViewById(R.id.tally_more_dialog_cv_search);
        cvTallyJilu = findViewById(R.id.tally_more_dialog_cv_tallyJilu);
//        cvTallyInfo = findViewById(R.id.tally_more_dialog_cv_tallyInfo);
        cvSet = findViewById(R.id.tally_more_dialog_cv_set);

        imgClose.setOnClickListener(this);
        cvSearch.setOnClickListener(this);
        cvTallyJilu.setOnClickListener(this);
//        cvTallyInfo.setOnClickListener(this);
        cvSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tally_more_dialog_img:
                cancel();
                break;
            case R.id.tally_more_dialog_cv_search:
                startIntent(getContext(), TallySearchActivity.class);
                cancel();
                break;
            case R.id.tally_more_dialog_cv_tallyJilu:
                startIntent(getContext(), TallyJiluActivity.class);
                cancel();
                break;
//            case R.id.tally_more_dialog_cv_tallyInfo:
//                Log.e("TAG", "onClick: tally_more_dialog_cv_tallyInfo");
//                cancel();
//                break;
            case R.id.tally_more_dialog_cv_set:
                List<TallyLvItemBean> data = TallyManger.getAll();
                if (data.size() > 0) {
                    AlertDialogUtils dialog = AlertDialogUtils.getInstance();
                    AlertDialogUtils.showConfirmDialog(getContext(), "友情提示", "所有记录即将被清空，数据一旦清空则不可恢复！您继续执行该操作吗？", "继续", "算了，再想想");
                    dialog.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick(AlertDialog dialog) {
                            //执行删除全部记录操作
                            TallyManger.delAllForJilutb();
                            //调用接口，通知Activity刷新数据
                            refresh.refreshData();
                            dialog.cancel();
                        }

                        @Override
                        public void onNegativeButtonClick(AlertDialog dialog) {
                            dialog.cancel();
                        }
                    });
                }else{
                    MyToast.showText(getContext(), "记录表为空，不需要清理！");
                }
                cancel();
                break;
        }
    }

    private void startIntent(Context context, Class cla){
        Intent intent = new Intent(context, cla);
        context.startActivity(intent);
    }


    public void setDialogSize(){
        //获取窗口对象
        Window window = getWindow();
        //获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕尺寸
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = d.getWidth();
//        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
