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
	 * ���˼·��Ȼ������������ 1��������棬��SharedPreferences�л�� ��ס�����״̬ischeck���Զ���¼isAuto��Ĭ�Ϸ���0
	 * 2������Զ���¼(isAuto==1)���������̣߳����ص�¼���棬��ʾ�Զ���¼��ֱ�������ڶ�������
	 * 3�������ס������(ischeck==1)�����ڴ�SharedPreference�л�ȡ������˺Ų����ø��ؼ��������ø�ѡ��Ϊѡ��״̬��
	 * 4������Զ���¼�ͼ�ס����ļ���������ѡ���Զ���¼���ס����Ҳѡ�У��������߶�ѡ�е������ȡ����ס������ȡ��
	 * 3����ӵ�¼��ť���������ж��˺����벻Ϊ�գ����ж������˺���ȷ�ԣ������������ //�ж�
	 * (1)�Զ���¼��ѡ�У�����ischeck��isAutoΪ1������ס���� (2)ֻ�м�ס���뱻ѡ��ʱ������ischeckΪ1������ס����
	 * (3)��δѡ��ʱ������ischeck��isAutoΪ0�� ��ת���� ������ʾ������Ϣ��
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

	private int isCheck = 0; // �Ƿ��ס���� 0Ĭ�� ����ס
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
		isCheck = shared.getInt("check", 0); // �ս�������ȡ �Ƿ��ס�����״̬

		// �Զ���¼
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
		// ��ס����
		if (isCheck == 1) {
			// ��ס���룬��SharedPreference�оͻ�ȡ�˺�����
			str_user = shared.getString("user", "");
			str_pass = shared.getString("pass", "");
			// ���ø��ؼ�
			user.setText(str_user);
			pass.setText(str_pass);
			// ���ÿؼ�Ϊѡ��״̬
			checkpass.setChecked(true);
		} else {
			// ����ס����
			checkpass.setChecked(false);
		}

		// �Զ���¼������
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean bool) {
				// TODO Auto-generated method stub
				if (bool) {// �Զ���¼ѡ�У���ס����Ҳѡ�С�
					checkpass.setChecked(true);
				}
			}
		});
		// ��ס���������
		checkpass.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean bool) {
				// TODO Auto-generated method stub
				if (!bool) {
					auto_login.setChecked(false);
					// ��ֹ ��������¼���ڴν����¼����ʱ��ֻ����ȡ����ס�����������û�е�¼�������
					editor.putInt("check", 0);
					editor.putInt("auto", 0);
					editor.commit();
				}
			}
		});
		// ��¼�¼�
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isLogin()) {// �жϲ�Ϊ��
					if (str_user.equals("123") && str_pass.equals("123")) {// �ж��˺�������ȷ
						// �ڵ�¼ʱ���жϿؼ��Ƿ��ס���룬ֻ����ȷ��¼���ܱ�������
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
						Toast.makeText(LoginActivity.this, "�˺Ż��������", 3000).show();
					}
				} else {
					Toast.makeText(LoginActivity.this, "�˺Ż����벻��Ϊ�գ�", 3000).show();
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
