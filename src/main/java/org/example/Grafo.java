package org.example;

import org.example.Excepciones.GrafoException;
import org.example.interfaz.IGrafo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<String> vertices;

    // banderas para saber si es poderado o dirigido
    private boolean esDirigido;
    private boolean esPonderado;

    // matriz la matriz almacena también los pesos (si es ponderado) por eso double.
    private double [][] matriz;


    // constructor
    // para crear la matriz, se toma de referencia el tamaño de la lista de verticess
    public Grafo(boolean dirigido, boolean ponderado){
        this.esDirigido = dirigido;
        this.esPonderado = ponderado;
        this.vertices = new ArrayList<>();
        this.matriz = new double[0][0];
    }

    /**
     *  Inserta un vertice a la matriz, esto conlleva incrementar el
     *  tamaño de la matriz.
     *  El agregar el vertice implica modificar y reeemplazar la matriz.
     * @param nombre el nombre del vértice
     */
    @Override
    public void insertarVertice(String nombre) {

        //  Verificar que no se repita la vertice
        if(vertices.contains(nombre)){
            throw new GrafoException("Vertice repetida.");
        }

        // sacar el tamaño antiguo
        int tamViejo = vertices.size();

        // Ahora sí a insertar
        this.vertices.add(nombre);

        // --- Actualizar el tamaño de la matriz ---

        // sacar el tamaño de la lista
        int tamLista = vertices.size();
        // Crear matriz nueva
        double [][] matrizNueva = new double[tamLista][tamLista];

        // llenar la matriz nueva de infinitos
        for (double[] casillas : matrizNueva){
            Arrays.fill(casillas, NO_ARISTAS);
        }

        // copiar elementos

        // primer for recorre filas
        for(int fila = 0; fila<tamViejo ; fila++){
            // recorre las columnas
            for(int col = 0; col<tamViejo; col++){
                // hace el copiado
                matrizNueva[fila][col] = matriz[fila][col];
            }
        }
        this.matriz = matrizNueva;
    }

    /**
     *  Elimina un vertice de la lista, por consiguiente,
     *  también de la matriz. El eliminarla implica un cambio en las
     *  filas y columnas
     * @param nombre es el nombre de la vertice a eliminar.
     */
    @Override
    public void eliminarVertice(String nombre) {
        int idx = vertices.indexOf(nombre);
        if (idx == -1) {
            throw new GrafoException("No existe el vértice.");
        }

        int tamViejo = vertices.size();
        double[][] matrizNueva = new double[tamViejo - 1][tamViejo - 1];

        int filaNueva = 0;
        for (int fila = 0; fila < tamViejo; fila++) {
            if (fila == idx){
                continue;          // saltar la fila eliminada
            }
            int colNueva = 0;
            for (int col = 0; col < tamViejo; col++) {
                if (col == idx){
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
     * @param origen Es el vertice origen del que sale la conexión
     * @param destino Es el vertice destino en el que llega la conexión
     * @param peso Es el costo asignado a esa arista
     */
    @Override
    public void insertarArista(String origen, String destino, double peso) {

        // extracción de indices
        int inicio = vertices.indexOf(origen);
        int fin = vertices.indexOf(destino);

        // validar si existe ese vertice
        if(inicio == -1 || fin == -1){
            throw new GrafoException("Uno de los vertices no existe.");
        }

        // Si no es ponderado entonces el peso será de 1
        if(!esPonderado){
            peso = 1.0;
        }
        if (Double.isNaN(peso) || Double.isInfinite(peso)) {
            throw new GrafoException("Peso inválido.");
        }

        // Crear arista para los vertices
        matriz[inicio][fin] = peso;

        //Si es bidireccional entonces también de fin a inicio se hace la relación
        if(!esDirigido){
            matriz[fin][inicio] = peso;
        }
    }

    /**
     * Al eliminar un una arista implica que se reemplace por un infinito
     * de tal forma que para llegar de punto a a b es infinito
     * por tanto no hay posibilidad
     *
     * Se hace la distinción de si es dirigido o no dirigido.
     *
     * @param origen es de donde sale la relación.
     * @param destino es a quien llega la relación.
     */
    @Override
    public void eliminarArista(String origen, String destino) {

        // extracción de indices
        int inicio = vertices.indexOf(origen);
        int fin = vertices.indexOf(destino);

        // validar si existe ese vertice
        if(inicio == -1 || fin == -1){
            throw new GrafoException("Uno de los vertices no existe.");
        }

        // dejar un peso infinito simulando que no hay conexión
        matriz[inicio][fin] = NO_ARISTAS;
        //Si es bidireccional entonces también de fin a inicio se hace la relación
        if(!esDirigido){
            matriz[fin][inicio] = NO_ARISTAS;
        }
    }

    /**
     * Actualiza el peso de una arista ponderada
     * Para ello se saca el indice de ese origen y ese destino, de tal forma que si es
     * no dirigido, se reemplaza de columna fila, pero si es dirigido, entonces solo de
     * fila columna
     * @param origen es de donde sale la relación o en otras palaabras, la fila
     * @param destino representa la columna (idealmente)
     * @param peso es el costo por cruzar esa arista
     */
    @Override
    public void actualizarPeso(String origen, String destino, double peso) {
        if (!esPonderado) {
            throw new GrafoException("El grafo no es ponderado.");
        }
        // extracción de indices
        int inicio = vertices.indexOf(origen);
        int fin = vertices.indexOf(destino);

        // validar si existe ese vertice
        if(inicio == -1 || fin == -1){
            throw new GrafoException("Uno de los vertices no existe.");
        }

        // Actualizar arista para los vertices
        if(matriz[inicio][fin] == NO_ARISTAS){
            throw new GrafoException("No existe arista.");
        }
        matriz[inicio][fin] = peso;
        //Si es bidireccional entonces también de fin a inicio se hace la relación
        if(!esDirigido){
            matriz[fin][inicio] = peso;
        }
    }

    /**
     * Es la cantidad de aristas que tiene de salida, es decir de tal fila
     * cuantas columnas tengo
     *  Horizontal
     * Se recorre la fila en específico de ese origen
     * @param vertice es el vertice del cuál se busca
     * @return regresa la cantidad de salidas de ese vertice
     */
    @Override
    public int gradoSalida(String vertice) {

        // posición de la fila a iterar
        int filaPos = vertices.indexOf(vertice);

        if(filaPos == -1){
            throw new  GrafoException("No existe el vértice.");
        }

        // recorrer esa fila y tener contador
        int degPos = 0;
        for(int col = 0; col <vertices.size(); col++){
            if(matriz[filaPos][col] != NO_ARISTAS){
                degPos++;
            }

        }
        return degPos;
    }

    /**
     * Representa el vertice final, es decir, en este caso se inspecciona la columna
     *  vertical
     * @param vertice vertice buscado
     * @return la cantidad de entradas que tiene ese vertice en concreto
     */
    @Override
    public int gradoEntrada(String vertice) {
        // posición de la columna a iterar
        int colPos = vertices.indexOf(vertice);

        if(colPos == -1){
            throw new  GrafoException("No existe el vértice.");
        }

        // recorrer esa fila y tener contador
        int degNeg = 0;
        for(int fila = 0; fila <vertices.size(); fila++){
            if(matriz[fila][colPos] != NO_ARISTAS){
                degNeg++;
            }

        }
        return degNeg;
    }

    /**
     * Saca el nombre de las vertices que tienen conexión con ese vertice en
     * específico.
     * @param vertice es el vertice del cual buscamos los vecinos
     * @return regresa la lista de vecinos que tiene.
     */
    @Override
    public List<String> adyacentes(String vertice) {

        int filaPos = vertices.indexOf(vertice);

        if(filaPos == -1){
            throw new  GrafoException("No existe la vertice.");
        }

        List<String> lista = new ArrayList<>();

        for (int col = 0; col <vertices.size(); col++){
            if(matriz[filaPos][col] != NO_ARISTAS){
                lista.add(vertices.get(col));
            }
        }
        return lista;
    }

    /**
     * Imprime la matriz, solo llama a un tostring que se encarga de ello
     * @return
     */
    @Override
    public String imprimirMatriz() {
        return toString();
    }

    /**
     * Regresa la cantidad de vertices existentes
     * @return la cantidad
     */
    @Override
    public int numVertices() {
        return vertices.size();
    }

    /**
     * NUmero de aristas existentes,
     *
     * Si es dirigido recorre toda
     *
     * si es no dirigido solo cuenta una parte para no duplicar
     * la cuenta
     * @return
     */
    @Override
    public int numAristas() {
        if(vertices.size() == 0){
            return 0;
        }
        int cantidadAristas = 0;

        // Decidir un camino
        if (esDirigido) {
            // si es dirigido, cuento todas las casillas.
            for (double[] fila : matriz){
                // recorre cada columna de la fila
                for(double valor : fila){
                    // validar si no es infinito
                    if(valor != NO_ARISTAS){
                        cantidadAristas++;
                    }
                }
            }
        }else{
            // contar solo una cara del espejo
            for(int fila = 0; fila < vertices.size(); fila++){
                for (int columna = fila; columna < vertices.size(); columna++ ){
                    if(matriz[fila][columna] != NO_ARISTAS){
                        cantidadAristas++;
                    }
                }
            }
        }
        return cantidadAristas;
    }

    /**
     * Formateo de impresión de la matriz
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // 1. Encabezado con los nombres de los vértices 🏷
        sb.append("   ");
        for (String v : vertices) {
            sb.append(v).append("  ");
        }
        sb.append("\n");

        // 2. Recorrido de la matriz (Fila x Columna)
        for (int i = 0; i < vertices.size(); i++) {
            sb.append(vertices.get(i)).append(" "); // Imprime el vértice origen

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
}

