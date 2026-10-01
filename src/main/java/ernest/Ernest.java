package ernest;

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
        printWelcomeMessage();
        if (runChat(taskList)) {
            System.out.println("Bye. See you again soon!");
            System.out.println(HORIZONTAL_LINE);
        }
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
    private static boolean runChat(TaskList taskList) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                if (!processCommand(command, taskList)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Processes one command and prints the corresponding response.
     *
     * @param commandLine command entered by the user.
     * @param taskList task list to update or display.
     * @return false when the command ends the chat; otherwise true.
     */
    private static boolean processCommand(String commandLine, TaskList taskList) {
        Parser.ParsedCommand command = Parser.parse(commandLine);

        if (command.parts().length == 0) {
            System.out.println("Sorry, please insert a valid command.");
        } else {
            String commandWord = command.parts()[0];
            switch (commandWord) {
                case COMMAND_BYE:
                    if (command.parts().length == 1) {
                        return false;
                    }
                    System.out.println("Sorry, please insert a valid command.");
                    break;
                case COMMAND_LIST:
                    if (command.parts().length == 1) {
                        taskList.listTasks();
                    } else {
                        System.out.println("Sorry, please insert a valid command.");
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
                        System.out.println("Sorry, please insert a valid command.");
                    }
                    break;
                case COMMAND_HELP:
                    if (command.parts().length == 1) {
                        printHelpMessage();
                    } else {
                        System.out.println("Sorry, please insert a valid command.");
                    }
                    break;
                case COMMAND_TODO:
                    // Fallthrough
                case COMMAND_DEADLINE:
                    // Fallthrough
                case COMMAND_EVENT:
                    if (command.parts().length == 1) {
                        System.out.println("Missing task description. Please try again.");
                    } else {
                        taskList.addTask(command.text());
                    }
                    break;
                default:
                    System.out.println("Sorry, please insert a valid command.");
                    break;
            }
        }

        System.out.println(HORIZONTAL_LINE);
        return true;
    }

    /**
     * Prints the commands supported by Ernest.
     */
    private static void printHelpMessage() {
        System.out.println("Available commands:");
        System.out.println("todo DESCRIPTION");
        System.out.println("deadline DESCRIPTION /by DATE");
        System.out.println("event DESCRIPTION /from START /to END");
        System.out.println("list, mark NUMBER, unmark NUMBER, clear, help, bye");
    }
}
