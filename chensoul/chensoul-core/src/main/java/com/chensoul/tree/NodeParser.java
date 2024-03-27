package com.chensoul.tree;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 *
 */
@FunctionalInterface
public interface NodeParser<T, E> {
    void parse(T object, Tree<E> treeNode);
}
