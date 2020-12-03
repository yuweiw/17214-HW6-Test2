package edu.cmu.cs.cs214.rec13;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public class CheckMersennePrimes implements Callable {
    private BigInteger num;

    public CheckMersennePrimes(BigInteger num) {
        this.num = num;
    }

    @Override
    public Object call() throws Exception {
        if (num.isProbablePrime(50)) return num;
        return null;
    }
}
