package ernest;

/**
 * Runs Ernest, a simple command-line task manager.
 */
public final class Ernest {
    private static final String DEFAULT_DATA_FILE_PATH = "data/ernest.txt";
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

    /** Storage used to load and save tasks. */
    private final Storage storage;

    /** Task list managed by this Ernest instance. */
    private final TaskList taskList;

    /** User interface used for console input and output. */
    private final Ui ui;

    /**
     * Creates an Ernest task manager backed by a specified data file.
     *
     * @param filePath path of the task data file.
     */
    public Ernest(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        Storage.LoadResult loadResult = storage.loadTasks(TaskList.getMaximumTaskCapacity());
        this.taskList = new TaskList(loadResult.tasks());
        this.ui.showTaskLoadingStatus(loadResult.status());
    }

    /**
     * Starts the Ernest command-line task manager.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Ernest(DEFAULT_DATA_FILE_PATH).run();
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
                Command exitCommand = new ExitCommand();
                executeCommand(exitCommand);
                if (exitCommand.isExit()) {
                    return;
                }
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
            executeCommand(new InvalidCommand());
        } else {
            String commandWord = command.parts()[0];
            switch (commandWord) {
                case COMMAND_BYE:
                    executeCommand(new InvalidCommand());
                    break;
                case COMMAND_LIST:
                    if (command.parts().length == 1) {
                        executeCommand(new ListCommand());
                    } else {
                        executeCommand(new InvalidCommand());
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
                        executeCommand(new ClearCommand());
                    } else {
                        executeCommand(new InvalidCommand());
                    }
                    break;
                case COMMAND_HELP:
                    if (command.parts().length == 1) {
                        executeCommand(new HelpCommand());
                    } else {
                        executeCommand(new InvalidCommand());
                    }
                    break;
                case COMMAND_TODO:
                    // Fallthrough
                case COMMAND_DEADLINE:
                    // Fallthrough
                case COMMAND_EVENT:
                    handleAddTaskCommand(command);
                    break;
                default:
                    executeCommand(new InvalidCommand());
                    break;
            }
        }

        ui.showHorizontalLine();
    }

    /**
     * Executes a command using this Ernest instance's components.
     *
     * @param command command to execute.
     */
    private void executeCommand(Command command) {
        command.execute(taskList, ui, storage);
    }

    /**
     * Handles a command that creates and adds a task.
     *
     * @param command parsed task command.
     */
    private void handleAddTaskCommand(Parser.ParsedCommand command) {
        Parser.TaskParseResult parseResult = Parser.parseTask(command);
        if (parseResult.status() == Parser.TaskParseStatus.SUCCESS) {
            executeCommand(new AddCommand(parseResult.task().orElseThrow()));
        } else {
            ui.showTaskParsingErrorMessage(parseResult.status());
        }
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
                executeCommand(new DeleteCommand(taskNumber));
                break;
            case COMMAND_MARK:
                executeCommand(new MarkCommand(taskNumber));
                break;
            case COMMAND_UNMARK:
                executeCommand(new UnmarkCommand(taskNumber));
                break;
            default:
                throw new IllegalArgumentException("Unsupported numbered command: " + commandWord);
        }
    }

}
