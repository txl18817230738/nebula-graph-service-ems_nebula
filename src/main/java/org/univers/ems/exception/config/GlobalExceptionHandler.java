package org.univers.ems.exception.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.univers.ems.exception.FileParsingException;
import org.univers.ems.pojo.response.ResponseMsg;

import java.util.Collections;
import java.util.List;

/**
 * @author jie.xi
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileParsingException.class)
    @ResponseBody
    public ResponseMsg handleCustomException(FileParsingException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setData(errors);
        responseMsg.setCode(201);
        responseMsg.setMessage("文件处理出现异常");
        return responseMsg;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseMsg handleAllExceptions(Exception ex, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setData(errors);
        responseMsg.setCode(201);
        responseMsg.setMessage("出现异常");
        return responseMsg;
    }

    // 可以添加更多的异常处理方法...
}