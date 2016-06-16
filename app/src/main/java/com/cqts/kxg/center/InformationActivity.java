package com.cqts.kxg.center;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.views.MyHeadImageView;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;

public class InformationActivity extends MyActivity implements View.OnClickListener {
    private LinearLayout head_layout;
    private LinearLayout name_layout;
    private LinearLayout nickname_layout;
    private LinearLayout sex_layout;
    private MyHeadImageView head_img;
    private TextView name_tv;
    private TextView nickname_tv;
    private TextView sex_tv;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        InitView();
    }

    private void InitView() {
        setMyTitle("个人资料");
        head_layout = (LinearLayout) findViewById(R.id.head_layout);
        name_layout = (LinearLayout) findViewById(R.id.name_layout);
        nickname_layout = (LinearLayout) findViewById(R.id.nickname_layout);
        sex_layout = (LinearLayout) findViewById(R.id.sex_layout);
        head_img = (MyHeadImageView) findViewById(R.id.head_img);
        name_tv = (TextView) findViewById(R.id.name_tv);
        nickname_tv = (TextView) findViewById(R.id.nickname_tv);
        sex_tv = (TextView) findViewById(R.id.sex_tv);

        head_layout.setOnClickListener(this);
        name_layout.setOnClickListener(this);
        nickname_layout.setOnClickListener(this);
        sex_layout.setOnClickListener(this);

        name_tv.setText(getUserInfo().user_name);
        setSex();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_layout://修改头像
                break;
            case R.id.name_layout: //用户名
                showToast("开心购会员名作为登录名不可以修改~");
                break;
            case R.id.nickname_layout: //昵称
                startActivity(new Intent(this,SetAliasActivity.class));
                break;
            case R.id.sex_layout: //性别
                showSexDialog();
                break;
            case R.id.dialog_tv1: //男
                changeSex(1);
                break;
            case R.id.dialog_tv2: //女
                changeSex(2);
                break;
            case R.id.dialog_tv3: //保密
                changeSex(0);
                break;
            default:
                break;
        }
    }

    private void showSexDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_sex, null);
        TextView dialog_tv1 = (TextView) view.findViewById(R.id.dialog_tv1);
        TextView dialog_tv2 = (TextView) view.findViewById(R.id.dialog_tv2);
        TextView dialog_tv3 = (TextView) view.findViewById(R.id.dialog_tv3);
        dialog_tv1.setOnClickListener(this);
        dialog_tv2.setOnClickListener(this);
        dialog_tv3.setOnClickListener(this);
        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(view);
    }

    /**
     * 改变性别
     */
    private void changeSex(final int sex) {
        dialog.dismiss();
        MyHttp.userSex(http, null, sex, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                showToast("修改性别成功!");
                MyApplication.userInfo.sex = sex;
                setSex();
            }
        });
    }

    /**
     * 改变昵称
     */
    private void setNickName() {
        //昵称为空,这显示用户名
        if (getUserInfo().alias.isEmpty()) {
            nickname_tv.setText(getUserInfo().user_name);
        } else {
            nickname_tv.setText(getUserInfo().alias);
        }
    }

    private void setSex() {
        //性别 ; 0保密; 1男; 2女
        if (getUserInfo().sex == 0) {
            sex_tv.setText("保密");
        }
        if (getUserInfo().sex == 1) {
            sex_tv.setText("男");
        }
        if (getUserInfo().sex == 2) {
            sex_tv.setText("女");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNickName();
    }
}
