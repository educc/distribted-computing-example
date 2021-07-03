package com.ecacho.interview.source;

import io.reactivex.Observable;

public class RandomAsciiStream {

    public static Observable<Byte> toObservable() {
        final String baseString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return Observable.fromIterable(new InfiniteRandomIterable(baseString));
    }
}
