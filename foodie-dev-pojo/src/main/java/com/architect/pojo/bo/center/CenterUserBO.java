package com.architect.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.architect.constant.Constant;

import java.util.Date;

/**
 * 用户中心传递对象
 *
 * @author 多宝
 * @since 2021/5/23 20:50
 */
@Data
@ApiModel(value = "用户对象", description = "用户对象")
public class CenterUserBO {

    @ApiModelProperty(value = "用户名", required = Constant.FALSE)
    private String userName;

    @ApiModelProperty(value = "密码", required = Constant.FALSE)
    private String password;

    @ApiModelProperty(value = "确认密码", required = Constant.FALSE)
    private String confirmPassword;

    @ApiModelProperty(value = "用户昵称", required = Constant.TRUE)
    private String nickName;

    @ApiModelProperty(value = "真实姓名", required = Constant.TRUE)
    private String realName;

    @ApiModelProperty(value = "手机号", required = Constant.TRUE)
    private String mobile;

    @ApiModelProperty(value = "邮箱地址", required = Constant.TRUE)
    private String email;

    @ApiModelProperty(value = "性别", required = Constant.TRUE)
    private Integer sex;

    @ApiModelProperty(value = "生日", required = Constant.TRUE)
    private Date birthday;
}
