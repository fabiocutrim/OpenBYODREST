package br.com.openbyod.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity
@Table(name = "usuario")
public class Usuario implements Entidade, Serializable {

    public Usuario() {

    }

    @Override
    public Usuario converteJSONEmEntidade(String json) {
        JSONObject jsonObject = new JSONObject(json);
        this.setEmail(jsonObject.getString("email"));
        this.setSenha(jsonObject.getString("senha"));
        this.setEnderecoMAC(jsonObject.getString("enderecoMAC"));
        return this;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        try {
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("nome", this.getNome())
                    .add("fotoPerfil", this.getFoto())
                    .build();
            return jsonObject.toString();
        } catch (Exception e) {
            return "Exceção Usuario.toString(): " + e.getLocalizedMessage();
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "enderecoMAC")
    private String enderecoMAC;

    @Column(name = "foto")
    private String foto;

    @Column(name = "token")
    private String token;

    @Column(name = "chaveToken")
    private String chaveToken;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEnderecoMAC() {
        return enderecoMAC;
    }

    public void setEnderecoMAC(String enderecoMAC) {
        this.enderecoMAC = enderecoMAC;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getChaveToken() {
        return chaveToken;
    }

    public void setChaveToken(String chaveToken) {
        this.chaveToken = chaveToken;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.id;
        hash = 89 * hash + Objects.hashCode(this.nome);
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hashCode(this.senha);
        hash = 89 * hash + Objects.hashCode(this.enderecoMAC);
        hash = 89 * hash + Objects.hashCode(this.foto);
        hash = 89 * hash + Objects.hashCode(this.token);
        hash = 89 * hash + Objects.hashCode(this.chaveToken);
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
        final Usuario other = (Usuario) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.senha, other.senha)) {
            return false;
        }
        if (!Objects.equals(this.enderecoMAC, other.enderecoMAC)) {
            return false;
        }
        if (!Objects.equals(this.foto, other.foto)) {
            return false;
        }
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        if (!Objects.equals(this.chaveToken, other.chaveToken)) {
            return false;
        }
        return true;
    }
}
