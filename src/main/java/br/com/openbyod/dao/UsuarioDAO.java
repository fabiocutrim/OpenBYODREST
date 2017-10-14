package br.com.openbyod.dao;

import br.com.openbyod.entidade.Entidade;
import java.util.List;

import javax.persistence.NoResultException;

import br.com.openbyod.entidade.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO extends EntidadeDAO {

    public UsuarioDAO() throws Exception {

    }

    /**
     * CRIA UM NOVO REGISTRO NO BANCO DE DADOS
     */
    public void salva(Usuario usuarioEntity) {

        entityManager.getTransaction().begin();
        entityManager.persist(usuarioEntity);
        entityManager.getTransaction().commit();
    }

    /**
     * ALTERA UM REGISTRO CADASTRADO
     */
    public void altera(Usuario usuarioEntity) {

        entityManager.getTransaction().begin();
        entityManager.merge(usuarioEntity);
        entityManager.getTransaction().commit();
    }

    /**
     * RETORNA TODAS AS PESSOAS CADASTRADAS NO BANCO DE DADOS
     */
    @SuppressWarnings("unchecked")
    public List<Usuario> lista() {

        return entityManager.createQuery("SELECT u FROM Usuario u ORDER BY u.nome").getResultList();
    }

    /**
     * CONSULTA UMA PESSOA CADASTRA PELO CÓDIGO
     */
    public Usuario getUsuario(Integer id) {

        return entityManager.find(Usuario.class, id);
    }

    /**
     * EXCLUINDO UM REGISTRO PELO CÓDIGO
     *
     */
    public void exclui(Integer id) {

        Usuario usuario = getUsuario(id);

        entityManager.getTransaction().begin();
        entityManager.remove(usuario);
        entityManager.getTransaction().commit();

    }

    public Usuario autentica(String email, String senha) {
        String jpql = "SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha";
        try {
            return entityManager.createQuery(jpql, Usuario.class)
                    .setParameter("email", email).setParameter("senha", senha).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuario getUsuario(String token) {
        String jpql = "SELECT u FROM Usuario u WHERE u.token = :token";
        try {
            Usuario usuario = entityManager.createQuery(jpql, Usuario.class)
                    .setParameter("token", token).getSingleResult();
            return usuario;
        } catch (NoResultException e) {
            return null;
        }
    }

    public void atualizaToken(String token, String chaveToken, int id) {
        Usuario usuario = getUsuario(id);
        entityManager.getTransaction().begin();
        usuario.setToken(token);
        usuario.setChaveToken(chaveToken);
        entityManager.getTransaction().commit();
    }

    @Override
    public boolean persiste(Entidade entidade, Usuario usuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String get(Usuario usuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }
}
