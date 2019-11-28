package com.company.naive;

import com.company.ResultTuple;

public class NaiveProducer implements Runnable {
    public NaiveSR sharedResource;
    public Integer amount;
    long time1 = 0;
    long time2 = 0;
    int id;

    public NaiveProducer(NaiveSR sharedResource, Integer amount,int id) {
        this.sharedResource = sharedResource;
        this.amount = amount;
        this.id = id;
    }

    @Override
    public void run() {
        this.time1 = System.nanoTime();
        try{
            sharedResource.lock.lock();
            while(this.amount + sharedResource.currentSize > sharedResource.limit){
                sharedResource.waitingProducers.await();
            }
            this.sharedResource.currentSize += this.amount;
            this.sharedResource.waitingConsumers.signalAll();
        }
        catch(Exception e){
            System.out.println("I am interrupted :) Producer");
        }
        finally{
            sharedResource.lock.unlock();
            this.time2 = System.nanoTime();
            sharedResource.resultConsumers[this.id] = new ResultTuple(time2 - time1, amount);
        }
    }
}
