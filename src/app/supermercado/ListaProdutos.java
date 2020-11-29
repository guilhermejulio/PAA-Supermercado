package app.supermercado;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que representa uma lista de compras, não é um carrinho
 * 
 * @param p
 */
public class ListaProdutos implements Comparable {
    public ArrayList<Produto> produtos;

    protected float valor = 0;
    protected int peso = 0;

    protected int qtdProdutos = 0;

    public ListaProdutos() {
        produtos = new ArrayList<Produto>();
    }

    public ListaProdutos(ListaProdutos lista) {
        this.produtos = new ArrayList<Produto>(lista.getProdutos());
        this.peso = lista.peso;
        this.valor = lista.valor;
        this.qtdProdutos = lista.qtdProdutos;
    }

    public void adicionarProduto(Produto p) {
        produtos.add(p);

        valor += p.valor;
        peso += p.peso;

        qtdProdutos++;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public Produto pegarUltimoProduto() {
        return produtos.get(produtos.size() - 1);
    }

    public int getQtdProdutos() {
        return qtdProdutos;
    }

    public float getValor() {
        return valor;
    }

    @Override
    public int compareTo(Object o) {
        ListaProdutos outro = (ListaProdutos) o;

        if (this.qtdProdutos < outro.qtdProdutos)
            return -1;
        else if (this.qtdProdutos > outro.qtdProdutos)
            return 1;

        return 0;
    }

    public void saveToFile(BufferedWriter bw) throws IOException {
        bw.write("\n\nProdutos: ");
        for (Produto p : produtos) {
            bw.write("\n"+p.toString());
        }
        bw.write("\n\n\nQuantidade de itens da lista: "+qtdProdutos);
        bw.write("\nValor de todos produtos: R$ "+valor+"\nPeso dos produtos: "+peso+" kg");
        

    }

    @Override
    public String toString() {
        StringBuffer saida = new StringBuffer(); 
        saida.append("\n\nProdutos: ");
        for (Produto p : produtos) {
            saida.append("\n"+p.toString());
        }

        return saida.toString();
    }

}
