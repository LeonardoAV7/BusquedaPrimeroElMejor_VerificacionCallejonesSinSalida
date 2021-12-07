package com.company;

import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        int matrizA[][] = {{0, 64, 432, 236, 403, 999999999, 999999999, 999999999, 999999999, 999999999},
                {64, 0, 321, 999999999,999999999,999999999, 999999999, 999999999, 999999999, 999999999},
                {432, 321, 0, 386, 362, 127, 268, 999999999, 999999999, 467},
                {236, 999999999, 386, 0, 182, 999999999, 999999999, 999999999, 999999999, 999999999},
                {403, 999999999, 362, 182, 0, 484, 312, 999999999, 999999999, 190},
                {999999999, 999999999, 127, 999999999, 484, 0, 141, 250, 999999999, 999999999},
                {999999999, 999999999, 268, 999999999, 312, 141, 0, 106, 335, 317},
                {999999999, 999999999, 999999999, 999999999, 999999999, 250, 106, 0, 318, 376},
                {999999999, 999999999, 999999999, 999999999, 999999999, 999999999, 335, 318, 0, 366},
                {999999999, 999999999, 467, 999999999, 190, 999999999, 317, 376, 366, 0}};

        String ciudades[] = {"Loja", "Zamora", "Macas", "Machala", "Guayaquil", "Puyo", "Latacunga", "Quito",
                "Esmeraldas", "Portoviejo"};

        int ciudadDestino = 0;
        int numCaminos = 4;
        ArrayList<String> soluciones = new ArrayList<String>();

        soluciones = algoritmo(matrizA, ciudadDestino, numCaminos);

        for (int i = 0; i < soluciones.size(); i++) {
            System.out.println("Solución " + (i+1) + ": ");
            char nodos[] = soluciones.get(i).toCharArray();
            String cadena = "";
            cadena += ciudades[Character.getNumericValue(nodos[0])];
            for (int j = 1; j < nodos.length; j++) {
                cadena += "-" + ciudades[Character.getNumericValue(nodos[j])];
            }
            int distancia = matrizA[ciudadDestino][Character.getNumericValue(nodos[0])];
            for (int j = 0; j < nodos.length - 1; j++) {
                distancia += matrizA[Character.getNumericValue(nodos[j])][Character.getNumericValue(nodos[j+1])];
            }
            cadena += "\t Con una distancia de: " + distancia + "km";
            System.out.println(cadena);
        }

    }

    public static ArrayList<String> algoritmo( int [][] matriz, int ciudadDestino, int numCaminos){

        int vertices = matriz.length;
        ArrayList<Integer> posicionesRecorridas = new ArrayList<Integer>();
        ArrayList<String> caminosEvitar = new ArrayList<String>();
        ArrayList<String> soluciones = new ArrayList<String>();
        int posicionInicial = ciudadDestino;

        // recorremos un bucle tantas veces como ciudades existan en la matriz
        for (int i = 0; i < vertices; i++) {

            int caminoMenor = caminoMenor(matriz[posicionInicial], posicionInicial, posicionesRecorridas, caminosEvitar, ciudadDestino);
            // en caso de encontrarnos con un callejón sin salida, regresamos una posición y añadimos la ruta sin
            // salida a caminosEvitar. En caso de que si encuentre una ruta se guarda el nodo con la menor ruta y
            // se lo establece como nueva posicion inicial, para apartir de el buscar el nuevo camino mas corto
            if(caminoMenor == 999999999){

                String camino = "";
                for (int j = 0; j <= posicionesRecorridas.size() - 1; j++) {
                    camino += posicionesRecorridas.get(j);
                }
                caminosEvitar.add(camino);

                posicionesRecorridas.remove(posicionesRecorridas.size() - 1);
                posicionInicial = posicionesRecorridas.get(posicionesRecorridas.size() - 1);
                i = i - 2;

            }else{

                posicionesRecorridas.add(caminoMenor);
                posicionInicial = caminoMenor;

            }
            // Cuando encontramos una solución la almacenamos en nuestra lista, y guardamos esa ruta en
            // en caminosEvitar, para que nos encuentre otra solución
            if(posicionesRecorridas.size() == 10 ){

                String camino = "";
                for (int j = 0; j < posicionesRecorridas.size() ; j++) {
                    camino += posicionesRecorridas.get(j);
                }

                soluciones.add(camino);
                caminosEvitar.add(camino);
                posicionesRecorridas.remove(posicionesRecorridas.size() - 1);
                posicionInicial = posicionesRecorridas.get(posicionesRecorridas.size() - 1);
                i = i - 2;
            }
            // cuando tenemos el número de soluciones deseadas salimos del bucle
            if(soluciones.size() == numCaminos) break;

        }

        return soluciones;

    }

    // Método encargado de elegir el camino con menor distancia desde un nodo determinado
    public static int caminoMenor( int [] distancias, int posicionInicial, ArrayList<Integer> posicionesRecorridas, ArrayList<String> caminosEvitar, int ciudadDestino){

        int vertices = distancias.length;
        int menor = 999999999;
        int caminoMenor = 999999999;
        boolean recorridoCompleto = (posicionesRecorridas.size() == vertices -1);
        boolean condicion;
        // definimos el camino recorrido como una cadena, para validar luego los caminos a evitar
        String caminoPrevio = "";
        for (int i = 0; i < posicionesRecorridas.size(); i++) {
            caminoPrevio += posicionesRecorridas.get(i);
        }
        // recorremos los posibles caminos que se tienen desde un nodo determinado
        for (int i = 0; i < vertices; i++) {
            // validamos sin ya se recorrieron las 9 ciudades, para que ahora sea posible llegar a la ciudad final
            if(i == ciudadDestino){
                condicion = recorridoCompleto;
            }else{
                condicion = true;
            }
            // buscamos la distancia menor desde nuestro nodo, evitando las posiciones ya recorridas,
            // el nodo de partida, la ciudad final a la que se debe llegar los nodos incalcanzables y
            // las rutas que se han establecido para evitar
            if (!posicionesRecorridas.contains(i) && distancias[i] != 999999999 && posicionInicial != i && condicion) {
                if(distancias[i] < menor) {
                    String caminoPosible = caminoPrevio + i;
                    if(!caminosEvitar.contains(caminoPosible)){
                        menor = distancias[i];
                        caminoMenor = i;
                    }
                }
            }
        }
        // retornamos el nodo que está a la distancia más corta
        return caminoMenor;
    }


}
