package org.architect.enums;

/**
 * 性别枚举
 *
 * @author 多宝
 * @since 2021/3/13 16:16
 */
public enum Sex {

    WOMAN(0,"女"),
    MEN(1,"男"),
    SECRIT(2,"保密");

    public final int type;

    public final String value;

    Sex(int type, String value) {
        this.type = type;
        this.value = value;
    }
}
