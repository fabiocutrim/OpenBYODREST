/*
 * This class is used to leave vlues to the Performance reports bean
 * - Reports of executed dials in a month, by user
 * - Reports of sent and received emails in a mounth by user
 */
package br.com.openbyod.bean;

/**
 *
 * @author Castro
 */
public class Relatorio {

    private int quantidade;
    private String rotulo;

    public Relatorio(String rotulo, int quantidade) {
        super();
        this.quantidade = quantidade;
        this.rotulo = rotulo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }
}
