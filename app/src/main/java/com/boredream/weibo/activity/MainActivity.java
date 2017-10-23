package com.boredream.weibo.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.fragment.FragmentController;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.fragment.HomeFragment;
import com.boredream.weibo.fragment.MessageFragment;
import com.boredream.weibo.fragment.SearchFragment;
import com.boredream.weibo.fragment.UserFragment;
import com.boredream.weibo.tinker.util.Utils;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements OnClickListener {
	
	private RadioGroup rg_tab;
	private ImageView iv_add;
	private FragmentController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		initView();
	}
	
	private void initView() {
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		iv_add.setOnClickListener(this);

		ArrayList<Fragment> fragments = new ArrayList<>();
		fragments.add(new HomeFragment());
		fragments.add(new MessageFragment());
		fragments.add(new SearchFragment());
		fragments.add(new UserFragment());
		controller = new FragmentController(this, R.id.fl_content, fragments);
		controller.showFragment(0);
		controller.setRadioGroup(rg_tab);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add:
			// FIXME: 2017/10/23
//			intent2Activity(WriteStatusActivity.class);

			showTip("更新咯！！！");
			//等下要push到SD卡里面去apk，以达到更新的目的
			TinkerInstaller.onReceiveUpgradePatch(this,
					Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Utils.setBackground(false);

	}

	@Override
	protected void onPause() {
		super.onPause();
		Utils.setBackground(true);
	}
}
