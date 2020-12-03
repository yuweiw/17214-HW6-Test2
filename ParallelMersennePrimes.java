package edu.cmu.cs.cs214.rec13;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.math.BigInteger.ONE;

/**
 *  Prints the first 20 Mersenne primes.  A Mersenne prime is a prime
 *  number of the form 2^p - 1, for some p that is itself prime.
 */
public class ParallelMersennePrimes {
    private static final int LIMIT = 20; // Number of Mersenne primes to find
    private static final BigInteger TWO = new BigInteger("2");

    public static void main(String[] args){
        long startTime = System.nanoTime(); // Record the start time for simple benchmarking

        // TODO: Use Java concurrency tools to improve the performance of Mersenne prime-finding here.
        ExecutorService executor = Executors.newFixedThreadPool(4);

        BlockingQueue<Future<BigInteger>> queue = new ArrayBlockingQueue<>(LIMIT);
        new Thread(() -> {
            BigInteger p = TWO;
            while (true) {
                BigInteger candidate = TWO.pow(p.intValueExact()).subtract(ONE); // A candidate Mersenne number
                CheckMersennePrimes check = new CheckMersennePrimes(candidate);
                try {
                    queue.put(executor.submit(check));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                p = p.nextProbablePrime(); // Advances to the next prime p
            }
        }).start();

        int count = 0;
        while (count < LIMIT) {
            BigInteger tmp = null;
            try {
                tmp = queue.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (tmp != null) {
                count++;
                System.out.println(tmp);
            }
        }

        double totalTime = (System.nanoTime() - startTime) / 1_000_000_000.0;
        System.out.printf("It took %.2f seconds to find %d Mersenne primes.\n", totalTime, LIMIT);

        executor.shutdown();
        System.exit(0); // Forces background threads to quit, also
    }
}
