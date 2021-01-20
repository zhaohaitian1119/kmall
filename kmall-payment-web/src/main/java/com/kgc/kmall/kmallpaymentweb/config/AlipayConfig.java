package com.kgc.kmall.kmallpaymentweb.config;

/**
 * @author shkstart
 * @create 2021-01-18 16:21
 */
public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号，此处是沙箱环境的appid
    public static String app_id = "2021000117601897";

    // 商户私钥，您的PKCS8格式RSA2私钥，需要使用支付宝平台开发助手生成私钥和公钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCCgz9ZZKQdS4Caf6w3AOq/cIlP9D2uMM/cC29K3BS1rDhWY/Pkm0OcIXH4Va9HMKvkZstD87/cQOof6iQRAVp6/+uifpA2G9IAS7R9QLY4WWcL6ek50QszygrB5L7acdZIcszy32nTtsVvKHnFvEb+z9yLOiZ0/2tE0e14uo4M8g8KlLoO5MOyGQJFjVZtgou6n+cUzSTclvf3huFAKS9CvIpxezNz3xw/FxxgccdF1dihCClg+ar7ZPV4BiyjyHdua8CWkH7QtGkdNUOo8AAQQ7U3WRfzPomV/lJf+1c5lXnln/Fv9DWXoXH3KDk/I69R6cw2VnY2/sNiIfogxy1nAgMBAAECggEAfy5W9U3Vf5ar4+K2d4M1PwjzRi7ETJLUe6HI+jItHDyPSeBtM0D6SZNt9InLHJyZZSGmwrktJf08KY2JR+kBuwrZHKgo8BF8Oz2KzOParl6wxDNVSzoiqH0irn+yXDzCjCcTATolbmKVUxUmVUJ8DKOSTzqDb0/XwGCyAQwKkSGMpTXyQx4BYVkCWJDL6omEb16BJBChOTr2SxGbrY+sHOIeJkb7pz6NBifwDMbXUmgu/CjpiujRWT3xCB/6SNAgCwT7ahOsycWl+gLC1YP3cRW/IqciCeYjg/7AwSU+5I7GIsqgZjFgb7SRoll7Kd/OdofD3fEYphfu4fyOGED6gQKBgQC6doggtG/tVV3EWE6ztTrYkDYju3wDdanftG6f+/7nPV3zsfzNTrYv7cfEIjxX9qwe1EFHrUdOnCQ/4PueWaTEOMySIANodSX1K+CrVHFuFV9cy7FXf2Nzol2tXWvcJxFRHQmxh8usUFxo5yEroLZwS2UTXZBFkhSvbemNLMoETwKBgQCzLy88GEx6Q954UQc0MxyCGQ1v1UkHMeaWO9DP6KEWTA4FgoL6HVvEHguPUTKQdnLNsmKdH/s8EjpZyWJZRx2KHhiAZqYjGVNy0lmDnOi6XZvnDttlDeDeTgzzpDlYdqymjGyMWqJuyShccbHTd0J3R5eBHxyei4OZq6Lsb87HaQKBgQCwKd5Qb8izRIyzPO8WL6mqAw1zUuVGa3sTw+ckNTNNixfhbg42diAARjkO3319aaqR8dy++EG/ThudUlTG9VCKplL311EA9nTnHg5EMCJ+a6jOBg/FZ7AxBnxUl0eZFjvxE2seE/rkEIROA9e93Tef/ks/MY1lfS8X0tLtsxwlVwKBgH4rlGQJbBiPn6Ay1hLAhqto/pgnpAOH9KkGt3MeiAdOSHVCv/1kHNDMozEoaKVmclI68MtY0Kws3Z+JeVIKDFfL9066ePOwCp2IVnWhUBKihXu+b2gJzLRcWyxYD/Ulo06nBGwUCdpelKf7es6/ZDLfW2QVRy6waFsVi2RXGlA5AoGBALitbFpk/sMkh2FwYjz+/A5gHUM9Abl96xPPKX/EE2h3Zdtkxyt5sN0FiNe2wViuT340OFxeLkIyKkMfds66WhsZba1EqwCv8u2RaEf8ugOAhP4kQEcv1gNRhAOPsi/nO1fZs7MRUHKV+snONwm/RNbHDFpfA+LXyHx050KV5sde";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiJU7ehrJo6bJS8QBPnxA2rJ1F68TL8+ubeaN4kUleYf0uo2QmHVbPJ5++6Z26sBP0YowK1d3e01m06V2um5EeqKZe1EQTsmkqAYFWYKW21N96cN/Aay4c+9tF2lQj1N99d/QbFApy2FPSbK/ZizF/+iWrbBDE9/wxUzfpt17zRxr9nEu5xDxZsEMulHaTgQPEPn0rA7Wo6i/TashUxfJMRLXgR+ghhYMIRihp5aXGewpL6rN9NfH4VUTybCwE7zJyX3LMGa/hU08MBDBIG7nuPN1OeJhlRrT9XjBL35NfOQfBAv9gvQpxV38NQMYZqj2Lyf9WkYh2eA1vurpONKFkwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url ="http://60.205.215.91/alipay/callback/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://payment.kmall.com:8088/alipay/callback/return";


    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝沙箱网关
    public static String gatewayUrl ="https://openapi.alipaydev.com/gateway.do";
}
