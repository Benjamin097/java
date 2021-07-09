package com.EncryDecryUtil;

import com.springCloud.customerFestival.util.JAVARSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 请求数据接收处理类<br>
 * 
 * 对加了@Decrypt的方法的数据进行解密操作<br>
 * 
 * 只对 @RequestBody 参数有效
 */
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Value("${spring.crypto.request.decrypt.charset:UTF-8}")
    private String charset = "UTF-8";

    @Value("${requestRsa.privateKey}")
	private String rsaKay;

    @Autowired
	@Qualifier("rrJAVARSA")
	private JAVARSA javaRsa;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return NeedCrypto.needDecrypt(methodParameter);
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
    	return new DecryptHttpInputMessage(inputMessage , charset , javaRsa,rsaKay);
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}
}