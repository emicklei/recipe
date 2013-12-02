package com.philemonworks.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class ExecutorBasedRecipe extends AbstractRecipe {
    ExecutorService executorService;

    public ExecutorBasedRecipe(ExecutorService exec, int completionTimeout) {
        this.executorService = exec;
        this.completionTimeout = completionTimeout;
    }

    /**
     * Call submit(...) for each RecipeTask you want to run concurrently.
     */
    abstract protected void submitTasks();

    /**
     * RecipeTasks to run concurrently.
     */
    private List<RecipeTask> submittedTasks = new ArrayList<RecipeTask>();

    /**
     * Schedule a new RecipeTask to run concurrently
     * @param task
     */
    public void submit(RecipeTask task) {
        submittedTasks.add(task);
    }

    public void run() {
        this.submitTasks();
        super.run();
    }

    @Override
    public int howManyConcurrentRequests() {
        return submittedTasks.size();
    }

    @Override
    protected void executeRequests() {
        for (RecipeTask each : submittedTasks) {
            each.setLatch(this.getLatch());
            this.executorService.execute(each);
        }
    }
}
