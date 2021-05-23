package org.architect.enums;

/**
 * 支付方式 枚举
 */
public enum PayMethod {

    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * 通过支付类型查找支付方式
     *
     * @param payMethod 支付类型
     * @return boolean
     * @since 2021/5/22 15:10
     */
    public static PayMethod getValueByType(Integer payMethod) {
        for (PayMethod value : PayMethod.values()) {
            if (payMethod.equals(value.type)) {
				return value;
            }
        }
        return null;
    }
}
