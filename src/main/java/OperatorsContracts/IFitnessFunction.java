package OperatorsContracts;

import Core._Chromosome;
import Core._Population;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public interface IFitnessFunction<T> {

    double calculate(_Chromosome<T> chromosome);

    default void evaluate(_Population<T> population) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);

        List<_Chromosome<T>> chromosomes = population.getChromosomes();
        AtomicInteger completedCount = new AtomicInteger(0);

        System.out.printf("Evaluating %d chromosomes with %d threads...%n",
                chromosomes.size(), processors);

        CountDownLatch latch = new CountDownLatch(chromosomes.size());

        long startTime = System.currentTimeMillis();

        for (_Chromosome<T> chromosome : chromosomes) {
            if (!chromosome.isFitnessCalculated()) {
                executor.submit(() -> {
                    try {
                        double fitness = calculate(chromosome);
                        synchronized (chromosome) {
                            chromosome.setFitness(fitness);
                        }

                        int done = completedCount.incrementAndGet();
                        if (done % 10 == 0 || done == chromosomes.size()) {
                            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                            System.out.printf("\r  Progress: %d/%d (%.0f%%) - %ds elapsed   ",
                                    done, chromosomes.size(), (done * 100.0 / chromosomes.size()), elapsed);
                        }

                    } catch (Exception e) {
                        System.err.println("\n Evaluation error: " + e.getMessage());
                        e.printStackTrace();
                        chromosome.setFitness(0.0);
                    } finally {
                        latch.countDown();
                    }
                });
            } else {
                latch.countDown();
            }
        }

        try {
            boolean finished = latch.await(30, TimeUnit.MINUTES);
            if (!finished) {
                System.err.println("\n Evaluation TIMEOUT after 30 minutes!");
            } else {
                long totalTime = (System.currentTimeMillis() - startTime) / 1000;
                System.out.printf("\n  Completed in %d seconds (avg %.2fs per chromosome)\n",
                        totalTime, (double) totalTime / chromosomes.size());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("\n Evaluation interrupted!");
        } finally {
            executor.shutdownNow();
        }
    }

    default boolean isMaximization() {
        return true;
    }
}