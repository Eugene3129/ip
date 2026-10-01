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
     * Stores the original-casing command text and its normalized parts.
     *
     * @param text trimmed command text with its original casing.
     * @param parts normalized command parts.
     */
    public record ParsedCommand(String text, String[] parts) {
    }
}
