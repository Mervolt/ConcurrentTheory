package com.company;

import java.awt.image.BufferedImage;

class MyThread implements Runnable {

    int i; //tasks' number
    BufferedImage image;
    double zx, zy, cX, cY, tmp;
    int MAX_ITER;
    double ZOOM;
    int iteration;
    int width;
    int height;
    static int n;//tasks amount

    static int tasksPerThread = 10;

    // static boolean[][] test =  new boolean[600][800];


    public MyThread(int i, BufferedImage image, int MAX_ITER, double ZOOM, int width, int height) {
        this.i = i;
        this.image = image;
        this.MAX_ITER = MAX_ITER;
        this.ZOOM = ZOOM;
        this.iteration = 0;
        this.width = width;
        this.height = height;
    }

    @Override
    public void run() {
        int curIteration = iteration;
        iteration++;
        if (!(curIteration >= tasksPerThread)) {
            int rowsToDo = image.getHeight() / n / tasksPerThread;
            int startingY = i * image.getHeight() / n + curIteration * (image.getHeight() / n / tasksPerThread);

            for (int y = startingY; y < startingY + rowsToDo; y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    zx = zy = 0;
                    cX = (x - this.width/2.0) / ZOOM;
                    cY = (y - this.height/2.0) / ZOOM;
                    int iter = MAX_ITER;
                    while (zx * zx + zy * zy < 4 && iter > 0) {
                        tmp = zx * zx - zy * zy + cX;
                        zy = 2.0 * zx * zy + cY;
                        zx = tmp;
                        iter--;
                    }
                    try {
                        image.setRGB(x, y, iter | (iter << 8));
                        // test[y][x]=true;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("X: " +x + "Y: " +y );
                    }
                }
            }
        }


    }
}
