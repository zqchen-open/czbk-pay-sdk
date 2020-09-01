package com.github.czq.constants;

import org.assertj.core.util.Lists;

import java.util.List;

/**
 * 支付平台请求参数定义
 */
public class PayConstants {
    /**
     * 支付类型
     */
    public static class Platform {
        /**
         * 支付宝
         */
        public static final String ALIPAY = "ALIPAY";
        /**
         * 花呗分期
         */
        public static final String INSTALMENTS = "INSTALMENTS";
        /**
         * 微信支付
         */
        public static final String WEIXIN = "WEIXIN";
        /**
         * 百度分期
         */
        public static final String UMONEY = "UMONEY";
        /**
         * OTHERS
         */
        public static final String OTHERS = "OTHERS";
        /**
         * POS
         */
        public static final String POS = "POS";
        /**
         * 招行一网通
         */
        public static final String YWT = "YWT";
        /**
         * 融易分期
         */
        public static final String RONG360 = "RONG360";

        public static final List<String> CONN_TYPES = Lists.newArrayList(ALIPAY, WEIXIN);
    }

    public static class ConnType {
        /**
         * 直连
         */
        public static final String DIRECT = "DIRECT";
        /**
         * 非直连
         */
        public static final String INDIRECT = "INDIRECT";

    }

    /**
     * 分期期数
     */
    public static class HbFqNum {

        public static final String ALIPAY_6 = "6";
        public static final String ALIPAY_12 = "12";

        public static final String RONG360_6_12 = "6_12";
        public static final String RONG360_9_12 = "9_12";

    }
}
