package com.chensoul.tree;

import java.io.Serializable;


/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 * @version $Id: $Id
 */
public interface Node<T> extends Comparable<Node<T>>, Serializable {

    /**
     * <p>getId.</p>
     *
     * @return a T object
     */
    T getId();

    /**
     * <p>setId.</p>
     *
     * @param var1 a T object
     * @return a {@link com.chensoul.tree.Node} object
     */
    Node<T> setId(T var1);

    /**
     * <p>getParentId.</p>
     *
     * @return a T object
     */
    T getParentId();

    /**
     * <p>setParentId.</p>
     *
     * @param var1 a T object
     * @return a {@link com.chensoul.tree.Node} object
     */
    Node<T> setParentId(T var1);

    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.CharSequence} object
     */
    CharSequence getName();

    /**
     * <p>setName.</p>
     *
     * @param var1 a {@link java.lang.CharSequence} object
     * @return a {@link com.chensoul.tree.Node} object
     */
    Node<T> setName(CharSequence var1);

    /**
     * <p>getWeight.</p>
     *
     * @return a {@link java.lang.Comparable} object
     */
    Comparable<?> getWeight();

    /**
     * <p>setWeight.</p>
     *
     * @param var1 a {@link java.lang.Comparable} object
     * @return a {@link com.chensoul.tree.Node} object
     */
    Node<T> setWeight(Comparable<?> var1);

    /**
     * <p>compareTo.</p>
     *
     * @param node the object to be compared.
     * @return a int
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
