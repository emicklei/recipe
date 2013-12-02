package com.philemonworks.recipe;

import java.util.concurrent.CountDownLatch;

/**
 * RecipeTask is Runnable that counts down a latch after performing its run.
 * 
 * @author emicklei
 */
public abstract class RecipeTask implements Runnable {
    private CountDownLatch latch;

    public abstract void execute() throws Exception;

    public abstract void handleException(Exception ex);

    @Override
    public void run() {
        try {
            this.execute();
        } catch (Exception ex) {
            this.handleException(ex);
        } finally {
            latch.countDown();
        }
    }

    protected void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}