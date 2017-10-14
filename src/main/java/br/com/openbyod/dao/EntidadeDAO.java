/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.openbyod.dao;

import br.com.openbyod.entidade.Entidade;
import br.com.openbyod.entidade.Usuario;
import br.com.openbyod.log.GenericLogger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Windows
 */
public abstract class EntidadeDAO {

    static EntityManagerFactory entityManagerFactory;
    static EntityManager entityManager;
    static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public EntidadeDAO() throws Exception {
        /*
		 * CRIANDO O EntityManagerFactory COM AS PROPRIEDADES DO ARQUIVO
		 * persistence.xml
         */
        entityManagerFactory = Persistence.createEntityManagerFactory("persistence_unit_bd_openbyod");
        entityManager = entityManagerFactory.createEntityManager();
    }

    abstract public boolean persiste(Entidade entidade, Usuario usuario) throws Exception;

    abstract public String get(Usuario usuario) throws Exception;
}
