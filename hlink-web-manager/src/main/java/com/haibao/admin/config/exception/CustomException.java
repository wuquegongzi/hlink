package com.haibao.admin.config.exception;

import cn.hutool.core.util.StrUtil;
import com.haibao.admin.web.common.enums.CodeEnum;

/*
 * @Author ml.c
 * @Description //自定义业务运行异常 需要手动抛出
 * @Date 15:02 2020-04-23
 **/
public class CustomException extends RuntimeException {

    private int code;
    private String message;

    public CustomException(CodeEnum codeEnum,String message) {
        super(message);
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        String msg = "";
        if(StrUtil.isEmpty(super.getMessage())){
            msg = this.message;
        }else {
            msg = super.getMessage();
        }
        return msg;
    }
}
