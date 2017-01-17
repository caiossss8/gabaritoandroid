package com.solutions.snap.gabarito;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caio on 12/01/2017.
 */

public class ReadImage {

    //declarando vetores de posicoes de respostas
    int[] posicaoa = new int[2];
    int[] posicaob = new int[2];
    int[] posicaoc = new int[2];
    int[] posicaod = new int[2];
    int[] posicaoe = new int[2];

    //80 = 66% da imagem preenchida
    static final int valorpintado = 80;
    static int contadorpintado = 0;

    //respostas
    static String p1;

    //lista de respostas
    static ArrayList<String> listarespostas = new ArrayList();

    private Bitmap bm;

    private IDecode iDecode;

    public ReadImage(Bitmap bm, IDecode idecode) {
        this.bm = bm;
        this.iDecode = idecode;
    }

    //inicia asyncatask
    public void execute() throws UnsupportedEncodingException {
        iDecode.onLoadStart();
        new calculoresposta().execute();

    }


    private class calculoresposta extends AsyncTask<Void, Void, List<String> >{

        @Override
        protected List<String> doInBackground(Void... params) {

            listarespostas.clear();

            //mapeando posicao em pixels na linha 1 , adicionar 20 para mudar de linha
            //tamanho do circulo = 11
            //posicao 0 = eixo x, 1 = eixo y
            posicaoa[0] = 42;
            posicaoa[1] = 4;

            posicaob[0] = 59;
            posicaob[1] = 4;

            posicaoc[0] = 79;
            posicaoc[1] = 4;

            posicaod[0] = 97;
            posicaod[1] = 4;

            posicaoe[0] = 115;
            posicaoe[1] = 4;

            int npergunta = 0;

            //for coluna
            for (int c = 0; c < 4; c++) {

                //for linha
                for (int v = 0; v < 19; v++) {

                    //a
                    for (int ey = posicaoa[1]; ey < posicaoa[1] + 12; ey++) {


                        for (int ex = posicaoa[0]; ex < posicaoa[0] + 12; ex++) {

                            int pixelColor = bm.getPixel(ex, ey);
                            //conta preto
                            if (pixelColor == Color.BLACK) {

                                contadorpintado++;


                                if (contadorpintado >= valorpintado) {
                                    p1 = "a";
                                }

                            }

                        }


                    }


                    //b
                    contadorpintado = 0;
                    for (int ey = posicaob[1]; ey < posicaob[1] + 12; ey++) {


                        for (int ex = posicaob[0]; ex < posicaob[0] + 12; ex++) {


                            int pixelColor = bm.getPixel(ex, ey);
                            //conta preto
                            if (pixelColor == Color.BLACK) {

                                contadorpintado++;


                                if (contadorpintado >= valorpintado) {
                                    p1 = "b";
                                }

                            }

                        }


                    }

                    //c
                    contadorpintado = 0;
                    for (int ey = posicaoc[1]; ey < posicaoc[1] + 12; ey++) {


                        for (int ex = posicaoc[0]; ex < posicaoc[0] + 12; ex++) {


                            int pixelColor = bm.getPixel(ex, ey);
                            //conta preto
                            if (pixelColor == Color.BLACK) {

                                contadorpintado++;


                                if (contadorpintado >= valorpintado) {
                                    p1 = "c";
                                }

                            }

                        }

                    }

                    //d
                    contadorpintado = 0;
                    for (int ey = posicaod[1]; ey < posicaod[1] + 12; ey++) {


                        for (int ex = posicaod[0]; ex < posicaod[0] + 12; ex++) {


                            int pixelColor = bm.getPixel(ex, ey);
                            //conta preto
                            if (pixelColor == Color.BLACK) {

                                contadorpintado++;


                                if (contadorpintado >= valorpintado) {
                                    p1 = "d";
                                }

                            }

                        }

                    }


                    //e
                    contadorpintado = 0;
                    for (int ey = posicaoe[1]; ey < posicaoe[1] + 12; ey++) {


                        for (int ex = posicaoe[0]; ex < posicaoe[0] + 12; ex++) {


                            int pixelColor = bm.getPixel(ex, ey);
                            //conta preto
                            if (pixelColor == Color.BLACK) {

                                contadorpintado++;


                                if (contadorpintado >= valorpintado) {
                                    p1 = "e";
                                }

                            }

                        }

                    }

                    //variacao de eixo y

                    posicaoa[1] += 20;
                    posicaob[1] += 20;
                    posicaoc[1] += 20;
                    posicaod[1] += 20;
                    posicaoe[1] += 20;

                    npergunta++;
                    if (npergunta <= 75)
                        listarespostas.add("Pergunta " + npergunta + " : " + p1);

                }

                //somando eixo x para mudar de coluna
                posicaoa[0] += 140;
                posicaob[0] += 140;
                posicaoc[0] += 140;
                posicaod[0] += 140;
                posicaoe[0] += 140;


                //reseta posicao inicial eixo y
                posicaoa[1] = 4;
                posicaob[1] = 4;
                posicaoc[1] = 4;
                posicaod[1] = 4;
                posicaoe[1] = 4;

            }

            return listarespostas;
        }
        @Override
        protected void onPostExecute(List<String> Lista){
           //retorna lista
            iDecode.Decode(Lista);
        }

    }
}
