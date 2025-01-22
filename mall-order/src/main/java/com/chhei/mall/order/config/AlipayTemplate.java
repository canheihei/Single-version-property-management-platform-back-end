package com.chhei.mall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.chhei.mall.order.vo.PayVo;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 支付宝的配置文件
 */
//@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    // 商户appid
    public static String APPID = "9021000143671523";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCpNSryMUXg9QnqrVyzq7YtwoYAfiSpgvbQExhhuZivCgZiwh9R2IXcNVYvCQBFJNml3UAriZhKdb8i9R69SOTZZdnw0XYtMDsrftIsBomXXCVcWmAE7nrggfivG0F1pN4IvvCPcXeSZa9CfpMDgEjFsPqashitYobkv/2sikTsH8iDuaiXM29h8fd59f9PBIsvBJJ8ees5z3af3VrmopI7VQwYK2d+zFTTXfSOVclQq4ZyFRUwsqy9haUabOey9NBJdYtTTNYN9VjWgqtIndr+cHiSj5p2hQHaTgyEX4AzQyRDcbF6547Koa57z3RpcT18WVxkHxSWeImpyYw9EuAZAgMBAAECggEAMl6eYyxhxd6Lzi0/PEjwWVzfsQPb/A39VOFqT6UFG44baoio9B9FcdD6z9OZHfwjQinDsXIrQHcep9RaCzTI0yJrpzFjQX5rDr6Y5/wmN0V55a0NikpHj8+A7PdrHA8w/V+BkuHPG2hjrKKrNs6YWykc4LIFgI+k4jF43UTn0XYCq6mDnyUO0T07bEr15jARm8aHQcuyroHR+inyM23du9sJ4f52ljZykiI9P9Q1i/fexI+fmWEDJosbbsU0YGxnezxF4o6Jt7urook4trX4jjzQhhgSOCjffTAR5OrxLQEn2YQ3NtxSakpT2qlasQkwxtdDOTEQ2nuNAeb2Jh7ogQKBgQD5vQw+THJpxvSkOzOYt1ZcOYu4nS0JV1V3D1NVsOdhgsqSFGLHXkWkhduvnUVHwViFkJwqwI5VYk564MEZmElfTubE9ng3LCd53cmANNPNiSGO6SKL+F75QqlhsSB+3o5p4LCmD+IivChTxRHBDqnZtpm+GI+g2SZUpC4OhnkSUQKBgQCtczsyQHUSRwvoh2lkBMPnPDmTXNqHmgJsRMk2OrQg4tYrKdH8seftOKWq9ykGxgnCUmIe2Q8Cw35rJGRJlqaPpmHh5SaoHdHeUBDzwVV1ctLEaMPvKsujws7JzoY3xn80abU8+rpYvu5Sww9a5/zkJWHYvDksYyCV+bt0uTl3SQKBgQDnMC/uWByueUSt6DQqksY6l/cVT0OK2zM+BYzyzdJUvZxDQOX0g/vagyRM3T1uYLlO5AD9vsXEmz3lmLyJsiVyOnt9KGE/Sfu9YCiSK9VK90B+qKmXg7R/8UsWKb/YUrTppNxCzn9cAm0ep1d5LdbkuZp3lQsZLwzB3D7aqXfZEQKBgGrd85wboMYZJhonzGSGlxlSvvqvnCkuV83Td9HEy5TzjhdcFfhGuOotG83BqPJ2dC7ZSYFOJXSDdNGYw32fp5IcZnSjFnVAoj0CQd+dwAS0MW44SniCeqO0dGpcp8+XqmplsvgWV1g0k2xEqKJPCz6aVbYPCn7xuHMPQlNBiEmZAoGBAOPAmyAwd1/NlBhlpxDoWQ+3PrwsnBh2vl4lCuIs0p7RIsSydu7BE+tyrAsiuhoxG9c5iVRTkai1IZfkKuHOc/iXxLnTrubqPC+fTQRfkHB4f7EJ/ApKmBLMEC/SVaauSrWsVmCQOyDnRNJtvBJG5uOVK5bd7x2L8wew2iFpj/IK";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/payed/notify";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://localhost:8030/orderPay";
    // 请求网关地址
    public static String URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjl5JqSvIE+/Bxzgd73PBJUp2H9C8ExdIgcNDev+0lpkmwjHCiUG1cCBvNttGosXNZfUZpnBbwdnfYdcouT9B1I0P5U8dqYXEqH43b3TS1X8pgHIvPUbWEp8XpYC8vtaceFGZd9Rmejj6H/+/Us5or2K9pGydboT0YoUo1nBk6xqCITw3DYcjNba6/nsJhCVAI2nZCHxd5K0SoECx/emc6bJrWhIggwmDavzTP7+6m3GxZgrHMlPwjESMUwkYtvHTWB58Rgp3RQACSwuVoqCHwAekVM+Fpz0744fn2Uw2HmZeEIlsi4Gx6B2yfxZC9gNIeD9ReqA7gqttC99Ie0M0GwIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";

    public String pay(PayVo payVo){
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(URL,
                APPID,
                RSA_PRIVATE_KEY,
                FORMAT,
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(payVo.getOut_trader_no());
        model.setSubject(payVo.getSubject());
        model.setTotalAmount(payVo.getTotal_amount());
        model.setBody(payVo.getBody());
        model.setTimeoutExpress("5000");
        model.setProductCode("11111");
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(return_url);

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipay_request).getBody();
            return form;
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  null;
    }
}
