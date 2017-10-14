package br.com.openbyod.seguranca.criptografia;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.logging.Logger;

import org.bouncycastle.jcajce.provider.digest.SHA3;

public class Encriptacao {

    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // Criptografa uma mensagem através do algoritmo AES
    public static String criptografa(String mensagem, byte[] chave) {
        return AES.criptografa(mensagem, chave);
    }

    public static byte[] MD5(String string) {
        byte[] hashMd5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes("UTF-8"));
            hashMd5 = md.digest();
        } catch (Exception ex) {
            LOG.info("Exceção MD5: " + ex.getLocalizedMessage());
        }
        return hashMd5;
    }

    public static String SHA3(String string) {
        byte[] digest = null;
        try {
            SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
            digest = digestSHA3.digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            LOG.info("Exceção SHA-3: " + ex.getLocalizedMessage());
        }
        return Base64.fromHex(digest);
    }

    static byte[] getChaveCriptografiaSenha(String email) {

        return MD5(email);
    }

    private static String criptografaSenha(String email, String senha) {
        byte[] chave = getChaveCriptografiaSenha(email);
        return AES.criptografa(senha, chave);
    }
}
