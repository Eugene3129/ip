package ernest;

/**
 * Represents a task in Ernest's to-do list.
 */
public class Task {
    private String taskName;
    private boolean isDone;

    /**
     * Creates an empty, incomplete task.
     */
    public Task() {
        this.taskName = "";
        this.isDone = false;
    }

    /**
     * Creates an incomplete task with the given description.
     *
     * @param taskName description of the task.
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}
