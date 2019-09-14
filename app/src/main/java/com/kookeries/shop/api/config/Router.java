package com.kookeries.shop.api.config;

public class Router {
    private static final String API_HOST = "https://kookeries.com";
    private static final String API_VERSION = "/api/v1";
    private static final String EP_API = API_HOST+API_VERSION;

    private static final String EP_USER_REGISTER = "/register";
    private static final String EP_USER_LOGIN = "/oauth/token";
    private static final String EP_USER_LOGOUT = "/logout";
    private static final String EP_USER_PROFILE = "/profile";
    private static final String EP_USER_PROFILE_UPDATE = "/profile/update";

    private static final String EP_CATALOG_CATEGORY_INDEX = "/catalog/categories";
    private static final String EP_CATALOG_PRODUCT_INDEX = "/catalog/products";

    private static final String EP_SELLER_ORDER_INDEX = "/seller/products/orders";
    private static final String EP_SELLER_ORDER_LATEST = "/seller/products/orders/latest";
    private static final String EP_SELLER_PRODUCT_INDEX = "/seller/products";
    private static final String EP_SELLER_PRODUCT_LATEST = "/seller/products/latest";
    private static final String EP_SELLER_PRODUCT_NEW = "/seller/products/new";
    private static final String EP_SELLER_PRODUCT_UPDATE = "/seller/products/update";

    public static String getURL(){
        switch (Event.active){

//            USER EVENTS

            case Event.USER_REGISTER:
                return  EP_API + EP_USER_REGISTER;
            case Event.USER_LOGIN:
                return  API_HOST + EP_USER_LOGIN;
            case Event.USER_LOGOUT:
                return  EP_API + EP_USER_LOGOUT;
            case Event.USER_PROFILE:
                return EP_API + EP_USER_PROFILE;
            case Event.USER_PROFILE_UPDATE:
                return EP_API + EP_USER_PROFILE_UPDATE;

//                CATALOG EVENTS

            case Event.CATALOG_CATEGORY_INDEX:
                return  EP_API + EP_CATALOG_CATEGORY_INDEX;
            case Event.CATALOG_PRODUCT_INDEX:
                return  EP_API + EP_CATALOG_PRODUCT_INDEX;

//                SELLER EVENTS

            case Event.SELLER_ORDER_INDEX:
                return EP_API + EP_SELLER_ORDER_INDEX;
            case Event.SELLER_ORDER_LATEST:
                return EP_API + EP_SELLER_ORDER_LATEST;
            case Event.SELLER_PRODUCT_INDEX:
                return EP_API + EP_SELLER_PRODUCT_INDEX;
            case Event.SELLER_PRODUCT_LATEST:
                return EP_API + EP_SELLER_PRODUCT_LATEST;
            case Event.SELLER_PRODUCT_NEW:
                return EP_API + EP_SELLER_PRODUCT_NEW;
            case Event.SELLER_PRODUCT_UPDATE:
                return EP_API + EP_SELLER_PRODUCT_UPDATE;
        }
        return null;
    }
}