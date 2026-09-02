package ernest;

/**
 * Represents a task that has a due date.
 */
public class Deadline extends Task {

    /** Due date associated with this task. */
    private final String dueDate;

    /**
     * Creates a deadline task.
     *
     * @param description description of the task.
     * @param dueDate due date for the task.
     */
    public Deadline(String description, String dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + dueDate + ")";
    }
}
