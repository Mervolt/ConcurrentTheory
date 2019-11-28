package com.company.fair;

import com.company.ResultTuple;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairSR {
    public final Lock lock = new ReentrantLock(true);
    public Condition waitingProducers = lock.newCondition();
    public Condition waitingConsumers = lock.newCondition();
    public Integer limit;
    public Integer currentSize;
    public ResultTuple[] resultProducers;
    public ResultTuple[] resultConsumers;
    boolean consumerWaits;
    boolean producerWaits;

    public FairSR(Integer limit, Integer currentSize, int resultSize) {
        this.limit = limit;
        this.currentSize = currentSize;
        resultProducers = new ResultTuple[resultSize];
        resultConsumers = new ResultTuple[resultSize];
        this.consumerWaits = false;
        this.producerWaits = false;
    }
}
