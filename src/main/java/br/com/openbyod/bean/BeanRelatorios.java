/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.openbyod.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.openbyod.dao.ChamadaDAO;
import br.com.openbyod.dao.EmailUsuarioDAO;
import br.com.openbyod.entidade.EmailUsuario;

/**
 *
 * @author Castro
 */
@ManagedBean(name = "beanRelatorios")
@RequestScoped
public class BeanRelatorios {

	private String mes;
	private Map<String, String> meses;
	private List<Relatorio> listaRelatorioChamadas;
	private List<Relatorio> listaRelatorioEmailsRecebidos;
	private List<Relatorio> listaRelatorioEmailsEnviados;
	private List<EmailUsuario> listaRelatorioPalavrasChaveEmails;
	private String palavrasChave = "NAMORO RELACIONAMENTO BALADA TESTE "
			+ "ANEXO REUNIÃO RELATÓRIO APLICAÇÃO MÓVEL OPEN BYOD";

	/**
	 * Creates a new instance of ReportsBean
	 */
	public BeanRelatorios() {
		meses = new LinkedHashMap<String, String>();
		meses.put("Janeiro", "1");
		meses.put("Fevereiro", "2");
		meses.put("Março", "3");
		meses.put("Abril", "4");
		meses.put("Maio", "5");
		meses.put("Junho", "6");
		meses.put("Julho", "7");
		meses.put("Agosto", "8");
		meses.put("Setembro", "9");
		meses.put("Outubro", "10");
		meses.put("Novembro", "11");
		meses.put("Dezembro", "12");
	}

	public void getRelatorioChamadas() throws Exception {
		// gets the paramter from the screen
		int month = Integer.parseInt(this.getMes());

		// gets the list from the database
		this.setListaRelatorioChamadas(new ChamadaDAO().getListaDeChamadas(month));
	}

	public void getRelatorioEmailsEnviados() throws Exception {
		// gets the paramter from the screen
		int month = Integer.parseInt(this.getMes());

		// gets the list from the database
		this.setListaRelatorioEmailsEnviados(new EmailUsuarioDAO().getListaDeEmailsEnviados(month));
	}

	public void getRelatorioEmailsRecebidos() throws Exception {
		// gets the paramter from the screen
		int month = Integer.parseInt(this.getMes());

		// gets the list from the database
		this.setListaRelatorioEmailsRecebidos(new EmailUsuarioDAO().getListaDeEmailsRecebidos(month));
	}

	public void getRelatorioPalavrasChave() throws Exception {
		// gets the paramter from the screen
		int month = Integer.parseInt(this.getMes());

		// gets the list from the database
		this.setListaRelatorioPalavrasChaveEmails(
				new EmailUsuarioDAO().getListaDePalavrasChaveEmails(month, this.getPalavrasChave()));
	}

	/**
	 * @return the mes
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * @param mes
	 *            the mes to set
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	/**
	 * @return the listaRelatorioChamadas
	 */
	public List<Relatorio> getListaRelatorioChamadas() {
		return listaRelatorioChamadas;
	}

	/**
	 * @param listaRelatorioChamadas
	 *            the listaRelatorioChamadas to set
	 */
	public void setListaRelatorioChamadas(List<Relatorio> listaRelatorioChamadas) {
		this.listaRelatorioChamadas = listaRelatorioChamadas;
	}

	/**
	 * @return the listaRelatorioEmailsRecebidos
	 */
	public List<Relatorio> getListaRelatorioEmailsRecebidos() {
		return listaRelatorioEmailsRecebidos;
	}

	/**
	 * @param listaRelatorioEmailsRecebidos
	 *            the listaRelatorioEmailsRecebidos to set
	 */
	public void setListaRelatorioEmailsRecebidos(List<Relatorio> listaRelatorioEmailsRecebidos) {
		this.listaRelatorioEmailsRecebidos = listaRelatorioEmailsRecebidos;
	}

	/**
	 * @return the listaRelatorioEmailsEnviados
	 */
	public List<Relatorio> getListaRelatorioEmailsEnviados() {
		return listaRelatorioEmailsEnviados;
	}

	/**
	 * @param listaRelatorioEmailsEnviados
	 *            the listaRelatorioEmailsEnviados to set
	 */
	public void setListaRelatorioEmailsEnviados(List<Relatorio> listaRelatorioEmailsEnviados) {
		this.listaRelatorioEmailsEnviados = listaRelatorioEmailsEnviados;
	}

	/**
	 * @return the listaRelatorioPalavrasChaveEmails
	 */
	public List<EmailUsuario> getListaRelatorioPalavrasChaveEmails() {
		return listaRelatorioPalavrasChaveEmails;
	}

	/**
	 * @param listaRelatorioPalavrasChaveEmails
	 *            the listaRelatorioPalavrasChaveEmails to set
	 */
	public void setListaRelatorioPalavrasChaveEmails(List<EmailUsuario> listaRelatorioPalavrasChaveEmails) {
		this.listaRelatorioPalavrasChaveEmails = listaRelatorioPalavrasChaveEmails;
	}

	/**
	 * @return the palavrasChave
	 */
	public String getPalavrasChave() {
		return palavrasChave;
	}

	/**
	 * @param palavrasChave
	 *            the palavrasChave to set
	 */
	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
	}

	/**
	 * @return the meses
	 */
	public Map<String, String> getMeses() {
		return meses;
	}

	/**
	 * @param meses
	 *            the meses to set
	 */
	public void setMeses(Map<String, String> meses) {
		this.meses = meses;
	}
}
