package ernest;

import java.util.Locale;
import java.util.Scanner;

/**
 * Runs Ernest, a simple command-line task manager.
 */
public final class Ernest {
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

    private Ernest() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Starts the Ernest command-line task manager.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        printWelcomeMessage();
        runChat();
        System.out.println("Bye. See you again soon!");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Prints Ernest's welcome message and available exit instruction.
     */
    private static void printWelcomeMessage() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.printf("Hi! I'm %s.%n", CHATBOT_NAME);
        System.out.println("How can I help you?");
        System.out.println(HORIZONTAL_LINE);
        System.out.println("(Type \"bye\" to exit the chat)");
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     */
    private static void runChat() {
        TaskList taskList = new TaskList();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                String normalizedCommand = command.toLowerCase(Locale.ROOT);

                if (command.equalsIgnoreCase("bye")) {
                    break;
                } else if (command.equalsIgnoreCase("list")) {
                    TaskList.listTasks(taskList.tasks);
                } else if (normalizedCommand.startsWith("mark ")) {
                    TaskList.markTask(command, taskList.tasks);
                } else if (normalizedCommand.startsWith("unmark ")) {
                    TaskList.unmarkTask(command, taskList.tasks);
                } else if (normalizedCommand.startsWith("todo ") ||
                        normalizedCommand.startsWith("deadline ") ||
                        normalizedCommand.startsWith("event ")) {
                    TaskList.addTask(command, taskList.tasks);
                } else {
                    System.out.println("Sorry, please insert a valid command.");
                }
                System.out.println(HORIZONTAL_LINE);
            }
        }
    }
}
