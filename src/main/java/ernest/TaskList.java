package ernest;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Stores and manages the tasks in Ernest's to-do list.
 */
public final class TaskList {
    // Creation of TaskList inspired by peilingggg, but code is my own work
    /** Maximum number of tasks that Ernest can store. */
    private static final int MAX_TASKS = 100;
    private static final String TODO_PREFIX = "todo ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String DEADLINE_MARKER = "/by";
    private static final String EVENT_FROM_MARKER = "/from";
    private static final String EVENT_TO_MARKER = "/to";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";

    /** Tasks currently stored in this list. */
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list loaded from the local data file.
     */
    public TaskList() {
        this.tasks = Storage.loadTasks(MAX_TASKS);
    }

    /**
     * Prints all tasks and their completion status.
     *
     */
    public void listTasks() {
        System.out.println("Your to-do list is:");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ". " + task.toString());
        }
    }

    /**
     * Removes every task from the list and saves the empty list.
     *
     */
    public void clearTasks() {
        tasks.clear();
        saveTasks();
        System.out.println("Task list cleared.");
    }

    /**
     * Marks a task as done when the command contains a valid task number.
     *
     * @param command mark command entered by the user.
     */
    public void markTask(String command) {
        String taskNumberText = getCommandArgument(command, MARK_COMMAND);
        if (taskNumberText.isEmpty()) {
            System.out.println("Missing task number. Please refer to the tasks list and "
                    + "try again.");
            return;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            System.out.println("Task number must be an integer.");
            return;
        }

        Task task = getTask(taskNumber, tasks);

        if (task == null) {
            System.out.println("Invalid task number. Please refer to the tasks list and "
                    + "try again.");
            return;
        }

        if (task.isDone()) {
            System.out.println("Sorry, task " + taskNumber + " is already done.");
            return;
        }

        task.setDone(true);
        saveTasks();
        System.out.println("Well done! Marked task " + taskNumber + " as done.");
    }

    /**
     * Marks a task as not done when the command contains a valid task number.
     *
     * @param command unmark command entered by the user.
     */
    public void unmarkTask(String command) {
        String taskNumberText = getCommandArgument(command, UNMARK_COMMAND);
        if (taskNumberText.isEmpty()) {
            System.out.println("Missing task number. Please refer to the tasks list and "
                    + "try again.");
            return;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            System.out.println("Task number must be an integer.");
            return;
        }

        Task task = getTask(taskNumber, tasks);

        if (task == null) {
            System.out.println("Invalid task number. Please refer to the tasks list and "
                    + "try again.");
            return;
        }

        if (!task.isDone()) {
            System.out.println("Sorry, task " + taskNumber
                    + " is already marked as not done yet.");
            return;
        }

        task.setDone(false);
        saveTasks();
        System.out.println("Ok, marked task " + taskNumber + " as not done yet.");
    }

    /**
     * Adds a new task when the task list has available space.
     *
     * @param taskCommand task command entered by the user.
     */
    public void addTask(String taskCommand) {
        if (tasks.size() < MAX_TASKS) {
            String normalizedTaskCommand = taskCommand.strip().replaceFirst("\\s+", " ");
            String validationMessage = getTaskValidationMessage(normalizedTaskCommand);
            if (validationMessage != null) {
                System.out.println(validationMessage);
                return;
            }

            Task task = createTask(normalizedTaskCommand);
            if (task == null) {
                System.out.println("Sorry, please insert a valid task.");
                return;
            }

            tasks.add(task);
            saveTasks();
            System.out.println("Added to task list:\n> " + task.toString());
            System.out.println("Current list size: " + tasks.size() + "/" + MAX_TASKS);
        } else {
            System.out.println("The list is full (" + MAX_TASKS + "/" + MAX_TASKS + ").");
        }
    }

    /**
     * Saves the current task list and warns the user when saving fails.
     */
    private void saveTasks() {
        if (!Storage.saveTasks(tasks)) {
            System.out.println("Warning: Task changes could not be saved.");
        }
    }

    /**
     * Creates the task represented by a supported task command.
     * The command type is matched without regard to letter case before the
     * corresponding task subtype is constructed.
     *
     * @param taskCommand command containing the task type and details.
     * @return task represented by the command.
     */
    private static Task createTask(String taskCommand) {
        String normalizedCommand = taskCommand.toLowerCase(Locale.ROOT);

        if (normalizedCommand.startsWith(DEADLINE_PREFIX)) {
            return createDeadline(taskCommand);
        } else if (normalizedCommand.startsWith(EVENT_PREFIX)) {
            return createEvent(taskCommand);
        } else if (normalizedCommand.startsWith(TODO_PREFIX)) {
            return createToDo(taskCommand);
        }
        return null;
    }

    /**
     * Creates a deadline from a deadline command.
     *
     * @param taskCommand deadline command containing a description and due date.
     * @return deadline represented by the command.
     */
    private static Deadline createDeadline(String taskCommand) {
        int deadlineMarker = findMarker(taskCommand.toLowerCase(Locale.ROOT), DEADLINE_MARKER);
        String taskName = taskCommand.substring(DEADLINE_PREFIX.length(), deadlineMarker).strip();
        String deadline = taskCommand.substring(deadlineMarker + DEADLINE_MARKER.length()).strip();
        return new Deadline(taskName, deadline);
    }

    /**
     * Creates an event from an event command.
     *
     * @param taskCommand event command containing a description and time range.
     * @return event represented by the command.
     */
    private static Event createEvent(String taskCommand) {
        String normalizedCommand = taskCommand.toLowerCase(Locale.ROOT);
        int fromMarker = findMarker(normalizedCommand, EVENT_FROM_MARKER);
        int toMarker = findMarker(normalizedCommand, EVENT_TO_MARKER);
        String taskName = taskCommand.substring(EVENT_PREFIX.length(), fromMarker).strip();
        String durationStart = taskCommand.substring(fromMarker + EVENT_FROM_MARKER.length(), toMarker)
                .strip();
        String durationEnd = taskCommand.substring(toMarker + EVENT_TO_MARKER.length()).strip();
        return new Event(taskName, durationStart, durationEnd);
    }

    /**
     * Creates a to-do task from a to-do command.
     *
     * @param taskCommand to-do command containing a description.
     * @return to-do task represented by the command.
     */
    private static ToDo createToDo(String taskCommand) {
        String taskName = taskCommand.substring(TODO_PREFIX.length()).strip();
        return new ToDo(taskName);
    }

    /**
     * Returns a validation message for an invalid task command.
     *
     * @param taskCommand task command to validate.
     * @return validation message, or {@code null} when the command is valid.
     */
    private static String getTaskValidationMessage(String taskCommand) {
        String normalizedCommand = taskCommand.toLowerCase(Locale.ROOT);

        if (normalizedCommand.startsWith(TODO_PREFIX)) {
            String taskName = taskCommand.substring(TODO_PREFIX.length()).strip();
            return taskName.isEmpty() ? "Missing task description. Please try again." : null;
        }

        if (normalizedCommand.startsWith(DEADLINE_PREFIX)) {
            int deadlineMarker = findMarker(normalizedCommand, DEADLINE_MARKER);
            if (deadlineMarker < 0) {
                return "Deadline must include a /by date.";
            }
            if (findMarker(normalizedCommand, DEADLINE_MARKER,
                    deadlineMarker + DEADLINE_MARKER.length()) >= 0) {
                return "Deadline may contain only one /by marker.";
            }

            String taskName = taskCommand.substring(DEADLINE_PREFIX.length(), deadlineMarker).strip();
            String deadline = taskCommand.substring(deadlineMarker + DEADLINE_MARKER.length()).strip();
            if (taskName.isEmpty()) {
                return "Missing deadline description. Please try again.";
            }
            return deadline.isEmpty() ? "Missing deadline date. Please try again." : null;
        }

        if (normalizedCommand.startsWith(EVENT_PREFIX)) {
            int fromMarker = findMarker(normalizedCommand, EVENT_FROM_MARKER);
            int toMarker = findMarker(normalizedCommand, EVENT_TO_MARKER);
            if (fromMarker < 0) {
                return "Event must include a /from time.";
            }
            if (toMarker < 0) {
                return "Event must include a /to time.";
            }
            if (toMarker <= fromMarker) {
                return "The /to marker must come after /from.";
            }
            if (findMarker(normalizedCommand, EVENT_FROM_MARKER,
                    fromMarker + EVENT_FROM_MARKER.length()) >= 0
                    || findMarker(normalizedCommand, EVENT_TO_MARKER,
                    toMarker + EVENT_TO_MARKER.length()) >= 0) {
                return "Event may only contain one /from and one /to marker.";
            }

            String taskName = taskCommand.substring(EVENT_PREFIX.length(), fromMarker).strip();
            String durationStart = taskCommand.substring(fromMarker + EVENT_FROM_MARKER.length(),
                    toMarker).strip();
            String durationEnd = taskCommand.substring(toMarker + EVENT_TO_MARKER.length()).strip();
            if (taskName.isEmpty()) {
                return "Missing event description. Please try again.";
            }
            if (durationStart.isEmpty()) {
                return "Missing event start time. Please try again.";
            }
            return durationEnd.isEmpty() ? "Missing event end time. Please try again." : null;
        }

        return "Sorry, please insert a valid task.";
    }

    /**
     * Returns an argument when a command has the expected command word.
     *
     * @param command complete command entered by the user.
     * @param commandWord expected command word.
     * @return stripped command argument, or an empty string when it is missing or malformed.
     */
    private static String getCommandArgument(String command, String commandWord) {
        String trimmedCommand = command.strip();
        if (trimmedCommand.length() <= commandWord.length()
                || !trimmedCommand.regionMatches(true, 0, commandWord, 0, commandWord.length())
                || !Character.isWhitespace(trimmedCommand.charAt(commandWord.length()))) {
            return "";
        }
        return trimmedCommand.substring(commandWord.length()).strip();
    }

    /**
     * Returns the position of a marker that is separated from surrounding text.
     *
     * @param command normalized command to search.
     * @param marker marker to find.
     * @return marker position, or {@code -1} when no valid marker is present.
     */
    private static int findMarker(String command, String marker) {
        return findMarker(command, marker, 0);
    }

    /**
     * Returns the position of a separated marker after a given position.
     *
     * @param command normalized command to search.
     * @param marker marker to find.
     * @param searchFrom position at which to start searching.
     * @return marker position, or {@code -1} when no valid marker is present.
     */
    private static int findMarker(String command, String marker, int searchFrom) {
        int markerPosition = command.indexOf(marker, searchFrom);
        while (markerPosition >= 0) {
            int markerEnd = markerPosition + marker.length();
            boolean hasWhitespaceBefore = markerPosition > 0
                    && Character.isWhitespace(command.charAt(markerPosition - 1));
            boolean hasWhitespaceAfter = markerEnd == command.length()
                    || Character.isWhitespace(command.charAt(markerEnd));
            if (hasWhitespaceBefore && hasWhitespaceAfter) {
                return markerPosition;
            }
            markerPosition = command.indexOf(marker, markerEnd);
        }
        return -1;
    }

    /**
     * Returns the task at a valid one-based task number.
     *
     * @param taskNumber one-based task number to look up.
     * @param tasks list containing the available tasks.
     * @return matching task, or {@code null} when the number is invalid.
     */
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
