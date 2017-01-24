package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 测试volley
 * 
 * @author zhangt
 * 
 */
public class VolleyTestActivity extends Activity implements OnClickListener {

	private Context mContext;
	private TextView resultTextView;
	private Button noCookieButton;
	private Button CookieButton;
	private EditText editText1;
	private EditText editText2;
	private String tel;
	private String Vcode;
	private String userID;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				resultTextView.setText(msg.obj.toString());
				Constant.removeCookie(mContext);
				Constant.setCookie(mContext, "wuzhen.smartdot.com", GloableConfig.COOKIE);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					userID = jsonObject.getString("user_id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (msg.what == 1002) {
				resultTextView.setText(msg.obj.toString());
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volley_test);
		initView();
	}

	private void initView() {
		mContext = this;
		resultTextView = (TextView) findViewById(R.id.textView1);
		noCookieButton = (Button) findViewById(R.id.button1);
		CookieButton = (Button) findViewById(R.id.button2);
		editText1 = (EditText) findViewById(R.id.editText1);
		editText2 = (EditText) findViewById(R.id.editText2);

		noCookieButton.setOnClickListener(this);
		CookieButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			tel = editText1.getText().toString();
			try {
				tel = URLEncoder.encode(Constant.encode(Constant.key, tel), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 不加header的使用方法
			VolleyUtil volleyUtil = new VolleyUtil(mContext);
			String url = "http://wuzhen.smartdot.com:8088/gaojiao/hylogin.do?method=yzCode&tel=" + tel + "&lg=FZZ5O8pq9%2Fg%3D";
			volleyUtil.stringRequest(handler, Request.Method.POST, url, 1001);
			break;
		case R.id.button2:
			tel = "6LsqRa6ypLs%3D";
			Vcode = "ayVqKX4rjkIGSCnOGTq1YoYjsCooHKfhSFLKSZ%2B6kbrVzDfN%2FMoNZA%3D%3D";
			// try {
			// tel = URLEncoder.encode(Constant.encode(Constant.key, tel),
			// "UTF-8");
			// Vcode = URLEncoder.encode(Constant.encode(Constant.key, Vcode),
			// "UTF-8");
			// userID = URLEncoder.encode(Constant.encode(Constant.key, userID),
			// "UTF-8");
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// 加header的使用方法
			VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
			Map<String, String> map = new HashMap<>();
			volleyUtil1.stringRequest(handler, "http://wuzhen.smartdot.com:8088/gaojiao/hylogin.?method=passwordLogin&userName=" + tel + "&password=" + Vcode
					+ "&lg=FZZ5O8pq9%2Fg%3D", map, 1002);
			break;

		default:
			break;
		}

	}
}
