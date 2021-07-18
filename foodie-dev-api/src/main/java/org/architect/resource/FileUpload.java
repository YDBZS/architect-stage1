package org.architect.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 多宝
 * @since 2021/6/2 21:44
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-prod.properties")
public class FileUpload {

    private String imageUserFaceLocation;

    private String imageServerUrl;

}
