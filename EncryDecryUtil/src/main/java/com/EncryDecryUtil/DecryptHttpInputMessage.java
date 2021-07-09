package com.EncryDecryUtil;

import com.springCloud.customerFestival.util.JAVARSA;
import com.springCloud.customerFestival.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Slf4j
public class DecryptHttpInputMessage implements HttpInputMessage {
    private HttpInputMessage inputMessage;
    private String charset;
    private JAVARSA javaRsa;
    private String rsaKay;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, String charset , JAVARSA javaRsa,String rsaKay) {
        this.inputMessage = inputMessage;
        this.charset = charset;
        this.javaRsa = javaRsa;
        this.rsaKay = rsaKay;
    }

    @Override
    public InputStream getBody() throws IOException {
        String content = IOUtils.toString(inputMessage.getBody() , charset);
        log.info("收到请求：{}",content);
        Map req = JsonUtils.fromJson(content, HashMap.class);
        String param = req.get("param").toString();
        log.info("解密前请求参数：{}",param);
        String decryptBody = null;
        try {
            decryptBody = javaRsa.decrypt(param, rsaKay);
            log.info("解密后请求参数：{}",decryptBody);
        } catch (Exception e) {
            log.error("请求参数解密异常：{}",e.getMessage());
            e.printStackTrace();
        }
        return new ByteArrayInputStream(decryptBody.getBytes(charset));
    }

    @Override
    public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
    }
}