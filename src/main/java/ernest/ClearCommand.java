package ernest;

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
