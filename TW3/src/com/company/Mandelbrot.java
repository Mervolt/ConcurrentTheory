package com.company;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame {


    BufferedImage I;

    public Mandelbrot(int width, int height) {
        super("Mandelbrot Set");
        setBounds(100, 100, width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) {

        final int[] threads_amount = {1, 2, 3, 5, 10, 15, 30};
        ImageConfigurationTuple config1 = new ImageConfigurationTuple(800,600, 150);
        ImageConfigurationTuple config2 = new ImageConfigurationTuple(1200,900, 225);
        ImageConfigurationTuple config3 = new ImageConfigurationTuple(1600,1200, 300);
        final ImageConfigurationTuple[] config = {config1, config2, config3};

        File file = new File("/home/michal/TWmandelbrot.csv");
        StringBuilder fullResult = new StringBuilder();
        try {
            FileWriter fileWriter = new FileWriter(file);

        final int numberOfTries = 3;
        for (int sizeIterator = 0; sizeIterator < config.length; sizeIterator++) {
            StringBuilder oneConfigResult = new StringBuilder();
            for(int tryNumber = 0; tryNumber < numberOfTries; tryNumber++) {
                for (int k = 0; k < threads_amount.length; k++) {
                    long timeStart = System.nanoTime();
                    int tasksPerThread = 10;
                    final int MAX_ITER = 570;


                    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads_amount[k]);

                    Collection<Future<?>> futures = new LinkedList<Future<?>>();

                    Mandelbrot mandelbrot = new Mandelbrot(config[sizeIterator].width, config[sizeIterator].height);

                    BufferedImage I = new BufferedImage(config[sizeIterator].width, config[sizeIterator].height, BufferedImage.TYPE_INT_RGB);

                    MyThread myThreads[] = new MyThread[threads_amount[k]];

                    MyThread.n = threads_amount[k];
                    for (int i = 0; i < threads_amount[k]; i++) {
                        myThreads[i] = new MyThread(i, I, MAX_ITER, config[sizeIterator].ZOOM, config[sizeIterator].width, config[sizeIterator].height);

                    }

                    for (int i = 0; i < tasksPerThread; i++) {
                        for (int j = 0; j < threads_amount[k]; j++) {
                            futures.add(executor.submit(myThreads[j]));
                        }

                    }
                    for (Future<?> future : futures) {
                        try {
                            future.get();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    mandelbrot.I = I;
                    mandelbrot.setVisible(true);

                    long timeEnd = System.nanoTime();
                    oneConfigResult.append(timeEnd - timeStart);
                    oneConfigResult.append(",");
                }
            }
            fullResult.append(oneConfigResult).append("\n");
        }
        fileWriter.append(fullResult.toString());
        fileWriter.flush();
        fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
