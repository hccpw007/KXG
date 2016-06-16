package com.cqts.kxg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.utils.MyHttp;
import com.cqts.kxg.views.FavoriteAnimation;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/5/28.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
    Context context;
    List<ArticleInfo> articleInfos;
    MyFragment myFragment;
    private HttpForVolley http;
    boolean canClick = true;



    public ArticleAdapter(Context context, List<ArticleInfo> articleInfos) {
        this.context = context;
        this.articleInfos = articleInfos;
    }

    public ArticleAdapter(MyFragment myFragment, List<ArticleInfo> articleInfos) {
        this.articleInfos = articleInfos;
        this.myFragment = myFragment;
        this.context = myFragment.getActivity();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, null);
        return new MyViewHolder(view);
    }

    boolean needLogin() {
        if (myFragment != null && myFragment.needLogin()) {
            http = myFragment.http;
            return true;
        }

        if (myFragment == null && ((MyActivity) context).needLogin()) {
            http = ((MyActivity) context).http;
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        myViewHolder.item_collect_layout.setOnClickListener(new View.OnClickListener() {
            private FavoriteAnimation animation;

            @Override
            public void onClick(View v) {
                if (!canClick){
                    return;
                }

                if (!needLogin()) {
                    return;
                }

                final MyHttp.MyHttpResult myHttpResult = new MyHttp.MyHttpResult() {
                    @Override
                    public void httpResult(Integer which, int code, String msg, Object bean) {
                        canClick = true;
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        if (code != 0) {
                            return;
                        }
                        int is_love = (int) myViewHolder.itemView.getTag();

                        if (is_love == 0) { //喜欢成功
                            animation = new FavoriteAnimation(myViewHolder.item_collect_img, true);

                        } else { //取消收藏成功
                            animation = new FavoriteAnimation(myViewHolder.item_collect_img, false);
                        }
                        myViewHolder.item_collect_img.startAnimation(animation);

                        ArticleInfo articleInfo = (ArticleInfo) bean;
                        myViewHolder.item_collect_tv.setText(articleInfo.love);
                    }
                };

                HttpForVolley.HttpTodo httpTodo = new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        if (response.optInt("code") != 0) {
                            canClick = true;
                            Toast.makeText(context, response.optString("msg", "发生错误"), Toast
                                    .LENGTH_SHORT).show();
                            return;
                        }

                        int is_love = response.optJSONObject("data").optInt("is_love");
                        myViewHolder.itemView.setTag(is_love);

                        MyHttp.articleLove(http, 2, articleInfos.get(i).article_id, is_love,
                                myHttpResult);
                    }
                };

                MyHttp.articleCollect(http, 1, articleInfos.get(i).article_id, httpTodo);
                canClick = false;
            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("title", articleInfos.get(i).title);
                intent.putExtra("url", articleInfos.get(i).article_url);
                context.startActivity(intent);
            }
        });

        int is_love = articleInfos.get(i).is_love;
        if (is_love == 0){ //未收藏
            myViewHolder.item_collect_img.setImageResource(R.mipmap.home_taoxin);
        }else {//已收藏
            myViewHolder.item_collect_img.setImageResource(R.mipmap.home_taoxin_hover);
        }

        myViewHolder.item_collect_tv.setText(articleInfos.get(i).love);
        myViewHolder.item_tv.setText(articleInfos.get(i).title);
        ImageLoader.getInstance().displayImage(articleInfos.get(i).cover_img, myViewHolder
                .item_img, BaseValue.getOptions(R.mipmap.solid_article));
    }

    @Override
    public int getItemCount() {
        return articleInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_tv;
        LinearLayout item_collect_layout;
        ImageView item_collect_img;
        TextView item_collect_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
            item_collect_layout = (LinearLayout) itemView.findViewById(R.id.item_collect_layout);
            item_collect_img = (ImageView) itemView.findViewById(R.id.item_collect_img);
            item_collect_tv = (TextView) itemView.findViewById(R.id.item_collect_tv);
        }
    }


}
