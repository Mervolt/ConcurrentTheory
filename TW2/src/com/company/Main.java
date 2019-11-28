package com.company;

import com.company.fair.FairConsumer;
import com.company.fair.FairProducer;
import com.company.fair.FairSR;
import com.company.naive.NaiveConsumer;
import com.company.naive.NaiveProducer;
import com.company.naive.NaiveSR;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static final File file = new File("/home/michal/TWpomiaryczasu.csv");
    public static void main(String[] args){
        naivePC();
        fairPC();
    }

    public static void writeToCsv(int size, ResultTuple[] prods, ResultTuple[] cons){
        try {
            FileWriter csvWriter = new FileWriter(file, true);
            StringBuilder builder1_size = new StringBuilder(", ");
            StringBuilder builder1_time = new StringBuilder(", ");
            for(int i = 0; i < size; i ++) {
                System.out.println(cons[i].size + " " + cons[i].time);
                builder1_size.append(cons[i].size).append(" ").append(", ");
                builder1_time.append(cons[i].time).append(" ").append(", ");
            }
            builder1_size.append("\n");
            builder1_time.append("\n");
            StringBuilder builder2_size = new StringBuilder(", ");
            StringBuilder builder2_time = new StringBuilder(", ");
            for(int i = 0; i < size; i ++) {
                System.out.println(prods[i].size + " " + prods[i].time);
                builder2_size.append(prods[i].size).append(" ").append(", ");
                builder2_time.append(prods[i].time).append(" ").append(", ");
            }
            builder2_size.append("\n");
            builder2_time.append("\n");
            csvWriter.append(builder1_size.toString()).append(builder1_time.toString()).append(builder2_size.toString())
                    .append(builder2_time.toString());
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void naivePC(){
        int n1 = 100, n2 = 1000;
        int m1 = 10000, m2 = 100000;

        naivePCone(true, n1, m1);
        naivePCone(true, n2, m1);
        naivePCone(true, n1, m2);
        naivePCone(true, n2, m2);
        naivePCone(false, n1, m1);
        naivePCone(false, n2, m1);
        naivePCone(false, n1, m2);
        naivePCone(false, n2, m2);

    }
    public static void fairPC(){
        int n1 = 100, n2 = 1000;
        int m1 = 10000, m2 = 100000;

        fairPCone(true, n1, m1);
        fairPCone(true, n2, m1);
        fairPCone(true, n1, m2);
        fairPCone(true, n2, m2);
        fairPCone(false, n1, m1);
        fairPCone(false, n2, m1);
        fairPCone(false, n1, m2);
        fairPCone(false, n2, m2);

    }

    public static void naivePCone(boolean isPlain, int n, int m){
        Thread[] threadTableCons = new Thread[n];
        Thread[] threadTableProds = new Thread[n];
        NaiveSR naiveRes = new NaiveSR(2 * m, 0, n);

        if(isPlain) {
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                threadTableCons[i] = new Thread(new NaiveProducer(naiveRes, random.nextInt(m), i));
                threadTableProds[i] = new Thread(new NaiveConsumer(naiveRes, random.nextInt(m), i));
                threadTableCons[i].start();
                threadTableProds[i].start();
            }
        }
        else{
            for (int i = 0; i < n; i++) {
            threadTableCons[i] = new Thread(new NaiveProducer(naiveRes, myRandom(m), i));
            threadTableProds[i] = new Thread(new NaiveConsumer(naiveRes, myRandom(m), i));
            threadTableCons[i].start();
            threadTableProds[i].start();
        }

        }
        try {
            System.out.println("Waiting :) for threads " + n + " buffer size " + 2 * m + " plain " + isPlain);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0;i < n; i++) {
            if (threadTableCons[i].isAlive()) {
                threadTableCons[i].interrupt();
            }
            if (threadTableProds[i].isAlive()) {
                threadTableProds[i].interrupt();
            }
        }
        writeToCsv(n, naiveRes.resultConsumers, naiveRes.resultProducers);
    }

    public static void fairPCone(boolean isPlain, int n, int m){
        Thread[] threadTableCons = new Thread[n];
        Thread[] threadTableProds = new Thread[n];
        FairSR fairRes = new FairSR(2 * m, 0, n);

        if(isPlain) {
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                threadTableCons[i] = new Thread(new FairProducer(fairRes, random.nextInt(m), i));
                threadTableProds[i] = new Thread(new FairConsumer(fairRes, random.nextInt(m), i));
                threadTableCons[i].start();
                threadTableProds[i].start();
            }
        }
        else{
            for (int i = 0; i < n; i++) {
                threadTableCons[i] = new Thread(new FairProducer(fairRes, myRandom(m), i));
                threadTableProds[i] = new Thread(new FairConsumer(fairRes, myRandom(m), i));
                threadTableCons[i].start();
                threadTableProds[i].start();
            }

        }
        try {
            System.out.println("Waiting :) for threads " + n + " buffer size " + 2 * m + " plain " + isPlain);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0;i < n; i++) {
            if (threadTableCons[i].isAlive()) {
                threadTableCons[i].interrupt();
            }
            if (threadTableProds[i].isAlive()) {
                threadTableProds[i].interrupt();
            }
        }
        writeToCsv(n, fairRes.resultConsumers, fairRes.resultProducers);
    }

    private static Integer myRandom(Integer m){
        //60% - (0,1/4m)
        //20% - (1/4m,2/4m)
        //10% - (2/4m,3/4m)
        //10% - (3/4m,m)
        Float tenth = m.floatValue()/10;
        Integer first = tenth.intValue() *6;
        Integer second = tenth.intValue() *8;
        Integer third = tenth.intValue() *9;
        Random random = new Random();
        int distribution = random.nextInt(10);
        if(distribution < 6){
            return random.nextInt(first);
        }
        else if(distribution < 8){
            return random.nextInt(second - first) + first;
        }
        else if(distribution < 9){
            return random.nextInt(third - second) + second;
        }
        else{
            return random.nextInt(m - third) + third;
        }
    }
}
