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
     *
     * @return operation result containing the number of removed tasks and save status.
     */
    public OperationResult<Integer> clearTasks() {
        int clearedTaskCount = tasks.size();
        tasks.clear();
        return saveMutation(clearedTaskCount);
    }

    /**
     * Marks a task as done when the task exists and is not already done.
     *
     * @param taskNumber one-based number of the task to mark.
     * @return operation result containing the mark outcome and save status.
     */
    public OperationResult<MarkStatus> markTask(int taskNumber) {
        Task task = getTask(taskNumber);
        if (task == null) {
            return new OperationResult<>(MarkStatus.INVALID_TASK_NUMBER, false);
        }

        if (task.isDone()) {
            return new OperationResult<>(MarkStatus.ALREADY_DONE, false);
        }

        task.setDone(true);
        return saveMutation(MarkStatus.SUCCESS);
    }

    /**
     * Marks a task as not done when the task exists and is currently done.
     *
     * @param taskNumber one-based number of the task to unmark.
     * @return operation result containing the unmark outcome and save status.
     */
    public OperationResult<UnmarkStatus> unmarkTask(int taskNumber) {
        Task task = getTask(taskNumber);
        if (task == null) {
            return new OperationResult<>(UnmarkStatus.INVALID_TASK_NUMBER, false);
        }

        if (!task.isDone()) {
            return new OperationResult<>(UnmarkStatus.ALREADY_NOT_DONE, false);
        }

        task.setDone(false);
        return saveMutation(UnmarkStatus.SUCCESS);
    }

    /**
     * Adds a task when the list has space.
     *
     * @param task task to add.
     * @return operation result containing the add outcome and save status.
     */
    public OperationResult<AddStatus> addTask(Task task) {
        if (tasks.size() >= MAX_TASKS) {
            return new OperationResult<>(AddStatus.FULL, false);
        }

        tasks.add(task);
        return saveMutation(AddStatus.SUCCESS);
    }

    /**
     * Deletes a task when the task number is valid.
     *
     * @param taskNumber one-based number of the task to delete.
     * @return operation result containing the deleted task and save status.
     */
    public OperationResult<Optional<Task>> deleteTask(int taskNumber) {
        Task task = getTask(taskNumber);
        if (task == null) {
            return new OperationResult<>(Optional.empty(), false);
        }

        tasks.remove(taskNumber - 1);
        return saveMutation(Optional.of(task));
    }

    /**
     * Saves the current task list and returns an operation result.
     *
     * @param outcome domain outcome of the completed mutation.
     * @param <T> type of the domain outcome.
     * @return operation result containing the outcome and save status.
     */
    private <T> OperationResult<T> saveMutation(T outcome) {
        boolean hasSaveFailure = !Storage.saveTasks(tasks);
        return new OperationResult<>(outcome, hasSaveFailure);
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

    /**
     * Stores a task-list operation's domain outcome and persistence result.
     *
     * @param outcome domain outcome of the operation.
     * @param hasSaveFailure whether a completed mutation could not be saved.
     * @param <T> type of the domain outcome.
     */
    public record OperationResult<T>(T outcome, boolean hasSaveFailure) {
    }
}
