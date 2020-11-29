package app.supermercado;

import java.util.Random;
/** 
 * MIT License
 *
 * Copyright(c) 2020 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Produto{
    // static Random sorteio = new Random(42);                  //--> fixo
    static Random sorteio = new Random(System.nanoTime());   //--> aleatório 
    static final int PESOMAX = 50;
    static final float VALMAX = 40f;

    public int codigo;
    public int peso;
    public float valor;
    

    public Produto(int codigo){
        this.codigo = codigo;
        this.peso = 1+sorteio.nextInt(PESOMAX);
        this.valor = (float)(peso*2+sorteio.nextDouble()*VALMAX) ;
    }

    public Produto(Produto p){
        this.codigo = p.codigo;
        this.peso = p.peso;
        this.valor = p.valor;
    }

    public int getPeso() {
        return peso;
    }

    public float getValor() {
        return valor;
    }

    @Override
    public String toString(){
        return "Codigo do Produto: "+this.codigo+" | Peso: "+this.peso+" kg | Valor: R$ "+this.valor;
    }

    

    

}
