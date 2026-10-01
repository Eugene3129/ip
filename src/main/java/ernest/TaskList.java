package ernest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stores and manages the tasks in Ernest's to-do list.
 */
public final class TaskList {
    // Creation of TaskList inspired by peilingggg, but code is my own work
    /** Maximum number of tasks that Ernest can store. */
    private static final int MAX_TASKS = 100;

    /** Tasks currently stored in this list. */
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list loaded from the local data file.
     */
    public TaskList() {
        this.tasks = Storage.loadTasks(MAX_TASKS);
    }

    /**
     * Returns an unmodifiable snapshot of the stored tasks.
     *
     * @return tasks in their current list order.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return number of stored tasks.
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Returns the maximum number of tasks that can be stored.
     *
     * @return maximum number of tasks.
     */
    public int getMaximumTaskCount() {
        return MAX_TASKS;
    }

    /**
     * Removes every task from the list and saves the empty list.
     */
    public void clearTasks() {
        tasks.clear();
        saveTasks();
    }

    /**
     * Marks a task as done when the task exists and is not already done.
     *
     * @param taskNumber one-based number of the task to mark.
     * @return outcome of the mark operation.
     */
    public MarkStatus markTask(int taskNumber) {
        Task task = getTask(taskNumber);
        if (task == null) {
            return MarkStatus.INVALID_TASK_NUMBER;
        }

        if (task.isDone()) {
            return MarkStatus.ALREADY_DONE;
        }

        task.setDone(true);
        saveTasks();
        return MarkStatus.SUCCESS;
    }

    /**
     * Marks a task as not done when the task exists and is currently done.
     *
     * @param taskNumber one-based number of the task to unmark.
     * @return outcome of the unmark operation.
     */
    public UnmarkStatus unmarkTask(int taskNumber) {
        Task task = getTask(taskNumber);
        if (task == null) {
            return UnmarkStatus.INVALID_TASK_NUMBER;
        }

        if (!task.isDone()) {
            return UnmarkStatus.ALREADY_NOT_DONE;
        }

        task.setDone(false);
        saveTasks();
        return UnmarkStatus.SUCCESS;
    }

    /**
     * Adds a task when the list has space.
     *
     * @param task task to add.
     * @return outcome of the add operation.
     */
    public AddStatus addTask(Task task) {
        if (tasks.size() >= MAX_TASKS) {
            return AddStatus.FULL;
        }

        tasks.add(task);
        saveTasks();
        return AddStatus.SUCCESS;
    }

    /**
     * Deletes a task when the task number is valid.
     *
     * @param taskNumber one-based number of the task to delete.
     * @return deleted task, or an empty result when the task number is invalid.
     */
    public Optional<Task> deleteTask(int taskNumber) {
        Task task = getTask(taskNumber);
        if (task == null) {
            return Optional.empty();
        }

        tasks.remove(taskNumber - 1);
        saveTasks();
        return Optional.of(task);
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
     * Returns the task at a valid one-based task number.
     *
     * @param taskNumber one-based task number to look up.
     * @return matching task, or {@code null} when the number is invalid.
     */
    private Task getTask(int taskNumber) {
        if (!isValidTaskNumber(taskNumber)) {
            return null;
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Checks whether a one-based task number identifies a task in the list.
     *
     * @param taskNumber one-based task number to check.
     * @return true if the number identifies a task; otherwise false.
     */
    private boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Describes the outcome of marking a task as done.
     */
    public enum MarkStatus {
        SUCCESS,
        INVALID_TASK_NUMBER,
        ALREADY_DONE
    }

    /**
     * Describes the outcome of marking a task as not done.
     */
    public enum UnmarkStatus {
        SUCCESS,
        INVALID_TASK_NUMBER,
        ALREADY_NOT_DONE
    }

    /**
     * Describes the outcome of adding a task.
     */
    public enum AddStatus {
        SUCCESS,
        FULL
    }
}
