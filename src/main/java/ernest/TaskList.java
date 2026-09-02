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
    public static void unmarkTask(String command, ArrayList<Task> tasks) {
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
     * @param taskCommand task command entered by the user.
     * @param tasks list to which the new task is added.
     */
    public static void addTask(String taskCommand, ArrayList<Task> tasks) {
        if (tasks.size() < MAX_TASKS) {
            Task task;
            String taskName;
            // Commands are filtered out to be deadline, event or todo
            if (taskCommand.startsWith("deadline ")) {
                taskName = taskCommand.substring(9, taskCommand.indexOf("/by")).strip();
                String deadline = taskCommand.substring(taskCommand.indexOf("/by") + 4).strip();
                task = new Deadline(taskName, deadline);
            } else if (taskCommand.startsWith("event ")) {
                taskName = taskCommand.substring(6, taskCommand.indexOf("/from")).strip();
                String duration_start = taskCommand.substring(taskCommand.indexOf("/from") + 6,
                                        taskCommand.indexOf("/to") - 1).strip();
                String duration_end = taskCommand.substring(taskCommand.indexOf("/to") + 4).strip();
                task = new Event(taskName, duration_start, duration_end);
            } else { // If task is todo
                taskName = taskCommand.substring(taskCommand.indexOf("todo") + 5).strip();
                task = new ToDo(taskName);
            }
            tasks.add(task);
            System.out.println("Added to task list:\n> " + task.toString());
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
    public static boolean isValidTaskNumber(int taskNumber, ArrayList<Task> tasks) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }
}
