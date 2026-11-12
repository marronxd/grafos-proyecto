package org.example.pantalla;

import org.example.Excepciones.GrafoException;
import org.example.clases.Grafo;
import org.example.clases.Vertice;
import org.example.interfaz.IGrafo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 * Pantalla de terminal para trabajar con varios grafos.
 * Todo el acceso al grafo se hace a través de la interfaz IGrafo.
 *
 * autor aaron xavier burciaga alcantar 262788
 */
public class MenuGrafos {

    /**
     * IGrafo no expone si el grafo es dirigido o ponderado,
     * así que se guardan las banderas junto con el grafo.
     */
    private static class EntradaGrafo {
        private final String nombre;
        private final IGrafo grafo;
        private final boolean dirigido;
        private final boolean ponderado;

        EntradaGrafo(String nombre, IGrafo grafo, boolean dirigido, boolean ponderado) {
            this.nombre = nombre;
            this.grafo = grafo;
            this.dirigido = dirigido;
            this.ponderado = ponderado;
        }

        String descripcion() {
            return nombre + " (" + (dirigido ? "dirigido" : "no dirigido") + ", "
                    + (ponderado ? "ponderado" : "no ponderado") + ")";
        }
    }

    private final Scanner sc = new Scanner(System.in);
    private final List<EntradaGrafo> grafos = new ArrayList<>();
    private EntradaGrafo activo = null;

    // ------------------------------------------------------------------
    // Bucle principal
    // ------------------------------------------------------------------

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            try {
                ejecutar(opcion);
            } catch (GrafoException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
        System.out.println("Hasta luego.");
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("========== GRAFOS (matriz de adyacencia) ==========");
        System.out.println("Grafo activo: " + (activo == null ? "ninguno" : activo.descripcion()));
        System.out.println("---------------------------------------------------");
        System.out.println(" 1. Crear grafo");
        System.out.println(" 2. Seleccionar grafo");
        System.out.println(" 3. Ver lista de grafos");
        System.out.println(" 4. Insertar vértice");
        System.out.println(" 5. Eliminar vértice");
        System.out.println(" 6. Insertar arista");
        System.out.println(" 7. Eliminar arista");
        System.out.println(" 8. Actualizar peso de arista");
        System.out.println(" 9. Imprimir grado de vértice");
        System.out.println("10. Imprimir vértices adyacentes");
        System.out.println("11. Imprimir matriz de adyacencia");
        System.out.println("12. Imprimir número de vértices y aristas");
        System.out.println("13. Búsqueda por anchura (BFS");
        System.out.println("14. Búsqueda por profundidad (DFS)");
        System.out.println("15. Ordenamiento Topológico.");
        System.out.println(" 0. Salir");
    }

    private void ejecutar(int opcion) {
        // Las opciones 4 a 12 necesitan un grafo activo
        if (opcion >= 4 && opcion <= 14 && activo == null) {
            System.out.println("Primero crea o selecciona un grafo (opciones 1 o 2).");
            return;
        }

        switch (opcion) {
            case 1 -> crearGrafo();
            case 2 -> seleccionarGrafo();
            case 3 -> listarGrafos();
            case 4 -> insertarVertice();
            case 5 -> eliminarVertice();
            case 6 -> insertarArista();
            case 7 -> eliminarArista();
            case 8 -> actualizarPeso();
            case 9 -> imprimirGrado();
            case 10 -> imprimirAdyacentes();
            case 11 -> System.out.println(activo.grafo.imprimirMatriz());
            case 12 -> imprimirConteos();
            case 13 -> ejecutarBusquedaAnchura();
            case 14 -> ejecutarBusquedaProfundidad();
            case 15 -> ejecutarOrdenTopologico();
            case 0 -> { }
            default -> System.out.println("Opción no válida.");
        }
    }

    // ------------------------------------------------------------------
    // Gestión de la lista de grafos
    // ------------------------------------------------------------------

    private void crearGrafo() {
        String nombre;
        while (true) {
            nombre = leerTexto("Nombre del grafo: ");
            if (!existeNombre(nombre)) {
                break;
            }
            System.out.println("Ya existe un grafo con ese nombre.");
        }
        boolean dirigido = leerSiNo("¿Es dirigido?");
        boolean ponderado = leerSiNo("¿Es ponderado?");

        EntradaGrafo nuevo = new EntradaGrafo(nombre, new Grafo(dirigido, ponderado), dirigido, ponderado);
        grafos.add(nuevo);
        activo = nuevo;
        System.out.println("Grafo creado y seleccionado: " + nuevo.descripcion());
    }

    private void seleccionarGrafo() {
        if (grafos.isEmpty()) {
            System.out.println("Todavía no hay grafos creados.");
            return;
        }
        listarGrafos();
        int num;
        while (true) {
            num = leerEntero("Número del grafo (0 para cancelar): ");
            if (num >= 0 && num <= grafos.size()) {
                break;
            }
            System.out.println("Número fuera de rango.");
        }
        if (num == 0) {
            return;
        }
        activo = grafos.get(num - 1);
        System.out.println("Grafo activo: " + activo.descripcion());
    }

    private void listarGrafos() {
        if (grafos.isEmpty()) {
            System.out.println("Todavía no hay grafos creados.");
            return;
        }
        System.out.println("Grafos creados (* = activo):");
        for (int i = 0; i < grafos.size(); i++) {
            EntradaGrafo e = grafos.get(i);
            System.out.println((e == activo ? "* " : "  ") + (i + 1) + ". " + e.descripcion()
                    + " - " + e.grafo.numVertices() + " vértices, " + e.grafo.numAristas() + " aristas");
        }
    }

    private boolean existeNombre(String nombre) {
        for (EntradaGrafo e : grafos) {
            if (e.nombre.equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Operaciones sobre el grafo activo
    // ------------------------------------------------------------------

    private void insertarVertice() {
        Vertice vertice = leerVertice("Nombre del vértice: ");
        activo.grafo.insertarVertice(vertice);
        System.out.println("Vértice insertado.");
    }

    private void eliminarVertice() {
        Vertice vertice = leerVertice("Nombre del vértice a eliminar: ");
        activo.grafo.eliminarVertice(vertice);
        System.out.println("Vértice eliminado.");
    }

    private void insertarArista() {
        Vertice origen = leerVertice("Vértice origen: ");
        Vertice destino = leerVertice("Vértice destino: ");
        double peso = 1.0;
        if (activo.ponderado) {
            peso = leerDouble("Peso de la arista: ");
        }
        activo.grafo.insertarArista(origen, destino, peso);
        System.out.println("Arista insertada.");
    }

    private void eliminarArista() {
        Vertice origen = leerVertice("Vértice origen: ");
        Vertice destino = leerVertice("Vértice destino: ");
        activo.grafo.eliminarArista(origen, destino);
        System.out.println("Arista eliminada.");
    }

    private void actualizarPeso() {
        if (!activo.ponderado) {
            System.out.println("El grafo activo no es ponderado.");
            return;
        }
        Vertice origen = leerVertice("Vértice origen: ");
        Vertice destino = leerVertice("Vértice destino: ");
        double peso = leerDouble("Nuevo peso: ");
        activo.grafo.actualizarPeso(origen, destino, peso);
        System.out.println("Peso actualizado.");
    }

    private void imprimirGrado() {
        Vertice vertice = leerVertice("Vértice: ");
        if (activo.dirigido) {
            System.out.println("Grado de entrada: " + activo.grafo.gradoEntrada(vertice));
            System.out.println("Grado de salida:  " + activo.grafo.gradoSalida(vertice));
        } else {
            // En no dirigidos la entrada y la salida son iguales
            System.out.println("Grado: " + activo.grafo.gradoSalida(vertice));
        }
    }

    private void imprimirAdyacentes() {
        Vertice vertice = leerVertice("Vértice: ");
        List<Vertice> ady = activo.grafo.adyacentes(vertice);
        if (ady.isEmpty()) {
            System.out.println("Adyacentes de " + vertice.getNombre() + ": (ninguno)");
        } else {
            String nombres = ady.stream()
                    .map(Vertice::getNombre)
                    .collect(Collectors.joining(", "));
            System.out.println("Adyacentes de " + vertice.getNombre() + ": " + nombres);
        }
    }

    private void imprimirConteos() {
        System.out.println("Número de vértices: " + activo.grafo.numVertices());
        System.out.println("Número de aristas:  " + activo.grafo.numAristas());
    }

    // ------------------------------------------------------------------
    // Búsquedas BFS y DFS
    // ------------------------------------------------------------------

    private void ejecutarBusquedaAnchura() {
        Vertice origen = leerVertice("Vértice origen del recorrido: ");
        List<Vertice> orden = activo.grafo.busquedaAnchura(origen);

        System.out.println("Orden de visita (BFS): " + nombresEnOrden(orden));
        System.out.println();
        System.out.printf("%-10s %-8s %-12s %-9s%n", "Vértice", "Color", "Predecesor", "Distancia");
        for (Vertice v : orden) {
            System.out.printf("%-10s %-8s %-12s %-9d%n",
                    v.getNombre(), v.getColor(), nombrePredecesor(v), v.getDistancia());
        }
    }

    private void ejecutarBusquedaProfundidad() {
        Vertice origen = leerVertice("Vértice origen del recorrido: ");
        List<Vertice> orden = activo.grafo.busquedaProfundidad(origen);

        System.out.println("Orden de visita (DFS): " + nombresEnOrden(orden));
        System.out.println();
        System.out.printf("%-10s %-8s %-12s %-8s %-8s%n",
                "Vértice", "Color", "Predecesor", "Entrada", "Salida");
        for (Vertice v : orden) {
            System.out.printf("%-10s %-8s %-12s %-8d %-8d%n",
                    v.getNombre(), v.getColor(), nombrePredecesor(v),
                    v.getTiempoEntrada(), v.getTiempoSalida());
        }
    }

    /** Une los nombres de los vértices en el orden recibido: A -> B -> C. */
    private String nombresEnOrden(List<Vertice> vertices) {
        return vertices.stream()
                .map(Vertice::getNombre)
                .collect(Collectors.joining(" -> "));
    }

    /** Nombre del predecesor o "-" si no tiene. */
    private String nombrePredecesor(Vertice v) {
        return v.getPredecesor() == null ? "-" : v.getPredecesor().getNombre();
    }
    // ------------------------------------------------------------------
    // Ordenamiento topológico
    // ------------------------------------------------------------------

    private void ejecutarOrdenTopologico(){
        System.out.println("\n--- ORDENAMIENTO TOPOLÓGICO ---");
        try {
            List<Vertice> orden = activo.grafo.ordenTopologico();

            System.out.print("Orden lineal: ");
            for (int i = 0; i < orden.size(); i++) {
                System.out.print(orden.get(i).getNombre());
                if (i < orden.size() - 1) {
                    System.out.print(" , ");
                }
            }
            System.out.println();
        } catch (GrafoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Lectura segura desde el teclado
    // ------------------------------------------------------------------

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero válido.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
                // parseDouble acepta "NaN" e "Infinity", que no sirven como peso
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    System.out.println("Peso inválido.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número válido.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("No puede estar vacío.");
        }
    }

    /** Lee un nombre desde el teclado y lo convierte en un Vertice. */
    private Vertice leerVertice(String mensaje) {
        return new Vertice(leerTexto(mensaje));
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String r = sc.nextLine().trim().toLowerCase();
            if (r.equals("s") || r.equals("si") || r.equals("sí")) {
                return true;
            }
            if (r.equals("n") || r.equals("no")) {
                return false;
            }
            System.out.println("Responde s o n.");
        }
    }
}