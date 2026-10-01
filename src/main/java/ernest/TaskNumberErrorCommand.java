package ernest;

/**
 * Reports why a task number could not be parsed.
 */
public final class TaskNumberErrorCommand extends Command {
    /** Task number parsing failure to report. */
    private final Parser.TaskNumberStatus status;

    /**
     * Creates a command that reports a task number parsing failure.
     *
     * @param status task number parsing failure to report.
     */
    public TaskNumberErrorCommand(Parser.TaskNumberStatus status) {
        if (status == Parser.TaskNumberStatus.VALID) {
            throw new IllegalArgumentException("A valid task number has no error to report.");
        }
        this.status = status;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        switch (status) {
            case MISSING:
                ui.showMissingTaskNumberMessage();
                break;
            case NOT_INTEGER:
                ui.showNonIntegerTaskNumberMessage();
                break;
            case VALID:
                throw new IllegalStateException("A valid task number has no error to report.");
        }
    }
}
