package br.com.openbyod.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.jsoup.Jsoup;

import br.com.openbyod.bean.Relatorio;
import br.com.openbyod.entidade.EmailUsuario;
import br.com.openbyod.entidade.Entidade;
import br.com.openbyod.entidade.Usuario;
import br.com.openbyod.seguranca.criptografia.Decriptacao;
import java.util.Date;
import java.sql.Timestamp;
import javax.persistence.Query;

public class EmailUsuarioDAO extends EntidadeDAO {
    
    public EmailUsuarioDAO() throws Exception {
        
    }
    
    private static final String EMAIL_LOCAL_SERVER = "localhost";
    private static final int EMAIL_LOCAL_SERVER_PORT_IMAP = 143;
    private static final int EMAIL_LOCAL_SERVER_PORT_SMTP = 25;

    /**
     * CRIA UM NOVO REGISTRO NO BANCO DE DADOS
     */
    private void salva(EmailUsuario emailEntity) {
        String idMensagem = emailEntity.getIdMensagem();
        String destinatario = emailEntity.getDestinatario();

        if (!getMensagemSalva(idMensagem, destinatario)) {
            entityManager.getTransaction().begin();
            entityManager.persist(emailEntity);
            entityManager.getTransaction().commit();
        }
    }

    private boolean getMensagemSalva(String idMensagem, String destinatario) {
        String jpql = "SELECT e FROM EmailUsuario e WHERE e.idMensagem = :idMensagem "
                + "AND e.destinatario = :destinatario";
        try {
            entityManager.createQuery(jpql, EmailUsuario.class).setParameter("idMensagem", idMensagem)
                    .setParameter("destinatario", destinatario).getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    // @SuppressWarnings("unchecked")
    public List<Relatorio> getListaDeEmailsEnviados(int mes) {
        String jpql = "SELECT e.remetente, COUNT(*) emailsEnviados"
                + " FROM Email e, Usuario u"
                + " WHERE e.remetente = u.email AND MONTH(e.dataEnvio) = :mes"
                + " GROUP BY e.remetente"
                + " ORDER BY 2 DESC";
        try {
            Query query = entityManager.createNativeQuery(jpql);
            query.setParameter("mes", mes);

            List<Object[]> results = query.getResultList();
            List<Relatorio> listaRelatorio = new ArrayList<Relatorio>();

            for (Object[] result : results) {
                int quantidade = ((Number) result[1]).intValue();
                String nome = (String) result[0];
                Relatorio relatorio = new Relatorio(nome, quantidade);
                listaRelatorio.add(relatorio);
            }
            return listaRelatorio;

        } catch (NoResultException e) {
            return null;
        }
    }

    // @SuppressWarnings("unchecked")
    public List<Relatorio> getListaDeEmailsRecebidos(int mes) {
        String jpql = "SELECT e.destinatario, COUNT(*) emailsRecebidos"
                + " FROM Email e, Usuario u"
                + " WHERE e.destinatario = u.email and MONTH(e.dataEnvio) = :mes"
                + " GROUP BY e.destinatario"
                + " ORDER BY 2 DESC";
        try {
            Query query = entityManager.createNativeQuery(jpql);
            query.setParameter("mes", mes);

            List<Object[]> results = query.getResultList();
            List<Relatorio> listaRelatorio = new ArrayList<Relatorio>();

            for (Object[] result : results) {
                int quantidade = ((Number) result[1]).intValue();
                String nome = (String) result[0];
                Relatorio relatorio = new Relatorio(nome, quantidade);
                listaRelatorio.add(relatorio);
            }
            return listaRelatorio;

        } catch (NoResultException e) {
            return null;
        }
    }

    // @SuppressWarnings("unchecked")
    public List<EmailUsuario> getListaDePalavrasChaveEmails(int mes, String palavrasChave) {
        String jpql = "SELECT e.conteudo, e.destinatario, e.assunto, e.remetente, "
                + " e.dataEnvio FROM Email e WHERE e.idMensagem IN"
                + " (SELECT e.idMensagem FROM Email e"
                + " WHERE MONTH(e.dataEnvio) = :mes AND MATCH(e.conteudo)"
                + " AGAINST (:palavrasChave))";
        try {
            Query query = entityManager.createNativeQuery(jpql);
            query.setParameter("mes", mes);
            query.setParameter("palavrasChave", palavrasChave);

            List<Object[]> results = query.getResultList();
            List<EmailUsuario> listaRelatorio = new ArrayList<EmailUsuario>();

            for (Object[] result : results) {
                String conteudo = (String) result[0];
                String destinatario = (String) result[1];
                String assunto = (String) result[2];
                String remetente = (String) result[3];
                Date dataEnvio = new Date(((Timestamp) result[4]).getTime());

                EmailUsuario email = new EmailUsuario();
                email.setDataString(dataEnvio);
                email.setAssunto(assunto);
                email.setConteudo(conteudo);
                email.setDestinatario(destinatario);
                email.setRemetente(remetente);

                listaRelatorio.add(email);
            }
            return listaRelatorio;

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public String get(Usuario usuarioEntity) throws Exception {
        // Recupera o usuário para autenticação junto ao servidor de E-mails
        Usuario usuario = new UsuarioDAO().getUsuario(usuarioEntity.getId());
        String senha = Decriptacao.decriptografaSenha(usuario.getEmail(), usuario.getSenha());
        List<EmailUsuario> caixaDeEntrada = new ArrayList<EmailUsuario>();

        // Seta as propriedades do servidor de E-mails
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        Session session = Session.getInstance(props, null);
        Store store = session.getStore();
        store.connect(EMAIL_LOCAL_SERVER, EMAIL_LOCAL_SERVER_PORT_IMAP, usuario.getEmail(), senha);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message[] msgInbox = inbox.getMessages();

        // Recupera as mensgens da caixa de entrada do usuário
        for (Message msg : msgInbox) {
            EmailUsuario jsonEmail = new EmailUsuario();
            String idMensagem = StringUtils.join(msg.getHeader("Message-ID"));
            jsonEmail.setRemetente(StringUtils.substringBetween(msg.getFrom()[0].toString(), "<", ">"));
            jsonEmail.setDataEnvio(msg.getSentDate());
            jsonEmail.setAssunto(msg.getSubject());
            jsonEmail.setDestinatario(usuario.getEmail());
            jsonEmail.setConteudo(StringUtils.removeEnd(getTextFromMessage(msg), "\r\n"));
            // Setting message ID, which will NOT be sent to the Android
            // application,
            // as the toString mthod doesnt reference this property
            jsonEmail.setIdMensagem(idMensagem);
            // Adds always on the top, so the newer emails are on top
            caixaDeEntrada.add(0, jsonEmail);
        }
        store.close();

        if (!caixaDeEntrada.isEmpty()) {
            for (EmailUsuario email : caixaDeEntrada) {
                salva(email);
            }
        }
        return caixaDeEntrada.toString();
    }

    /**
     * Saves a new contact on the database
     *
     * @return if the contact was saved successfully the method return TRUE,
     * else FALSE
     */
    @Override
    public boolean persiste(Entidade entidade, Usuario usuarioEntity) {
        EmailUsuario emailAEnviar = (EmailUsuario) entidade;
        boolean enviado = false;

        try {
            // Recupera o usuário para autenticação junto ao servidor de E-mails
            Usuario usuario = new UsuarioDAO().getUsuario(usuarioEntity.getId());
            String senha = Decriptacao.decriptografaSenha(usuario.getEmail(), usuario.getSenha());
            // TRY TO SEND THE EMAIL
            Email email = new SimpleEmail();
            email.setHostName(EMAIL_LOCAL_SERVER);
            email.setSmtpPort(EMAIL_LOCAL_SERVER_PORT_SMTP);
            email.setAuthenticator(new DefaultAuthenticator(usuario.getEmail(), senha));
            email.setSSLOnConnect(false);
            email.setFrom(emailAEnviar.getRemetente());
            email.setSubject(emailAEnviar.getAssunto());
            email.setMsg(emailAEnviar.getConteudo());
            email.addTo(emailAEnviar.getDestinatario());
            email.send();
            emailAEnviar.setDataEnvio(email.getSentDate());
            emailAEnviar.setIdMensagem(email.getMimeMessage().getMessageID());
            // turns 'sent' to TRUE as it worked
            enviado = true;
        } catch (Exception e) {
            LOG.severe("Exceção persisteEmail(): " + e.getLocalizedMessage());
            // Salva o arquivo de LOG
            LOG.finest("Arquivo de Log salvo!");
            return enviado;
        }
        // if it sent the email, save in the database for the email report
        if (enviado) {
            salva(emailAEnviar);
        }
        return enviado;
    }

    private String getTextFromMessage(Message message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {
        int count = mimeMultipart.getCount();
        if (count == 0) {
            throw new MessagingException("Multipart with no body parts not supported");
        }
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt) // alternatives appear in an order of increasing
        // faithfulness to the original content. Customize as req'd.
        {
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        }
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    private String getTextFromBodyPart(BodyPart bodyPart) throws IOException, MessagingException {
        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return result;
    }
}
