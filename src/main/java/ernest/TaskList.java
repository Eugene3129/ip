package ernest;

import java.util.ArrayList;

public class TaskList {
    // Creation of TaskList inspired by peilingggg, but code is my own work
    /** Maximum number of tasks that Ernest can store. */
    private static final int MAX_TASKS = 100;

    protected ArrayList<Task> tasks;

    public TaskList(){
        this.tasks = new ArrayList<>();
    }

    /**
     * Prints all tasks and their completion status.
     *
     * @param tasks tasks to print.
     */
    public static void listTasks(ArrayList<Task> tasks) {
        System.out.println("Your to-do list is:");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ". " + task.toString());
        }
    }

    /**
     * Marks a task as done when the command contains a valid task number.
     *
     * @param command mark command entered by the user.
     * @param tasks tasks that can be marked.
     */
    public static void markTask(String command, ArrayList<Task> tasks) {
        try {
            int taskNumber = Integer.parseInt(command.substring(5).strip());
            Task task = getTask(taskNumber, tasks);

            if (task == null) {
                System.out.println("Invalid task number.");
                return;
            }

            if (task.isDone()) {
                System.out.println("Sorry, task " + taskNumber + " is already done.");
                return;
            }

            task.setDone(true);
            System.out.println("Well done! Marked task " + taskNumber + " as done.");
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
    public static void unmarkTask(String command, ArrayList<Task> tasks) {
        try {
            int taskNumber = Integer.parseInt(command.substring(7).strip());
            Task task = getTask(taskNumber, tasks);

            if (task == null) {
                System.out.println("Invalid task number.");
                return;
            }

            if (!task.isDone()) {
                System.out.println("Sorry, task " + taskNumber
                        + " is already marked as not done yet.");
                return;
            }

            task.setDone(false);
            System.out.println("Ok, marked task " + taskNumber + " as not done yet.");
        } catch (NumberFormatException exception) {
            System.out.println("Usage: unmark <task number>");
        }
    }

    /**
     * Adds a new task when the task list has available space.
     *
     * @param taskCommand task command entered by the user.
     * @param tasks list to which the new task is added.
     */
    public static void addTask(String taskCommand, ArrayList<Task> tasks) {
        if (tasks.size() < MAX_TASKS) {
            Task task = createTask(taskCommand);
            tasks.add(task);
            System.out.println("Added to task list:\n> " + task.toString());
            System.out.println("Current list size: " + tasks.size() + "/" + MAX_TASKS);
        } else {
            System.out.println("The list is full (" + MAX_TASKS + "/" + MAX_TASKS + ").");
        }
    }

    private static Task createTask(String taskCommand) {
        if (taskCommand.startsWith("deadline ")) {
            return createDeadline(taskCommand);
        } else if (taskCommand.startsWith("event ")) {
            return createEvent(taskCommand);
        } else {
            return createToDo(taskCommand);
        }
    }

    private static Deadline createDeadline(String taskCommand) {
        int deadlineMarker = taskCommand.indexOf("/by");
        String taskName = taskCommand.substring(9, deadlineMarker).strip();
        String deadline = taskCommand.substring(deadlineMarker + 4).strip();
        return new Deadline(taskName, deadline);
    }

    private static Event createEvent(String taskCommand) {
        int fromMarker = taskCommand.indexOf("/from");
        int toMarker = taskCommand.indexOf("/to");
        String taskName = taskCommand.substring(6, fromMarker).strip();
        String durationStart = taskCommand.substring(fromMarker + 6, toMarker - 1).strip();
        String durationEnd = taskCommand.substring(toMarker + 4).strip();
        return new Event(taskName, durationStart, durationEnd);
    }

    private static ToDo createToDo(String taskCommand) {
        String taskName = taskCommand.substring(taskCommand.indexOf("todo") + 5).strip();
        return new ToDo(taskName);
    }

    private static Task getTask(int taskNumber, ArrayList<Task> tasks) {
        if (!isValidTaskNumber(taskNumber, tasks)) {
            return null;
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Checks whether a one-based task number identifies a task in the list.
     *
     * @param taskNumber one-based task number to check.
     * @param tasks list containing the available tasks.
     * @return true if the number identifies a task; otherwise false.
     */
    public static boolean isValidTaskNumber(int taskNumber, ArrayList<Task> tasks) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }
}
