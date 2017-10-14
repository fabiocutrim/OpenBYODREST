package br.com.openbyod.seguranca.criptografia;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.logging.Logger;

public class AES {

    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    static String criptografa(String mensagem, byte[] chaveSimetrica) {
        String mensagemEncriptada = "";
        try {
            // Criptografa a mensagem
            Cipher encripta = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(chaveSimetrica, "AES");
            encripta.init(Cipher.ENCRYPT_MODE, key);
            byte[] textoBytes = Base64.nullPadString(mensagem).getBytes("UTF-8");
            byte[] textoEncriptado = encripta.doFinal(textoBytes);
            mensagemEncriptada = Base64.fromHex(textoEncriptado);
        } catch (Exception ex) {
            LOG.info("Exceção Criptografia: " +  ex.getLocalizedMessage());
        }
        return mensagemEncriptada;
    }

    static String decriptografa(String mensagemCriptografada, byte[] chaveSimetrica) {
        String mensagemOriginal = "";
        try {
            // Decriptografa a mensagem
            Cipher decripta = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(chaveSimetrica, "AES");
            decripta.init(Cipher.DECRYPT_MODE, key);
            byte[] textoBytes = Base64.toHex(mensagemCriptografada);
            byte[] textoDecriptado = decripta.doFinal(textoBytes);
            mensagemOriginal = new String(textoDecriptado, "UTF-8").trim();
        } catch (Exception e) {
            LOG.info("Exceção Decriptografia: " +  e.getLocalizedMessage());
        }
        return mensagemOriginal;
    }
}
