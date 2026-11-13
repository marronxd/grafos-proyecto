package org.example.interfaz;

import org.example.clases.Vertice;

import java.util.List;

public interface IDAG {

    List<Vertice> ordenTopologico();

    List<Vertice> caminoMasCorto(Vertice origen);

    List<Vertice> dijkstra(Vertice origen);
}
