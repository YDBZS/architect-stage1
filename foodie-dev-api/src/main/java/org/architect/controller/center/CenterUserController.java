package org.architect.controller.center;

import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.pojo.Users;
import org.architect.pojo.bo.center.CenterUserBO;
import org.architect.pojo.vo.UsersVO;
import org.architect.resource.FileUpload;
import org.architect.service.center.CenterUserService;
import org.architect.util.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 用户中心接口
 *
 * @author 多宝
 * @since 2021/5/23 20:46
 */
@Slf4j
@RestController
@RequestMapping("userInfo")
@Api(value = "用户信息接口", tags = {"用户信息接口"})
public class CenterUserController {

    @Resource
    private CenterUserService centerUserService;
    @Resource
    private FileUpload fileUpload;
    @Resource
    private DistributedSessionUtil distributedSessionUtil;

    @PostMapping("/update")
    @ApiOperation(value = "用户修改账户信息", httpMethod = Constant.INTERFACE_METHOD_POST)
    public ReturnResult update(@RequestParam
                               @ApiParam(name = "userId", value = "用户ID", required = true)
                                       String userId, @RequestBody @Valid CenterUserBO centerUserBO,
                               BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {

        // 判断BindingResult是否包含错误的验证信息，如果有则直接return就可以了
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return ReturnResult.errorMap(map);
        }

        Users userInfo = centerUserService.updateUserInfo(userId, centerUserBO);
        // 增加令牌tocken，会整合进redis，分布式会话
        UsersVO usersVO = distributedSessionUtil.convertUsersBO(userInfo);

//        userInfo = passprotController.setNull(userInfo);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        return ReturnResult.ok();
    }


    @PostMapping("/uploadFace")
    @ApiOperation(value = "头像修改", httpMethod = Constant.INTERFACE_METHOD_POST)
    @SneakyThrows
    public ReturnResult uploadFace(
            @RequestParam
            @ApiParam(name = "userId", value = "用户ID", required = true)
                    String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 定义头像保存的地址
//        String fileSpace = Constant.IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        // 在路径上为每一个用户增加一个userId，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;
        // 开始文件上传
        if (file.isEmpty()) {
            return ReturnResult.errorMsg("文件不能为空！");
        } else {
            // 获取上传的文件的文件名称
            String filename = file.getOriginalFilename();
            if (StringUtils.isNotBlank(filename)) {
                String[] fileNameArr = filename.split("\\.");
                //获取文件后缀名
                String suffix = fileNameArr[fileNameArr.length - 1];
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")) {
                    return ReturnResult.errorMsg("图片格式不正确");
                }
                // 文件名称重组  覆盖式上传
                String newFileName = "face-" + userId + "." + suffix;
                // 上传头像最终保存的位置
                String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

                //用于提供给web服务访问的地址
                uploadPathPrefix += ("/" + newFileName);

                File outFile = new File(finalFacePath);
                if (outFile.getParentFile() != null) {
                    // 创建文件夹
                    outFile.getParentFile().mkdirs();
                }
                // 文件输出保存到目录
                FileOutputStream stream = new FileOutputStream(outFile);
                InputStream inputStream = file.getInputStream();
                IOUtils.copy(inputStream, stream);

                stream.flush();
                stream.close();
                // face-{userid}.png
            }
        }

        // 更新用户头像到数据库
        String imageServerUrl = fileUpload.getImageServerUrl();

        // 由于浏览器可能出现缓存的情况，所以我们需要加上时间戳来保证更新后的图片可以即时刷新
        String finalUserFaceUrl =
                imageServerUrl + uploadPathPrefix + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        Users users = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        // 增加令牌tocken，会整合进redis，分布式会话
        UsersVO usersVO = distributedSessionUtil.convertUsersBO(users);
//        users = passprotController.setNull(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);


        return ReturnResult.ok();
    }

    /**
     * 处理validator错误信息
     *
     * @param result 信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 多宝
     * @since 2021/5/29 19:52
     */
    private Map<String, String> getErrors(BindingResult result) {

        Map<String, String> map = Maps.newHashMap();

        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors) {
            // 发生验证错误所对应的某一个属性
            String field = error.getField();
            // 验证错误信息
            String message = error.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }

}
