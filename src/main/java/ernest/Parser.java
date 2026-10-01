package ernest;

import java.util.Locale;
import java.util.Optional;

/**
 * Converts raw user input into a form that Ernest can process.
 */
public final class Parser {
    private static final String TODO_PREFIX = "todo ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String DEADLINE_MARKER = "/by";
    private static final String EVENT_FROM_MARKER = "/from";
    private static final String EVENT_TO_MARKER = "/to";

    private Parser() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Trims and normalizes a command while preserving its original casing.
     *
     * @param commandLine command entered by the user.
     * @return the parsed command text and its normalized parts.
     */
    public static ParsedCommand parse(String commandLine) {
        String trimmedCommand = commandLine.strip();
        String normalizedCommand = trimmedCommand.toLowerCase(Locale.ROOT);
        String[] commandParts = normalizedCommand.isEmpty()
                ? new String[0] : normalizedCommand.split("\\s+", 2);
        return new ParsedCommand(trimmedCommand, commandParts);
    }

    /**
     * Parses and constructs the task represented by a task command.
     *
     * @param command parsed task command.
     * @return task parsing result containing either a task or a failure status.
     */
    public static TaskParseResult parseTask(ParsedCommand command) {
        if (command.parts().length < 2) {
            return failedTaskParse(TaskParseStatus.MISSING_TASK_DESCRIPTION);
        }

        String taskCommand = command.text().strip().replaceFirst("\\s+", " ");
        String normalizedCommand = taskCommand.toLowerCase(Locale.ROOT);

        if (normalizedCommand.startsWith(TODO_PREFIX)) {
            return parseToDo(taskCommand);
        } else if (normalizedCommand.startsWith(DEADLINE_PREFIX)) {
            return parseDeadline(taskCommand, normalizedCommand);
        } else if (normalizedCommand.startsWith(EVENT_PREFIX)) {
            return parseEvent(taskCommand, normalizedCommand);
        } else {
            return failedTaskParse(TaskParseStatus.INVALID_TASK_COMMAND);
        }
    }

    /**
     * Parses the task number argument of a numbered task command.
     *
     * @param command parsed command containing the task number argument.
     * @return task number parsing result.
     */
    public static TaskNumberParseResult parseTaskNumber(ParsedCommand command) {
        if (command.parts().length < 2 || command.parts()[1].isEmpty()) {
            return new TaskNumberParseResult(TaskNumberStatus.MISSING, 0);
        }

        try {
            int taskNumber = Integer.parseInt(command.parts()[1]);
            return new TaskNumberParseResult(TaskNumberStatus.VALID, taskNumber);
        } catch (NumberFormatException exception) {
            return new TaskNumberParseResult(TaskNumberStatus.NOT_INTEGER, 0);
        }
    }

    /**
     * Parses a to-do task command.
     *
     * @param taskCommand command containing the task description.
     * @return task parsing result.
     */
    private static TaskParseResult parseToDo(String taskCommand) {
        String taskName = taskCommand.substring(TODO_PREFIX.length()).strip();
        if (taskName.isEmpty()) {
            return failedTaskParse(TaskParseStatus.MISSING_TASK_DESCRIPTION);
        }
        return successfulTaskParse(new ToDo(taskName));
    }

    /**
     * Parses a deadline task command.
     *
     * @param taskCommand command containing the original-casing task details.
     * @param normalizedCommand normalized command used to find markers.
     * @return task parsing result.
     */
    private static TaskParseResult parseDeadline(String taskCommand, String normalizedCommand) {
        int deadlineMarker = findMarker(normalizedCommand, DEADLINE_MARKER);
        if (deadlineMarker < 0) {
            return failedTaskParse(TaskParseStatus.MISSING_DEADLINE_MARKER);
        }
        if (findMarker(normalizedCommand, DEADLINE_MARKER,
                deadlineMarker + DEADLINE_MARKER.length()) >= 0) {
            return failedTaskParse(TaskParseStatus.MULTIPLE_DEADLINE_MARKERS);
        }

        String taskName = taskCommand.substring(DEADLINE_PREFIX.length(), deadlineMarker).strip();
        String deadline = taskCommand.substring(deadlineMarker + DEADLINE_MARKER.length()).strip();
        if (taskName.isEmpty()) {
            return failedTaskParse(TaskParseStatus.MISSING_DEADLINE_DESCRIPTION);
        }
        if (deadline.isEmpty()) {
            return failedTaskParse(TaskParseStatus.MISSING_DEADLINE_DATE);
        }
        return successfulTaskParse(new Deadline(taskName, deadline));
    }

    /**
     * Parses an event task command.
     *
     * @param taskCommand command containing the original-casing task details.
     * @param normalizedCommand normalized command used to find markers.
     * @return task parsing result.
     */
    private static TaskParseResult parseEvent(String taskCommand, String normalizedCommand) {
        int fromMarker = findMarker(normalizedCommand, EVENT_FROM_MARKER);
        int toMarker = findMarker(normalizedCommand, EVENT_TO_MARKER);
        if (fromMarker < 0) {
            return failedTaskParse(TaskParseStatus.MISSING_EVENT_FROM_MARKER);
        }
        if (toMarker < 0) {
            return failedTaskParse(TaskParseStatus.MISSING_EVENT_TO_MARKER);
        }
        if (toMarker <= fromMarker) {
            return failedTaskParse(TaskParseStatus.REVERSED_EVENT_MARKERS);
        }
        if (findMarker(normalizedCommand, EVENT_FROM_MARKER,
                fromMarker + EVENT_FROM_MARKER.length()) >= 0
                || findMarker(normalizedCommand, EVENT_TO_MARKER,
                toMarker + EVENT_TO_MARKER.length()) >= 0) {
            return failedTaskParse(TaskParseStatus.MULTIPLE_EVENT_MARKERS);
        }

        String taskName = taskCommand.substring(EVENT_PREFIX.length(), fromMarker).strip();
        String durationStart = taskCommand.substring(fromMarker + EVENT_FROM_MARKER.length(), toMarker)
                .strip();
        String durationEnd = taskCommand.substring(toMarker + EVENT_TO_MARKER.length()).strip();
        if (taskName.isEmpty()) {
            return failedTaskParse(TaskParseStatus.MISSING_EVENT_DESCRIPTION);
        }
        if (durationStart.isEmpty()) {
            return failedTaskParse(TaskParseStatus.MISSING_EVENT_START);
        }
        if (durationEnd.isEmpty()) {
            return failedTaskParse(TaskParseStatus.MISSING_EVENT_END);
        }
        return successfulTaskParse(new Event(taskName, durationStart, durationEnd));
    }

    /**
     * Returns a successful task parsing result.
     *
     * @param task parsed task.
     * @return successful parsing result.
     */
    private static TaskParseResult successfulTaskParse(Task task) {
        return new TaskParseResult(TaskParseStatus.SUCCESS, Optional.of(task));
    }

    /**
     * Returns a failed task parsing result.
     *
     * @param status reason the task command could not be parsed.
     * @return failed parsing result.
     */
    private static TaskParseResult failedTaskParse(TaskParseStatus status) {
        return new TaskParseResult(status, Optional.empty());
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
     * Stores the original-casing command text and its normalized parts.
     *
     * @param text trimmed command text with its original casing.
     * @param parts normalized command parts.
     */
    public record ParsedCommand(String text, String[] parts) {
    }

    /**
     * Describes the outcome of parsing a task command.
     */
    public enum TaskParseStatus {
        SUCCESS,
        INVALID_TASK_COMMAND,
        MISSING_TASK_DESCRIPTION,
        MISSING_DEADLINE_MARKER,
        MULTIPLE_DEADLINE_MARKERS,
        MISSING_DEADLINE_DESCRIPTION,
        MISSING_DEADLINE_DATE,
        MISSING_EVENT_FROM_MARKER,
        MISSING_EVENT_TO_MARKER,
        REVERSED_EVENT_MARKERS,
        MULTIPLE_EVENT_MARKERS,
        MISSING_EVENT_DESCRIPTION,
        MISSING_EVENT_START,
        MISSING_EVENT_END
    }

    /**
     * Stores the result of parsing a task command.
     *
     * @param status outcome of parsing the command.
     * @param task parsed task, or an empty result when parsing failed.
     */
    public record TaskParseResult(TaskParseStatus status, Optional<Task> task) {
    }

    /**
     * Describes whether a task number argument is valid, missing, or nonnumeric.
     */
    public enum TaskNumberStatus {
        VALID,
        MISSING,
        NOT_INTEGER
    }

    /**
     * Stores the result of parsing a task number argument.
     *
     * @param status outcome of parsing the argument.
     * @param taskNumber parsed task number, or zero when no number is available.
     */
    public record TaskNumberParseResult(TaskNumberStatus status, int taskNumber) {
    }
}
