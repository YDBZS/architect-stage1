package org.architect.oss.constant;

/**
 * @author duobao
 * @since 2021/11/10 20:27
 */
public interface Constant {
    String INTERFACE_METHOD_POST = "POST";
    // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    String ACCESSKEYID = "LTAI4FtZn5rDaPbLUjtwQULV";
    String ACCESSKEYSECRET = "lOWBhVhqvOB5YtrXr8SBUiFrRD0saM";

    String BUCKETNAME = "foodie-shop-dev";

    String PUBLIC_VISIT_WEBSITE = "https://foodie-shop-dev.oss-cn-beijing.aliyuncs.com/";
}
