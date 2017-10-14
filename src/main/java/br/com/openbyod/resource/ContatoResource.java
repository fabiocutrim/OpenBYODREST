package br.com.openbyod.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.openbyod.dao.ContatoDAO;
import br.com.openbyod.entidade.Contato;

@Path("/contato")
public class ContatoResource extends GenericResource {
    private final Contato contato = new Contato();
    private ContatoDAO contatoDAO;

    @GET
    @Produces("application/json; charset=UTF-8")
    @Path("/lista")
    public Response lista(@HeaderParam("token") String token,
            @HeaderParam("chave") String chave) {
        try {
            contatoDAO = new ContatoDAO();
        } catch (Exception ex) {
            LOG.severe(ex.getLocalizedMessage());
        }
        // Processa a requisição
        return processaRequisicao(new Resource("", ""), token, chave, contato, contatoDAO,
                    "Contato", "Lista", METODO_HTTP.GET);
    }

    @POST
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    @Path("/insere")
    public Response insere(Resource resource,
            @HeaderParam("token") String token,
            @HeaderParam("chave") String chave) {
        try {
            contatoDAO = new ContatoDAO();
        } catch (Exception ex) {
            LOG.severe(ex.getLocalizedMessage());
        }
        // Processa a requisição
        return processaRequisicao(resource, token, chave, contato, contatoDAO, 
                "Contato", "Inserção", METODO_HTTP.POST);
    }

    @PUT
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    @Path("/atualiza")
    public Response atualiza(Resource resource,
            @HeaderParam("token") String token,
            @HeaderParam("chave") String chave) {
        try {
            contatoDAO = new ContatoDAO();
        } catch (Exception ex) {
            LOG.severe(ex.getLocalizedMessage());
        }
        // Processa a requisição
        return processaRequisicao(resource, token, chave, contato, contatoDAO, 
                "Contato", "Atualização", METODO_HTTP.PUT);
    }
}
