package org.example.interfaz;

import org.example.clases.Vertice;

import java.util.List;

/**
 * autor aaron xavier burciaga alcantar 262788
 */

public interface IGrafo  extends IBusquedaAnchura, IBusquedaProfundidad{


    /** Inserta un vértice y agrega una fila y una columna a la matriz. */
    void insertarVertice(Vertice vertice);

    /** Elimina un vértice y su fila y columna de la matriz. */
    void eliminarVertice(Vertice vertice);

    /** Inserta una arista; en grafos no ponderados el peso se ignora y se guarda 1. */
    void insertarArista(Vertice origen, Vertice destino, double peso);

    /** Elimina la arista entre origen y destino. */
    void eliminarArista(Vertice origen, Vertice destino);

    /** Actualiza el peso de una arista existente (solo grafos ponderados). */
    void actualizarPeso(Vertice origen, Vertice destino, double peso);

    /** Cantidad de aristas que salen del vértice. */
    int gradoSalida(Vertice vertice);

    /** Cantidad de aristas que llegan al vértice. */
    int gradoEntrada(Vertice vertice);

    /** Vértices a los que se puede llegar desde el vértice dado. */
    List<Vertice> adyacentes(Vertice vertice);

    /** Devuelve la matriz de adyacencia lista para imprimir. */
    String imprimirMatriz();

    /** Número de vértices del grafo. */
    int numVertices();

    /** Número de aristas del grafo. */
    int numAristas();
}
