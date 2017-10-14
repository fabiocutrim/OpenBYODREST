package br.com.openbyod.seguranca.criptografia;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String ALGORITHM = "RSA/None/NoPadding";
    private static final String PROVIDER = "BC";

    public RSA() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private static void geraParChaves(int keySize)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String algorithm = "RSA"; // or DSA, DH, etc.

        // Generate a 2048-bit Digital Signature Algorithm (RSA) key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keySize);
        KeyPair keypair = keyGen.genKeyPair();
        PrivateKey privateKey = keypair.getPrivate();
        PublicKey publicKey = keypair.getPublic();

        byte[] privateKeyBytes = privateKey.getEncoded();
        byte[] publicKeyBytes = publicKey.getEncoded();

        String chavePublica = Base64.fromHex(publicKeyBytes);
        String chavePrivada = Base64.fromHex(privateKeyBytes);

        System.out.println("Chave Pública: " + chavePublica);
        System.out.println("Chave Privada: " + chavePrivada);

        System.out.println("Tamanho Chave Pública: " + chavePublica.length());
        System.out.println("Tamanho Chave Privada: " + chavePrivada.length());
    }

    byte[] decriptografa(String chaveSimetrica) {
        byte[] chaveDecriptada = null;
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
            // Decriptografa o texto puro usando a chave Privada
            cipher.init(Cipher.DECRYPT_MODE, getChavePrivada());
            chaveDecriptada = cipher.doFinal(Base64.toHex(chaveSimetrica));
        } catch (Exception e) {
            LOG.info("Exceção Decriptografia Chave Simétrica: " + e.getLocalizedMessage());
        }
        return chaveDecriptada;
    }

    private PrivateKey getChavePrivada() {
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.toHex(CHAVE_PRIVADA));
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception ex) {
            LOG.info("Exceção Get Chave Privada: " + ex.getLocalizedMessage());
        }
        return privateKey;
    }

    private static final String CHAVE_PRIVADA = "308204be020100300d06092a864886"
            + "f70d0101010500048204a8308204a40201000282010100c2734c469104eed940"
            + "74ebe285f579e58fca877f088413ce4e2a6253ac79a601b1b286148f5e04bb28"
            + "22d688087cbebb7c5569372872bbab75eeefdd840b9e0968a2e20e340a0bc659"
            + "fe23b3f63e2be4505ca2994875f49398679e26efb42d528b26760ecf3cc930fb"
            + "41302417e9c204258ebd8977ecf80536910b670f6dfb4d2b814655f2096768b8"
            + "5966398b0e48906e629c7d4f9ef4c2f8e017e0bf197167bb87a32c5a47b94cd1"
            + "d1e737737e0d7947dd5eddda1548b8cbc4967254a750f86c39fedd126afe456b"
            + "0c1258548299281d632e6237a7502dd71980dc4d1b62bc8526792083e6934283"
            + "84c1408a5f581435d106cdde30d686ddb9ee3565362f7d020301000102820100"
            + "049ad663956b987c6c9a583bb5106b22949527404ae355d4bce5240ef736aa47"
            + "bffd8a4f5255d7a6d6fc7c381c7b57c8f8d8ccf51170262199595f34cfbbb6b2"
            + "6257f9227cf8ffee6293322540df980a801cb17d0fca3b50837b70b35b59d8f8"
            + "eb72adb90c0f5474647ed6b5041bf50c44176d45a421687e371c98388895022e"
            + "771801dddf7bc798abc66376ec32452c386964de440c623b247bfceee5c9c61f"
            + "23d39a968cf7e2dbb6668b5441f762e0b8c903bab01498ce0a73e306c0b85d09"
            + "6a1d30ec74b441f81cd2be7adcbe455e60acc63dd65e27f29a4d6fda8b6aff3b"
            + "a47c2a405c77873cf97830bf50134ae1152dd7901c26c5e2a5e03316f92d5401"
            + "02818100e03d88e40aba281b378c3a38acd3d50af1304c7aa0dce6034ea68a54"
            + "f4de0d475b29735c3d2e8a6d839d5531cb5f8ee75c211c2e4d297bd2833d0b59"
            + "a403f7b0277f4e7b846cb8c1eecf4a1c7d5622741b54c6f1ab34080e643d7f08"
            + "b1c62fae66ecb4fe7bdcbc71a1f0d164d95775ab8bc814f93e93d883ce09b320"
            + "a43922cd02818100ddfda4b8f046297488dfc584e1fd863ea1380a20e1c8f079"
            + "f04e6eb835020101b45e72c1abdf265c756bb5305462505dda4b59d3deda7fed"
            + "a9dc69abdda65ba0d227b6b514793646fbc427a38e68d0fce876d488dc495531"
            + "8d3e9edc4de1f54249cdc4b4b78fd36f1d6d4da17e74ec3e6d82e2555494bd1e"
            + "26f8a61aa5e31f7102818100bbec75eb7eddf1bc9579cc5530e9ba32185d3cca"
            + "cc4333f473967879a858eb51f0a1354f88ea97121e69dd3b04d04987b51afe6e"
            + "03368fd1a530a4717455eaded2cb0aefd2d3f6a678477a3994047a8d49566bff"
            + "9e55d5a691f5439ffab437c375f84bdab5dd10843e8dfe3e01331da72d56f9d4"
            + "c3cfcd74f0580aa981576cc50281804d22793e199f07808a9ed68f19c7720209"
            + "1ae3ca112a3504e9cf1701c2c973781828494faa5cdb837cf973f7db1a5e3a7e"
            + "43786efa1c8a1f16ca939d386339ab63da90cdd2d35b5cc6d69dbf631860a5c0"
            + "50aa00f42a370404c421870338e473a8196ba42fb4360f81f61ce7647d54294c"
            + "fc1190fcd6c1efdc32c1e4b88776c1028181009e0ba42c0fd426f034bbd3087b"
            + "865e80fcec531329ab0106ef43428293c152677ed066e9fe7c26e79b320d22de"
            + "9f94d94a2a6199b1661351ab90faec8dc65efd12a3fc4d7490352df4abf5d0a1"
            + "90ccb08540e0a1e0d42e897f106fb813f5cdf2bbf61871a3582892cfc3e77a87"
            + "445389d912de1b54083c07c517dcbceb94abf1";
}
