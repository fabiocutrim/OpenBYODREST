/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.openbyod.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.openbyod.dao.ChamadaDAO;
import br.com.openbyod.dao.ContatoDAO;
import br.com.openbyod.entidade.Chamada;
import static br.com.openbyod.resource.GenericResource.LOG;

/**
 *
 * @author ARTLAN-00
 */
@Path("/chamada")
public class ChamadaResource extends GenericResource {
    private final Chamada chamada = new Chamada();
    private ChamadaDAO chamadaDAO;

    @POST
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    @Path("/salva")
    public Response salva(Resource resource, @HeaderParam("token") String token,
            @HeaderParam("chave") String chave) {
        try {
            chamadaDAO = new ChamadaDAO();
        } catch (Exception ex) {
            LOG.severe(ex.getLocalizedMessage());
        }
        // Processa a requisição
        return processaRequisicao(resource, token, chave, chamada, chamadaDAO, 
                "Chamada", "Inserção", METODO_HTTP.POST);
    }
}
