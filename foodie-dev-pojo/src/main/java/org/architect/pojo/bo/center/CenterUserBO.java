package org.architect.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
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

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;

    @ApiModelProperty(value = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 12, message = "用户昵称不能超过12位")
    private String nickName;

    @ApiModelProperty(value = "真实姓名")
    @Length(max = 12, message = "用户真实姓名不能超过12位")
    private String realName;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号格式不正确")
    private String mobile;

    @ApiModelProperty(value = "邮箱地址")
    @Email
    private String email;

    @ApiModelProperty(value = "性别")
    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    private Date birthday;
}
