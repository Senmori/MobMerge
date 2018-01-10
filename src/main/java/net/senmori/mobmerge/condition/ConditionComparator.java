package net.senmori.mobmerge.condition;

import java.util.Comparator;

/**
 * This class is used to compare {@link Condition}s.
 * <br>
 */
public class ConditionComparator implements Comparator<Condition> {
    @Override
    public int compare(Condition first, Condition other) {
        return Integer.compare(first.getPriority().getPriorityID(), other.getPriority().getPriorityID());
    }
}
