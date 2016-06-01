package com.cqts.kxg.nine.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class NineInfo {
    public Banner banner;
    public List<NineListInfo> goods_list;


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

    public class NineListInfo {
        public long goods_id;//  648,
        public long cat_id;//  34,
        public String goods_name;//  "禧诺纯棉毛巾字母印花三层纱布儿童毛巾全棉面巾柔软童巾",
        public String goods_sn;//  "43964182031",
        public int click_count;//  14,
        public String shop_price;//  "9.90",
        public String goods_thumb;//  "https://img.alicdn
        // .com/bao/uploaded/i2/TB16WSyIFXXXXbaXFXXXXXXXXXX_!!0-item_pic.jpg_300x300.jpg",
        public String goods_img;//  "https://img.alicdn
        // .com/bao/uploaded/i2/TB16WSyIFXXXXbaXFXXXXXXXXXX_!!0-item_pic.jpg_600x600.jpg",
        public long volume;//  1246,
        public String love;//  null,
        public String share_sum;//  null,
        public long supplier_id;//  4527,
        public String url;//  "http://www.kxg99.com/mobile/goods.php?id=648"
    }
}
