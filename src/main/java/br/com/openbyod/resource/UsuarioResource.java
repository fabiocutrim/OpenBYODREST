package br.com.openbyod.resource;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.openbyod.dao.UsuarioDAO;
import br.com.openbyod.entidade.Usuario;
import br.com.openbyod.log.GenericLogger;
import br.com.openbyod.seguranca.criptografia.Base64;
import br.com.openbyod.seguranca.criptografia.Decriptacao;
import br.com.openbyod.seguranca.criptografia.Encriptacao;
import br.com.openbyod.seguranca.sessao.Token;
import java.util.logging.Level;

/**
 * Essa classe vai expor os nossos métodos para serem acessasdos via http
 *
 * @Path - Caminho para a chamada da classe que vai representar o nosso serviço
 */
@Path("/usuario")
public class UsuarioResource {
    private String enderecoMAC;
    private byte[] chaveOriginal;
    private UsuarioDAO usuarioDAO;
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @GET
    @Produces("application/json; charset=UTF-8")
    @Path("/getUsuario/{id}")
    public Usuario getUsuario(@PathParam("id") Integer id) {
        Usuario usuario = usuarioDAO.getUsuario(id);

        if (usuario != null) {
            return usuario;
        }
        return null;
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    @Path("/lista")
    public List<Usuario> lista() {
        return usuarioDAO.lista();
    }

    @POST
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    @Path("/autentica")
    public Response autentica(Resource resource,
            @HeaderParam("chave") String chave) {
        
        // Usuário não autenticado
        Response response = Response.status(Response.Status.UNAUTHORIZED).build();
        try {
            // Inicia a gravação do LOG
            GenericLogger.init();
            LOG.setLevel(Level.INFO);
            LOG.info("=== REQUISIÇÃO Login ===");
           
            mensagemRequisicao(resource, chave);
            
            usuarioDAO = new UsuarioDAO();
            // Autentica o usuário
            Usuario usuario = autentica(chave, resource);

            // Autenticou o usuário
            if (usuario != null) {
                /*
                 Checa o endereço MAC do dispositivo
                 */
                // Dispositivo não pertecente ao usuário
                if (!usuario.getEnderecoMAC().equalsIgnoreCase(enderecoMAC)) {

                    response = Response.status(Response.Status.FORBIDDEN).build();

                    // MAC validado
                } else {

                    // Gera o token de autenticação e controle de sessão e
                    // atualiza na base de dados
                    String token = geraToken(usuario);

                    // Recupera o usuário na base de dados e envia criptografado
                    // como resposta ao cliente
                    resource = getResource(usuario, token);
                    response = Response.status(Response.Status.ACCEPTED).entity(resource).build();
                }
            }

        } catch (Exception ex) {
            mensagemExcecao(ex);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        mensagemResource(resource);
        mensagemResponse(response);
        // Salva o arquivo de LOG
        LOG.finest("Arquivo de Log salvo!");
        return response;
    }

    private Usuario autentica(String chave, Resource resource) {
        // Decriptografa a mensagem
        chaveOriginal = Decriptacao.decriptografaChave(chave);
        String credenciais = Decriptacao.decriptografa(resource.getResource(), chaveOriginal);

        mensagemCredenciais(credenciais);

        // Recupera as credenciais do usuário e converte em um objeto Usuario
        Usuario usuario = new Usuario().converteJSONEmEntidade(credenciais);
        String email = usuario.getEmail();
        String senha = usuario.getSenha();
        enderecoMAC = usuario.getEnderecoMAC();
        usuario = usuarioDAO.autentica(email, senha);
        return usuario;
    }

    private String geraToken(Usuario usuario) {
        Token autenticacao = new Token();
        String chaveToken = autenticacao.getChaveAssinatura();
        String token = autenticacao.geraToken(chaveToken);
        usuarioDAO.atualizaToken(token, chaveToken, usuario.getId());
        return token;
    }

    private Resource getResource(Usuario usuario, String token) {
        String respostaServidor = Encriptacao.criptografa(usuario.toString(), chaveOriginal);
        Resource resource = new Resource(respostaServidor, token);
        return resource;
    }

    private void mensagemRequisicao(Resource resource, String chave) {
        LOG.info("Requisição: " + resource.getResource());
        LOG.info("Chave Criptografada: " + chave);
    }

    private void mensagemCredenciais(String credenciais) {
        LOG.info("Chave original: " + Base64.fromHex(chaveOriginal));
        LOG.info("Credenciais: " + credenciais);
    }

    private void mensagemResponse(Response response) {
        if (response.getStatus() == 401 || response.getStatus() == 403) {
            LOG.warning("Response Login: " + response.toString());
        } else {
            LOG.info("Response Login: " + response.toString());
        }
    }

    private void mensagemExcecao(Exception ex) {
        LOG.severe("Exceção Login: " + ex.getLocalizedMessage());
    }

    private void mensagemResource(Resource resource) {
        LOG.info("Resource: " + resource);
    }
}
