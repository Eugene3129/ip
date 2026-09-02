package ernest;

public class Event extends Task {
    protected String duration_start;
    protected String duration_end;

    public Event(String description, String duration_start, String duration_end) {
        super(description);
        this.duration_start = duration_start;
        this.duration_end = duration_end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + duration_start + " to: " + duration_end + ")";
    }
}
