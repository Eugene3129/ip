package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Displays tasks whose descriptions contain a search query.
 */
public final class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches task descriptions for the given keyword.
     *
     * @param keyword text to search for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showMatchingTasks(taskList.findTasks(keyword));
    }
}
