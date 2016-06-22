package com.cqts.kxg.center;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.MyGridDecoration;
import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.ApprenticeAdapter;
import com.cqts.kxg.bean.EaringApprenticeInfo;
import com.cqts.kxg.bean.MyApprenticeInfo;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * 收徒弟页面
 */
public class ApprenticeActivity extends MyActivity implements View.OnClickListener {
    private TextView table1Tv, table2Tv, table3Tv, table4Tv;
    private MyEditText invitationEt;
    private Button shareBtn;
    private ImageView empty1Img, empty2Img, qrImg;
    private TextView change_tv;
    MyApprenticeInfo myApprenticeInfo;
    ArrayList<MyApprenticeInfo.Apprentice> signup = new ArrayList<>();
    ArrayList<MyApprenticeInfo.Apprentice> task = new ArrayList<>();
    private ApprenticeAdapter adapter1, adapter2;
    private RecyclerView recyclerview1, recyclerview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentice);
        InitView();
        InitRC1();
        InitRC2();
        getData();
    }

    private void InitRC1() {
        GridLayoutManager manager = new GridLayoutManager(this, 6);
        MyGridDecoration gridDecoration = new MyGridDecoration(0, 0, Color.WHITE, true);
        adapter1 = new ApprenticeAdapter(signup);
        recyclerview1.setLayoutManager(manager);
        recyclerview1.addItemDecoration(gridDecoration);
        recyclerview1.setAdapter(adapter1);
    }

    private void InitRC2() {
        GridLayoutManager manager = new GridLayoutManager(this, 6);
        MyGridDecoration gridDecoration = new MyGridDecoration(0, 0, Color.WHITE, true);
        adapter2 = new ApprenticeAdapter(task);
        recyclerview2.setLayoutManager(manager);
        recyclerview2.addItemDecoration(gridDecoration);
        recyclerview2.setAdapter(adapter2);
    }

    private void InitView() {
        setMyTitle("收徒");
        change_tv = (TextView) findViewById(R.id.change_tv);
        table1Tv = (TextView) findViewById(R.id.table1_tv);
        table2Tv = (TextView) findViewById(R.id.table2_tv);
        table3Tv = (TextView) findViewById(R.id.table3_tv);
        table4Tv = (TextView) findViewById(R.id.table4_tv);

        qrImg = (ImageView) findViewById(R.id.qr_img);
        invitationEt = (MyEditText) findViewById(R.id.invitation_et);
        shareBtn = (Button) findViewById(R.id.share_btn);
        empty1Img = (ImageView) findViewById(R.id.empty1_img);
        recyclerview1 = (RecyclerView) findViewById(R.id.recyclerview1);
        recyclerview2 = (RecyclerView) findViewById(R.id.recyclerview2);
        empty2Img = (ImageView) findViewById(R.id.empty2_img);
        change_tv.setOnClickListener(this);
        invitationEt.setText(getUserInfo().invite_code + "");
    }

    private void getData() {
        //查询徒弟收益信息
        MyHttp.userApprentice(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code!=0){
                    showToast(msg);
                    return;
                }
                EaringApprenticeInfo apprenticeInfo = (EaringApprenticeInfo) bean;
                table1Tv.setText(TextUtils.isEmpty(apprenticeInfo.total)?"0":apprenticeInfo.total+"人");
                table2Tv.setText(TextUtils.isEmpty(apprenticeInfo.today)?"0":apprenticeInfo.today+"人");
                table3Tv.setText(TextUtils.isEmpty(apprenticeInfo.apprentice)?"0.00":apprenticeInfo.apprentice+"元");
                table4Tv.setText(TextUtils.isEmpty(apprenticeInfo.shared)?"0":apprenticeInfo.shared+"次");
            }
        });

        //查询答题和为答题的徒弟
        MyHttp.apprenticeListing(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    empty1Img.setVisibility(View.VISIBLE);
                    empty2Img.setVisibility(View.VISIBLE);
                    recyclerview2.setVisibility(View.GONE);
                    recyclerview1.setVisibility(View.GONE);
                    return;
                }
                myApprenticeInfo = (MyApprenticeInfo) bean;
                if (myApprenticeInfo != null && myApprenticeInfo.signup != null &&
                        myApprenticeInfo.signup.size() != 0) {
                    signup.addAll(myApprenticeInfo.signup);
                    adapter1.notifyDataSetChanged();
                    recyclerview1.setVisibility(View.VISIBLE);
                    empty1Img.setVisibility(View.GONE);
                } else {
                    empty1Img.setVisibility(View.VISIBLE);
                    recyclerview1.setVisibility(View.GONE);
                }

                if (myApprenticeInfo != null && myApprenticeInfo.task != null && myApprenticeInfo
                        .task.size() != 0) {
                    task.addAll(myApprenticeInfo.task);
                    adapter2.notifyDataSetChanged();
                    recyclerview2.setVisibility(View.VISIBLE);
                    empty2Img.setVisibility(View.GONE);
                } else {
                    empty2Img.setVisibility(View.VISIBLE);
                    recyclerview2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_tv:
                changeCode();
                break;
            default:
                break;
        }
    }

    //修改邀请码
    private void changeCode() {
        final String text = invitationEt.getText().toString().trim();
        boolean enabled = invitationEt.isEnabled();

        if (!enabled) {
            change_tv.setText("确认");
            invitationEt.setEnabled(true);
            if (!TextUtils.isEmpty(text)) {
                invitationEt.setSelection(text.length());
            }
        }

        if (enabled) {
            if (text.isEmpty() || text.length() < 6) {
                showToast("请输入1~6位邀请码");
                return;
            }
            MyHttp.userInvitecode(http, null, text, new MyHttp.MyHttpResult() {
                @Override
                public void httpResult(Integer which, int code, String msg, Object bean) {
                    showToast(msg);
                    if (code == 0) {
                        invitationEt.setText(text);
                        getUserInfo().invite_code = text;
                        change_tv.setText("修改");
                        invitationEt.setEnabled(false);
                    }
                }
            });
        }
    }
}
