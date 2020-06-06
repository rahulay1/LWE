//import java.math.BigInteger;
//
//public class MatrixMultiAdv {
//
//
//    public static final int NUM_OF_THREADS = 4;
//
//    public MatrixMultiAdv(BigInteger[] A, BigInteger [][] B, BigInteger [] e2, BigInteger [] y,BigInteger q, BigInteger m){
//        int rowcol = A.length;
//        int col = B[0].length;
//        BigInteger c2_transpose []= new BigInteger[col];
//        int colThread=col/NUM_OF_THREADS;
//        int threadcount = 0;
//        Thread[] thrd = new Thread[NUM_OF_THREADS];
//
//
//        try {
//            for(int numTh = 0 ; numTh < NUM_OF_THREADS; numTh++){
//
//                    // creating thread for multiplications
//                    thrd[threadcount] = new Thread(new WorkerThAdv(rowcol,colThread, numTh,q,A,B,e2,y,m,c2_transpose));
//                    thrd[threadcount].start(); //thread start
//
//                    thrd[threadcount].join(); // joining threads
//                    threadcount++;
//            }
//        }
//        catch (InterruptedException ie){}
//
//
//
//// printing resulting matrix C after multiplication
//        System.out.println(" Resulting C Matrix : ");
////        for(row = 0 ; row < 3; row++){
////            for (col = 0 ; col < 3; col++ ){
////                System.out.print("  "+C[row][col]);
////            }
////            System.out.println();
////        }
//    }
//}
//
//
//
//class WorkerThAdv implements Runnable{
//    private int rc;
//    private int colStart;
//    private int colEnd;
//    private BigInteger[] A;
//    private BigInteger[][] B;
//    private BigInteger [] c2_transpose;
//    private BigInteger q;
//    private BigInteger [] e2;
//    private BigInteger [] y;
//    private BigInteger m;
//
//    public WorkerThAdv(int rowcol, int colTh, int numTh, BigInteger q, BigInteger[] A, BigInteger [][] B, BigInteger[] e2, BigInteger [] y, BigInteger m, BigInteger [] c2_transpose){
//        this.colStart = numTh*colTh;
//        this.colEnd = colStart + colTh;
//        this.rc = rowcol;
//        this.A = A;
//        this.B = B;
//        this.e2 = e2;
//        this.c2_transpose = c2_transpose;
//        this.q=q;
//        this.y=y;
//        this.m=m;
//    }
//
//    @Override
//    public void run(){
//        for (int c = colStart; c < colEnd; c++) {
//            c2_transpose[c] = new BigInteger("0");
//            for (int row = 0; row < rc; row++){
//                c2_transpose[c] = ((c2_transpose[c].add(A[row].multiply(B[row][c]))).add(e2[row])).mod(q);
//            }
//            BigInteger scaled_y = q.multiply(y[c]).divide(m);
//            c2_transpose[c] = (c2_transpose[c].add(scaled_y)).mod(q);
//       }
//    }
//}