package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Multithreader {

    private final Assignment8 dataReader = new Assignment8();
    private final Map<Integer, Integer> numberCounts = new HashMap<>();

    public void asynchronousProcessor() {
        List<String> numbers = new FileService().readFile("output.txt");
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        int totalBatches = 10000;
        int batchSize = numbers.size() / totalBatches;

        for (int i = 0; i < totalBatches; i++) {
            final int index = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                List<Integer> integerNumbers = new ArrayList<>();
                int start = index * batchSize;
                int end = Math.min(start + batchSize, numbers.size());

                for (int j = start; j < end; j++) {
                    integerNumbers.add(Integer.parseInt(numbers.get(j)));
                }

                countUniqueNumbers(integerNumbers);
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
            tasks.add(future);
        }

        CompletableFuture<Void> allCompletedFutures = CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]));
        try {
            allCompletedFutures.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        printTotalCounts();
    }

    private void countUniqueNumbers(List<Integer> numbers) {
        synchronized (numberCounts) {
            for (Integer number : numbers) {
                numberCounts.put(number, numberCounts.getOrDefault(number, 0) + 1);
            }
        }
    }

    public void printTotalCounts() {
        synchronized (numberCounts) {
            for (Integer key : numberCounts.keySet()) {
                Integer value = numberCounts.get(key);
                System.out.println(key + "=" + value);
            }
        }
    }
}