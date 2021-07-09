package com.EncryDecryUtil;

import com.springCloud.customerFestival.util.JAVARSA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Request-Response加解密体系的加解密方式
 */
@Configuration
public class RRCryptoConfig {
    /**
     * 加密解密方式使用一样的
     */
    @Bean("rrJAVARSA")
    public JAVARSA rrJAVARSA(){
        return new JAVARSA();
    }

}