package org.architect.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户前段传递信息
 *
 * @author 多宝
 * @since 2021/3/13 16:03
 */
@Data
@ApiModel(value = "用户注册的信息", description = "用户注册的信息")
public class UsersBO {

    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("确认密码")
    private String confirmPassword;

}
