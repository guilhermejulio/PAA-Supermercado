package app.algoritmos;

import java.util.ArrayList;

import app.supermercado.CarrinhoSupermercado;
import app.supermercado.ListaProdutos;
import app.supermercado.Produto;

/* Maior quantidade de produtos
 
Criterio guloso: 
    Maximização: devolver a maior quantidade de produtos
    --> Baseado no orçamento maximo definido e peso maximo definido do carrinho.

Escolha gulosa: Escolher o menor objeto possível (Condiderando Peso e Valor);
*/

public class Guloso {

    // #region Produtos (mochila + supermercado)
    static final int QUANTPROD = 50;

    static ArrayList<Produto> geraProduto(int tam) {
        ArrayList<Produto> prod = new ArrayList<Produto>();
        for (int i = 0; i < tam; i++) {
            Produto novo = new Produto(i * tam);
            prod.add(novo);
        }
        return prod;
        }
    
        // #endregion
    
    //Passa um carrinho por parametro, e os produtos.

    // --> Ordena os produtos menor para maior [menor1/menor2/menor3]
    // --> Criterio de 
    // --> para cada produto, verificar se cabe no carrinho, 
    // --> Item atual = produtos[x]
    // --> Produto -> cabe no carrinho? sim, coloca
    // --> Passa pro proximo produto


    public static void main(String[] args) {
        ArrayList<Produto> produtos = geraProduto(20);

        int teste =0;

        produtos.sort(null);

        teste=0;
    }
}
