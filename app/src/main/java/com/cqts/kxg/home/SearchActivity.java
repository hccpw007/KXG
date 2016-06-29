package com.cqts.kxg.home;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.base.views.MyEditText;
import com.base.views.MyTagView;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.utils.MyHttp;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 搜索页面
 */
public class SearchActivity extends MyActivity implements MyTagView.OnTagClickListener, View
        .OnClickListener, TextView.OnEditorActionListener, HttpForVolley.HttpTodo {
    private MyTagView search_tag;
    private MyEditText search_et;
    private ImageView search_finish_iv;
    private TextView search_choice_tv;
    private TextView search_tv;
    private PopupWindow popupWindow;
    private int type = 1;
    public static final int type_goods = 1;
    public static final int type_article = 2;
    public static final int type_shop = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        InitView();
        getHotKeyWord();
    }

    private void getHotKeyWord() {
        MyHttp.hotKeyword(http, null, this);
    }

    private void InitView() {
        search_finish_iv = (ImageView) findViewById(R.id.search_finish_iv);
        search_tag = (MyTagView) findViewById(R.id.search_tag);
        search_et = (MyEditText) findViewById(R.id.search_et);
        search_choice_tv = (TextView) findViewById(R.id.search_choice_tv);
        search_tv = (TextView) findViewById(R.id.search_tv);

        search_finish_iv.setOnClickListener(this);
        search_choice_tv.setOnClickListener(this);
        search_tv.setOnClickListener(this);

        search_et.setOnEditorActionListener(this);
        search_tag.setOnTagClickListener(this);
        createPop();
    }

    @Override
    public void onTagClick(View v) {
        String text = ((TextView) v).getText().toString().trim();
        search_et.setText(text);
        search_et.setSelection(text.length());
        search();
//        BaseValue.imm.showSoftInput(search_et, 0);//展开输入法
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_finish_iv: //返回
                finish();
                break;
            case R.id.search_tv: //搜索
                search();
                break;
            case R.id.search_choice_tv: //选择类型
                popupWindow.showAsDropDown(search_choice_tv, BaseValue.dp2px(-6), BaseValue.dp2px
                        (8));
                break;
            case R.id.pop_search_goods: //选择商品
                type = type_goods;
                search_choice_tv.setText("商品");
                popupWindow.dismiss();
                break;
            case R.id.pop_search_article: //选择文章
                type = type_article;
                search_choice_tv.setText("文章");
                popupWindow.dismiss();
                break;
            case R.id.pop_search_shop: //选择店铺
                search_choice_tv.setText("店铺");
                type = type_shop;
                popupWindow.dismiss();
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
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
        }
        return false;
    }

    private void createPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_search, null);
        View pop_search_goods = view.findViewById(R.id.pop_search_goods);
        View pop_search_article = view.findViewById(R.id.pop_search_article);
        View pop_search_shop = view.findViewById(R.id.pop_search_shop);
        pop_search_goods.setOnClickListener(this);
        pop_search_article.setOnClickListener(this);
        pop_search_shop.setOnClickListener(this);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
    }

    private void search() {
        String searchStr = search_et.getText().toString().trim();
        if (searchStr.isEmpty()) {
            return;
        }
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra("keyword", searchStr);
        intent.putExtra("type", type);
        startActivity(intent);
    }
    @Override
    public void httpTodo(Integer which, JSONObject response) {
        int code = response.optInt("code", 1);
        String msg = response.optString("msg", "发生错误");
        String data = response.optString("data", "");
        if (code != 0) {
            showToast(msg);
            return;
        }

        data = data.replace("[", "");
        data = data.replace("]", "");
        data = data.replace("\"", "");
        String[] split = data.split(",");
        search_tag.setMyTag(split);
    }
}
