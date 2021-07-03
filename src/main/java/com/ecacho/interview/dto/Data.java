package com.ecacho.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;

@Getter
@AllArgsConstructor
public class Data implements Comparable<Data> {

    private long elapsedTimeInMilliseconds;
    private long byteRead;
    private Status status;


    @Override
    public int compareTo(Data another) {
        return Comparator.comparing(Data::getElapsedTimeInMilliseconds)
                .thenComparing(Data::getByteRead)
                .thenComparing(Data::getStatus)
                .compare(another, this);
    }
}
