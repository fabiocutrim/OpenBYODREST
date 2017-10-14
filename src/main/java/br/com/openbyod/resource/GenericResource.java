/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.openbyod.resource;

import java.util.logging.Logger;

import br.com.openbyod.dao.EntidadeDAO;
import br.com.openbyod.dao.UsuarioDAO;
import br.com.openbyod.entidade.Entidade;
import br.com.openbyod.entidade.Usuario;
import br.com.openbyod.log.GenericLogger;
import br.com.openbyod.seguranca.criptografia.Base64;
import br.com.openbyod.seguranca.criptografia.Decriptacao;
import br.com.openbyod.seguranca.criptografia.Encriptacao;
import br.com.openbyod.seguranca.sessao.Token;
import java.util.logging.Level;
import javax.ws.rs.core.Response;

/**
 *
 * @author Windows
 */
public class GenericResource {

    enum METODO_HTTP {
        GET, POST, PUT;
    }

    private UsuarioDAO usuarioDAO;
    final Token autenticacao = new Token();
    static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    Response processaRequisicao(Resource resource, String token, String chave,
            Entidade entidade, EntidadeDAO entidadeDAO,
            String nomeEntidade, String operacao, METODO_HTTP metodoHttp) {

        Response response;
        try {
            // Inicia a gravação do LOG
            GenericLogger.init();
            LOG.setLevel(Level.INFO);
            LOG.info("=== REQUISIÇÃO " + operacao + " " + nomeEntidade + " ===");

            // Recupera a chave do token
            usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.getUsuario(token);
            String chaveToken = usuario.getChaveToken();

            mensagemRequisicao(metodoHttp, resource, usuario, token, chave, chaveToken);

            // Verifica a validade e a data de expiração do token
            response = verificaToken(autenticacao, token, chaveToken);

            // Caso o token seja válido, processa a requisição
            if (response == null) {
                // Atualiza o token na base de dados
                String tokenAtualizado = atualizaToken(autenticacao, token, chaveToken, usuarioDAO, usuario);
                // Decriptografa a chave
                byte[] chaveOriginal = Decriptacao.decriptografaChave(chave);
                mensagemChaveOriginal(chaveOriginal);

                // Processa as requisições GET
                if (metodoHttp.equals(METODO_HTTP.GET)) {

                    response = processaRequisicao(entidadeDAO, usuario, resource,
                            tokenAtualizado, chaveOriginal);

                    // Processa as requisições não-seguras
                } else if (metodoHttp.equals(METODO_HTTP.POST)
                        || metodoHttp.equals(METODO_HTTP.PUT)) {

                    response = processaRequisicao(entidadeDAO, entidade, nomeEntidade,
                            usuario, resource, tokenAtualizado, response, chaveOriginal);

                }
            }

        } catch (Exception ex) {

            mensagemExcecao(operacao, ex);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        }
        mensagemResource(resource);
        mensagemResponse(nomeEntidade, operacao, response);
        LOG.finest("Arquivo de Log salvo!");
        return response;
    }

    private Response verificaToken(Token autenticacao, String token,
            String chaveToken) {
        // Checa se o token é válido
        if (!autenticacao.validaToken(token, chaveToken)) {

            return Response.status(Response.Status.BAD_REQUEST).build();

        }
        // Checa se o token já expirou
        if (!autenticacao.checaDataExpiracaoValida(token)) {

            return Response.status(Response.Status.FORBIDDEN).build();

        }
        return null;
    }

    private String atualizaToken(Token autenticacao, String token,
            String chaveToken, UsuarioDAO usuarioDAO,
            Usuario usuario) {
        // Atualiza a data de expiração
        String tokenAtualizado = autenticacao.atualizaDataExpiracao(token, chaveToken);
        // Atualiza o token na base de dados
        usuarioDAO.atualizaToken(tokenAtualizado, chaveToken, usuario.getId());
        return tokenAtualizado;
    }

    // Processa as requisições GET
    private Response processaRequisicao(EntidadeDAO entidadeDAO, Usuario usuario,
            Resource resource, String tokenAtualizado, byte[] chaveOriginal) 
            throws Exception {
        
        Response response;
        // Recupera a lista
        String lista = entidadeDAO.get(usuario);
        // "Seta" o token atualizado
        resource.setToken(tokenAtualizado);
        mensagemLista(lista);

        // Retorna a lista vazia
        if (lista.equals("[]")) {

            response = Response.status(Response.Status.PARTIAL_CONTENT).entity(resource).build();

            // Retorna a lista
        } else {
            // Criptografa a lista de usuários 
            String list = Encriptacao.criptografa(lista, chaveOriginal);
            resource.setResource(list);
            response = Response.status(Response.Status.OK).entity(resource).build();

        }
        return response;
    }

    // Processa as requisições POST e PUT
    private Response processaRequisicao(EntidadeDAO entidadeDAO, Entidade entidade,
            String nomeEntidade, Usuario usuario, Resource resource,
            String tokenAtualizado, Response response, byte[] chaveOriginal)
            throws Exception {
        // Decriptografa a mensagem
        String json = Decriptacao.decriptografa(resource.getResource(), chaveOriginal);
        entidade = entidade.converteJSONEmEntidade(json);
        mensagemEntidade(entidade, nomeEntidade);

        // "Seta" o token atualizado para enviar ao cliente
        resource.setToken(tokenAtualizado);
        resource.setResource("");

        // Salva e retorna o token atualizado
        if (entidadeDAO.persiste(entidade, usuario)) {

            response = Response.status(Response.Status.CREATED).entity(resource).build();
        }
        return response;
    }

    private void mensagemRequisicao(METODO_HTTP metodoHttp, Resource resource,
            Usuario usuario, String token, String chave, String chaveToken) {
        LOG.info("Usuário Logado: " + usuario.getEmail());
        if (!metodoHttp.equals(METODO_HTTP.GET)) {
            LOG.info("Mensagem: " + resource.getResource());
        }
        LOG.info("Token: " + token);
        LOG.info("Chave Criptografada: " + chave);
        LOG.info("Chave Token: " + chaveToken);
    }

    private void mensagemChaveOriginal(byte[] chaveOriginal) {
        LOG.info("Chave original: " + Base64.fromHex(chaveOriginal));
    }

    private void mensagemLista(String lista) {
        LOG.info("Lista: " + lista);
    }

    private void mensagemEntidade(Entidade entidade, String nomeEntidade) {
        LOG.info(nomeEntidade + ": " + entidade);
    }

    private void mensagemExcecao(String operacao, Exception e) {
        LOG.severe("Exceção " + operacao + ": " + e.getLocalizedMessage());
    }

    private void mensagemResponse(String nomeEntidade, String operacao, Response response) {
        int statusCode = response.getStatus();
        if (statusCode == 400 || statusCode == 403) {
            LOG.warning("Response " + operacao + " " + nomeEntidade + ": " + response.toString());
        } else {
            LOG.info("Response " + operacao + " " + nomeEntidade + ": " + response.toString());
        }
    }

    private void mensagemResource(Resource resource) {
        LOG.info("Resource: " + resource);
    }
}
