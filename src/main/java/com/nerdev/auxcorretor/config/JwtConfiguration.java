package com.nerdev.auxcorretor.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;


@Configuration
public class JwtConfiguration {

    @Value("${app.security.rsa-keys-private}")
    private String pathPrivateKeyRSA;
    @Value("${app.security.rsa-keys-public}")
    private String pathPublicKeyRSA;
    @Value("${app.security.rsa-keys-id}")
    private String pathIdKeyRSA;

    @Bean
    public RSAKey rsaKey() throws Exception {
        if (!verificarChaves()){
            RSAKey rsaKey = gerarRSAKey();
            salvarRSAKey(rsaKey);
            return rsaKey;
        }
        return carregarRSAKey();
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws Exception {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource (RSAKey rsaKey){
        JWKSet  jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .requireProofKey(true)
                .build();
    }

    @Bean
    public TokenSettings  tokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofHours(4))
                .refreshTokenTimeToLive(Duration.ofHours(12))
                .build();
    }

    private RSAKey gerarRSAKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey
                .Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private RSAKey carregarRSAKey() throws Exception {

        RSAPrivateKey privateKey = carregaChavePrivada();
        RSAPublicKey publicKey = carregaChavePublica();
        String id = buscaUUID();

        return new RSAKey
                .Builder(publicKey)
                .privateKey(privateKey)
                .keyID(id)
                .build();
    }

    private RSAPublicKey carregaChavePublica() throws Exception {
        Path path = Paths.get(pathPublicKeyRSA);
        String conteudo = Files.readString(path);
        String chaveLimpa = conteudo
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] chaveBase64 = Base64.getDecoder().decode(chaveLimpa);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(chaveBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    private RSAPrivateKey carregaChavePrivada() throws Exception {
        Path path = Paths.get(pathPrivateKeyRSA);
        String conteudo = Files.readString(path);
        String chaveLimpa = conteudo
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] chaveBase64 = Base64.getDecoder().decode(chaveLimpa);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(chaveBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    private void salvarRSAKey(RSAKey rsaKey) throws Exception {
        PrivateKey privateKey = rsaKey.toPrivateKey();
        PublicKey publicKey = rsaKey.toPublicKey();

        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        Path pathPrivatePem = Path.of(pathPrivateKeyRSA);
        Path pathPublicPem = Path.of(pathPublicKeyRSA);

        String arquivoPrvK = formataPemRSA("PRIVATE KEY", privateKeyBase64);
        String arquivoPubK = formataPemRSA("PUBLIC KEY", publicKeyBase64);

        Files.writeString(pathPrivatePem, arquivoPrvK);
        Files.writeString(pathPublicPem, arquivoPubK);

        salvarRsaId(rsaKey);
    }

    private String formataPemRSA(String tipoChave, String base64){
        StringBuilder pem = new StringBuilder();

        pem.append("-----BEGIN ").append(tipoChave).append("-----\n");

        int indice = 0;
        while (indice < base64.length()) {
            int limit = Math.min(indice + 64, base64.length());
            pem.append(base64, indice, limit).append("\n");
            indice += 64;
        }

        pem.append("-----END ").append(tipoChave).append("-----\n");

        return pem.toString();
    }

    private void salvarRsaId(RSAKey rsaKey) throws Exception {
        String id = rsaKey.getKeyID();
        Path idPath = Path.of(pathIdKeyRSA);

        Files.writeString(idPath, id);
    }

    private String buscaUUID() throws Exception {
        Path path = Paths.get(pathIdKeyRSA);
        return Files.readString(path).trim();
    }

    private boolean verificarChaves() {
        Path privatePath = Paths.get(pathPrivateKeyRSA);
        Path publicPath = Paths.get(pathPublicKeyRSA);
        Path idPath = Paths.get(pathIdKeyRSA);

        if (Files.notExists(idPath) || Files.notExists(privatePath) || Files.notExists(publicPath)) {
            return false;
        }
        return true;
    }

}
