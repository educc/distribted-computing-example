package com.ecacho.interview.worker;

import com.ecacho.interview.dto.Data;
import com.ecacho.interview.dto.Status;
import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Worker {

    private String expectedString;
    private long bytesRead;
    private long startInMilli;
    private long timeoutInMillis;
    private Observable<Byte> source;

    public Worker(Observable<Byte> source, String expectedString, long timeoutInMillis) {
        this.source = source;
        this.expectedString = expectedString;
        this.timeoutInMillis = timeoutInMillis;
        bytesRead = 0;
    }

    public Single<Data> findData() {
        return source
                .scan("", this::bufferString)
                .filter(it -> expectedString.equals(it))
                .map(it -> buildData(Status.SUCCESS))
                .firstOrError()
                .timeout(timeoutInMillis, TimeUnit.MILLISECONDS)
                .onErrorReturn(this::handleError)
                .doOnSubscribe((it) -> {
                    startInMilli = System.currentTimeMillis();
                });
    }

    private Data buildData(Status status) {
        var elapsed  = System.currentTimeMillis() - startInMilli;
        return new Data(elapsed, bytesRead, status);
    }

    private String bufferString(String acc, Byte item) {
        bytesRead++;

        int sizeExpected = expectedString.length();
        if (acc.length() < sizeExpected) {
            return concat(acc, item);
        }
        return concat(acc.substring(1), item);
    }

    private static String concat(String str, Byte someByte) {
        var another = new String(new byte[]{someByte});
        return str + another;
    }

    private Data handleError(Throwable ex) {
        log.error("on worker", ex);
        if (TimeoutException.class.equals(ex.getClass())) {
            return buildData(Status.TIMEOUT);
        }
        return buildData(Status.FAILURE);
    }
}
