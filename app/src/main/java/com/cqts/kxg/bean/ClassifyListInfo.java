package com.cqts.kxg.bean;

import java.util.ArrayList;

/**
 * 分类合集
 * Created by Administrator on 2016/4/26.
 */
public class ClassifyListInfo {
    public boolean ischecked;
    public long cat_id;// 1,
    public String cat_name;// "女装",
    public String cat_desc;// "",
    public String cover_img;// "http://cdn.kxg99.com/data/afficheimg//123123/312",
    public int hot;// 0,
    public long parent_id;// 0,
    public ArrayList<ClassifyChildInfo> son;
    public class ClassifyChildInfo {
        public String cat_id;//  2,
        public String cat_name;//  "上衣/外套",
        public String cat_desc;//  "",
        public String cover_img;//  null,
        public String hot;//  0,
        public String parent_id;//  1
    }
}
