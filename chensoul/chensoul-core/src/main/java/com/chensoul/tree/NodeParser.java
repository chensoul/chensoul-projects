package com.chensoul.tree;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 * @version $Id: $Id
 */
@FunctionalInterface
public interface NodeParser<T, E> {
    /**
     * <p>parse.</p>
     *
     * @param object a T object
     * @param treeNode a {@link com.chensoul.tree.Tree} object
     */
    void parse(T object, Tree<E> treeNode);
}
