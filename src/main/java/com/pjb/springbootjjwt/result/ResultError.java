package com.pjb.springbootjjwt.result;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ResultError implements Serializable {
    private static final long serialVersionUID = 8042733799291115991L;
    @ApiModelProperty(value = "字段名（字段校验类异常）", position = 0)
    private String field;
    @ApiModelProperty(value = "异常消息", position = 1)
    private String errmsg;
    @ApiModelProperty(value = "异常编码", position = 2)
    private String errcode;

    public ResultError() {
    }

    public ResultError(String errmsg, String field) {
        this.field = field;
        this.errmsg = errmsg;
    }

    public ResultError(String errcode, String errmsg, String field) {
        this.field = field;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public ResultError(UserError userError){
        this.errcode = userError.getErrorCode();
        this.errmsg = userError.getErrorMessage();
    }

    public String getField() {
        return this.field;
    }

    public String getErrmsg() {
        return this.errmsg;
    }

    public String getErrcode() {
        return this.errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResultError{");
        sb.append("field='").append(field).append('\'');
        sb.append(", errmsg='").append(errmsg).append('\'');
        sb.append(", errcode='").append(errcode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}