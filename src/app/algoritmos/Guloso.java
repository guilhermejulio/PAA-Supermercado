package app.algoritmos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import app.supermercado.CarrinhoSupermercado;
import app.supermercado.Produto;

/* Maior quantidade de produtos
 
Criterio guloso: 
    Maximização: devolver a maior quantidade de produtos
    --> Baseado no orçamento maximo definido e peso maximo definido do carrinho.

Escolha gulosa: Escolher o menor objeto possível (Condiderando Peso e Valor);
*/

final class Guloso {

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

    // #region Supermercado
    static final float PROPORCAOORCAMENTO = 13.5f;
    static double criarOrcamento(List<Produto> lista, float proporcao) {
        double valorTotal = lista.stream().mapToDouble(p -> p.getValor()).sum();
        int quantTotal = lista.size();
        double media = valorTotal / quantTotal;

        return (int) Math.ceil(media * proporcao);
    }

    static int criarPesoMax(List<Produto> lista, int proporcao) {
        int pesoTotal = lista.stream().mapToInt(p -> p.getPeso()).sum();
        int quantTotal = lista.size();
        int media = pesoTotal / quantTotal;

        return media * proporcao;
    }

    // #endregion
    
    //  #region Instancias Crescentes
    public static void instanciasCrescentes() {

        //Linha abaixo para evitar Bug do JDK na ordenação da lista de produtos
        //Caso apresente algum problema na execução, remova a linha abaixo
        //E verifique o tamanho da instancia definido 
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

        int instancia = 4;

        while (instancia <= 1048576) {
            ArrayList<Produto> produtos = new ArrayList<Produto>(geraProduto(instancia));

            float orcamento = (float) criarOrcamento(produtos, PROPORCAOORCAMENTO);
            int pesomax = criarPesoMax(produtos, instancia - 1);

            CarrinhoSupermercado carrinho = new CarrinhoSupermercado(orcamento, pesomax);
            GDY(produtos,carrinho);

            instancia *= 2;
        }
    }
    // #endregion

    //  #region Execução do algoritmo Guloso.
    /**
     * Criterio guloso: 
     *  
     * - Maximização: maior quantidade de produtos possivel 
     * Baseado no orçamento maximo e peso maximo definidos no carrinho
     * 
     * - Escolha gulosa: Escolher o menor objeto possivel da lista de produtos
     * Considerando peso e valor
     * @param produtos
     * @param carrinho
     */
    public static void GDY(ArrayList<Produto> produtos, 
        CarrinhoSupermercado carrinho) {

        int tamInstancia = produtos.size();
        StringBuffer logTempo = new StringBuffer();

        long tempoInicial = System.nanoTime();
        
        produtos.sort(null); // ==> Conjunto de candiadatos

        boolean resolveu = false; // ==> Função de solução
        int i=0;

        //==> Função de seleção escolha otima local do produto sem olhar para trás
        while(!resolveu){

            //Cabe no carrinho ==> Função de viabilidade
            //Adicionar produto ==> Função objetivo
            if(carrinho.cabeNoCarrinho(produtos.get(i))) carrinho.adicionarProduto(produtos.get(i));
            else resolveu = true;

            i++;
        }
        long tempoFinal = System.nanoTime();


        long tempo = tempoFinal - tempoInicial;

        // Formatação da saída
        if (TimeUnit.SECONDS.convert(tempo, TimeUnit.NANOSECONDS) <= 0) {
            tempo = TimeUnit.MILLISECONDS.convert(tempo, TimeUnit.NANOSECONDS);
            logTempo.append("\nTempo de execução: " + tempo + " Milisegundo(s) \n\n");
        } else {
            tempo = TimeUnit.SECONDS.convert(tempo, TimeUnit.NANOSECONDS);
            logTempo.append("\nTempo de execução: " + tempo + " Segundo(s) \n\n");
        }


        //gravar dados da execução 
        gravarInstancia(tamInstancia, carrinho, logTempo);

    }
    //  #endregion

    // #region Gravar em aruivo
    private static void gravarInstancia(int tamInstancia, CarrinhoSupermercado carrinho, 
        StringBuffer logTempo) {    
        
        String path = "src/app/algoritmos/log-guloso/Instancia_" + tamInstancia + "Produtos.txt";

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            carrinho.saveToFile(bw);
            bw.write("\n" + logTempo.toString());
        }catch(final IOException e){
            e.printStackTrace();
        }
    
    }


    //  #endregion


    //#region Menu
    /**
     * Menu básico p/ facilitar a execução
     * 
     * @param leitor
     * @return
     */
    public static int menu(Scanner leitor) {
        System.out.println();
        System.out.println("\tLista de Supermercado - Algoritmo Guloso");
        System.out.println("\n0. Fim do programa");
        System.out.println("1. Executar algoritmo guloso");
        System.out.println("\nOpcao:");
        int opcao = Integer.parseInt(leitor.nextLine());
        return opcao;
    }
    //  #endregion
    public static void main(String[] args) {
        int opcao;
        Scanner entrada = new Scanner(System.in);
        Scanner teclado = new Scanner(System.in);

        try {
            do {
                opcao = menu(entrada);

                switch (opcao) {
                    case 1:
                        System.out
                                .println("\n\nO algoritmo será executado com instancias crescentes de 4 a 1.048.576 produtos");
                        System.out.println(
                                "\nPara cada instancia, o carrinho será salvo em arquivo TXT na pasta 'log-guloso' com o nome da instancia, exemplo: Instancia_XProdutos.txt");
                        System.out.println("\nOs dados de tempo de execução serão salvos no mesmo arquivo");
                        System.out.println("\nSe compreendeu, prescione <enter> para executar...");
                        teclado.nextLine();

                        instanciasCrescentes();
                    break;

                    default:
                        System.out.println("\n\nAdeus!!");
                    break;
                }

            } while (opcao != 0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
