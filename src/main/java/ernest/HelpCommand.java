package ernest;

/**
 * Displays guidance for Ernest's supported commands.
 */
public final class HelpCommand extends Command {
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showHelpMessage();
    }
}
