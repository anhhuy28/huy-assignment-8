package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multithreader {

    private final Assignment8 dataReader = new Assignment8();
    private final Map<Integer, Integer> numberCounts = new HashMap<>();
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public void asynchronousProcessor() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                List<Integer> numbers = dataReader.getNumbers();
                countUniqueNumbers(numbers);
            }, pool).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
            futures.add(future);
        }

        CompletableFuture<Void> allCompletedFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            allCompletedFutures.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        printTotalCounts();
        pool.shutdown();
    }

    private synchronized void countUniqueNumbers(List<Integer> numbers) {
        for (Integer number : numbers) {
            numberCounts.put(number, numberCounts.getOrDefault(number, 0) + 1);
        }
    }

    public synchronized void printTotalCounts() {
        for (Map.Entry<Integer, Integer> entry : numberCounts.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }
}