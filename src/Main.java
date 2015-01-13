
/**
 * Created by IntelliJ IDEA.
 * User: sergio
 * Date: 25/05/12
 * Time: 07:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public Main(){
        FordFulkerson ff = new FordFulkerson(new int[][]
             {{0, 6, 5, 0, 0, 0},
             {0, 0, 0, 2, 0, 3},
             {0, 0, 0, 1, 2, 0},
             {0, 0, 0, 0, 9, 7},
             {0, 0, 0, 0, 0, 5},
             {0, 0, 0, 0, 0, 0}});
        ff.exec();
    }
    
    public static void main(String args[]){
        Main main = new Main();
    }

}