package com.chensoul.util.tree;

import java.io.Serializable;

/**
 * @param <T>
 */
public interface Node<T> extends Comparable<Node<T>>, Serializable {

    /**
     * @return
     */
    T getId();

    /**
     * @param var1
     * @return
     */
    Node<T> setId(T var1);

    /**
     * @return
     */
    T getParentId();

    /**
     * @param var1
     * @return
     */
    Node<T> setParentId(T var1);

    /**
     * @return
     */
    CharSequence getName();

    /**
     * @param var1
     * @return
     */
    Node<T> setName(CharSequence var1);

    /**
     * @return
     */
    Comparable<?> getWeight();

    /**
     * @param var1
     * @return
     */
    Node<T> setWeight(Comparable<?> var1);

    /**
     * @param node the object to be compared.
     * @return
     */
    default int compareTo(Node node) {
        Comparable weight = this.getWeight();
        if (null != weight) {
            Comparable weightOther = node.getWeight();
            return weight.compareTo(weightOther);
        } else {
            return 0;
        }
    }

}
