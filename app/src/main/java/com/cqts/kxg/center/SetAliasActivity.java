package com.cqts.kxg.center;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.utils.MyHttp;

import java.io.UnsupportedEncodingException;

public class SetAliasActivity extends MyActivity implements View.OnClickListener {
    private MyEditText nickname_et;
    private ImageView clean_img;
    private Button set_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setalias);
        InitView();
    }

    private void InitView() {
        setMyTitle("昵称修改");
        nickname_et = (MyEditText) findViewById(R.id.nickname_et);
        clean_img = (ImageView) findViewById(R.id.clean_img);
        set_btn = (Button) findViewById(R.id.set_btn);
        clean_img.setOnClickListener(this);
        set_btn.setOnClickListener(this);

        //昵称为空,这显示用户名
        if (getUserInfo().alias.isEmpty()) {
            nickname_et.setText(getUserInfo().user_name);
        } else {
            nickname_et.setText(getUserInfo().alias);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean_img:
                nickname_et.setText("");
                break;
            case R.id.set_btn:
                setNickName();
                break;
            default:
                break;
        }
    }

    private void setNickName() {
        final String nickname = nickname_et.getText().toString().trim();
        byte[] bytes = new byte[0];
        try {
            bytes = nickname.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <bytes.length ; i++) {
            System.out.println(bytes[i]);
        }

        if (nickname.isEmpty()||bytes.length<4) {
            showToast("请输入昵称（4-20个字符）");
            return;
        }

        MyHttp.userAlias(http, null, nickname, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                showToast("修改昵称成功!");
                getUserInfo().alias = nickname;
                finish();
            }
        });
    }
}
