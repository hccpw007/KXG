package com.cqts.kxg.bean;
import java.util.ArrayList;

/**
 * 我的徒弟
 */
public class MyApprenticeInfo {
    public ArrayList<Apprentice> signup;
    public ArrayList<Apprentice> task;

    public class Apprentice {
        public String user_name;//  "15215029766",
        public String alias;//  "",
        public String headimg;//  ""
    }
}
