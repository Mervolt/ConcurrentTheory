package com.company.naive;

import com.company.ResultTuple;

public class NaiveConsumer implements Runnable {
    public NaiveSR sharedResource;
    public Integer amount;
    long time1 = 0;
    long time2 = 0;
    int id;

    public NaiveConsumer(NaiveSR sharedResource, Integer amount, int id) {
        this.sharedResource = sharedResource;
        this.amount = amount;
        this.id = id;
    }

    @Override
    public void run() {
        time1 = System.nanoTime();
        try{
            sharedResource.lock.lock();
            while(this.amount > sharedResource.currentSize){
                sharedResource.waitingConsumers.await();
            }
            this.sharedResource.currentSize -= this.amount;
            this.sharedResource.waitingProducers.signalAll();
        }
        catch(Exception e){
            System.out.println("I am interrupted :) Consumer");
        }
        finally{
            sharedResource.lock.unlock();
            time2 = System.nanoTime();
            sharedResource.resultProducers[this.id] = new ResultTuple(time2 - time1, amount);
        }
    }
}
