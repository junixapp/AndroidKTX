package com.lxj.pay.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.blankj.utilcode.util.LogUtils;
import com.lxj.pay.PayVM;
import com.lxj.pay.WxPayResult;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, PayVM.INSTANCE.getWxAppId());
		api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		LogUtils.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.errCode==0) {
			//success
			PayVM.INSTANCE.getWxPayData().postValue(new WxPayResult("success", resp.openId, resp.transaction));
		}else if(resp.errCode==-1){
			PayVM.INSTANCE.getWxPayData().postValue(new WxPayResult("false", resp.openId, resp.transaction));
		}else {
			PayVM.INSTANCE.getWxPayData().postValue(new WxPayResult("cancel", resp.openId, resp.transaction));
		}
		finish();
	}
}