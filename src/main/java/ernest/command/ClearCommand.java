package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Removes every task from Ernest's task list.
 */
public final class ClearCommand extends Command {
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        taskList.clearTasks();
        saveTasks(taskList, ui, storage);
        ui.showTaskListClearedMessage();
    }
}
