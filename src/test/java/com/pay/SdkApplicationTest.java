package com.pay;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.czq.config.SnowflakeConfig;
import com.github.czq.domain.UnifyOrderRequest;
import com.github.czq.factory.Factory;
import com.github.czq.utils.RSA2ForMchtUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 统一下单测试单元
 */
public class SdkApplicationTest {

    @Before
    public void setUp() {
        Factory.setOptions(TestAccount.Configs.CONFIG);
    }

    /**
     * 统一下单
     * @throws Exception 下单异常
     */
    @Test
    public void unifyOrderTest() throws Exception {
        SnowflakeConfig snowflakeConfig = new SnowflakeConfig();
        // Factory.能力类别.支付平台.场景类别.连接方式.接口方法名称( ... )
        String merchantOrderNo = snowflakeConfig.getIdWorker().nextId("K", true);
        JSONObject json = passbackParam();
        UnifyOrderRequest build = UnifyOrderRequest.builder()
                .merchantOrderNo(merchantOrderNo)
                .totalAmount(new BigDecimal(1000).setScale(2))
                .originalPrice(new BigDecimal(1000).setScale(2))
                .productName("商品名称")
                .orderDesc("订单描述")
                .clientIp("117.136.38.111")
                .passbackParam(json.toJSONString())
                .build();
//        Factory.Payment.Common().endpoint(EndpointConstants.Fields.PC).platform(PayConstants.Platform.ALIPAY).connType(PayConstants.ConnType.INDIRECT).pay(build);
//        Factory.Payment.Alipay.Pc().connTypeIndirect().pay(build);
//        Factory.Payment.rong360.Pc().hbFqNum(PayConstants.HbFqNum.RONG360_6_12).pay(build);
    }

    /**
     * 额外业务自定义参数
     *
     * @return
     */
    private JSONObject passbackParam() {
        JSONObject json = new JSONObject();
        json.put("skms", "1");
        json.put("agent_id", "111");
        json.put("subbranch_id", "111");
        return json;
    }

    /**
     * 回调
     */
    @Test
    public void notifyTest() {
        String publicKey = "";
        String json = "{\"app_id\":\"pp20180127GSJPBq4z\",\"biz_params\":\"{\\\"conn_type\\\":\\\"INDIRECT\\\",\\\"original_price\\\":1000.00,\\\"total_amount\\\":1000.00,\\\"currency\\\":\\\"CNY\\\",\\\"client_ip\\\":\\\"117.136.38.165\\\",\\\"client_type\\\":\\\"PC\\\",\\\"order_desc\\\":\\\"订单描述\\\",\\\"product_name\\\":\\\"商品名称\\\",\\\"merchant_order_no\\\":\\\"K20200901460606710218752000\\\",\\\"payment_method\\\":\\\"ALIPAY\\\"}\",\"charset\":\"UTF-8\",\"format\":\"json\",\"notify_url\":\"https://pay.boxuegu.com/pay/notify_notify\",\"return_url\":\"https://pay.boxuegu.com/pay/return_notify\",\"sign\":\"WmAUxUurPRKnW73LDsJAZ8T4MKSWTz+DU/seGZPWanSNRKbLLZ/61gu0og77rYRO0OnaYI4jBeRTKIYL8r7mgq4FVxJHENJZsjaMwOVRJc7pCFuDQWU7qboGIu/58d1UkZPp7EamaAvSkgF3Cp8NzIHXaIEF54Vtg40pn7+xKg+mXoCr7EZ/B7xl5GDsTp+reQQQnoLhqPOa7anw6n/dUiWGauc33rgaHCM1gHFNR2vEGtb2otQgswcnxNXO7u8ICXvzZhDezXvmd8Bu2UqPYb7BWeEF+kHz621jvam8HNt0kRp9HjJVaOdpSaYxa9ATFvu3V5bSWN68teglRoNPQg==\",\"timestamp\":\"1598928807\",\"version\":\"1.0\"}\n";
        Map map = JSON.parseObject(json, Map.class);
        boolean b = RSA2ForMchtUtils.rsa2Check(map, publicKey);
        System.out.println(b);
    }
}
