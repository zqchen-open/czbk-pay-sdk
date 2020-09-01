package com.github.czq.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.czq.utils.CzbkWebStatus;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundResponse implements Serializable {
    private static final long serialVersionUID = -7318467237446066728L;

    private int code;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == CzbkWebStatus.SUCCESS.getCode();
    }


}
