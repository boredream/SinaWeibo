package com.boredream.weibo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.net.RxComposer;
import com.boredream.weibo.net.WbHttpRequest;

import io.reactivex.annotations.NonNull;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_nickname;
    private EditText et_password;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    private void submit() {
        // validate
        String username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            showTip("请输入登录用户名");
            return;
        }

        String nickname = et_nickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            showTip("请输入昵称");
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            showTip("请输入密码");
            return;
        }

        // validate success, do something
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(password);
        WbHttpRequest.getInstance()
                .getApiService()
                .userRegist(user)
                .compose(RxComposer.<User>commonProgress(this))
                .subscribe(new SimpleDisObserver<User>() {
                    @Override
                    public void onNext(@NonNull User user) {
                        showTip("注册成功");
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                submit();
                break;
        }
    }
}
