package com.boredream.weibo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.constants.AccessTokenKeeper;
import com.boredream.weibo.constants.WeiboConstants;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

public class LoginActivity extends BaseActivity {

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        
        mAuthInfo = new AuthInfo(this, WeiboConstants.APP_KEY, WeiboConstants.REDIRECT_URL, WeiboConstants.SCOPE);
        WbSdk.install(this,mAuthInfo);

        mSsoHandler = new SsoHandler(this);
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mSsoHandler.authorize(new SelfWbAuthListener());
			}
		});
	}
	
	/**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private class SelfWbAuthListener implements WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                        showTip("登陆成功");
                        intent2Activity(MainActivity.class);
                    } else {
                        showTip("登陆失败" + token.toString());
                    }
                }
            });
        }

        @Override
        public void cancel() {
            showTip("取消");
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            showTip(errorMessage.toString());
        }
    }

}
