/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.openbyod.entidade;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.json.JSONObject;

import br.com.openbyod.dao.ContatoDAO;
import javax.persistence.Column;

/**
 *
 * @author ARTLAN-00
 */
@Entity
@Table(name = "chamada")
public class Chamada implements Entidade, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataChamada;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "contato_id")
    private Contato contato;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    /**
     * @return the data
     */
    public Calendar getDataChamada() {
        return dataChamada;
    }

    private String getDataToString(Date data) {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return s.format(data.getTime());
    }

    /**
     * @param data the data to set
     */
    public void setDataChamada(Date data) {
        String dataString = getDataToString(data);
        Calendar dataCalendar = new GregorianCalendar();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        try {
            dataCalendar.setTime(sd.parse(dataString));
            this.dataChamada = dataCalendar;
        } catch (ParseException e) {
            e.getLocalizedMessage();
        }
    }
    
    public void setData(Date data) {
        String dataString = getDataToString(data);
        Calendar dataCalendar = new GregorianCalendar();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            dataCalendar.setTime(sd.parse(dataString));
            this.dataChamada = dataCalendar;
        } catch (ParseException e) {
            e.getLocalizedMessage();
        }
    }

    @Override
    public Chamada converteJSONEmEntidade(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        Contato contatoJSON = new ContatoDAO().getContato(jsonObject.getInt("id"));
        this.setContato(contatoJSON);
        this.setUsuario(contatoJSON.getUsuario());
        this.setData(new Date(System.currentTimeMillis()));
        return this;
    }

    @Override
    public String toString() {
        try {
            JsonObject jsonobject = Json.createObjectBuilder()
                    .add("data", getDataToString(this.getDataChamada().getTime()))
                    .add("usuario", this.getUsuario().toString())
                    .add("contato", this.getContato().toString())
                    .build();
            return jsonobject.toString();
        } catch (Exception e) {
           return "Exceção Chamada.toString(): " + e.getLocalizedMessage();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contato == null) ? 0 : contato.hashCode());
        result = prime * result + ((dataChamada == null) ? 0 : dataChamada.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
        return result;
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
        Chamada other = (Chamada) obj;
        if (contato == null) {
            if (other.contato != null) {
                return false;
            }
        } else if (!contato.equals(other.contato)) {
            return false;
        }
        if (dataChamada == null) {
            if (other.dataChamada != null) {
                return false;
            }
        } else if (!dataChamada.equals(other.dataChamada)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (usuario == null) {
            if (other.usuario != null) {
                return false;
            }
        } else if (!usuario.equals(other.usuario)) {
            return false;
        }
        return true;
    }
}
