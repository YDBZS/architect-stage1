package org.architect.constant;

import java.io.File;

/**
 * 常量
 *
 * @author 多宝
 * @since 2021/3/13 16:09
 */
public interface Constant {

    String USER_FACE = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201910%2F02%2F20191002185012_ocvro.thumb.400_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1618215121&t=9af2aa5926ef899bdd852c64510845ef";

    Integer COMMENT_PAGE_SIZE = 10;

    Integer PAGE_SIZE = 20;

    Integer STATUS_OK = 200;

    String FOODIE_SHOPCART = "shopcart";

    String INTERFACE_METHOD_POST = "POST";

    String INTERFACE_METHOD_GET = "GET";

    // 微信支付成功 -> 通知支付中心 -> 天天吃货平台
    //                           |->  回调通知的URL
    String PAY_RETURN_URL = "http://api.z.mukewang.com:8088/foodie-dev-api/orders/notifyMerchantOrderPaid";

    // 支付中心调用地址
    String PAYMENTURL = "https://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    Boolean FALSE = false;

    Boolean TRUE = true;

    // 用户头像上传的位置
    // File.separator   是适配不同操作系统的文件夹分隔符号
    String IMAGE_USER_FACE_LOCATION = "E:"
            + File.separator +
            "mayuncode"
            + File.separator +
            "aichitect-stage1"
            + File.separator +
            "userface";

    // 用户登录Token
    String REDIS_USER_TOKEN = "redis_user_token";

    // 单点登录CAS生成全局票据服务端会话标识
    String REDIS_USER_TICKET = "redis_user_ticket";

    // 单点登录CAS生成临时票据服务端会话标识
    String REDIS_TMP_TICKET = "redis_tmp_ticket";

    String COOKIE_USER_TICKET = "cookie_user_ticket";

}
