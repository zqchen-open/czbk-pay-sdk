package com.github.czq.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CzbkResponse implements Serializable {
    private static final long serialVersionUID = -7318467237446066728L;

    private int code;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;


    public static CzbkResponse success() {
        return builder().code(CzbkWebStatus.SUCCESS.getCode()).msg(CzbkWebStatus.SUCCESS.getMsg()).build();
    }

    public static CzbkResponse success(Object data) {
        return builder().code(CzbkWebStatus.SUCCESS.getCode()).msg(CzbkWebStatus.SUCCESS.getMsg()).data(data).build();
    }

    public static CzbkResponse failure() {
        return builder().code(CzbkWebStatus.FAILURE.getCode()).msg(CzbkWebStatus.FAILURE.getMsg()).build();
    }

    public static CzbkResponse error(Exception e) {
        return builder().code(CzbkWebStatus.FAILURE.getCode()).msg(e.getMessage()).build();
    }

    public static CzbkResponse error(CzbkStatus czbkStatus) {
        return builder().code(czbkStatus.getCode()).msg(czbkStatus.getMsg()).build();
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == CzbkWebStatus.SUCCESS.getCode();
    }


}
