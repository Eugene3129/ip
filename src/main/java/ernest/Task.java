package ernest;

/**
 * Represents a task in Ernest's to-do list.
 */
public class Task {
    private String taskName;
    private boolean isDone;
    private String status;

    /**
     * Creates an empty, incomplete task.
     */
    public Task() {
        this.taskName = "";
        this.isDone = false;
        this.status = "[ ]";
    }

    /**
     * Creates an incomplete task with the given description.
     *
     * @param taskName description of the task.
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
        this.status = "[ ]";
    }

    @Override
    public String toString() {
        return this.status + " " + this.taskName;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
        this.status = this.isDone() ? "[X]" : "[ ]";
    }
}
