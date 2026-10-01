package ernest;

import java.util.Locale;

/**
 * Converts raw user input into a form that Ernest can process.
 */
public final class Parser {
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
     * Stores the original-casing command text and its normalized parts.
     *
     * @param text trimmed command text with its original casing.
     * @param parts normalized command parts.
     */
    public record ParsedCommand(String text, String[] parts) {
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
