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

    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return status + " " + this.taskName;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}
