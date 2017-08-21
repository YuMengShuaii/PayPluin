package com.enation.javashop.pay.utils;

import android.app.Activity;

/**
 * Created by LDD on 17/5/4.
 */

public interface PaymentActControl  {
    void paymentCallback(int code, String message);
    Activity getActivity();
}
