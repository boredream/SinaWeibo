package com.boredream.emotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class EmotionUtils implements Serializable {
	
	public static Map<String, Integer> emojiMap;
	
	static {
		emojiMap = new HashMap<>();
		emojiMap.put("[呵呵]", R.drawable.d_hehe);
		emojiMap.put("[嘻嘻]", R.drawable.d_xixi);
		emojiMap.put("[哈哈]", R.drawable.d_haha);
		emojiMap.put("[爱你]", R.drawable.d_aini);
		emojiMap.put("[挖鼻屎]", R.drawable.d_wabishi);
		emojiMap.put("[吃惊]", R.drawable.d_chijing);
		emojiMap.put("[晕]", R.drawable.d_yun);
		emojiMap.put("[泪]", R.drawable.d_lei);
		emojiMap.put("[馋嘴]", R.drawable.d_chanzui);
		emojiMap.put("[抓狂]", R.drawable.d_zhuakuang);
		emojiMap.put("[哼]", R.drawable.d_heng);
		emojiMap.put("[可爱]", R.drawable.d_keai);
		emojiMap.put("[怒]", R.drawable.d_nu);
		emojiMap.put("[汗]", R.drawable.d_han);
		emojiMap.put("[害羞]", R.drawable.d_haixiu);
		emojiMap.put("[睡觉]", R.drawable.d_shuijiao);
		emojiMap.put("[钱]", R.drawable.d_qian);
		emojiMap.put("[偷笑]", R.drawable.d_touxiao);
		emojiMap.put("[笑cry]", R.drawable.d_xiaoku);
		emojiMap.put("[doge]", R.drawable.d_doge);
		emojiMap.put("[喵喵]", R.drawable.d_miao);
		emojiMap.put("[酷]", R.drawable.d_ku);
		emojiMap.put("[衰]", R.drawable.d_shuai);
		emojiMap.put("[闭嘴]", R.drawable.d_bizui);
		emojiMap.put("[鄙视]", R.drawable.d_bishi);
		emojiMap.put("[花心]", R.drawable.d_huaxin);
		emojiMap.put("[鼓掌]", R.drawable.d_guzhang);
		emojiMap.put("[悲伤]", R.drawable.d_beishang);
		emojiMap.put("[思考]", R.drawable.d_sikao);
		emojiMap.put("[生病]", R.drawable.d_shengbing);
		emojiMap.put("[亲亲]", R.drawable.d_qinqin);
		emojiMap.put("[怒骂]", R.drawable.d_numa);
		emojiMap.put("[太开心]", R.drawable.d_taikaixin);
		emojiMap.put("[懒得理你]", R.drawable.d_landelini);
		emojiMap.put("[右哼哼]", R.drawable.d_youhengheng);
		emojiMap.put("[左哼哼]", R.drawable.d_zuohengheng);
		emojiMap.put("[嘘]", R.drawable.d_xu);
		emojiMap.put("[委屈]", R.drawable.d_weiqu);
		emojiMap.put("[吐]", R.drawable.d_tu);
		emojiMap.put("[可怜]", R.drawable.d_kelian);
		emojiMap.put("[打哈气]", R.drawable.d_dahaqi);
		emojiMap.put("[挤眼]", R.drawable.d_jiyan);
		emojiMap.put("[失望]", R.drawable.d_shiwang);
		emojiMap.put("[顶]", R.drawable.d_ding);
		emojiMap.put("[疑问]", R.drawable.d_yiwen);
		emojiMap.put("[困]", R.drawable.d_kun);
		emojiMap.put("[感冒]", R.drawable.d_ganmao);
		emojiMap.put("[拜拜]", R.drawable.d_baibai);
		emojiMap.put("[黑线]", R.drawable.d_heixian);
		emojiMap.put("[阴险]", R.drawable.d_yinxian);
		emojiMap.put("[打脸]", R.drawable.d_dalian);
		emojiMap.put("[傻眼]", R.drawable.d_shayan);
		emojiMap.put("[猪头]", R.drawable.d_zhutou);
		emojiMap.put("[熊猫]", R.drawable.d_xiongmao);
		emojiMap.put("[兔子]", R.drawable.d_tuzi);
	}

	public static List<String> getImageNameList() {
		return new ArrayList<>(emojiMap.keySet());
	}
	
	public static int getImgByName(String imgName) {
		Integer integer = emojiMap.get(imgName);
		return integer == null ? -1 : integer;
	}

	public static SpannableString getEmotionContent(final Context context, final int emojiSizeDp, CharSequence source) {
		String regex = "\\[[\u4e00-\u9fa5\\w]+\\]";
		SpannableString spannableString = new SpannableString(source);

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(spannableString);

		while(matcher.find()) {
			String emojiStr = matcher.group();
			int start = matcher.start();

			int imgRes = EmotionUtils.getImgByName(emojiStr);
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);

			if(bitmap != null) {
				bitmap = Bitmap.createScaledBitmap(bitmap, emojiSizeDp, emojiSizeDp, true);

				ImageSpan imageSpan = new ImageSpan(context, bitmap);
				spannableString.setSpan(imageSpan, start, start + emojiStr.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}

	/**
	 * 初始化表情面板内容
	 * @param vp 面板容器
	 * @param et 输入框
	 * @param maxSize 文字最大数量
	 * @param column 列数
	 * @param row 行数
	 * @param width 面板宽度
	 * @param padding 四边间距
	 */
	public static void initEmotion(final ViewPager vp, final EditText et, final int maxSize,
								   int column, int row, int width, int padding) {
		// 注意包含边距
		int itemWidth = (width - 2 * padding) / 7;
		int height = itemWidth * 3 + padding * 2;

		List<GridView> gvs = new ArrayList<>();
		List<String> emotionNames = new ArrayList<>();
		// 每页最后一个是回退键
		int pageCount = column * row - 1;

		// 遍历全部表情文字
		List<String> imageNameList = EmotionUtils.getImageNameList();
		for (int i = 0; i < imageNameList.size(); i++) {
			emotionNames.add(imageNameList.get(i));

			// 每满一组或者最后不足一组的，创建一个GridView，加到vp中
			if(emotionNames.size() == pageCount || i == imageNameList.size()-1) {
				GridView gv = new GridView(vp.getContext());
				gv.setPadding(padding, padding, padding, padding);
				gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
				gv.setNumColumns(column);

				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
				gv.setLayoutParams(params);

				final EmotionGvAdapter adapter = new EmotionGvAdapter(vp.getContext(), emotionNames, itemWidth);
				gv.setAdapter(adapter);
				gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if(position == adapter.getCount()-1) {
							// 回退键
							et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
						} else {
							String emotionName = adapter.getItem(position);
							Editable text = et.getText();

							if(emotionName.length() + text.length() > maxSize) {
								// 表情文字超过最大数量，不再处理，防止只插入一半表情文字
								return;
							}

							// 获取当前光标位置，在光标位置插入表情
							int curPosition = et.getSelectionStart();
							text.insert(curPosition, emotionName);

							// 插入表情后，设置spannableString显示图片
							et.setText(EmotionUtils.getEmotionContent(vp.getContext(), (int) et.getTextSize(), text));

							// 插入表情后，重新设置新的光标位置
							int newPosition = curPosition + emotionName.length();
							if(newPosition <= et.getText().length()) {
								et.setSelection(newPosition);
							}
						}
					}
				});

				gvs.add(gv);
				emotionNames = new ArrayList<>();
			}
		}

		EmotionPagerAdapter pagerAdapter = new EmotionPagerAdapter(gvs);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		vp.setLayoutParams(params);
		vp.setAdapter(pagerAdapter);
	}

}
