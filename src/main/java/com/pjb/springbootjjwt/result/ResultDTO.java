package com.pjb.springbootjjwt.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class ResultDTO<T> extends AbstractResultDTO {
    private static final long serialVersionUID = 1668914867578552488L;
    @ApiModelProperty(value = "业务数据", position = 1)
    private T data;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = "data", index = 2)
    public T getData() {
        return this.data;
    }

    private void setData(T data) {
        this.data = data;
    }

    public ResultDTO() {
    }

    ResultDTO(Status status) {
        this.status = status;
    }

    public static <T> ResultDTO<T> success() {
        ResultDTO<T> result = new ResultDTO<T>(Status.success);
        return result;
    }

    public static <T> ResultDTO<T> failure(ResultError... errors) {
        ResultDTO<T> result = new ResultDTO<T>(Status.failure);
        result.setErrors(errors);
        return result;
    }

    public static <T> ResultDTO<T> success(T data) {
        ResultDTO<T> result = new ResultDTO<T>(Status.success);
        result.setData(data);
        return result;
    }

    public static <T> ResultDTO<T> failure(T data, ResultError... errors) {
        ResultDTO<T> result = new ResultDTO<T>(Status.failure);
        result.setData(data);
        result.setErrors(errors);
        return result;
    }
}