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

    /** Task list managed by this Ernest instance. */
    private final TaskList taskList;

    /** User interface used for console input and output. */
    private final Ui ui;

    /**
     * Creates an Ernest task manager with its task list and user interface.
     */
    public Ernest() {
        this.taskList = new TaskList();
        this.ui = new Ui();
    }

    /**
     * Starts the Ernest command-line task manager.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Ernest().run();
    }

    /**
     * Runs the command loop until the user exits or input ends.
     */
    public void run() {
        try {
            ui.showWelcomeMessage();
            if (runChat()) {
                ui.showGoodbyeMessage();
            }
        } finally {
            ui.close();
        }
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     */
    private boolean runChat() {
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();

            if (!processCommand(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Processes one command and prints the corresponding response.
     *
     * @param commandLine command entered by the user.
     * @return false when the command ends the chat; otherwise true.
     */
    private boolean processCommand(String commandLine) {
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
