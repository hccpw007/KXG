package com.cqts.kxg.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class NineInfo {
    public Banner banner;
    public List<GoodsInfo> goods_list;

    /**
     * 9快9的banner图
     */
    public class Banner {
        public   int ad_id;// 70,
        public  int position_id;// 67,
        public  int media_type;// 0,
        public  String ad_name;// "app 9块9包邮banner图片",
        public  String ad_link;// "http://www.kxg99.com/",
        public  String ad_code;// "http://cdn.kxg99.com/data/afficheimg/1464648214857613674.jpg",
        public  String start_time;// 1464595200,
        public  long end_time;// 1530259200,
        public   String link_man;// "",
        public  String link_email;// "",
        public  String link_phone;// "",
        public   int click_count;// 0,
        public   int enabled;// 1
    }
}
