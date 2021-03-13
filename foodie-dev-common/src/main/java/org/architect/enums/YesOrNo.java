package org.architect.enums;

/**
 * 是否枚举
 *
 * @author 多宝
 * @since 2021/3/13 22:06
 */
public enum YesOrNo {
    YES(1, "是"),
    NO(0, "否");

    public final Integer type;

    public final String val;

    YesOrNo(Integer type, String val) {
        this.type = type;
        this.val = val;
    }
}
