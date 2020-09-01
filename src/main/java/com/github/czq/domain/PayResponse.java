package com.github.czq.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.czq.utils.CzbkWebStatus;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayResponse implements Serializable {
    private static final long serialVersionUID = -7318467237446066728L;

    private String code;
    private String msg;
    private String sign;
    private String timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object biz_params;

    @JsonIgnore
    public boolean isSuccess() {
        return this.code.equals("0000");
    }


}
