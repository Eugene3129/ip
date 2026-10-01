package ernest;

/**
 * Reports a command that Ernest does not recognize.
 */
public final class InvalidCommand extends Command {
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showInvalidCommandMessage();
    }
}
