package br.com.openbyod.seguranca.sessao;

import br.com.openbyod.seguranca.criptografia.Base64;
import java.util.Date;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import br.com.openbyod.seguranca.criptografia.Encriptacao;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Token {

    // Tempo de expiração do token em milissegundos
    private static final long TEMPO_EXPIRACAO = 600000; // 10 minutos
    private Date tempoInicio;
    private Date tempoFim;
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // Define as datas de criação e expiração do token
    private void getTempo() {
        long nowMillis = System.currentTimeMillis();
        tempoInicio = new Date(nowMillis);
        long ttlMillis = TEMPO_EXPIRACAO;
        tempoFim = null;
        // if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            tempoFim = new Date(expMillis);
        }
    }

    public String geraToken(String chaveAssinatura) {
        SignedJWT signedJWT = null;
        try {
            // Create HMAC signer
            JWSSigner signer = new MACSigner(chaveAssinatura);

            // Define as datas de criação e expiração do token
            getTempo();

            // Generate the jti
            String jti = Encriptacao.SHA3(chaveAssinatura);

            // Prepare JWT with claims set
            JWTClaimsSet claimsSet = new JWTClaimsSet.
                    Builder().
                    jwtID(jti).
                    issueTime(tempoInicio).
                    expirationTime(tempoFim).
                    build();

            // Assina o token
            signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            // Apply the HMAC protection
            signedJWT.sign(signer);
        } catch (Exception ex) {
            LOG.info("Exceção Geração Token: " + ex.getLocalizedMessage());
        }
        // Serialize to compact form, produces something like
        // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
        return signedJWT.serialize();
    }

    public boolean checaDataExpiracaoValida(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
        } catch (Exception ex) {
            LOG.info("Exceção Verificação Data Expiração: " + ex.getLocalizedMessage());
            return false;
        }
    }

    public boolean validaToken(String token, String chaveAssinatura) {
        try {
            return checaAssinatura(token, chaveAssinatura)
                    && validaNonce(token, chaveAssinatura)
                    && checaDiferencaCriacaoExpiracao(token);
        } catch (Exception ex) {
            LOG.info("Exceção Validação Token: " + ex.getLocalizedMessage());
            return false;
        }
    }

    private boolean checaAssinatura(String token, String chaveUsuario) throws Exception {
        // On the consumer side, parse the JWS and verify its HMAC
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(chaveUsuario);
        return signedJWT.verify(verifier);
    }

    private boolean checaDiferencaCriacaoExpiracao(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        long issueDate = signedJWT.getJWTClaimsSet().getIssueTime().getTime();
        long expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime().getTime();
        return expirationDate - issueDate == TEMPO_EXPIRACAO;
    }

    private boolean validaNonce(String token, String chaveAssinatura)
            throws Exception {

        SignedJWT signedJWT = SignedJWT.parse(token);
        String jti = Encriptacao.SHA3(chaveAssinatura);
        return signedJWT.getJWTClaimsSet().getJWTID().equals(jti);
    }

    public String atualizaDataExpiracao(String token, String chaveAssinatura) {
        SignedJWT newSignedJWT = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Define as datas de criação e expiração do token
            getTempo();

            // Get the jti
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            // Define os "claims"
            JWTClaimsSet claimsSet = new JWTClaimsSet.
                    Builder().
                    jwtID(jti).
                    issueTime(tempoInicio).
                    expirationTime(tempoFim).
                    build();

            // Create HMAC signer
            JWSSigner signer = new MACSigner(chaveAssinatura);

            // Assina o token
            newSignedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            // Apply the HMAC protection
            newSignedJWT.sign(signer);
        } catch (Exception ex) {
            LOG.info("Exceção Atualização Data Expiração Token: " + ex.getLocalizedMessage());
        }
        return newSignedJWT.serialize();
    }

    public String getChaveAssinatura() {
        String chave = "";
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            SecretKey sk = kg.generateKey();
            chave = Base64.fromHex(sk.getEncoded());
        }
        catch (NoSuchAlgorithmException e) {
            LOG.info("Exceção Geração Chave Assinatura: " + e.getLocalizedMessage());
        }
        return chave;
    }
}
