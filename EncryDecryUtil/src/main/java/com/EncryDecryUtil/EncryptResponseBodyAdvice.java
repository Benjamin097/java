package com.EncryDecryUtil;

import com.springCloud.customerFestival.util.JAVARSA;
import com.springCloud.customerFestival.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求响应处理类<br>
 * 
 * 对加了@Encrypt的方法的数据进行加密操作
 *
 *
 */
@ControllerAdvice
@Slf4j
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Value("${spring.crypto.request.decrypt.charset:UTF-8}")
    private String charset = "UTF-8";

	@Value("${responseRsa.publicKey}")
	private String publicKey;

    @Autowired
    @Qualifier("rrJAVARSA")
    private JAVARSA javaRsa;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return NeedCrypto.needEncrypt(returnType);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //TODO 实现具体的加密方法
		log.info("加密前返回参数：{}",body);
		String respParam = JsonUtils.toJson(body);
		String respEncrypt = null;
		Map resp = new HashMap();
		try {
			respEncrypt = javaRsa.encrypt(respParam,publicKey);
			log.info("加密后返回参数：{}",respEncrypt);
			resp.put("state","success");
			resp.put("encryptData",respEncrypt);
		} catch (Exception e) {
			log.error("加密返回参数异常：{}",e.getMessage());
			resp.put("state","error");
			resp.put("msg","返回信息异常");
			resp.put("encryptData","");
			e.printStackTrace();
		}
		return resp;
	}

}