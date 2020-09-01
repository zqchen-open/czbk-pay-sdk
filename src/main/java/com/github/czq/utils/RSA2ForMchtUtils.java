package com.github.czq.utils;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * RSA2加密验签工具类
 */
public class RSA2ForMchtUtils {

    public static final String SIGN_TYPE_RSA = "RSA";

    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * RSA2 加密生成签名
     * @param map 请求参数列表
     * @param privateKey 私钥
     * @return 签名
     */
    public static String RSA2Sign(Map<String, ?> map, String privateKey) {
        if (null == privateKey || "".equals(privateKey)) {
            throw new RuntimeException("私钥获取为空");
        }
        Object charset = map.get("charset");//编码
        String content = getSignContent(map);
        System.out.println("签名：" + content);
        //检验签名使用类型，并进行签名
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);

            signature.initSign(priKey);
            if (null == charset || "".equals(charset.toString().trim())) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset.toString()));
            }
            byte[] signed = signature.sign();
            return new String(com.github.czq.utils.Base64.encodeBase64(signed));
        } catch (Exception ex) {
            throw new RuntimeException("签名加密错误：content = " + content + "; charset = " + charset, ex);
        }
    }


    /**
     * 验签
     *
     * @param params    content   参数的合成字符串格式
     *                  sign      签名
     * @param publicKey 公钥
     * @return 签名
     */
    public static boolean rsa2Check(Map<String, ? extends Object> params, String publicKey) {
        String sign = params.get("sign").toString();//签名
        Object charset = params.get("charset");
        String content = getSignCheckContent(params);//获取需要进行签名验证的内容

        try {
            PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
            signature.initVerify(pubKey);
            if (null == charset || "".equals(charset.toString().trim())) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset.toString()));
            }
            return signature.verify(com.github.czq.utils.Base64.decodeBase64(sign.getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException("签名验证错误：content = " + content + "; charset = " + charset, ex);
        }
    }

    private static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || null == algorithm || "".equals(algorithm)) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = readText(ins).getBytes();

        encodedKey = com.github.czq.utils.Base64.decodeBase64(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer, -1);

        byte[] encodedKey = writer.toString().getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    private static String getSignCheckContent(Map<String, ? extends Object> params) {
        if (params == null) {
            return null;
        }
        params.remove("sign");//移除sign签名参数

        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = (null == params.get(key)) ? "" : params.get(key).toString();
            if (null != key && !"".equals(key) && null != value && !"".equals(value)) {
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            }
        }
        return content.toString();
    }

    public static String getSignContent(Map<String, ? extends Object> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = (null == sortedParams.get(key)) ? "" : sortedParams.get(key).toString();
            if (null != key && !"".equals(key) && null != value && !"".equals(value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    private static String readText(InputStream ins) throws IOException {
        Reader reader = new InputStreamReader(ins);
        StringWriter writer = new StringWriter();
        io(reader, writer, -1);
        return writer.toString();
    }

    private static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = DEFAULT_BUFFER_SIZE >> 1;
        }

        char[] buffer = new char[bufferSize];
        int amount;

        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }
}
