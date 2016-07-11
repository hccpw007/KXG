package com.cqts.kxg.center;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.base.utils.PhotoPopupWindow;
import com.base.utils.PhotoUtil;
import com.base.views.MyHeadImageView;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

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

    private PhotoUtil photoUtil;
    private PhotoPopupWindow mPhotoPopupWindow;
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
        if (null!=name_tv&&null!=getUserInfo()&&!TextUtils.isEmpty(getUserInfo().user_name)){
            name_tv.setText(getUserInfo().user_name);
        }
        ImageLoader.getInstance().displayImage(getUserInfo().headimg,head_img, BaseValue.getOptions(R.mipmap.center_head));
        setSex();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_layout://修改头像
                photoUtil = new PhotoUtil(this, 3, 3);
                mPhotoPopupWindow = new PhotoPopupWindow(this, photoUtil);
                mPhotoPopupWindow.showpop(v);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.FromWhere.camera:
            case PhotoUtil.FromWhere.photo:
                photoUtil.onActivityResult(requestCode, resultCode, data);
                break;
            case PhotoUtil.FromWhere.forfex:
                if (resultCode == RESULT_OK) {
                    MyHttp.uploadImage(http, null, photoUtil.getForfexPath(), new HttpForVolley
                            .HttpTodo() {
                        @Override
                        public void httpTodo(Integer which, JSONObject response) {
                            if (response.optInt("code",1)!=0){
                                showToast("上传图片发生错误!");
                                return;
                            }
                            showToast("修改头像成功!");
                            String filename = response.optJSONObject("data").optString("filename");
                            ImageLoader.getInstance().displayImage(filename, head_img);
                            getUserInfo().headimg = filename;
                        }
                    });
                }
                break;
            default:
                break;
        }
    }
}
