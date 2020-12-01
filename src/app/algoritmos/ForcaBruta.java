package app.algoritmos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import app.supermercado.CarrinhoSupermercado;
import app.supermercado.ListaProdutos;
import app.supermercado.Produto;

final class ForcaBruta {

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
    // calcula o valor médio dos produtos
    // gera o orçamento como proporção do valor médio
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

    // #region Instancias Crescentes
    /**
     * Metodo responsável por realizar a execução do força bruta A execução é feita
     * com instancias crescentes Além das instancias crescentes, é buscado o maior
     * conjunto em até 5 segundos Todos os dados são salvos em arquivo TXT, na pasta
     * 'log-fb'
     */
    public static void instanciasCrescentes() {
        // Execução baseado no crescimento de instancias
        int instancia = 4;

        while (instancia <= 24) {
            // Gerar lista de produtos de acordo com o tamanho da instancia
            ArrayList<Produto> produtos = new ArrayList<Produto>(geraProduto(instancia));

            float orcamento = (float) criarOrcamento(produtos, PROPORCAOORCAMENTO);
            int pesomax = criarPesoMax(produtos, instancia - 1);

            CarrinhoSupermercado carrinho = new CarrinhoSupermercado(orcamento, pesomax);
            executarForcaBruta(produtos, carrinho);

            instancia += 2;
        }

    }

    // #endregion

    // #region Força Bruta

    /**
     * Metodo que gera todas as listas de compras possiveis baseado na lista de
     * produtos:
     * 
     * Base do codigo: Um ArrayList temporario de listas de compras que armazena
     * possibilidades anteriores Um ArrayList auxiliar que armazena possibilidades
     * baseado nas anteriores
     * 
     * Exemplo:
     * 
     * Para barra '/' leia-se 'com', 1 com 2...
     * 
     * Lista Temporaria = { 1, 2, 3, 4 } Lista auxiliar que será gerada = {1/2, 1/3,
     * 1/4, 2/3, 2/4, 3/4}
     * 
     * Após a gerção destas possibilidades, a lista auxiliar passará a ser a nova
     * temporaria E será gerado novas combinações baseado na anterior Até que seja
     * gerado todas as combinações possiveis
     * 
     * @param produtos
     * @return
     */
    public static void executarForcaBruta(ArrayList<Produto> produtos, CarrinhoSupermercado carrinho) {
        ArrayList<ListaProdutos> listasTemp = new ArrayList<ListaProdutos>();

        int tamanhoInstancia = produtos.size();

        int possibilidades = 0;
        int possibilidadesValidas = 0;

        StringBuffer logTempo = new StringBuffer();

        long tempoInicial = System.nanoTime();

        // Combinações para itens isolados.
        for (Produto p : produtos) {
            ListaProdutos aux = new ListaProdutos();
            aux.adicionarProduto(p);
            listasTemp.add(aux);
            possibilidades++;
        }

        // Sequencia do força bruta, combinação dos itens com os subsequentes.
        boolean gerouTodasPossibilidades = false;

        while (!gerouTodasPossibilidades) {

            ArrayList<ListaProdutos> aux = new ArrayList<ListaProdutos>();
            boolean gerouSequentes = false;

            // Variaveis para controlar as posições na lista temporaria.
            int indexAtual = 0;
            int indexSequente = 1;

            /*
             * Gerar combinações sequentes Exemplo: Dado uma lista [1,2,3,4] Será gerado
             * combinações sequentes de todo mundo com 2 elementos
             */

            while (!gerouSequentes) {
                // Lista atual --> Representa uma lista X de produtos que será combinada com uma
                // sequente.
                ListaProdutos listaAtual = new ListaProdutos(listasTemp.get(indexAtual));

                /* Condição de parada se já gerou todas possibilidades */
                if (listaAtual.pegarUltimoProduto() == produtos.get(produtos.size() - 1)) {
                    gerouSequentes = true;
                } else {
                    Produto p = listasTemp.get(indexSequente).pegarUltimoProduto();
                    listaAtual.adicionarProduto(p);

                    // Após a combinação ser gerada, será armazenada no ArrayList Auxiliar.
                    aux.add(listaAtual);
                    possibilidades++;

                    // Se já gerou todas as possibilidades para a lista atual
                    if (p == produtos.get(produtos.size() - 1)) {
                        // passar para proxima lista, e gerar possibilidades dela.
                        indexAtual++;
                        indexSequente = indexAtual + 1;

                        /*
                         * While para garantir a condição de parada sem 'comer' elementos
                         * 
                         * Se encontrou um produto que não tem combinações sequentes
                         * 
                         * Exemplo lista --> { 1, 2, 3 ,4} O 4 não possui combinação com ninguem, não
                         * tem alguem a frente dele.
                         * 
                         * Porém é verificado se é o fim da lista mesmo Caso não sejá fim da lista,
                         * prossegue para proxima lista.
                         */
                        while (listasTemp.get(indexAtual).pegarUltimoProduto() == produtos.get(produtos.size() - 1)
                                && indexAtual != listasTemp.size() - 1) {

                            // Se encontrou mas não é no fim da lista
                            // Significa que existem combinações para os sequentes
                            indexAtual++;
                            indexSequente++;

                        }
                    } else {
                        indexSequente++;
                    }
                }

            }

            /*
             * Condição de parada, se a ultima lista do ultimo conjunto gerado possui a
             * mesma quantidade de produtos da lista de produtos
             * 
             * Significa que gerei todas as possibilidades
             * 
             * Validar possibilidades: Verificar as listas geradas se cabem e se são a
             * melhor p/carrinho
             * 
             * É validado neste momento para que seja descartado as listas não mais usadas
             * Otimizando desta forma a memoria diminuindo o risco de estouro de memoria
             */
            if (aux.get(aux.size() - 1).getQtdProdutos() == produtos.size()) {
                gerouTodasPossibilidades = true;
                possibilidadesValidas += validarPossibilidades(listasTemp, carrinho);
                possibilidadesValidas += validarPossibilidades(aux, carrinho);
            } else {
                possibilidadesValidas += validarPossibilidades(listasTemp, carrinho);
                listasTemp = new ArrayList<ListaProdutos>(aux);
            }

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

        // Investigação B) --> Maior conjunto no tempo limite de 5segundos
        if (tempo <= 5)
            gravarMaiorConjunto(carrinho.getListaProdutos(), logTempo, tamanhoInstancia);

        gravarInstancia(tamanhoInstancia, possibilidades, possibilidadesValidas, carrinho, logTempo);

    }

    // #endregion

    // #region Validação de possibilidades
    /**
     * Metodo que realiza a verificação de cada possibilidade, se cabe e se é a
     * melhor atualmente
     * 
     * @param listaPossibilidades
     * @param carrinho
     * @return
     */
    public static int validarPossibilidades(ArrayList<ListaProdutos> listaPossibilidades,
            CarrinhoSupermercado carrinho) {

        int possibilidadesValidas = 0;

        for (ListaProdutos listaProdutos : listaPossibilidades) {
            if (carrinho.cabeNoCarrinho(listaProdutos)) {
                possibilidadesValidas++;

                int melhor = listaProdutos.compareTo(carrinho.getListaProdutos());

                if (melhor == 1) {
                    carrinho.trocarLista(listaProdutos);
                } else if (melhor == 0) {
                    if (listaProdutos.getValor() < carrinho.getListaProdutos().getValor()) {
                        carrinho.trocarLista(listaProdutos);
                        // Em caso de empate, escolhe a lista que custa menos.
                    }
                }
            }
        }

        return possibilidadesValidas;

    }
    // #endregion

    // #region Gravar em Arquivo
    /**
     * Metodo para gravar instancia em arquivo texto, facilitando a visualização
     * 
     * @param instancia
     * @param possibilidades
     * @param possibilidadesValidas
     * @param carrinho
     * @param logTempo
     */
    public static void gravarInstancia(int instancia, int possibilidades, int possibilidadesValidas,
            CarrinhoSupermercado carrinho, StringBuffer logTempo) {

        String path = "log-fb/Instancia_" + instancia + "Produtos.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            carrinho.saveToFile(bw);
            bw.write("\n\nNumero total de possibilidades validas para o carrinho: " + possibilidadesValidas);
            bw.write("\nNumero total de possibilidades encontradas (válidas ou inválidas): " + possibilidades);
            bw.write("\n" + logTempo.toString());

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Metodo para gravar o item B da investigação -> maior conjunto no tempo de 5s
     * 
     * @param lista
     * @param logTempo
     * @param tamanhoInstancia
     */
    public static void gravarMaiorConjunto(ListaProdutos lista, StringBuffer logTempo, int tamanhoInstancia) {

        String path = "log-fb/Maior-Conjunto-5Segundos.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("- Maior Conjunto Encontrado no Tempo limite de 5 segundos -");
            lista.saveToFile(bw);
            bw.write("\n" + logTempo.toString());
            bw.write("\nTamanho da instancia onde foi obtido: " + tamanhoInstancia);

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    // #endregion

    public static void criarPastaLog(){
        File file = new File("log-fb/");

        if(!file.exists()){
            file.mkdir();
        }
    }
    // #region Menu
    /***
     * Menu básico p/ facilitar a execução
     * 
     * @param leitor
     * @return
     */
    public static int menu(Scanner leitor) {
        System.out.println();
        System.out.println("\tLista de Supermercado - Força Bruta");
        System.out.println("\n0. Fim do programa");
        System.out.println("1. Executar força bruta");
        System.out.println("\nOpcao:");
        int opcao = Integer.parseInt(leitor.nextLine());
        return opcao;
    }
    // #endregion

    public static void main(String[] args) throws Exception {
        int opcao;
        Scanner entrada = new Scanner(System.in);
        Scanner teclado = new Scanner(System.in);

        try {
            do {
                opcao = menu(entrada);

                switch (opcao) {
                    case 1:
                        criarPastaLog();
                        System.out
                                .println("\n\nO algoritmo será executado com instâncias crescentes de 4 a 24 produtos");
                        System.out.println(
                                "\nCada intancia gerada o carrinho final será salvo em arquivo TXT na pasta 'log-fb' com o nome da instancia, exemplo: Instancia_4Produtos.txt");
                        System.out.println("\nOs dados de tempo de execução serão salvos no mesmo arquivo");
                        System.out.println(
                                "\nO maior conjunto no tempo limite de 5 segundos, também será escrito na mesma pasta");
                        System.out.println("\n\nSe compreendeu, prescione <enter> para executar...");
                        teclado.nextLine();

                        instanciasCrescentes();

                        break;

                    default:
                        System.out.println("\n\nAdeus!!!!");
                        break;

                }
            } while (opcao != 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
