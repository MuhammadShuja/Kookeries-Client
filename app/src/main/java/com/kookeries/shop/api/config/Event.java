package com.kookeries.shop.api.config;

public class Event {
    public static final int USER_REGISTER = 101;
    public static final int USER_LOGIN = 102;
    public static final int USER_LOGOUT = 103;
    public static final int USER_PROFILE = 104;
    public static final int USER_PROFILE_UPDATE = 105;

    public static final int CATALOG_CATEGORY_INDEX = 111;
    public static final int CATALOG_PRODUCT_INDEX = 112;

    public static final int SELLER_DASHBOARD = 121;
    public static final int SELLER_ORDER_INDEX = 122;
    public static final int SELLER_ORDER_LATEST = 123;
    public static final int SELLER_PRODUCT_INDEX = 124;
    public static final int SELLER_PRODUCT_LATEST = 125;
    public static final int SELLER_PRODUCT_NEW = 126;
    public static final int SELLER_PRODUCT_UPDATE = 127;

    public static int active = -1;

}