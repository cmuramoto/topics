package com.nc.topics.quarkus;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProcessingService {

    public static final int MAX = 40;

    static long fibo(long n) {
        return n < 1
        ? 0L
        : n <= 2
          ? 1L
          : n <= MAX
            ? fibo(n - 1) + fibo(n - 2)
            : -1L;
    }

    public OutputObject process(InputObject input) {
        var result = fibo(input.getMax());
        var out = new OutputObject();
        out.setResult(result);
        return out;
    }
}
