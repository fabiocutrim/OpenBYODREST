package br.com.openbyod.resource;


import br.com.openbyod.dao.ContatoDAO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.openbyod.dao.EmailUsuarioDAO;
import br.com.openbyod.entidade.EmailUsuario;
import static br.com.openbyod.resource.GenericResource.LOG;

@Path("/email")
public class EmailResource extends GenericResource {
    private final EmailUsuario emailUsuario = new EmailUsuario();
    private EmailUsuarioDAO emailDAO;

    @GET
    @Produces("application/json; charset=UTF-8")
    @Path("/inbox")
    public Response getCaixaDeEntrada(@HeaderParam("token") String token,
            @HeaderParam("chave") String chave) {
        try {
            emailDAO = new EmailUsuarioDAO();
        } catch (Exception ex) {
            LOG.severe(ex.getLocalizedMessage());
        }
        // Processa a requisição
        return processaRequisicao(new Resource("", ""), token, chave, emailUsuario, emailDAO,
                "E-mail", "Lista", METODO_HTTP.GET);
    }

    @POST
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    @Path("/envia")
    public Response enviar(Resource resource, @HeaderParam("token") String token,
            @HeaderParam("chave") String chave) {
        try {
            emailDAO = new EmailUsuarioDAO();
        } catch (Exception ex) {
            LOG.severe(ex.getLocalizedMessage());
        }
        // Processa a requisição
        return processaRequisicao(resource, token, chave, emailUsuario, emailDAO, 
                "E-mail", "Envio", METODO_HTTP.POST);
    }
}
