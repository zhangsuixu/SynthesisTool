package com.service.bean;


import com.service.base.BaseBean;

public class UserInfoResponse extends BaseBean<UserInfoResponse> {

    /**
     * user_info : {"qq":"","imgurl":"","phone":"13750077520","diy_sign":"这家伙很懒，什么都没写","nickname":"13750077520","id":3}
     * token : acc059eddfc2cf9d175c241168f54f91
     */

    public String token;
    public String userId;
    public String phone;
    public String userName;
    public String identNo;
    public String bankNo;
    public String detailAddress;
    public String email;
    public String roleName;
    public String userRole;              //用户角色 mock=MAC-商户，SELLER-店员，VIVOSELLER-vivo销售，OPPOSELLER-oppo销售，SELLER_GIVEU-即有销售，SELLER_DXT-迪信通销售
    public String province;
    public String city;
    public String area;
    public String provinceName;
    public String cityName;
    public String areaName;
    public String hasInsuranceRight;      //销售是否拥有保险权限 0:没有，1:有  此字段只有销售用户登录才返回
    public String storeCityName;          //销售所在城市
    public String hasMerchant;            //商户登录 是否有商户 0:没有 1:有  此字段只有商户用户登录才返回
    public String hasHebaoRight;           //销售登录是否有和包权限  0:没有,1:有 此字段只有销售用户登录才返回
}
