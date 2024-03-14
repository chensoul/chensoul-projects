package com.chensoul.lang;

import static java.lang.Integer.compare;

import java.util.Comparator;

/**
 * {@code Prioritized} interface can be implemented by objects that
 * should be sorted, for example the tasks in executable queue.
 *
 * @since 0.0.1
 * @author chensoul
 * @version $Id: $Id
 */
public interface Prioritized extends Comparable<Prioritized> {

    /**
     * The maximum priority
     */
    int MAX_PRIORITY = Integer.MIN_VALUE;
    /**
     * The minimum priority
     */
    int MIN_PRIORITY = Integer.MAX_VALUE;
    /**
     * Normal Priority
     */
    int NORMAL_PRIORITY = 0;
    /**
     * The {@link Comparator} of {@link Prioritized}
     */
    Comparator<Object> COMPARATOR = (one, two) -> {
        boolean b1 = one instanceof Prioritized;
        boolean b2 = two instanceof Prioritized;
        if (b1 && !b2) {        // one is Prioritized, two is not
            return -1;
        } else if (b2 && !b1) { // two is Prioritized, one is not
            return 1;
        } else if (b1 && b2) {  //  one and two both are Prioritized
            return ((Prioritized) one).compareTo((Prioritized) two);
        } else {                // no different
            return 0;
        }
    };

    /**
     * Get the priority
     *
     * @return the default is {@link #MIN_PRIORITY minimum one}
     */
    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    /** {@inheritDoc} */
    @Override
    default int compareTo(Prioritized that) {
        return compare(this.getPriority(), that.getPriority());
    }
}
