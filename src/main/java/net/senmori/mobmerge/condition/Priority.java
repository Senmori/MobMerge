package net.senmori.mobmerge.condition;

/**
 * Priority dictates in which order {@link Condition}s are ran.<br>
 * Higher priorities will run first.
 */
public enum Priority {
    /**
     * This priority will <b>always</b> run before any other priorities.
     */
    DEFAULT(-1),
    HIGHEST(1),
    HIGH(2),
    NORMAL(3),
    LOW(4),
    LOWEST(5);

    private final int priority;
    private Priority(int priority) {
        this.priority = priority;
    }

    public int getPriorityID() {
        return priority;
    }
}
