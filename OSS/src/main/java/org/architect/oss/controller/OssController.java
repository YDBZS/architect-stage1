package org.architect.oss.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.StorageClass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.architect.oss.constant.Constant;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author duobao
 * @since 2021/11/10 20:25
 */
@Slf4j
@RestController
@RequestMapping("oss")
@Api(value = "阿里云文件服务器", tags = {"阿里云文件服务器"})
public class OssController {

    @PostMapping("/upload")
    @ApiOperation(value = "普通上传", httpMethod = Constant.INTERFACE_METHOD_POST)
    @SneakyThrows
    public String upload(@RequestParam MultipartFile file) {
        if (!file.isEmpty()) {

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(Constant.ENDPOINT, Constant.ACCESSKEYID,
                    Constant.ACCESSKEYSECRET);

            String uploadPath = "simpleupload/" + file.getOriginalFilename();


            PutObjectRequest putObjectRequest = new PutObjectRequest(Constant.BUCKETNAME, uploadPath,
                    file.getInputStream());

            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            metadata.setObjectAcl(CannedAccessControlList.PublicRead);
            putObjectRequest.setMetadata(metadata);

            // 上传文件。
            ossClient.putObject(putObjectRequest);

            // 关闭OSSClient。
            ossClient.shutdown();

            return Constant.PUBLIC_VISIT_WEBSITE + uploadPath;
        }
        return "";
    }

}
