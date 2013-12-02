package com.philemonworks.recipe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TimingTest {

    public static final class TimingRecipe extends ExecutorBasedRecipe {

        long completedAt;
        
        public TimingRecipe(ExecutorService exec, int completionTimeout) {
            super(exec, completionTimeout);
        }
        @Override
        protected void submitTasks() {
            this.submit(new RecipeTask() {                
                @Override
                public void handleException(Exception ex) {
                    System.err.println(ex.getMessage());
                }               
                @Override
                public void execute() throws Exception {
                    Thread.sleep(1000);
                    TimingRecipe.this.completedAt = System.currentTimeMillis();    
                }
            });            
        }        
    }
    
    @Test
    public void testHappyFlow() {
        TimingRecipe r = new TimingRecipe(Executors.newFixedThreadPool(2), 5000);
        long startedAt = System.currentTimeMillis();
        r.run();
        System.out.println("Completed in:" + (r.completedAt - startedAt));
    }
}
