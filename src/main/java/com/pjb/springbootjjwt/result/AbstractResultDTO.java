package com.pjb.springbootjjwt.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractResultDTO implements Serializable {
    private static final long serialVersionUID = 3874081007408979058L;
    @ApiModelProperty(value = "结果状态（成功 或 失败）", position = 0)
    protected Status status;
    @ApiModelProperty(value = "异常信息", position = 10)
    protected ResultError[] errors;
    @ApiModelProperty(value = "系统处理时间戳（增量拉数据时使用）", position = 11)
    @JsonInclude(Include.NON_NULL)
    protected Date timestamp;

    @JsonProperty(value = "status", index = 0)
    public Status getStatus() {
        return this.status;
    }

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = "errors", index = 1)
    public ResultError[] getErrors() {
        return this.errors;
    }

    protected void setErrors(ResultError... errors) {
        this.errors = errors;
    }

    @JsonIgnore
    public boolean isFailure() {
        return Status.failure == this.status;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return Status.success == this.status;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JsonIgnore
    public String errorsToString() {
        if (this.errors != null && this.errors.length > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("Errors : [");
            ResultError[] arg1 = this.errors;
            int arg2 = arg1.length;

            for (int arg3 = 0; arg3 < arg2; ++arg3) {
                ResultError error = arg1[arg3];
                builder.append(error.toString());
            }

            builder.append("]");
            return builder.toString();
        } else {
            return "errors : []";
        }
    }

    public enum Status {
        success, failure;

        Status() {}
    }
}