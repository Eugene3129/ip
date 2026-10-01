package ernest;

/**
 * Marks a numbered task as done.
 */
public final class MarkCommand extends Command {
    /** One-based number of the task to mark. */
    private final int taskNumber;

    /**
     * Creates a command that marks a specified task number.
     *
     * @param taskNumber one-based number of the task to mark.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        TaskList.MarkStatus status = taskList.markTask(taskNumber);
        switch (status) {
            case SUCCESS:
                saveTasks(taskList, ui, storage);
                ui.showTaskMarkedMessage(taskNumber);
                break;
            case INVALID_TASK_NUMBER:
                ui.showInvalidTaskNumberMessage();
                break;
            case ALREADY_DONE:
                ui.showTaskAlreadyDoneMessage(taskNumber);
                break;
        }
    }
}
