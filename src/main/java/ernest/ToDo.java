package ernest;

/**
 * Represents a simple task without a date or time range.
 */
public class ToDo extends Task {
    /**
     * Creates a to-do task.
     *
     * @param description description of the task.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
