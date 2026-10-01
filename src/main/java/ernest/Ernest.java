package ernest;

/**
 * Runs Ernest, a simple command-line task manager.
 */
public final class Ernest {
    private static final String DEFAULT_DATA_FILE_PATH = "data/ernest.txt";

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
            Command command = Parser.parse(ui.readCommand());
            command.execute(taskList, ui, storage);
            if (command.isExit()) {
                return;
            }
            ui.showHorizontalLine();
        }
    }
}
