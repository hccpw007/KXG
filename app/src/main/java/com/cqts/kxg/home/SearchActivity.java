package com.cqts.kxg.home;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseValue;
import com.base.views.MyEditText;
import com.base.views.MyTagView;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

/**
 * 搜索页面
 */
public class SearchActivity extends MyActivity implements MyTagView.OnTagClickListener, View
        .OnClickListener, TextView.OnEditorActionListener {
    private MyTagView search_tag;
    private MyEditText search_et;
    private ImageView search_cart_iv;
    private ImageView search_finish_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        InitView();
    }

    private void InitView() {
        search_finish_iv = (ImageView) findViewById(R.id.search_finish_iv);
        search_cart_iv = (ImageView) findViewById(R.id.search_cart_iv);
        search_tag = (MyTagView) findViewById(R.id.search_tag);
        search_et = (MyEditText) findViewById(R.id.search_et);

        search_cart_iv.setOnClickListener(this);
        search_finish_iv.setOnClickListener(this);
        search_et.setOnEditorActionListener(this);

        String[] texts = new String[]{"泥沙的", "发点发", "嘎的速度", "嘎帅得", "点噶嗲噶", "235日3", "大傻瓜",
                "嘎帅得过", "嘎的速度高达", "嘎帅得过", "点噶嗲噶", "泥沙的", "发点发", "嘎的速度高达", "嘎帅得过", "点噶嗲噶",
                "235日3", "大傻瓜", "嘎帅得过", "嘎的速度高达", "嘎帅得过", "点噶嗲噶", "泥沙的", "发点发", "嘎的速度高达", "嘎帅得过",
                "点噶嗲噶", "235日3", "大傻瓜", "嘎帅得过", "嘎的速度高达", "嘎帅得过", "点噶嗲噶"};
        search_tag.setMyTag(texts);
        search_tag.setOnTagClickListener(this);
    }

    @Override
    public void onTagClick(View v) {
        String text = ((TextView) v).getText().toString().trim();
        search_et.setText(text);
        search_et.setSelection(text.length());
        BaseValue.imm.showSoftInput(search_et,0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_finish_iv: //返回
                finish();
                break;
            case R.id.search_cart_iv: //购物车
                break;
            default:
                break;
        }
    }


    /**
     * 搜索
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){

        }
        return false;
    }
}
