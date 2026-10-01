package ernest;

/**
 * Runs Ernest, a simple command-line task manager.
 */
public final class Ernest {
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private Ernest() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Starts the Ernest command-line task manager.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        TaskList taskList = new TaskList();
        try (Ui ui = new Ui()) {
            ui.showWelcomeMessage();
            if (runChat(taskList, ui)) {
                ui.showGoodbyeMessage();
            }
        }
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     */
    private static boolean runChat(TaskList taskList, Ui ui) {
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();

            if (!processCommand(command, taskList, ui)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Processes one command and prints the corresponding response.
     *
     * @param commandLine command entered by the user.
     * @param taskList task list to update or display.
     * @param ui user interface used to display command responses.
     * @return false when the command ends the chat; otherwise true.
     */
    private static boolean processCommand(String commandLine, TaskList taskList, Ui ui) {
        Parser.ParsedCommand command = Parser.parse(commandLine);

        if (command.parts().length == 0) {
            ui.showInvalidCommandMessage();
        } else {
            String commandWord = command.parts()[0];
            switch (commandWord) {
                case COMMAND_BYE:
                    if (command.parts().length == 1) {
                        return false;
                    }
                    ui.showInvalidCommandMessage();
                    break;
                case COMMAND_LIST:
                    if (command.parts().length == 1) {
                        taskList.listTasks();
                    } else {
                        ui.showInvalidCommandMessage();
                    }
                    break;
                case COMMAND_DELETE:
                    taskList.deleteTask(command.text());
                    break;
                case COMMAND_MARK:
                    taskList.markTask(command.text());
                    break;
                case COMMAND_UNMARK:
                    taskList.unmarkTask(command.text());
                    break;
                case COMMAND_CLEAR:
                    if (command.parts().length == 1) {
                        taskList.clearTasks();
                    } else {
                        ui.showInvalidCommandMessage();
                    }
                    break;
                case COMMAND_HELP:
                    if (command.parts().length == 1) {
                        ui.showHelpMessage();
                    } else {
                        ui.showInvalidCommandMessage();
                    }
                    break;
                case COMMAND_TODO:
                    // Fallthrough
                case COMMAND_DEADLINE:
                    // Fallthrough
                case COMMAND_EVENT:
                    if (command.parts().length == 1) {
                        ui.showMissingTaskDescriptionMessage();
                    } else {
                        taskList.addTask(command.text());
                    }
                    break;
                default:
                    ui.showInvalidCommandMessage();
                    break;
            }
        }

        ui.showHorizontalLine();
        return true;
    }
}
