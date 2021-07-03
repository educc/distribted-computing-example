package com.ecacho.interview;

import com.ecacho.interview.dto.Data;
import com.ecacho.interview.dto.Status;
import com.ecacho.interview.worker.ParallelWorker;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Main {

    private static final String EXPECTED_STRING = "Lpfn";

    public static void main(String[] args) {
        if (args.length < 1) {
            log.error("it require parameters");
            log.info("how to use:\njava -jar app.jar 60");
            return;
        }
        var timeoutInMillis = Long.parseLong(args[0]);
        var howManyWorkers = 10;

        var allData = ParallelWorker.create(EXPECTED_STRING, howManyWorkers, timeoutInMillis)
                .sorted()
                .doOnNext(Main::printWorkerResult)
                .toList()
                .blockingGet();
        printSummarize(allData);
    }

    private static void printSummarize(List<Data> listData) {
        Optional<Long> average = Optional.empty();

        var bytesPerMilliList = listData.stream()
                .filter(it -> !Status.SUCCESS.equals(it.getStatus()))
                .map(it -> it.getByteRead() / it.getElapsedTimeInMilliseconds())
                .collect(Collectors.toList());

        if (!bytesPerMilliList.isEmpty()) {
            var sum = bytesPerMilliList.stream().reduce(0L, Long::sum);
            average = Optional.of(sum / bytesPerMilliList.size());
        }

        average.ifPresentOrElse(
                (avr)-> log.info("Average bytes/ms={} for fail/timeout workers", avr),
                ()-> log.info("No fail or timeout workers"));
    }

    private static void printWorkerResult(Data data) {
        log.info("worker result: bytesRead={}, elapsedTimeInMilliseconds={}, status={}",
                data.getByteRead(), data.getElapsedTimeInMilliseconds(), data.getStatus());
    }
}
