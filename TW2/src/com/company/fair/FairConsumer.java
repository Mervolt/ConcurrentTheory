package com.company.fair;

import com.company.ResultTuple;

public class FairConsumer implements Runnable {
    public FairSR sharedResource;
    public Integer amount;
    long time1;
    long time2;
    int id;

    public FairConsumer(FairSR sharedResource, Integer amount, int id) {
        this.sharedResource = sharedResource;
        this.amount = amount;
        this.id = id;
    }

    @Override
    public void run() {
        time1 = System.nanoTime();
        try{
            sharedResource.lock.lock();
            if(sharedResource.consumerWaits == true){
                sharedResource.waitingConsumers.await();
            }
            while(this.amount > sharedResource.currentSize){
                sharedResource.consumerWaits = true;
                sharedResource.waitingConsumers.await();
            }
            sharedResource.currentSize -= this.amount;
            sharedResource.consumerWaits = false;
            sharedResource.waitingConsumers.signalAll();
            sharedResource.waitingProducers.signalAll();
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
