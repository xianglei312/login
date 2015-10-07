package com.R.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	/*
	 * 编程思路自然语言描述：： 1，进入界面，从SharedPreferences中获得 记住密码的状态ischeck和自动登录isAuto。默认返回0
	 * 2，如果自动登录(isAuto==1)，则启动线程，隐藏登录界面，显示自动登录，直接跳到第二个界面
	 * 3，如果记住了密码(ischeck==1)，就在从SharedPreference中获取密码和账号并设置给控件，并设置复选框为选中状态。
	 * 4，添加自动登录和记住密码的监听器：若选择自动登录则记住密码也选中，若在两者都选中的情况下取消记住密码则都取消
	 * 3，添加登录按钮监听器：判断账号密码不为空，在判断密码账号正确性：如果都成立， //判断
	 * (1)自动登录被选中：设置ischeck和isAuto为1，并记住密码 (2)只有记住密码被选中时，设置ischeck为1，并记住密码
	 * (3)都未选中时，设置ischeck和isAuto为0。 跳转界面 否则，提示错误信息。
	 *
	 */
	private EditText user, pass = null;
	private CheckBox checkpass, auto_login = null;
	private Button login = null;
	private LinearLayout main = null;
	private TextView auto = null;

	private String str_user, str_pass = null;

	private SharedPreferences shared = null;
	private SharedPreferences.Editor editor = null;

	private int isCheck = 0; // 是否记住密码 0默认 不记住
	private int isAuto = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		InitView();
		//
		shared = this.getSharedPreferences("login", Context.MODE_PRIVATE);
		editor = shared.edit();

		isAuto = shared.getInt("auto", 0);
		isCheck = shared.getInt("check", 0); // 刚进入界面获取 是否记住密码的状态

		// 自动登录
		if (isAuto == 1) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						main.setVisibility(View.GONE);
						auto.setVisibility(View.VISIBLE);
						Thread.sleep(2000);
						Intent intent = new Intent(LoginActivity.this, TwoActivity.class);
						startActivity(intent);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		// 记住密码
		if (isCheck == 1) {
			// 记住密码，从SharedPreference中就获取账号密码
			str_user = shared.getString("user", "");
			str_pass = shared.getString("pass", "");
			// 设置给控件
			user.setText(str_user);
			pass.setText(str_pass);
			// 设置控件为选中状态
			checkpass.setChecked(true);
		} else {
			// 不记住密码
			checkpass.setChecked(false);
		}

		// 自动登录监听器
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean bool) {
				// TODO Auto-generated method stub
				if (bool) {// 自动登录选中，记住密码也选中。
					checkpass.setChecked(true);
				}
			}
		});
		// 记住密码监听器
		checkpass.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean bool) {
				// TODO Auto-generated method stub
				if (!bool) {
					auto_login.setChecked(false);
					// 防止 在正常登录后，在次进入登录界面时，只做了取消记住密码操作，而没有登录的情况。
					editor.putInt("check", 0);
					editor.putInt("auto", 0);
					editor.commit();
				}
			}
		});
		// 登录事件
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isLogin()) {// 判断不为空
					if (str_user.equals("123") && str_pass.equals("123")) {// 判断账号密码正确
						// 在登录时，判断控件是否记住密码，只有正确登录才能保存密码
						if (auto_login.isChecked()) {
							editor.putInt("check", 1);
							editor.putInt("auto", 1);
							editor.putString("user", str_user);
							editor.putString("pass", str_pass);
						} else if (checkpass.isChecked()) {
							editor.putInt("check", 1);
							editor.putString("user", str_user);
							editor.putString("pass", str_pass);
						} else {
							editor.putInt("check", 0);
							editor.putInt("auto", 0);
						}
						editor.commit();
						Intent intent = new Intent(LoginActivity.this, TwoActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(LoginActivity.this, "账号或密码错误！", 3000).show();
					}
				} else {
					Toast.makeText(LoginActivity.this, "账号或密码不能为空！", 3000).show();
				}

			}
		});
	}

	private Boolean isLogin() {
		str_user = user.getText().toString();
		str_pass = pass.getText().toString();
		if (str_user.equals("") || str_pass.equals(""))
			return false;
		return true;
	}

	private void InitView() {
		// TODO Auto-generated method stub
		user = (EditText) findViewById(R.id.user);
		pass = (EditText) findViewById(R.id.pass);
		login = (Button) findViewById(R.id.login);
		checkpass = (CheckBox) findViewById(R.id.checkpass);
		auto_login = (CheckBox) findViewById(R.id.auto_login);
		auto = (TextView) findViewById(R.id.auto);
		main = (LinearLayout) findViewById(R.id.mian);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		isAuto = shared.getInt("auto", 0);
		main.setVisibility(View.VISIBLE);
		auto.setVisibility(View.GONE);
		if (isAuto == 1) {
			auto_login.setChecked(true);
		} else {
			auto_login.setChecked(false);
		}
	}

}
