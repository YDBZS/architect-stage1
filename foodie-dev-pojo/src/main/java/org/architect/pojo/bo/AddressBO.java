package org.architect.pojo.bo;

import lombok.Data;

/**
 * 用于新增或者是修改地址的BO
 *
 * @author 多宝
 * @since 2021/5/22 13:13
 */
@Data
public class AddressBO {

    private String addressId;

    private String userId;

    private String receiver;

    private String mobile;

    private String province;

    private String city;

    private String district;

    private String detail;

}
