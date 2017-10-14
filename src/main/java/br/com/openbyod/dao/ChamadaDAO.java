/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.openbyod.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import br.com.openbyod.bean.Relatorio;
import br.com.openbyod.entidade.Chamada;
import br.com.openbyod.entidade.Entidade;
import br.com.openbyod.entidade.Usuario;
import javax.persistence.Query;

/**
 *
 * @author ARTLAN-00
 */
public class ChamadaDAO extends EntidadeDAO {
    
    public ChamadaDAO() throws Exception {
        
    }
    
    // @SuppressWarnings("unchecked")
    public List<Relatorio> getListaDeChamadas(int mes) {
        try {
            String jpql = "SELECT u.nome Nome, COUNT(*) Quantidade"
                    + " FROM Chamada c, Usuario u"
                    + " WHERE u.id = c.usuario_id AND MONTH(c.dataChamada) = :mes"
                    + " GROUP BY u.nome"
                    + " ORDER BY 2 DESC";
            
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
    
    @Override
    public boolean persiste(Entidade entidade, Usuario usuario) {
        try {
            Chamada chamada = (Chamada) entidade;
            entityManager.getTransaction().begin();
            entityManager.persist(chamada);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            LOG.severe("Exceção persisteChamada(): " + e.getLocalizedMessage());
            // Salva o arquivo de LOG
            LOG.finest("Arquivo de Log salvo!");
            return false;
        }
        return true;
    }
    
    @Override
    public String get(Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }
}
