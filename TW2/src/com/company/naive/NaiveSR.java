package com.company.naive;

import com.company.ResultTuple;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NaiveSR {
    public final Lock lock = new ReentrantLock();
    public Condition waitingProducers = lock.newCondition();
    public Condition waitingConsumers = lock.newCondition();
    public Integer limit;
    public Integer currentSize;
    public ResultTuple[] resultProducers;
    public ResultTuple[] resultConsumers;

    public NaiveSR(Integer limit, Integer currentSize, int resultSize) {
        this.limit = limit;
        this.currentSize = currentSize;
        resultProducers = new ResultTuple[resultSize];
        resultConsumers = new ResultTuple[resultSize];
    }
}
