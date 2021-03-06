package br.com.openbyod.seguranca.criptografia;

public class Base64 {

    // Converte uma mensagem criptografada para uma string de sua representação hexadecimal.
    public static String fromHex(byte[] hex) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < hex.length; i++) {
            sb.append( Integer.toString( ( hex[i] & 0xff ) + 0x100, 16).substring( 1 ) );
        }
        return sb.toString();
    }

    // Corrige o tamanho de uma String para multiplo de 16
    public static String nullPadString(String original) {
        StringBuffer output = new StringBuffer(original);
        int remain = output.length() % 16;
        if (remain != 0) {
            remain = 16 - remain;
            for (int i = 0; i < remain; i++)
                output.append((char) 0);
        }
        return output.toString();
    }

    // Converte uma representação hexadecimal para seus bytes hexadecimal (valor encriptado)
    public static byte[] toHex(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
