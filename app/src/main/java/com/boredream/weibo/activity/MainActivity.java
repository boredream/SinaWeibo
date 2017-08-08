package com.boredream.weibo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.fragment.FragmentController;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;

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
		controller = new FragmentController(this, R.id.fl_content, fragments);
		controller.showFragment(0);
		controller.setRadioGroup(rg_tab);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add:
			intent2Activity(WriteStatusActivity.class);
			break;
		}
	}
}
