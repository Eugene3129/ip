package ernest;

import java.util.List;
import java.util.Scanner;

/**
 * Handles console input and output for Ernest.
 */
public final class Ui implements AutoCloseable {
    /** Line used to separate sections of the command-line interface. */
    private static final String HORIZONTAL_LINE = "______________________________________";

    /** ASCII-art banner displayed when Ernest starts. */
    private static final String BANNER = " _____ ____  _     _  ____  ____ _____\n"
            + "| ____|  _ \\| \\   | | ____|/ ___|_   _|\n"
            + "|  _| | |_) |  \\  | |  _|  \\___\\  | |\n"
            + "| |___|  _ /| | \\ | | |___ ___) | | |\n"
            + "|_____|_| \\ |_|  \\|_|_____||____/ |_|\n";

    /** Name displayed by the chatbot. */
    private static final String CHATBOT_NAME = "Ernest";

    /** Reads commands entered through standard input. */
    private final Scanner scanner;

    /**
     * Creates a user interface connected to standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available to read.
     *
     * @return true if another command is available; otherwise false.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next command entered by the user.
     *
     * @return the next command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows Ernest's welcome message and exit instruction.
     */
    public void showWelcomeMessage() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.printf("Hi! I'm %s.%n", CHATBOT_NAME);
        System.out.println("How can I help you?");
        System.out.println(HORIZONTAL_LINE);
        System.out.println("(Type \"bye\" to exit the chat)");
    }

    /**
     * Shows Ernest's farewell message.
     */
    public void showGoodbyeMessage() {
        System.out.println("Bye. See you again soon!");
        showHorizontalLine();
    }

    /**
     * Shows a message explaining that the command is invalid.
     */
    public void showInvalidCommandMessage() {
        System.out.println("Sorry, please insert a valid command.");
    }

    /**
     * Shows a message explaining that a task description is required.
     */
    public void showMissingTaskDescriptionMessage() {
        System.out.println("Missing task description. Please try again.");
    }

    /**
     * Shows all tasks and their completion status.
     *
     * @param tasks tasks to display in list order.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Your to-do list is:");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ". " + task);
        }
    }

    /**
     * Shows confirmation that all tasks were removed.
     */
    public void showTaskListClearedMessage() {
        System.out.println("Task list cleared.");
    }

    /**
     * Shows the task removed from the list and the remaining capacity.
     *
     * @param task deleted task.
     * @param taskCount number of tasks remaining in the list.
     * @param maximumTaskCount maximum number of tasks allowed in the list.
     */
    public void showTaskDeletedMessage(Task task, int taskCount, int maximumTaskCount) {
        System.out.println("Ok, I've deleted this task from the task list:\n> " + task);
        System.out.println("Current list size: " + taskCount + "/" + maximumTaskCount);
    }

    /**
     * Shows the commands supported by Ernest.
     */
    public void showHelpMessage() {
        System.out.println("Available commands:");
        System.out.println("todo DESCRIPTION");
        System.out.println("deadline DESCRIPTION /by DATE");
        System.out.println("event DESCRIPTION /from START /to END");
        System.out.println("list, mark NUMBER, unmark NUMBER, clear, help, bye");
    }

    /**
     * Shows a horizontal line separating command responses.
     */
    public void showHorizontalLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    @Override
    public void close() {
        scanner.close();
    }
}
