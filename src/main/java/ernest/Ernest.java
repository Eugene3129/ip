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
            runChat();
        } finally {
            ui.close();
        }
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     */
    private void runChat() {
        while (ui.hasNextCommand()) {
            Parser.ParsedCommand command = Parser.parse(ui.readCommand());

            if (isExitCommand(command)) {
                ui.showGoodbyeMessage();
                return;
            }
            handleCommand(command);
        }
    }

    /**
     * Returns whether the command requests a valid chat exit.
     *
     * @param command parsed command entered by the user.
     * @return true if the command exits the chat; otherwise false.
     */
    private static boolean isExitCommand(Parser.ParsedCommand command) {
        return command.parts().length == 1 && COMMAND_BYE.equals(command.parts()[0]);
    }

    /**
     * Handles one command and prints the corresponding response.
     *
     * @param command parsed command entered by the user.
     */
    private void handleCommand(Parser.ParsedCommand command) {
        if (command.parts().length == 0) {
            ui.showInvalidCommandMessage();
        } else {
            String commandWord = command.parts()[0];
            switch (commandWord) {
                case COMMAND_BYE:
                    ui.showInvalidCommandMessage();
                    break;
                case COMMAND_LIST:
                    if (command.parts().length == 1) {
                        ui.showTaskList(taskList.getTasks());
                    } else {
                        ui.showInvalidCommandMessage();
                    }
                    break;
                case COMMAND_DELETE:
                    // Fallthrough
                case COMMAND_MARK:
                    // Fallthrough
                case COMMAND_UNMARK:
                    handleNumberedTaskCommand(command);
                    break;
                case COMMAND_CLEAR:
                    if (command.parts().length == 1) {
                        taskList.clearTasks();
                        ui.showTaskListClearedMessage();
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
                        Task addedTask = taskList.addTask(command.text());
                        if (addedTask != null) {
                            ui.showTaskAddedMessage(addedTask, taskList.getTaskCount(),
                                    taskList.getMaximumTaskCount());
                        }
                    }
                    break;
                default:
                    ui.showInvalidCommandMessage();
                    break;
            }
        }

        ui.showHorizontalLine();
    }

    /**
     * Handles a task command that requires a numeric task argument.
     *
     * @param command parsed numbered task command.
     */
    private void handleNumberedTaskCommand(Parser.ParsedCommand command) {
        Parser.TaskNumberParseResult parseResult = Parser.parseTaskNumber(command);

        switch (parseResult.status()) {
            case MISSING:
                ui.showMissingTaskNumberMessage();
                break;
            case NOT_INTEGER:
                ui.showNonIntegerTaskNumberMessage();
                break;
            case VALID:
                executeNumberedTaskCommand(command.parts()[0], parseResult.taskNumber());
                break;
        }
    }

    /**
     * Executes a numbered task command using an already parsed task number.
     *
     * @param commandWord normalized command word.
     * @param taskNumber parsed task number.
     */
    private void executeNumberedTaskCommand(String commandWord, int taskNumber) {
        switch (commandWord) {
            case COMMAND_DELETE:
                Task deletedTask = taskList.deleteTask(taskNumber);
                if (deletedTask != null) {
                    ui.showTaskDeletedMessage(deletedTask, taskList.getTaskCount(),
                            taskList.getMaximumTaskCount());
                }
                break;
            case COMMAND_MARK:
                Integer markedTaskNumber = taskList.markTask(taskNumber);
                if (markedTaskNumber != null) {
                    ui.showTaskMarkedMessage(markedTaskNumber);
                }
                break;
            case COMMAND_UNMARK:
                Integer unmarkedTaskNumber = taskList.unmarkTask(taskNumber);
                if (unmarkedTaskNumber != null) {
                    ui.showTaskUnmarkedMessage(unmarkedTaskNumber);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported numbered command: " + commandWord);
        }
    }
}
