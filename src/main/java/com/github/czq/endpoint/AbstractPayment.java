package com.github.czq.endpoint;

import com.github.czq.constants.EndpointConstants;
import com.github.czq.constants.PayConstants;

public abstract class AbstractPayment {

    /**
     * 连接方式:直连-DIRECT;非直连-INDIRECT;
     * {@link PayConstants.ConnType#DIRECT}
     * {@link PayConstants.ConnType#INDIRECT}
     *
     * @param connType 连接方式
     * @return 支付处理
     */
    public abstract Object connType(String connType);

    /**
     * 平台
     * {@link PayConstants.Platform#ALIPAY}
     *
     * @param Platform 平台
     * @return 支付处理
     */
    public abstract Object platform(String Platform);

    /**
     * 终端
     * {@link EndpointConstants#APP}
     * {@link EndpointConstants#PC}
     * {@link EndpointConstants#H5}
     * {@link EndpointConstants#WCOA}
     * {@link EndpointConstants#WXPROG}
     *
     * @param endpoint 终端
     * @return 支付处理
     */
    public abstract Object endpoint(String endpoint);

}
