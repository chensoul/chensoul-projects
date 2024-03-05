package com.chensoul.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class TreeUtils {
    /**
     *
     */
    private TreeUtils() {
    }

    /**
     * @param list
     * @param parentId
     * @return {@link Tree}<{@link E}>
     */
    public static <E> Tree<E> buildSingle(final List<TreeNode<E>> list, final E parentId) {
        return buildSingle(list, parentId, TreeNodeConfig.treeNodeConfig, new DefaultNodeParser<>());
    }


    /**
     * @param list
     * @param parentId
     * @param treeNodeConfig
     * @return {@link Tree}<{@link E}>
     */
    public static <E> Tree<E> buildSingle(final List<TreeNode<E>> list, final E parentId, final TreeNodeConfig treeNodeConfig) {
        return buildSingle(list, parentId, treeNodeConfig, new DefaultNodeParser<>());
    }


    /**
     * @param list
     * @param parentId
     * @param nodeParser
     * @return {@link Tree}<{@link E}>
     */
    public static <T, E> Tree<E> buildSingle(final List<T> list, final E parentId, final NodeParser<T, E> nodeParser) {
        return buildSingle(list, parentId, TreeNodeConfig.treeNodeConfig, nodeParser);
    }


    /**
     * @param list
     * @param parentId
     * @return {@link List}<{@link Tree}<{@link E}>>
     */
    public static <E> List<Tree<E>> build(final List<TreeNode<E>> list, final E parentId) {
        return build(list, parentId, TreeNodeConfig.treeNodeConfig, new DefaultNodeParser<>());
    }

    /**
     * @param list
     * @param parentId
     * @param treeNodeConfig
     * @return {@link List}<{@link Tree}<{@link E}>>
     */
    public static <E> List<Tree<E>> build(final List<TreeNode<E>> list, final E parentId, final TreeNodeConfig treeNodeConfig) {
        return build(list, parentId, treeNodeConfig, new DefaultNodeParser<>());
    }


    /**
     * @param list
     * @param parentId
     * @param nodeParser
     * @return {@link List}<{@link Tree}<{@link E}>>
     */
    public static <T, E> List<Tree<E>> build(final List<T> list, final E parentId, final NodeParser<T, E> nodeParser) {
        return build(list, parentId, TreeNodeConfig.treeNodeConfig, nodeParser);
    }

    /**
     * @param list
     * @param rootId
     * @param treeNodeConfig
     * @param nodeParser
     * @return {@link List}<{@link Tree}<{@link E}>>
     */
    public static <T, E> List<Tree<E>> build(final List<T> list, final E rootId, final TreeNodeConfig treeNodeConfig,
                                             final NodeParser<T, E> nodeParser) {
        return buildSingle(list, rootId, treeNodeConfig, nodeParser).getChildren();
    }


    /**
     * @param list
     * @param rootId
     * @param treeNodeConfig
     * @param nodeParser
     * @return {@link Tree}<{@link E}>
     */
    public static <T, E> Tree<E> buildSingle(final List<T> list, final E rootId, final TreeNodeConfig treeNodeConfig,
                                             final NodeParser<T, E> nodeParser) {
        return TreeSupplier.of(rootId, treeNodeConfig).append(list, nodeParser).get();
    }


    /**
     * @param map
     * @param rootId
     * @return {@link List}<{@link Tree}<{@link E}>>
     */
    public static <E> List<Tree<E>> build(final Map<E, Tree<E>> map, final E rootId) {
        return buildSingle(map, rootId).getChildren();
    }


    /**
     * @param map
     * @param rootId
     * @return {@link Tree}<{@link E}>
     */
    public static <E> Tree<E> buildSingle(final Map<E, Tree<E>> map, final E rootId) {
        final Tree<E> tree = getFirstNoneNull(map.values());
        if (null != tree) {
            final TreeNodeConfig config = tree.getConfig();
            return TreeSupplier.of(rootId, config).append(map).get();
        }

        return createEmptyNode(rootId);
    }

    /**
     * @param iterable
     * @return {@link T}
     */
    public static <T> T getFirstNoneNull(final Iterable<T> iterable) {
        if (null == iterable) {
            return null;
        }
        final Iterator<T> iterator = iterable.iterator();
        if (null != iterator) {
            while (iterator.hasNext()) {
                final T next = iterator.next();
                if (null != next) {
                    return next;
                }
            }
        }
        return null;
    }


    /**
     * @param node
     * @param id
     * @return {@link Tree}<{@link T}>
     */
    public static <T> Tree<T> getNode(final Tree<T> node, final T id) {
        if (Objects.equals(id, node.getId())) {
            return node;
        }

        final List<Tree<T>> children = node.getChildren();
        if (null == children) {
            return null;
        }

        // 查找子节点
        Tree<T> childNode;
        for (final Tree<T> child : children) {
            childNode = child.getNode(id);
            if (null != childNode) {
                return childNode;
            }
        }

        // 未找到节点
        return null;
    }

    /**
     * @param node
     * @param includeCurrentNode
     * @return {@link List}<{@link CharSequence}>
     */
    public static <T> List<CharSequence> getParentsName(final Tree<T> node, final boolean includeCurrentNode) {
        final List<CharSequence> result = new ArrayList<>();
        if (null == node) {
            return result;
        }

        if (includeCurrentNode) {
            result.add(node.getName());
        }

        Tree<T> parent = node.getParent();
        while (null != parent) {
            result.add(parent.getName());
            parent = parent.getParent();
        }
        return result;
    }

    /**
     * @param id
     * @return {@link Tree}<{@link E}>
     */
    public static <E> Tree<E> createEmptyNode(final E id) {
        return new Tree<E>().setId(id);
    }

}
