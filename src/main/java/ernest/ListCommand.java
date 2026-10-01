package ernest;

/**
 * Displays all tasks in Ernest's task list.
 */
public final class ListCommand extends Command {
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showTaskList(taskList.getTasks());
    }
}
