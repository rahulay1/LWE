import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.lang.Math.*;

public class MainBinary {

    public static void main(String[] args) {

        final int NUM_OF_THREADS = 4;
        int threadcount = 0;
        Thread[] thrd = new Thread[NUM_OF_THREADS];
        int n = 50;
        int primesize=570;
        BigInteger q = new BigInteger(primesize, 64, new Random());;//new BigInteger("2").pow(50).add(new BigInteger("1"));
        BigInteger alpha= new BigInteger("1");//BigDecimal.valueOf(0.00000000018260*q.doubleValue()).toBigInteger();
        BigInteger m = new BigInteger("30000");//BigInteger.valueOf(n*primesize);


        int[] x = new int[m.intValue()];
        int[] y = new int[m.intValue()];
        BigInteger [] bigX = new BigInteger[m.intValue()];
        BigInteger [] bigY = new BigInteger[m.intValue()];
        BigInteger[][] bigA = new BigInteger[n][m.intValue()];
        int s_ori = 0;
        for (int mm = 0; mm < m.intValue(); mm++) {
            for (int nn = 0; nn < n; nn++) {
                bigA[nn][mm] = new BigInteger(primesize+10, new Random()).mod(q);
            }
            x[mm] = (int) Math.round(Math.random());
            y[mm] = (int) Math.round(Math.random());
            bigX[mm]= new BigInteger(String.valueOf(x[mm]));
            bigY[mm]= new BigInteger(String.valueOf(y[mm]));
            s_ori = s_ori + x[mm] * y[mm];
        }

        long time1,time2,time21, time22, time23,time3,time4;
        time1=System.currentTimeMillis();
        //Step 1
        BigInteger [] bigU = new BigInteger[n];
        bigU = bigIntMatVecMult(bigA,bigX,n,m.intValue(),q);
        time2=System.currentTimeMillis();

        //Step 2
        BigInteger[] bigt = new BigInteger[n];
        for (int nn = 0; nn < n; nn++) {
            bigt[nn] = new BigInteger(150, new Random()).mod(q);
        }
        //time21=System.currentTimeMillis();

        BigInteger e1 =new BigInteger(100, new Random()).mod(alpha);;
        BigInteger [] e2 = new BigInteger[m.intValue()];
        for (int mm = 0; mm < m.intValue(); mm++) {
            e2[mm] = new BigInteger(100, new Random()).mod(alpha);
        }
        //time22=System.currentTimeMillis();

        BigInteger d1 = bigIntVecVecMult(bigt,bigU,n,q).add(e1).mod(q);
        //time23=System.currentTimeMillis();
        BigInteger [] c2_transpose = new BigInteger[m.intValue()];

        for (int c = 0; c < m.intValue(); c++) {
            c2_transpose[c] = new BigInteger("0");
            for (int rc = 0; rc < n; rc++){
                c2_transpose[c] = ((c2_transpose[c].add(bigt[rc].multiply(bigA[rc][c]))).add(e2[rc])).mod(q);
            }
            BigInteger scaled_y = q.multiply(bigY[c]).divide(m);
            c2_transpose[c] = (c2_transpose[c].add(scaled_y)).mod(q);
        }


//        int rowcol = bigt.length;
//        int col = bigA[0].length;
//        int colThread=col/NUM_OF_THREADS;
//        for(int numTh = 0 ; numTh < NUM_OF_THREADS; numTh++){
//
//            // creating thread for multiplications
//            thrd[threadcount] = new Thread(new WorkerThAdv(rowcol,colThread, numTh,q,bigt,bigA,e2,bigY,m,c2_transpose));
//            thrd[threadcount].start(); //thread start
//
//            //thrd[threadcount].join(); // joining threads
//            threadcount++;
//        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        time3=System.currentTimeMillis();

        //Step 3
        BigInteger s_noise1 = bigIntVecVecMult(c2_transpose,bigX,m.intValue(),q);
        BigInteger s_noise = s_noise1.subtract(d1).mod(q);
        BigInteger s_lwe = s_noise.divide(q.divide(m));
        time4=System.currentTimeMillis();
        System.out.println("Test cost time: Step 1  "+(time2-time1)+"ms");
        //System.out.println("Test cost time: Step 21  "+(time21-time2)+"ms");
        //System.out.println("Test cost time: Step 22  "+(time22-time21)+"ms");
        //System.out.println("Test cost time: Step 23  "+(time23-time22)+"ms");
        //System.out.println("Test cost time: Step 23  "+(time3-time23)+"ms");
        System.out.println("Test cost time: Step 2  "+(time3-time2)+"ms");
        System.out.println("Test cost time: Step 3  "+(time4-time3)+"ms");
        System.out.println("Test cost time: Total  "+(time4-time1)+"ms");
        System.out.println("s_ori :" +s_ori+ "\n" +"s_lwe :" +s_lwe);
        System.out.println("q :" +q+ "\n" +"m :" +m  + "\n" +"alpha_q :" +alpha);
        System.out.println("END");

    }





    public static BigInteger[][] bigIntMatMult(BigInteger[][] A, BigInteger[][] B, int rowA, int colArowB, int colB, BigInteger q) {
        BigInteger[][] C = new BigInteger[rowA][colB];
        for (int r = 0; r < rowA; r++) {
            for (int c = 0; c < colB; c++) {
                for (int rc = 0; rc < colArowB; rc++) {
                    C[r][c] = (C[r][c].add(A[r][rc]).multiply(B[rc][c])).mod(q);
                }
            }
        }
        return C;
    }


    public static BigInteger[] bigIntMatVecMult(BigInteger[][] A, BigInteger[] B, int rowA, int colArowB,  BigInteger q) {
        BigInteger[] C = new BigInteger[rowA];
        for (int r = 0; r < rowA; r++) {
            C[r] = new BigInteger("0");
            for (int rc = 0; rc < colArowB; rc++) {
                    C[r] = (C[r].add((A[r][rc]).multiply(B[rc]))).mod(q);
                }
        }
        return C;
    }

    public static BigInteger bigIntVecVecMult(BigInteger[] A, BigInteger[] B, int rowA,  BigInteger q) {
        BigInteger C = new BigInteger("0");
        for (int r = 0; r < rowA; r++) {
            C = (C.add(A[r].multiply(B[r]))).mod(q);
            //System.out.println(r);
        }
        return C;
    }

    public static BigInteger[] bigIntVecTransMult(BigInteger[] A, BigInteger[][] B, int colArowB, int colB, BigInteger q) {
        BigInteger[] C = new BigInteger[colB];
        for (int c = 0; c < colB; c++) {
            C[c] = new BigInteger("0");
            for (int rc = 0; rc < colArowB; rc++){
                C[c] = (C[c].add(A[rc]).multiply(B[rc][c])).mod(q);
            }
        }
        return C;
    }
}


class WorkerThAdv implements Runnable{
    private int rc;
    private int colStart;
    private int colEnd;
    private BigInteger[] A;
    private BigInteger[][] B;
    private BigInteger [] c2_transpose;
    private BigInteger q;
    private BigInteger [] e2;
    private BigInteger [] y;
    private BigInteger m;

    public WorkerThAdv(int rowcol, int colTh, int numTh, BigInteger q, BigInteger[] A, BigInteger [][] B, BigInteger[] e2, BigInteger [] y, BigInteger m, BigInteger [] c2_transpose){
        this.colStart = numTh*colTh;
        this.colEnd = colStart + colTh;
        this.rc = rowcol;
        this.A = A;
        this.B = B;
        this.e2 = e2;
        this.c2_transpose = c2_transpose;
        this.q=q;
        this.y=y;
        this.m=m;
    }

    @Override
    public void run(){
        for (int c = colStart; c < colEnd; c++) {
            c2_transpose[c] = new BigInteger("0");
            for (int row = 0; row < rc; row++){
                c2_transpose[c] = ((c2_transpose[c].add(A[row].multiply(B[row][c]))).add(e2[row])).mod(q);
            }
            BigInteger scaled_y = q.multiply(y[c]).divide(m);
            c2_transpose[c] = (c2_transpose[c].add(scaled_y)).mod(q);
        }
    }
}