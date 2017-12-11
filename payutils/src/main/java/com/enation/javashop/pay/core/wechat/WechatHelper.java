package com.enation.javashop.pay.core.wechat;

import android.util.Log;
import android.util.Xml;
import com.enation.javashop.pay.iinter.PaymentActControl;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.xmlpull.v1.XmlPullParser;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class WechatHelper {

    /**
     * appid
     */
    private String appId = "";

    private IWXAPI msgApi = null;


    private String resultStr = "";

    public WechatHelper(String xmlStr,PaymentActControl paymentActivity) {
        msgApi = WXAPIFactory.createWXAPI(paymentActivity.getActivity(), null);
        this.resultStr =xmlStr;
        init(resultStr);
    }

    /**
     * 发起微信支付
     */
    public void pay(){
        PayReq payReq = xmlToPayReq(resultStr);
        msgApi.registerApp(appId);
        msgApi.sendReq(payReq);
    }

    private void init(String xml){
        Map<String,String> resultMap =  decodeXml(xml);
        appId = resultMap.get("appid");
    }

    private PayReq xmlToPayReq(String xml){
        Map<String,String> resultMap =  decodeXml(xml);
        PayReq payReq = new PayReq();
        payReq.appId = resultMap.get("appid");
        payReq.partnerId = resultMap.get("partnerid");
        payReq.prepayId = resultMap.get("prepayid");
        payReq.packageValue = resultMap.get("package");
        payReq.nonceStr = resultMap.get("noncestr");
        payReq.timeStamp = resultMap.get("timestamp");
        payReq.sign = resultMap.get("sign");
        return payReq;
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {

                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("XmlError", e.toString());
        }
        return null;

    }

}
