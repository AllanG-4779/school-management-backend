package com.stmics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class KeyStoreConfig {
    @Value("${keys.name}")
    private String keyStoreName;
    @Value("${keys.password}")
    private String keyStorePassword;
    @Value("${keys.alias}")
    private String keyStoreAlias;

    @Bean
    RSAPublicKey publicKey() {
        try {
            Certificate certificate = keyStore().getCertificate(keyStoreAlias);
            PublicKey publicKey = certificate.getPublicKey();
            if (publicKey instanceof RSAPublicKey) {
                return (RSAPublicKey) publicKey;
            }
        } catch (KeyStoreException ex) {
            throw new IllegalStateException("Unable to load RSA public key", ex);
        }
        throw new IllegalStateException("Unable to load RSA public key");
    }
    @Bean
    RSAPrivateKey rsaPrivateKey() {
        try {
            return (RSAPrivateKey) keyStore().getKey(keyStoreAlias, keyStorePassword.toCharArray());
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load RSA private key", ex);
        }
    }


    private KeyStore keyStore() throws KeyStoreException {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            ClassPathResource resource = new ClassPathResource(keyStoreName);
            keyStore.load(resource.getInputStream(), keyStorePassword.toCharArray());
            return keyStore;
        } catch (Exception e) {
            throw new KeyStoreException("Unable to load keystore");
        }
    }
}
