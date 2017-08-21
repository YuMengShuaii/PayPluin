package com.enation.javashop.pay.config;

/**
 * Created by LDD on 17/7/31.
 */

public class PayConfig {

    private static String payUrl = "";

    public static String getPayUrl(){
        return payUrl;
    }

    public static void initPayUrl(String mPayUrl){
        payUrl = mPayUrl;
    }
}
