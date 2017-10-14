package br.com.openbyod.dao;

import javax.persistence.NoResultException;

import br.com.openbyod.entidade.Contato;
import br.com.openbyod.entidade.Entidade;
import br.com.openbyod.entidade.Usuario;

public class ContatoDAO extends EntidadeDAO {
    
    public ContatoDAO() throws Exception {
        
    }
    /**
     * CONSULTA UMA PESSOA CADASTRA PELO CÓDIGO
     */
    public Contato getContato(Integer id) {

        return entityManager.find(Contato.class, id);
    }

    @Override
    public boolean persiste(Entidade entidade, Usuario usuario) {
        try {
            Contato contato = (Contato) entidade;
            contato.setUsuario(usuario);
            entityManager.getTransaction().begin();
            entityManager.merge(contato);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            LOG.severe("Exceção persisteContato(): " + e.getLocalizedMessage());
            // Salva o arquivo de LOG
            LOG.finest("Arquivo de Log salvo!");
            return false;
        }
        return true;
    }

    @Override
    public String get(Usuario usuario) {
        String jpql = "SELECT c FROM Contato c WHERE c.usuario = :usuario ORDER BY c.nome";
        try {
            return entityManager.createQuery(jpql, Contato.class).setParameter("usuario", usuario)
                    .getResultList().toString();
        } catch (NoResultException e) {
            return "[]";
        }
    }
}
