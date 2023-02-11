package com.yyzy.constellation.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyzy.constellation.utils.MyToast;

class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private String APP_ID = "wx116eea39d7c46d32";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID,true);
        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.i("info","param onPayRequest="+req.toString());
    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     * app发送消息给微信，处理返回消息的回调
     */
    private String AY_BROADCAST_RECEIVER_ACTION = "AY_BROADCAST_RECEIVER_ACTION";
    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            if(baseResp.errCode == BaseResp.ErrCode.ERR_OK){
//                MyToast.showText(this,"ERR_OK");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(AY_BROADCAST_RECEIVER_ACTION));
            }else if(baseResp.errCode == BaseResp.ErrCode.ERR_COMM){
                Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
            }
        }
        MyToast.showText(this,"finish");
        finish();

    }

}