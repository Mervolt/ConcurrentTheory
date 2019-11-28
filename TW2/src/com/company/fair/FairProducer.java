package com.company.fair;

import com.company.ResultTuple;

public class FairProducer implements Runnable {
    public FairSR sharedResource;
    public Integer amount;
    long time1;
    long time2;
    int id;

    public FairProducer(FairSR sharedResource, Integer amount, int id) {
        this.sharedResource = sharedResource;
        this.amount = amount;
        this.id = id;
    }

    @Override
    public void run() {
        this.time1 = System.nanoTime();
        try{
            sharedResource.lock.lock();
            if(sharedResource.producerWaits == true){
                sharedResource.waitingProducers.await();
            }
            while(this.amount + sharedResource.currentSize > sharedResource.limit){
                sharedResource.producerWaits = true;
                sharedResource.waitingProducers.await();
            }
            this.sharedResource.currentSize += this.amount;
            sharedResource.producerWaits = false;
            sharedResource.waitingProducers.signalAll();
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
