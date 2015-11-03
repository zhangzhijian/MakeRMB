package com.dream.makermb.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dream.makermb.CustomApp;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApp.get().getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        CustomApp.get().getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //int result = 0;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //result = R.string.errcode_success;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //result = R.string.errcode_cancel;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //result = R.string.errcode_deny;
                    break;
                default:
                    //result = R.string.errcode_unknown;
                    break;
            }
            //ToastUtils.showShortToast(getApplicationContext(), result);
            finish();
        }
    }
}