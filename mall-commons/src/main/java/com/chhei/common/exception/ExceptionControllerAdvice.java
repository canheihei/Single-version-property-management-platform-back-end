package com.chhei.common.exception;

import com.chhei.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一的异常处理类
 */
/*@ResponseBody
@ControllerAdvice*/
@Slf4j
@RestControllerAdvice(basePackages = "com.chhei.mall.product.controller")
public class ExceptionControllerAdvice {
    /**
     * 处理验证异常的方法
     * @param e
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handlerValidExecption(MethodArgumentNotValidException e){
        Map<String,String> map = new HashMap<>();
        e.getFieldErrors().forEach((fieldError)->{
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        //return R.error(400,"提交的数据不合法").put("data",map);
        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMsg())
                .put("data",map);
    }

    /**
     * 系统其他的异常处理
     * @param throwable
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public R handlerExecption(Throwable throwable){
        log.error("错误信息：",throwable);
        //return R.error(400,"未知异常信息").put("data",throwable.getMessage());
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg())
                .put("data",throwable.getMessage());
    }
}
