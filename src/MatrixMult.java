public class MatrixMult {


    public static final int NUM_OF_THREADS = 9;

    public static void main(String args[]){
        int row;
        int col;
        int A[][] = {{1,4},{2,5},{3,6}};
        int B[][] = {{8,7,6},{5,4,3}};
        int C[][] = new int[3][3];
        int threadcount = 0;
        Thread[] thrd = new Thread[NUM_OF_THREADS];


        try {
            for(row = 0 ; row < 3; row++){
                for (col = 0 ; col < 3; col++ ) {
                    // creating thread for multiplications
                    thrd[threadcount] = new Thread(new WorkerTh(row, col, A, B, C));
                    thrd[threadcount].start(); //thread start

                    thrd[threadcount].join(); // joining threads
                    threadcount++;
                }
            }
        }
        catch (InterruptedException ie){}

        // printing matrix A
        System.out.println(" A Matrix : ");
        for(row = 0 ; row < 3; row++){
            for (col = 0 ; col < 2; col++ ) {
            System.out.print("  "+A[row][col]);
        }
        System.out.println();
    }

        // printing matrix B
        System.out.println(" B Matrix : ");
        for(row = 0 ; row < 2; row++)
        {
            for (col = 0 ; col < 3; col++ )
            {
                System.out.print("  "+B[row][col]);
            }
            System.out.println();
        }

        // printing resulting matrix C after multiplication
        System.out.println(" Resulting C Matrix : ");
        for(row = 0 ; row < 3; row++){
            for (col = 0 ; col < 3; col++ ){
                System.out.print("  "+C[row][col]);
            }
            System.out.println();
        }
    }
}



class WorkerTh implements Runnable{
    private int row;
    private int col;
    private int A[][];
    private int B[][];
    private int C[][];

    public WorkerTh(int row, int col, int A[][], int B[][], int C[][] ){
        this.row = row;
        this.col = col;
        this.A = A;
        this.B = B;
        this.C = C;
    }

    @Override
    public void run(){
        for(int k = 0; k < B.length; k++){
            C[row][col] += A[row][k] * B[k][col];
        }
    }
}