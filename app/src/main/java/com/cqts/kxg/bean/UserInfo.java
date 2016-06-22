package com.cqts.kxg.bean;

/**
 * Created by Administrator on 2016/5/26.
 */
public class UserInfo {
    public String headimg;// "",
    public String user_id;//会员资料自增id
    public String email;//会员Email
    public String user_name;//	用户名
    public int sex;//性别 ; 0保密; 1男; 2女
    public String birthday;//出生日期
    public String user_money;//用户现有资金
    public String frozen_money;//	用户冻结资金
    public String pay_points;//	消费积分
    public String rank_points;//	会员等级积分
    public String reg_time;//	注册时间
    public String last_login;//	最后一次登录时间
    public String last_time;//	应该是最后一次修改信息时间，该表信息从其他表同步过来考虑
    public String last_ip;//	最后一次登录IP
    public String parent_id;//	推荐人会员id
    public String alias;//昵称
    public String mobile_phone;//移动电话
    public String invite_code;//邀请码
    public double app_money;//余额

}
