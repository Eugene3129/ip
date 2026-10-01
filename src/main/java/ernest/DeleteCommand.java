package ernest;

import java.util.Optional;

/**
 * Deletes a numbered task from Ernest's task list.
 */
public final class DeleteCommand extends Command {
    /** One-based number of the task to delete. */
    private final int taskNumber;

    /**
     * Creates a command that deletes a specified task number.
     *
     * @param taskNumber one-based number of the task to delete.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        Optional<Task> deletedTask = taskList.deleteTask(taskNumber);
        if (deletedTask.isPresent()) {
            saveTasks(taskList, ui, storage);
            ui.showTaskDeletedMessage(deletedTask.get(), taskList.getTaskCount(),
                    taskList.getMaximumTaskCount());
        } else {
            ui.showInvalidTaskNumberMessage();
        }
    }
}
