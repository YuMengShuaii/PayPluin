package com.enation.javashop.pay.core.alipay;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.enation.javashop.pay.model.PayResult;
import com.enation.javashop.pay.iinter.PaymentActControl;

/**
 * Created by Dawei on 10/8/16.
 */

public class AlipayHelper {

    private String payStr;

    private PaymentActControl paymentActivity;
    /**
     * 支付宝支付回调处理
     */
    private Handler alipayHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);

            // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
            String resultInfo = payResult.getResult();
            String resultStatus = payResult.getResultStatus();

            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                paymentActivity.paymentCallback(1, "订单支付成功！");
            } else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    paymentActivity.paymentCallback(0, "支付结果确认中！");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    paymentActivity.paymentCallback(0, "支付失败，请您重试！");
                }
            }
        }
    };

    public AlipayHelper(String payStr, PaymentActControl paymentActivity) {
        this.payStr = payStr;
        this.paymentActivity = paymentActivity;
    }


    /**
     * 发起支付
     */
    public void pay(){

        final String payInfo = payStr;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(paymentActivity.getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo,true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                alipayHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

}
