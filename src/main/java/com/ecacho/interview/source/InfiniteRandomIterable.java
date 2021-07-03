package com.ecacho.interview.source;

import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;

@AllArgsConstructor
public class InfiniteRandomIterable implements Iterable<Byte> {

    private String baseString;

    @Override
    public Iterator<Byte> iterator() {
        var bytes = baseString.getBytes(StandardCharsets.UTF_8);
        var random = new Random();

        return new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Byte next() {
                var idx = random.nextInt(bytes.length);
                return bytes[idx];
            }
        };
    }
}
