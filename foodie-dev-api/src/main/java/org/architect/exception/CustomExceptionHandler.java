package org.architect.exception;

import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 自定义的异常助手类
 *
 * @author 多宝
 * @since 2021/6/2 22:53
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    // 上传文件超过500k，捕获异常 MaxUploadSizeExceededException
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ReturnResult handlerMaxUploadFile(MaxUploadSizeExceededException ex) {
        return ReturnResult.errorMsg("文件上传大小不能超过500k，请压缩图片或者降低图片质量！！！");
    }

}
