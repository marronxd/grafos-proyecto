package org.example.clases;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import org.example.Enums.Colores;

/**
 * Vertice que almacena los atributos para poder hacer las busquedas
 */
public class Vertice {
    // identificador de vertice
    private String nombre;

    // Para poder hacer la busqueda de anchura y profundidad
    private Colores color;
    private Vertice predecesor;
    private int distancia;
    private int tiempoEntrada;
    private int tiempoSalida;

    // atributo agregado para el costo mínimo
    private double costoMinimo;

    public Vertice(String nombre) {
        this.nombre = nombre;
        reiniciar();
    }

    /**
     * Deja los datos de búsqueda como al inicio. Se llama antes de cada BFS o DFS.
     */
    public void reiniciar() {
        this.color = Colores.BLANCO;
        this.predecesor = null;
        this.distancia = -1;
        this.tiempoEntrada = 0;
        this.tiempoSalida = 0;
        this.costoMinimo = Double.POSITIVE_INFINITY;
    }

    public String getNombre() {
        return nombre;
    }

    public Colores getColor() {
        return color;
    }

    public void setColor(Colores color) {
        this.color = color;
    }

    public Vertice getPredecesor() {
        return predecesor;
    }

    public void setPredecesor(Vertice predecesor) {
        this.predecesor = predecesor;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getTiempoEntrada() {
        return tiempoEntrada;
    }

    public void setTiempoEntrada(int tiempoEntrada) {
        this.tiempoEntrada = tiempoEntrada;
    }

    public int getTiempoSalida() {
        return tiempoSalida;
    }

    public void setTiempoSalida(int tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }

    public double getCostoMinimo() {
        return costoMinimo;
    }

    public void setCostoMinimo(double costoMinimo) {
        this.costoMinimo = costoMinimo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vertice)) {
            return false;
        }
        Vertice otro = (Vertice) o;
        return Objects.equals(nombre, otro.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}