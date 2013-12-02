package com.philemonworks.recipe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Base implementation of the Recipe pattern.
 * 
 * @author emicklei
 */
public abstract class AbstractRecipe {
    protected CountDownLatch latch;
    protected boolean timedOut;
    protected int completionTimeout = 1000; // 1 sec

    abstract public int howManyConcurrentRequests();

    abstract protected void executeRequests();

    public void setTimeout(int milliseconds) {
        this.completionTimeout = milliseconds;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public int getCompletionTimeout() {
        return completionTimeout;
    }

    /**
     * Execute all the requests for this recipe.
     * Wait for them to complete unless time exceeds this timeout.
     */
    public void run() {
        this.latch = new CountDownLatch(this.howManyConcurrentRequests());
        this.timedOut = false;
        this.executeRequests();
        try {
            this.timedOut = !this.latch.await(this.completionTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to wait for countdown latch", e);
        }
    }
}