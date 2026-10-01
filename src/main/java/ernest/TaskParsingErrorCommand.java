package ernest;

/**
 * Reports why a task-creation command could not be parsed.
 */
public final class TaskParsingErrorCommand extends Command {
    /** Task parsing failure to report. */
    private final Parser.TaskParseStatus status;

    /**
     * Creates a command that reports a task parsing failure.
     *
     * @param status task parsing failure to report.
     */
    public TaskParsingErrorCommand(Parser.TaskParseStatus status) {
        if (status == Parser.TaskParseStatus.SUCCESS) {
            throw new IllegalArgumentException("A successful task parse has no error to report.");
        }
        this.status = status;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showTaskParsingErrorMessage(status);
    }
}
