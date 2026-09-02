package ernest;

/**
 * Represents a task that has a due date.
 */
public class Deadline extends Task {

    /** Due date associated with this task. */
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description description of the task.
     * @param by due date for the task.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
