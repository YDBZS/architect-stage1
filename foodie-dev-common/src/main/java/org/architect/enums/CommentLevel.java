package org.architect.enums;

/**
 * 商品评价等级的枚举
 *
 * @author 多宝
 * @since 2021/3/13 22:06
 */
public enum CommentLevel {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3,"差评");

    public final Integer type;

    public final String val;

    CommentLevel(Integer type, String val) {
        this.type = type;
        this.val = val;
    }
}
