package net.senmori.mobmerge.filter;

import com.google.common.base.Predicate;

import java.util.List;

public interface Filter<T> extends Predicate<T> {

    /**
     * Gets a list which contains the allowed types for this filter.
     * @return a list of unique types that are valid for this filter
     */
    public List<T> getAllowedTypes();

    /**
     * Gets the unique name of this filter
     * @return the unique name of this filter.
     */
    public String getName();
}
