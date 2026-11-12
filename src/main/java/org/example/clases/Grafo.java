package org.example.clases;

import org.example.Enums.Colores;
import org.example.Excepciones.GrafoException;
import org.example.interfaz.IGrafo;

import java.util.*;

/**
 *  Clase grafo, representa un grafo.
 *
 *  Tiene atributos representativos de una matriz de adyacencia.
 *
 *  Ofrece diferentes métodos que permiten saber el estado de la matriz
 *  autor aaron xavier burciaga alcantar 262788
 */

public class Grafo implements IGrafo {

    // Es infinito porque si no hay acceso o no hay conexxión,
    // entonces ir de a a b cuesta infinito
    private static final double NO_ARISTAS = Double.POSITIVE_INFINITY;

    private List<Vertice> vertices;

    // banderas para saber si es poderado o dirigido
    private boolean esDirigido;
    private boolean esPonderado;

    // matriz la matriz almacena también los pesos (si es ponderado) por eso double.
    private double[][] matriz;

    // constructor
    // para crear la matriz, se toma de referencia el tamaño de la lista de verticess
    public Grafo(boolean dirigido, boolean ponderado) {
        this.esDirigido = dirigido;
        this.esPonderado = ponderado;
        this.vertices = new ArrayList<>();
        this.matriz = new double[0][0];
    }

    /**
     * Inserta un vertice a la matriz, esto conlleva incrementar el
     * tamaño de la matriz.
     * El agregar el vertice implica modificar y reeemplazar la matriz.
     *
     * @param vertice el vértice a insertar
     */
    @Override
    public void insertarVertice(Vertice vertice) {

        //  Verificar que no se repita la vertice
        if (vertices.contains(vertice)) {
            throw new GrafoException("Vertice repetida.");
        }

        // sacar el tamaño antiguo
        int tamViejo = vertices.size();

        // Ahora sí a insertar
        this.vertices.add(vertice);

        // --- Actualizar el tamaño de la matriz ---

        // sacar el tamaño de la lista
        int tamLista = vertices.size();
        // Crear matriz nueva
        double[][] matrizNueva = new double[tamLista][tamLista];

        // llenar la matriz nueva de infinitos
        for (double[] casillas : matrizNueva) {
            Arrays.fill(casillas, NO_ARISTAS);
        }

        // copiar elementos

        // primer for recorre filas
        for (int fila = 0; fila < tamViejo; fila++) {
            // recorre las columnas
            for (int col = 0; col < tamViejo; col++) {
                // hace el copiado
                matrizNueva[fila][col] = matriz[fila][col];
            }
        }
        this.matriz = matrizNueva;
    }

    /**
     * Elimina un vertice de la lista, por consiguiente,
     * también de la matriz. El eliminarla implica un cambio en las
     * filas y columnas
     *
     * @param vertice es la vertice a eliminar.
     */
    @Override
    public void eliminarVertice(Vertice vertice) {
        int idx = vertices.indexOf(vertice);
        if (idx == -1) {
            throw new GrafoException("No existe el vértice.");
        }

        int tamViejo = vertices.size();
        double[][] matrizNueva = new double[tamViejo - 1][tamViejo - 1];

        int filaNueva = 0;
        for (int fila = 0; fila < tamViejo; fila++) {
            if (fila == idx) {
                continue;          // saltar la fila eliminada
            }
            int colNueva = 0;
            for (int col = 0; col < tamViejo; col++) {
                if (col == idx) {
                    continue;       // saltar la columna eliminada
                }
                matrizNueva[filaNueva][colNueva] = matriz[fila][col];
                colNueva++;
            }
            filaNueva++;
        }

        vertices.remove(idx);   // idx es int: borra por posición
        this.matriz = matrizNueva;
    }

    /**
     * Agrega una arista, en caso de ser no ponderado, entonces el peso base es de 1
     * En caso de ser ponderado el peso es correspondiente a la entrada.
     *
     * @param origen  Es el vertice origen del que sale la conexión
     * @param destino Es el vertice destino en el que llega la conexión
     * @param peso    Es el costo asignado a esa arista
     */
    @Override
    public void insertarArista(Vertice origen, Vertice destino, double peso) {

        // extracción de indices
        int inicio = vertices.indexOf(origen);
        int fin = vertices.indexOf(destino);

        // validar si existe ese vertice
        if (inicio == -1 || fin == -1) {
            throw new GrafoException("Uno de los vertices no existe.");
        }

        // Si no es ponderado entonces el peso será de 1
        if (!esPonderado) {
            peso = 1.0;
        }
        if (Double.isNaN(peso) || Double.isInfinite(peso)) {
            throw new GrafoException("Peso inválido.");
        }

        // Crear arista para los vertices
        matriz[inicio][fin] = peso;

        //Si es bidireccional entonces también de fin a inicio se hace la relación
        if (!esDirigido) {
            matriz[fin][inicio] = peso;
        }
    }

    /**
     * Al eliminar un una arista implica que se reemplace por un infinito
     * de tal forma que para llegar de punto a a b es infinito
     * por tanto no hay posibilidad
     * <p>
     * Se hace la distinción de si es dirigido o no dirigido.
     *
     * @param origen  es de donde sale la relación.
     * @param destino es a quien llega la relación.
     */
    @Override
    public void eliminarArista(Vertice origen, Vertice destino) {

        // extracción de indices
        int inicio = vertices.indexOf(origen);
        int fin = vertices.indexOf(destino);

        // validar si existe ese vertice
        if (inicio == -1 || fin == -1) {
            throw new GrafoException("Uno de los vertices no existe.");
        }

        // dejar un peso infinito simulando que no hay conexión
        matriz[inicio][fin] = NO_ARISTAS;
        //Si es bidireccional entonces también de fin a inicio se hace la relación
        if (!esDirigido) {
            matriz[fin][inicio] = NO_ARISTAS;
        }
    }

    /**
     * Actualiza el peso de una arista ponderada
     * Para ello se saca el indice de ese origen y ese destino, de tal forma que si es
     * no dirigido, se reemplaza de columna fila, pero si es dirigido, entonces solo de
     * fila columna
     *
     * @param origen  es de donde sale la relación o en otras palaabras, la fila
     * @param destino representa la columna (idealmente)
     * @param peso    es el costo por cruzar esa arista
     */
    @Override
    public void actualizarPeso(Vertice origen, Vertice destino, double peso) {
        if (!esPonderado) {
            throw new GrafoException("El grafo no es ponderado.");
        }
        // extracción de indices
        int inicio = vertices.indexOf(origen);
        int fin = vertices.indexOf(destino);

        // validar si existe ese vertice
        if (inicio == -1 || fin == -1) {
            throw new GrafoException("Uno de los vertices no existe.");
        }

        // Actualizar arista para los vertices
        if (matriz[inicio][fin] == NO_ARISTAS) {
            throw new GrafoException("No existe arista.");
        }
        matriz[inicio][fin] = peso;
        //Si es bidireccional entonces también de fin a inicio se hace la relación
        if (!esDirigido) {
            matriz[fin][inicio] = peso;
        }
    }

    /**
     * Es la cantidad de aristas que tiene de salida, es decir de tal fila
     * cuantas columnas tengo
     * Horizontal
     * Se recorre la fila en específico de ese origen
     *
     * @param vertice es el vertice del cuál se busca
     * @return regresa la cantidad de salidas de ese vertice
     */
    @Override
    public int gradoSalida(Vertice vertice) {

        // posición de la fila a iterar
        int filaPos = vertices.indexOf(vertice);

        if (filaPos == -1) {
            throw new GrafoException("No existe el vértice.");
        }

        // recorrer esa fila y tener contador
        int degPos = 0;
        for (int col = 0; col < vertices.size(); col++) {
            if (matriz[filaPos][col] != NO_ARISTAS) {
                degPos++;
            }

        }
        return degPos;
    }

    /**
     * Representa el vertice final, es decir, en este caso se inspecciona la columna
     * vertical
     *
     * @param vertice vertice buscado
     * @return la cantidad de entradas que tiene ese vertice en concreto
     */
    @Override
    public int gradoEntrada(Vertice vertice) {
        // posición de la columna a iterar
        int colPos = vertices.indexOf(vertice);

        if (colPos == -1) {
            throw new GrafoException("No existe el vértice.");
        }

        // recorrer esa fila y tener contador
        int degNeg = 0;
        for (int fila = 0; fila < vertices.size(); fila++) {
            if (matriz[fila][colPos] != NO_ARISTAS) {
                degNeg++;
            }

        }
        return degNeg;
    }

    /**
     * Saca las vertices que tienen conexión con ese vertice en
     * específico.
     *
     * @param vertice es el vertice del cual buscamos los vecinos
     * @return regresa la lista de vecinos que tiene.
     */
    @Override
    public List<Vertice> adyacentes(Vertice vertice) {

        int filaPos = vertices.indexOf(vertice);

        if (filaPos == -1) {
            throw new GrafoException("No existe la vertice.");
        }

        List<Vertice> lista = new ArrayList<>();

        for (int col = 0; col < vertices.size(); col++) {
            if (matriz[filaPos][col] != NO_ARISTAS) {
                lista.add(vertices.get(col));
            }
        }
        return lista;
    }

    /**
     * Imprime la matriz, solo llama a un tostring que se encarga de ello
     *
     * @return
     */
    @Override
    public String imprimirMatriz() {
        return toString();
    }

    /**
     * Regresa la cantidad de vertices existentes
     *
     * @return la cantidad
     */
    @Override
    public int numVertices() {
        return vertices.size();
    }

    /**
     * NUmero de aristas existentes,
     * <p>
     * Si es dirigido recorre toda
     * <p>
     * si es no dirigido solo cuenta una parte para no duplicar
     * la cuenta
     *
     * @return
     */
    @Override
    public int numAristas() {
        if (vertices.size() == 0) {
            return 0;
        }
        int cantidadAristas = 0;

        // Decidir un camino
        if (esDirigido) {
            // si es dirigido, cuento todas las casillas.
            for (double[] fila : matriz) {
                // recorre cada columna de la fila
                for (double valor : fila) {
                    // validar si no es infinito
                    if (valor != NO_ARISTAS) {
                        cantidadAristas++;
                    }
                }
            }
        } else {
            // contar solo una cara del espejo
            for (int fila = 0; fila < vertices.size(); fila++) {
                for (int columna = fila; columna < vertices.size(); columna++) {
                    if (matriz[fila][columna] != NO_ARISTAS) {
                        cantidadAristas++;
                    }
                }
            }
        }
        return cantidadAristas;
    }

    /**
     * Formateo de impresión de la matriz
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // 1. Encabezado con los nombres de los vértices 🏷
        sb.append("   ");
        for (Vertice v : vertices) {
            sb.append(v.getNombre()).append("  ");
        }
        sb.append("\n");

        // 2. Recorrido de la matriz (Fila x Columna)
        for (int i = 0; i < vertices.size(); i++) {
            sb.append(vertices.get(i).getNombre()).append(" "); // Imprime el vértice origen

            for (int j = 0; j < vertices.size(); j++) {
                if (matriz[i][j] == NO_ARISTAS) {
                    sb.append(" ∞ "); // Muestra un símbolo claro para sin conexión
                } else {
                    sb.append(String.format("%.2f ", matriz[i][j])); // Muestra el peso
                }
            }
            sb.append("\n"); // Salto de línea al terminar la fila
        }
        return sb.toString();
    }

    // --- MÉTODOS PARA LAS BUSQUEDAS BFS Y DFS ---


    /**
     * Realiza un recorrido en anchura (BFS - Breadth-First Search)
     * a partir de un vértice origen.
     * <p>
     * El algoritmo explora el grafo nivel por nivel desde el vértice inicial,
     * calculando la distancia mínima en número de aristas hacia todos los vértices
     * alcanzables y determinando el orden de visita. Utiliza una cola FIFO para
     * gestionar el orden de exploración y colores para controlar los estados.
     *
     * @param vertice El vértice inicial desde el cual comenzará la búsqueda en anchura.
     * @return Una lista de {@link Vertice} con los nodos alcanzables en el orden exacto en que fueron procesados.
     * @throws GrafoException Si el vértice especificado no existe en la lista del grafo.
     */
    @Override
    public  List<Vertice>  busquedaAnchura(Vertice vertice) {
        // Hago esto para trabajar en la misma instancia
        int posActual = vertices.indexOf(vertice);

        if (posActual == -1){
            throw new GrafoException("Vertice innexistente.");
        }

        // Es una instancia del arreglo vertices
        Vertice verticeActual = vertices.get(posActual);

        Queue<Vertice> cola;

        // Todos configuración default
        for(Vertice v : vertices){
            v.reiniciar();
        }

        // crear lista en la que se meterá el órden en el que fueron visitados
        List<Vertice> ordenVisita = new ArrayList<>();

        // setear ahora al vertice origen
        verticeActual.setColor(Colores.GRIS);
        verticeActual.setDistancia(0);
        verticeActual.setPredecesor(null);
        cola = new ArrayDeque<>();
        cola.add(verticeActual);

        // ahora sí para recorrer vecinos
        while(!cola.isEmpty()){
            Vertice u = cola.poll();

            // Se visitan los vecicnos de ese vértice
            for(Vertice v : adyacentes(u)){

                // si no se ha visitado
                if(v.getColor() == Colores.BLANCO){
                    v.setColor(Colores.GRIS);
                    v.setDistancia(u.getDistancia() + 1); // es la distancia del predecesor + 1
                    v.setPredecesor(u);
                    cola.add(v); // meter ese vecino a la cola para explorarlo después
                }
            }
            u.setColor(Colores.NEGRO);
            ordenVisita.add(u); // se van guardadno en el orden de visita
        }
        return ordenVisita;
    }

    /**
     * Realiza un recorrido en profundidad (DFS - Depth-First Search) en todo el grafo.
     *
     * Explora los vértices siguiendo cada rama hasta el límite antes de retroceder.
     * Inicia la búsqueda desde el vértice recibido como parámetro
     * y posteriormente recorre los vértices restantes no visitados, asegurando la
     * cobertura total de componentes no conexas o disconexas. Emplea un reloj global
     * administrado por un arreglo de tamaño unitario para registrar los marcas
     * de tiempo de descubrimiento (tiempoEntrada) y finalización (tiempoSalida).
     *
     * @param vertice El vértice prioritario para dar inicio al recorrido en profundidad.
     * @return Una lista de {@link Vertice} en el orden en que fueron descubiertos por primera vez.
     * @throws GrafoException Si el vértice especificado no existe en la lista del grafo.
     */
    @Override
    public List<Vertice> busquedaProfundidad(Vertice vertice) {

        // Para trabajar en una instancia del registro vertices
        int posActual = vertices.indexOf(vertice);
        if (posActual == -1) {
            throw new GrafoException("Vertice inexistente.");
        }
        Vertice verticeActual = vertices.get(posActual);

        for (Vertice v : vertices) {
            v.reiniciar();
        }

        List<Vertice> ordenVisita = new ArrayList<>();
        int[] tiempo = {0};   // contador global compartido

        // El origen por el que empieza
        dfsVisit(verticeActual, tiempo, ordenVisita);

        // Es para alcanzar cada rincón el arbol que no pudo explroarse
        for (Vertice v : vertices) {
            if (v.getColor() == Colores.BLANCO) {
                dfsVisit(v, tiempo, ordenVisita);
            }
        }
        return ordenVisita;
    }

    /**
     * Método auxiliar recursivo para explorar en profundidad a partir de un vértice
     * específico. Marca el vértice como descubierto (GRIS), incrementa el tiempo global
     * para establecer el, lo registra en la lista de visitas y llama recursivamente
     * a sí mismo para todos sus vecinos no visitados (BLANCOS).
     * Al finalizar el recorrido de su descendencia,
     * marca el vértice como procesado (NEGRO) y actualiza su {@code tiempoSalida}.
     *
     * @param vertice     El vértice que se está explorando actualmente.
     * @param tiempo      Arreglo de un solo elemento utilizado como envoltorio para simular el paso
     *                    por referencia del contador de tiempo global a través de la recursión.
     * @param ordenVisita Lista compartida donde se agregan los vértices en su orden de descubrimiento.
     */
    private void dfsVisit(Vertice vertice, int[] tiempo, List<Vertice> ordenVisita) {
        vertice.setColor(Colores.GRIS);
        vertice.setTiempoEntrada(++tiempo[0]);   // sube y asigna
        ordenVisita.add(vertice);                // se registra al descubrirlo

        for (Vertice v : adyacentes(vertice)) {
            if (v.getColor() == Colores.BLANCO) {
                v.setPredecesor(vertice);
                dfsVisit(v, tiempo, ordenVisita);
            }
        }

        vertice.setColor(Colores.NEGRO);
        vertice.setTiempoSalida(++tiempo[0]);    // el tiempo siguió corriendo mientras tanto
    }


    // -- NOVEDAD PARA ORDEN TOPOLÓGICO


    /**
     * Ordenamiento topológico, es decir, un orden lineal para los no acíclicos
     * @return
     */
    @Override
    public List<Vertice> ordenTopologico() {

        if(vertices.isEmpty()){
            throw new GrafoException("No hay vértices.");
        }

        if(!this.esDirigido){
            throw new GrafoException("No es un grafo dirigido.");
        }

        // CREACION DE LISTA EN ORDEN y NEXT
        List<Vertice> ordenLineal = new ArrayList<>();
        List<Vertice> siguiente = new ArrayList<>(); // gE = 0

        // PARA HACER MAS FACIL EL GUARDADO DEL VERTICE Y SU GE
        Map<Vertice, Integer> gradosEntrada = new HashMap<>();

        // PARA NO EXCLUIR PARTES DEL GRAFO
        for (Vertice v : vertices){
            int grado = gradoEntrada(v);
            gradosEntrada.put(v,grado);
            if(grado == 0){
                siguiente.add(v);
            }
        }

        // Siguiente funciona como el motor que hace avanzar el recorrido
        while(!siguiente.isEmpty()){

            Vertice u = siguiente.remove(0);

            ordenLineal.add(u);

            // este es para ir visitando los vecinos de un vertice
            // cada visita implica restarle un grado de entrada, significando
            //
            for(Vertice vecino : adyacentes(u)){

                // u ya salió, al vecino le falta predecesor menos.
                gradosEntrada.put(vecino, gradosEntrada.get(vecino) - 1);

                // si sus grados de entrada son 0, ya no le faltan vecinos
                // y se procesa.
                if(gradosEntrada.get(vecino) == 0){
                    siguiente.add(vecino);
                }
            }
        }
        // si el orden lineal tiene más vertices, quiere decir que se regresó
        // y guardó 2 veces un vértice
        if(ordenLineal.size() < vertices.size()){
            throw new GrafoException("El grafo tiene un ciclo: no existe orden topológico.");
        }
        return ordenLineal;
    }
}