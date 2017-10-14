package br.com.openbyod.seguranca.criptografia;

public class Decriptacao {
    
    // Decriptografa uma mensagem criptografada com o algoritmo AES
    public static String decriptografa(String mensagem, byte[] chave) {
        return AES.decriptografa(mensagem, chave);
    }

    /* Decriptografa a senha do Usuário para autenticação junto ao servidor de
       E-mails
     */
    public static String decriptografaSenha(String email, String senha) {
        byte[] stringEncriptacaoSenha = Encriptacao.getChaveCriptografiaSenha(email);
        return AES.decriptografa(senha, stringEncriptacaoSenha);
    }

    // Decriptografa a chave simétrica
    public static byte[] decriptografaChave(String chaveEncriptada) {
        // Decriptografa a chave simétrica com o algoritmo assimétrico
        byte[] chaveDecriptada = new RSA().decriptografa(chaveEncriptada);
        return chaveDecriptada;
    }
}
