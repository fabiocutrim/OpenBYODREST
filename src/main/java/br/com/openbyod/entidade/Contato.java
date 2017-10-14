package br.com.openbyod.entidade;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Column;
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

@Entity
@Table(name = "contato")
public class Contato implements Entidade, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "nome")
    private String nome;

    @Column(name = "empresa")
    private String empresa;

    @Column(name = "email")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataCriacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Override
    public Contato converteJSONEmEntidade(String json) {
        JSONObject object = new JSONObject(json);
        if (object.has("id")) {
            this.setId(object.getInt("id"));
        }
        this.setNome(object.getString("nome"));
        this.setTelefone(object.getString("telefone"));
        this.setEmail(object.getString("email"));
        this.setEmpresa(object.getString("empresa"));
        this.setDataCriacao(new Date(System.currentTimeMillis()));
        return this;
    }

    @Override
    public String toString() {
        String idContato = String.valueOf(this.getId());

        if (idContato.equals(0)) {
            idContato = "";
        }
        
        try {
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("id", idContato)
                    .add("nome", this.getNome())
                    .add("telefone", this.getTelefone())
                    .add("email", this.getEmail())
                    .add("empresa", this.getEmpresa())
                    .build();
            return jsonObject.toString();
        } catch (Exception e) {
            return "Exceção Contato.toString(): " + e.getLocalizedMessage();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private String getDataToString(Date data) {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return s.format(data.getTime());
    }

    public Calendar getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date data) {
        String dataString = getDataToString(data);
        Calendar dataCalendar = new GregorianCalendar();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            dataCalendar.setTime(sd.parse(dataString));
            this.dataCriacao = dataCalendar;
        } catch (ParseException e) {
            e.getLocalizedMessage();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
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
        Contato other = (Contato) obj;
        if (dataCriacao == null) {
            if (other.dataCriacao != null) {
                return false;
            }
        } else if (!dataCriacao.equals(other.dataCriacao)) {
            return false;
        }
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (empresa == null) {
            if (other.empresa != null) {
                return false;
            }
        } else if (!empresa.equals(other.empresa)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (nome == null) {
            if (other.nome != null) {
                return false;
            }
        } else if (!nome.equals(other.nome)) {
            return false;
        }
        if (telefone == null) {
            if (other.telefone != null) {
                return false;
            }
        } else if (!telefone.equals(other.telefone)) {
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
