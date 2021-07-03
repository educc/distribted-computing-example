package com.ecacho.interview.worker;

import com.ecacho.interview.dto.Data;
import com.ecacho.interview.source.RandomAsciiStream;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class ParallelWorker {

    public static Flowable<Data> create(String expectedString, int howManyWorkers, long timeoutInSecondsPerWorker) {
        return Flowable.range(1, howManyWorkers)
                .flatMap(v -> {
                    var source = RandomAsciiStream.toObservable();
                    var worker = new Worker(source, expectedString, timeoutInSecondsPerWorker);
                    return worker
                            .findData()
                            .toFlowable()
                            .subscribeOn(Schedulers.newThread()); //this does the magic to execute in parallel
                });
    }
}
