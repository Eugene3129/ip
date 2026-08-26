package ernest;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Runs Ernest, a simple command-line task manager.
 */
public final class Ernest {
    /** Maximum number of tasks that Ernest can store. */
    private static final int MAX_TASKS = 100;

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
        ArrayList<Task> tasks = new ArrayList<>(MAX_TASKS);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                System.out.print("> ");
                String command = scanner.nextLine();
                String normalizedCommand = command.toLowerCase(Locale.ROOT);

                if (command.equalsIgnoreCase("bye")) {
                    break;
                } else if (command.equalsIgnoreCase("list")) {
                    listTasks(tasks);
                } else if (normalizedCommand.startsWith("mark ")) {
                    markTask(command, tasks);
                } else if (normalizedCommand.startsWith("unmark ")) {
                    unmarkTask(command, tasks);
                } else {
                    addTask(command, tasks);
                }
            }
        }
    }

    /**
     * Prints all tasks and their completion status.
     *
     * @param tasks tasks to print.
     */
    private static void listTasks(ArrayList<Task> tasks) {
        System.out.println("Your to-do list is:");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String status = task.isDone() ? "[X]" : "[ ]";
            System.out.println((i + 1) + ". " + status + " " + task.getTaskName());
        }
    }

    /**
     * Marks a task as done when the command contains a valid task number.
     *
     * @param command mark command entered by the user.
     * @param tasks tasks that can be marked.
     */
    private static void markTask(String command, ArrayList<Task> tasks) {
        try {
            int taskNumber = Integer.parseInt(command.substring(5).strip());

            if (isValidTaskNumber(taskNumber, tasks)) {
                Task task = tasks.get(taskNumber - 1);

                if (!task.isDone()) {
                    task.setDone(true);
                    System.out.println("Well done! Marked task " + taskNumber + " as done.");
                } else {
                    System.out.println("Sorry, task " + taskNumber + " is already done.");
                }
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (NumberFormatException exception) {
            System.out.println("Usage: mark <task number>");
        }
    }

    /**
     * Marks a task as not done when the command contains a valid task number.
     *
     * @param command unmark command entered by the user.
     * @param tasks tasks that can be unmarked.
     */
    private static void unmarkTask(String command, ArrayList<Task> tasks) {
        try {
            int taskNumber = Integer.parseInt(command.substring(7).strip());

            if (isValidTaskNumber(taskNumber, tasks)) {
                Task task = tasks.get(taskNumber - 1);

                if (task.isDone()) {
                    task.setDone(false);
                    System.out.println("Ok, marked task " + taskNumber + " as not done yet.");
                } else {
                    System.out.println("Sorry, task " + taskNumber
                            + " is already marked as not done yet.");
                }
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (NumberFormatException exception) {
            System.out.println("Usage: unmark <task number>");
        }
    }

    /**
     * Adds a new task when the task list has available space.
     *
     * @param taskName task description entered by the user.
     * @param tasks list to which the new task is added.
     */
    private static void addTask(String taskName, ArrayList<Task> tasks) {
        if (tasks.size() < MAX_TASKS) {
            Task task = new Task(taskName);
            tasks.add(task);
            System.out.println("Added to list: " + taskName);
            System.out.println("Current list size: " + tasks.size() + "/" + MAX_TASKS);
        } else {
            System.out.println("The list is full (" + MAX_TASKS + "/" + MAX_TASKS + ").");
        }
    }

    /**
     * Checks whether a one-based task number identifies a task in the list.
     *
     * @param taskNumber one-based task number to check.
     * @param tasks list containing the available tasks.
     * @return true if the number identifies a task; otherwise false.
     */
    private static boolean isValidTaskNumber(int taskNumber, ArrayList<Task> tasks) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }
}
