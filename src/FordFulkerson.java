/**
 * Created by IntelliJ IDEA.
 * User: sergio
 * Date: 7/05/12
 * Time: 09:07 PM
 * To change this template use File | Settings | File Templates.
 */

public class FordFulkerson {

    private final boolean DEBUG = true;
    
    /* RED RESIDUAL */
    private int red_residual[][]; /*=
            {{0, 6, 5, 0, 0, 0},
             {0, 0, 0, 2, 0, 3},
             {0, 0, 0, 1, 2, 0},
             {0, 0, 0, 0, 9, 7},
             {0, 0, 0, 0, 0, 5},
             {0, 0, 0, 0, 0, 0}};*/

    /* RED DE FLUJO INICIAL */
    private int red_flujo[][];

    /* NODOS VISITADOS */
    private int visitados[][];

    /* CAMINO */
    private  int camino[];
    
    private int iterations;

    //int j = 0;
    private boolean existecamino = true;

    public FordFulkerson(int[][] red_residual) {
        this.red_residual = red_residual;
        this.red_flujo = zero2DMatrix(red_residual.length, red_residual[0].length);
        this.visitados = zero2DMatrix(red_residual.length, red_residual[0].length);
        this.camino = zeroMatrix(red_residual.length);
    }

    public void exec(){
        Ford_Fulkerson(red_residual, red_flujo, camino, visitados);
    }

    private void Ford_Fulkerson(int[][] rr, int[][] rf, int[] cam, int[][] visitados) {
        if( DEBUG ){
            System.out.println("Red Residual: ");
            imprime(rr);
            System.out.println("Red de Flujo: ");
            imprime(rf);
            System.out.println();
        }

        while (existecamino == true) {  //mientras exista un camino

            camino(rr, cam, visitados);  //buscamos un camino
            if( DEBUG ){
                System.out.println("Existe un camino: " + existecamino);
                System.out.println("Camino encontrado: ");
                imprimearreglo(camino);
                System.out.println();
            }
            if (existecamino == false) {
            } else {
                aumentaflujo(rr, rf, cam);   //aumentamos el flujo
                for (int k = 0; k < cam.length; k++) {
                    cam[k] = 0;          //limpieamos el camino
                }
                for (int x = 0; x < visitados.length; x++) {
                    for (int y = 0; y < visitados.length; y++) {
                        visitados[x][y] = 0;  //limpiamos la matriz de nodos visitados
                    }
                }
                if( DEBUG ) {
                    System.out.println("Red Residual:");
                    imprime(rr);
                    System.out.println("Red de Flujo:");
                    imprime(rf);
                    System.out.println("======================================================");
                }
            }
            iterations = getIterations() + 1;
        }
        if( DEBUG ){
            System.out.println("Iteraciones: " + getIterations());
        }
    }

    // método para aumentar el flujo dado un camino de la fuente al sink
    private void aumentaflujo(int[][] rr, int[][] rf, int[] cam) {
        int min = minimo(rr, cam);   //encontramos el flujo residual mínimo
        if( DEBUG ) System.out.println("minimo es: " + min);
        int c = 0;
        while (cam[c] != rr.length-1) {
            int d = c + 1;
            rr[cam[c]][cam[d]] -= min;   //modificamos la red residual
            rr[cam[d]][cam[c]] += min;
            rf[cam[c]][cam[d]] += min;   //modificamos la red de flujo
            rf[cam[d]][cam[c]] = -rf[cam[c]][cam[c + 1]];
            c++;
        }
    }

    // método para encontrar el flujo residual mínimo
    // dado un camino de la fuente al sink
    private int minimo(int[][] rr, int[] cam) {
        int c = 0;
        int aux = 0;
        int min = 1000000;
        while (cam[c] != rr.length-1) {
            aux = rr[cam[c]][cam[c + 1]];
            if (aux < min) {
                min = aux;
            }
            c++;
        }
        return min;
    }

    // método para encontrar un camino de aumento de la fuente al sink
    // regresa un arreglo con la secuencia de nodos si es que existe
    // un camino de aumento
    private void camino(int[][] rr, int[] cam, int[][] visitados) {
        int c = 0;    // fuente
        int d = rr.length-1;    // destino
        int j = 1;
        existecamino = false;
        int flag = 0;
        while (valido(rr[c], cam, visitados, c) != -1 && flag == 0) {//mientras exista un nodo sin
            cam[j] = valido(rr[c], cam, visitados, c);               //explorar a fondo
            if (cam[j] == d) { // si el camino ya llegó al sink
                flag = 1;
                existecamino = true;
            } else {
                c = cam[j];
            }
            j++;
        }
        if (posible(rr[0], visitados) == -1 || existecamino == true) {
            //si existen números en el renglón de la fuente pero ya
            //fueron visitados
        } else if (!buscar(rr.length-1, cam)) {  //si el camino encontrado no llegó al sink
            j--;
            visitados[cam[j - 1]][cam[j]] = 1;
            for (int k = 0; k < cam.length; k++) {
                cam[k] = 0;
            }
            camino(rr, cam, visitados);
        }
    }

    // método para determinar si un nodo es válido basándonos
    // en el camino recorrido hasta el momento y en la matriz
    // de nodos ya visitados
    private int valido(int[] r, int[] cam, int[][] visitados, int i) {//renglón, camino, matriz
        int c = 0;                                                       //de visitados y num de renglon
        while (buscar(c, cam) || visitados[i][c] == 1) {
            c = siguiente(r, c);
            if (c == -1) {
                return -1;
            }
        }
        return c;
    }

    // método para determinar si un nodo es una posibilidad
    // basándonos en la matriz de nodos visitados
    private int posible(int[] r, int[][] visitados) {
        int c = 0;
        while (r[c] == 0 || visitados[0][c] == 1) {
            c = siguiente(r, c);
            if (c == -1) {
                return -1;
            }
        }
        return c;
    }

    // método para obtener el siguiente posible nodo
    // dado un renglón
    private int siguiente(int[] r, int j) {
        j++;
        while (j < r.length) {
            if (r[j] != 0) {
                return j;
            } else {
                j++;
            }
        }
        return -1;
    }

    // método para buscar un nodo en el camino recorrido
    private boolean buscar(int j, int[] cam) {
        for (int k = 0; k < cam.length; k++) {
            if (cam[k] == j)
                return true;
        }
        return false;
    }

    // método para imprimir la matriz
    private void imprime(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print("\t" + a[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    // método para imprimir un arreglo
    private void imprimearreglo(int[] a) {

        for (int j = 0; j < a.length; j++) {
            System.out.print("\t" + a[j] + "\t");
        }
        System.out.println();
    }
    
    private int[][] zero2DMatrix(int rows, int cols){
        int[][] zero = new int[rows][cols];
        for( int i = 0; i < rows; i++ ){
            for( int j = 0; j < cols; j++ ){
                zero[i][j] = 0;
            }
        }
        return zero;
    }

    private int[] zeroMatrix(int rows){
        int[] zero = new int[rows];
        for( int i = 0; i < rows; i++ ){
            zero[i] = 0;
        }
        return zero;
    }

    public int getIterations() {
        return iterations;
    }
}