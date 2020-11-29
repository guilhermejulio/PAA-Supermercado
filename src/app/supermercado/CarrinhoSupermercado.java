package app.supermercado;

import java.io.BufferedWriter;
import java.io.IOException;

public class CarrinhoSupermercado {
    // Lista de compras do carrinho;
    ListaProdutos listaProdutos;

    // Orçamento e Peso Máximo que o carrinho pode ter.
    float orcamento;
    int pesomax;


    /**
     * Um carrinho possui um orçamento máximo 
     * E um peso maximo 
     * 
     * A soma dos produtos não devem ultrapassar nem orçamento nem o peso.
     * @param orcamento
     * @param pesomax
     */
    public CarrinhoSupermercado(float orcamento, int pesomax){
        listaProdutos = new ListaProdutos();
        this.orcamento = orcamento;
        this.pesomax = pesomax;
    }

    public boolean cabeNoCarrinho(ListaProdutos lista){
        //se cabe no peso e no orçamento
        if(lista.peso <= this.pesomax)
            if(lista.valor <= this.orcamento) return true;

        return false;
    }

    public boolean cabeNoCarrinho(Produto p){
        if(listaProdutos.peso + p.peso <=pesomax){
            if(listaProdutos.valor + p.valor <= orcamento)
                return true;
        }

        return false;
    }

    public void adicionarProduto(Produto p){
        listaProdutos.adicionarProduto(p);
    }

    public void trocarLista(ListaProdutos lista){
        listaProdutos = new ListaProdutos(lista);
    }

    public ListaProdutos getListaProdutos() {
        return listaProdutos;
    }
    
    public void saveToFile(BufferedWriter bw) throws IOException {
        bw.write("- Carrinho de Compras - \n\nOrçamento definido: R$ "+orcamento+"\nPeso maximo: "+pesomax+" kg");
        bw.write("\n\nMelhor lista de produtos encontrada: ");
        listaProdutos.saveToFile(bw);

    }

}
