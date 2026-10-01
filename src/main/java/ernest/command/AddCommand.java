package ernest.command;

import ernest.storage.Storage;
import ernest.task.Task;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Adds a task to Ernest's task list.
 */
public final class AddCommand extends Command {
    /** Task to add. */
    private final Task task;

    /**
     * Creates a command that adds a specified task.
     *
     * @param task task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        TaskList.AddStatus status = taskList.addTask(task);
        switch (status) {
            case SUCCESS:
                saveTasks(taskList, ui, storage);
                ui.showTaskAddedMessage(task, taskList.getTaskCount(),
                        taskList.getMaximumTaskCount());
                break;
            case FULL:
                ui.showTaskListFullMessage(taskList.getTaskCount(),
                        taskList.getMaximumTaskCount());
                break;
        }
    }
}
