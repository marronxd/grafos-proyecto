package org.example.interfaz;

import java.util.List;

/**
 * autor aaron xavier burciaga alcantar 262788
 */

public interface IGrafo {


    /** Inserta un vértice y agrega una fila y una columna a la matriz. */
    void insertarVertice(String nombre);

    /** Elimina un vértice y su fila y columna de la matriz. */
    void eliminarVertice(String nombre);

    /** Inserta una arista; en grafos no ponderados el peso se ignora y se guarda 1. */
    void insertarArista(String origen, String destino, double peso);

    /** Elimina la arista entre origen y destino. */
    void eliminarArista(String origen, String destino);

    /** Actualiza el peso de una arista existente (solo grafos ponderados). */
    void actualizarPeso(String origen, String destino, double peso);

    /** Cantidad de aristas que salen del vértice. */
    int gradoSalida(String vertice);

    /** Cantidad de aristas que llegan al vértice. */
    int gradoEntrada(String vertice);

    /** Nombres de los vértices a los que se puede llegar desde el vértice dado. */
    List<String> adyacentes(String vertice);

    /** Devuelve la matriz de adyacencia lista para imprimir. */
    String imprimirMatriz();

    /** Número de vértices del grafo. */
    int numVertices();

    /** Número de aristas del grafo. */
    int numAristas();
}
