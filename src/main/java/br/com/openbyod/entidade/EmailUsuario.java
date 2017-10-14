package br.com.openbyod.entidade;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.json.JSONObject;

@Entity
@Table(name = "email")
public class EmailUsuario implements Entidade, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
       Calendar data = this.getDataEnvio();
       String dataJson;

        if (data == null) {
            dataJson = "";
        } else {
            dataJson = getDataToString(this.getDataEnvio().getTime());
        }
        
        try {
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("data", dataJson)
                    .add("remetente", this.getRemetente())
                    .add("destinatario", this.getDestinatario())
                    .add("conteudo", this.getConteudo())
                    .add("assunto", this.getAssunto()).build();
            return jsonObject.toString();
        } catch (Exception e) {
            return "Exceção Email.toString(): " + e.getLocalizedMessage();
        }
    }

    private String getDataToString(Date data) {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return s.format(data.getTime());
    }

    @Override
    public EmailUsuario converteJSONEmEntidade(String json) {
        JSONObject object = new JSONObject(json);
        this.setRemetente(object.getString("remetente"));
        this.setDestinatario(object.getString("destinatario"));
        this.setAssunto(object.getString("assunto"));
        this.setConteudo(object.getString("conteudo"));
        return this;
    }

    public EmailUsuario() {

    }

    public EmailUsuario(Calendar data, String remetente,
            String destinatario, String assunto, String conteudo) {
        this.conteudo = conteudo;
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.remetente = remetente;
        this.dataEnvio = data;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "idMensagem")
    private String idMensagem;

    @Column(name = "remetente")
    private String remetente;

    @Column(name = "destinatario")
    private String destinatario;

    @Column(name = "assunto")
    private String assunto;

    @Column(name = "conteudo")
    private String conteudo;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataEnvio;
    
    private String dataString;

    public String getDataString() {
        return dataString;
    }

    public void setDataString(Date data) {
        this.dataString = getDataToString(data);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(String idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Calendar getDataEnvio() {
        return this.dataEnvio;
    }

    public void setDataEnvio(Date data) {
        String dataStr = getDataToString(data);
        Calendar dataCalendar = new GregorianCalendar();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            dataCalendar.setTime(sd.parse(dataStr));
            this.dataEnvio = dataCalendar;
        } catch (ParseException e) {
            e.getLocalizedMessage();
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.idMensagem);
        hash = 97 * hash + Objects.hashCode(this.remetente);
        hash = 97 * hash + Objects.hashCode(this.destinatario);
        hash = 97 * hash + Objects.hashCode(this.assunto);
        hash = 97 * hash + Objects.hashCode(this.conteudo);
        hash = 97 * hash + Objects.hashCode(this.dataEnvio);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmailUsuario other = (EmailUsuario) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.idMensagem, other.idMensagem)) {
            return false;
        }
        if (!Objects.equals(this.remetente, other.remetente)) {
            return false;
        }
        if (!Objects.equals(this.destinatario, other.destinatario)) {
            return false;
        }
        if (!Objects.equals(this.assunto, other.assunto)) {
            return false;
        }
        if (!Objects.equals(this.conteudo, other.conteudo)) {
            return false;
        }
        if (!Objects.equals(this.dataEnvio, other.dataEnvio)) {
            return false;
        }
        return true;
    }
}
