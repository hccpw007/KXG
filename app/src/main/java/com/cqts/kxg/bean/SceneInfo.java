package com.cqts.kxg.bean;

/**
 * 场景菜单查询 <p>
 * <p/>
 * 加载首页场景分类目前只有十个场景分类菜单<br>
 * Created by Administrator on 2016/5/25.
 */
public class SceneInfo {
         String cat_id; // 20,
         String cat_name; //String 首页测试分类1",
         String cat_type; // 6,
         String keywords; //String ",
         String cat_desc; //String ",
         String sort_order; // 50,
         String show_in_nav; // 0,
         String parent_id; // 0,
         String path_name; //String "

        public String getPath_name() {
                return path_name;
        }

        public void setPath_name(String path_name) {
                this.path_name = path_name;
        }

        public String getCat_id() {
                return cat_id;
        }

        public void setCat_id(String cat_id) {
                this.cat_id = cat_id;
        }

        public String getCat_name() {
                return cat_name;
        }

        public void setCat_name(String cat_name) {
                this.cat_name = cat_name;
        }

        public String getCat_type() {
                return cat_type;
        }

        public void setCat_type(String cat_type) {
                this.cat_type = cat_type;
        }

        public String getKeywords() {
                return keywords;
        }

        public void setKeywords(String keywords) {
                this.keywords = keywords;
        }

        public String getCat_desc() {
                return cat_desc;
        }

        public void setCat_desc(String cat_desc) {
                this.cat_desc = cat_desc;
        }

        public String getSort_order() {
                return sort_order;
        }

        public void setSort_order(String sort_order) {
                this.sort_order = sort_order;
        }

        public String getShow_in_nav() {
                return show_in_nav;
        }

        public void setShow_in_nav(String show_in_nav) {
                this.show_in_nav = show_in_nav;
        }

        public String getParent_id() {
                return parent_id;
        }

        public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
        }
}
