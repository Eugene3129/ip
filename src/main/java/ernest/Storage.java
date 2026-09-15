package ernest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads Ernest task lists from the local data file.
 */
public final class Storage {
    /** Location of the task data file relative to the project root. */
    private static final Path DATA_FILE_PATH = Path.of("data", "ernest.txt");
    private static final String CSV_HEADER = "type,isDone,description,deadline,startTime,endTime";
    private static final int CSV_FIELD_COUNT = 6;

    private Storage() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Writes all tasks to the data file in CSV format.
     *
     * @param tasks tasks to save.
     * @return true if the tasks were saved; otherwise false.
     */
    public static boolean saveTasks(List<Task> tasks) {
        List<String> records = new ArrayList<>();
        records.add(CSV_HEADER);
        for (Task task : tasks) {
            records.add(toCsvRecord(task));
        }

        try {
            Files.createDirectories(DATA_FILE_PATH.getParent());
            Files.write(DATA_FILE_PATH, records);
            return true;
        } catch (IOException | SecurityException exception) {
            return false;
        }
    }

    /**
     * Returns tasks loaded from the data file, or an empty list when no data file exists.
     *
     * @return tasks read from the data file.
     */
    public static ArrayList<Task> loadTasks(int maximumTasks) {
        ArrayList<Task> tasks = new ArrayList<>();
        if (maximumTasks <= 0) {
            return tasks;
        }

        try {
            if (!Files.exists(DATA_FILE_PATH)) {
                return tasks;
            }
            List<String> records = Files.readAllLines(DATA_FILE_PATH);
            if (records.isEmpty()) {
                return tasks;
            }
            if (!CSV_HEADER.equals(records.get(0))) {
                System.out.println("Warning: Saved tasks could not be loaded because the file header is invalid.");
                return tasks;
            }

            boolean hasInvalidRecord = false;
            for (int i = 1; i < records.size(); i++) {
                if (records.get(i).isBlank()) {
                    continue;
                }
                try {
                    if (tasks.size() == maximumTasks) {
                        hasInvalidRecord = true;
                        break;
                    }
                    tasks.add(toTask(parseCsvRecord(records.get(i))));
                } catch (IllegalArgumentException exception) {
                    hasInvalidRecord = true;
                }
            }
            if (hasInvalidRecord) {
                System.out.println("Warning: Some saved tasks could not be loaded.");
            }
        } catch (IOException | SecurityException exception) {
            System.out.println("Warning: Saved tasks could not be loaded.");
        }
        return tasks;
    }

    /**
     * Returns one task encoded as a CSV record.
     *
     * @param task task to encode.
     * @return CSV record for the task.
     */
    private static String toCsvRecord(Task task) {
        if (task instanceof Deadline deadline) {
            return toCsvRecord("deadline", deadline, deadline.getDueDate(), "", "");
        }
        if (task instanceof Event event) {
            return toCsvRecord("event", event, "", event.getDurationStart(), event.getDurationEnd());
        }
        return toCsvRecord("todo", task, "", "", "");
    }

    /**
     * Returns CSV columns for a task.
     *
     * @param type task type.
     * @param task task containing the shared fields.
     * @param deadline deadline date, if applicable.
     * @param startTime event start time, if applicable.
     * @param endTime event end time, if applicable.
     * @return CSV record for the supplied columns.
     */
    private static String toCsvRecord(String type, Task task, String deadline, String startTime, String endTime) {
        return String.join(",", escapeCsv(type), escapeCsv(Boolean.toString(task.isDone())),
                escapeCsv(task.getTaskName()), escapeCsv(deadline), escapeCsv(startTime), escapeCsv(endTime));
    }

    /**
     * Escapes one CSV field using double quotes.
     *
     * @param value field value.
     * @return escaped CSV field.
     */
    private static String escapeCsv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    /**
     * Returns a task reconstructed from a CSV record.
     *
     * @param fields fields in one CSV record.
     * @return task represented by the record.
     */
    private static Task toTask(List<String> fields) {
        if (fields.size() != CSV_FIELD_COUNT) {
            throw new IllegalArgumentException("Task record has an invalid number of fields.");
        }

        boolean isDone = switch (fields.get(1)) {
        case "true" -> true;
        case "false" -> false;
        default -> throw new IllegalArgumentException("Task record has an invalid completion status.");
        };

        Task task;
        switch (fields.get(0)) {
        case "todo":
            task = new ToDo(fields.get(2));
            break;
        case "deadline":
            task = new Deadline(fields.get(2), fields.get(3));
            break;
        case "event":
            task = new Event(fields.get(2), fields.get(4), fields.get(5));
            break;
        default:
            throw new IllegalArgumentException("Task record has an unknown type.");
        }
        task.setDone(isDone);
        return task;
    }

    /**
     * Returns fields parsed from a CSV record written by this class.
     *
     * @param record CSV record to parse.
     * @return fields in the CSV record.
     */
    private static List<String> parseCsvRecord(String record) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isQuoted = false;
        boolean hasClosedQuote = false;

        for (int i = 0; i < record.length(); i++) {
            char character = record.charAt(i);
            if (isQuoted) {
                if (character == '"' && i + 1 < record.length() && record.charAt(i + 1) == '"') {
                    field.append(character);
                    i++;
                } else if (character == '"') {
                    isQuoted = false;
                    hasClosedQuote = true;
                } else {
                    field.append(character);
                }
            } else if (character == ',') {
                fields.add(field.toString());
                field.setLength(0);
                hasClosedQuote = false;
            } else if (character == '"') {
                if (field.length() != 0 || hasClosedQuote) {
                    throw new IllegalArgumentException("Task record has an invalid quoted field.");
                }
                isQuoted = true;
            } else {
                if (hasClosedQuote) {
                    throw new IllegalArgumentException("Task record has an invalid quoted field.");
                }
                field.append(character);
            }
        }
        if (isQuoted) {
            throw new IllegalArgumentException("Task record has an unclosed quoted field.");
        }
        fields.add(field.toString());
        return fields;
    }
}
