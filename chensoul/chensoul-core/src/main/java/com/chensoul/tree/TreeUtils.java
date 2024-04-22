package com.chensoul.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public abstract class TreeUtils {
    private TreeUtils() {
    }

    /**
     * <p>buildSingle.</p>
     *
     * @param list a {@link java.util.List} object
     * @param parentId a E object
     * @return {@link com.chensoul.tree.Tree}<{@link E}>
     * @param <E> a E class
     */
    public static <E> Tree<E> buildSingle(final List<TreeNode<E>> list, final E parentId) {
        return buildSingle(list, parentId, TreeNodeConfig.treeNodeConfig, new DefaultNodeParser<>());
    }


    /**
     * <p>buildSingle.</p>
     *
     * @param list a {@link java.util.List} object
     * @param parentId a E object
     * @param treeNodeConfig a {@link com.chensoul.tree.TreeNodeConfig} object
     * @return {@link com.chensoul.tree.Tree}<{@link E}>
     * @param <E> a E class
     */
    public static <E> Tree<E> buildSingle(final List<TreeNode<E>> list, final E parentId, final TreeNodeConfig treeNodeConfig) {
        return buildSingle(list, parentId, treeNodeConfig, new DefaultNodeParser<>());
    }


    /**
     * <p>buildSingle.</p>
     *
     * @param list a {@link java.util.List} object
     * @param parentId a E object
     * @param nodeParser a {@link com.chensoul.tree.NodeParser} object
     * @return {@link com.chensoul.tree.Tree}<{@link E}>
     * @param <T> a T class
     * @param <E> a E class
     */
    public static <T, E> Tree<E> buildSingle(final List<T> list, final E parentId, final NodeParser<T, E> nodeParser) {
        return buildSingle(list, parentId, TreeNodeConfig.treeNodeConfig, nodeParser);
    }


    /**
     * <p>build.</p>
     *
     * @param list a {@link java.util.List} object
     * @param parentId a E object
     * @return {@link java.util.List}<{@link com.chensoul.tree.Tree}<{@link E}>>
     * @param <E> a E class
     */
    public static <E> List<Tree<E>> build(final List<TreeNode<E>> list, final E parentId) {
        return build(list, parentId, TreeNodeConfig.treeNodeConfig, new DefaultNodeParser<>());
    }

    /**
     * <p>build.</p>
     *
     * @param list a {@link java.util.List} object
     * @param parentId a E object
     * @param treeNodeConfig a {@link com.chensoul.tree.TreeNodeConfig} object
     * @return {@link java.util.List}<{@link com.chensoul.tree.Tree}<{@link E}>>
     * @param <E> a E class
     */
    public static <E> List<Tree<E>> build(final List<TreeNode<E>> list, final E parentId, final TreeNodeConfig treeNodeConfig) {
        return build(list, parentId, treeNodeConfig, new DefaultNodeParser<>());
    }


    /**
     * <p>build.</p>
     *
     * @param list a {@link java.util.List} object
     * @param parentId a E object
     * @param nodeParser a {@link com.chensoul.tree.NodeParser} object
     * @return {@link java.util.List}<{@link com.chensoul.tree.Tree}<{@link E}>>
     * @param <T> a T class
     * @param <E> a E class
     */
    public static <T, E> List<Tree<E>> build(final List<T> list, final E parentId, final NodeParser<T, E> nodeParser) {
        return build(list, parentId, TreeNodeConfig.treeNodeConfig, nodeParser);
    }

    /**
     * <p>build.</p>
     *
     * @param list a {@link java.util.List} object
     * @param rootId a E object
     * @param treeNodeConfig a {@link com.chensoul.tree.TreeNodeConfig} object
     * @param nodeParser a {@link com.chensoul.tree.NodeParser} object
     * @return {@link java.util.List}<{@link com.chensoul.tree.Tree}<{@link E}>>
     * @param <T> a T class
     * @param <E> a E class
     */
    public static <T, E> List<Tree<E>> build(final List<T> list, final E rootId, final TreeNodeConfig treeNodeConfig,
        final NodeParser<T, E> nodeParser) {
        return buildSingle(list, rootId, treeNodeConfig, nodeParser).getChildren();
    }


    /**
     * <p>buildSingle.</p>
     *
     * @param list a {@link java.util.List} object
     * @param rootId a E object
     * @param treeNodeConfig a {@link com.chensoul.tree.TreeNodeConfig} object
     * @param nodeParser a {@link com.chensoul.tree.NodeParser} object
     * @return {@link com.chensoul.tree.Tree}<{@link E}>
     * @param <T> a T class
     * @param <E> a E class
     */
    public static <T, E> Tree<E> buildSingle(final List<T> list, final E rootId, final TreeNodeConfig treeNodeConfig,
        final NodeParser<T, E> nodeParser) {
        return TreeSupplier.of(rootId, treeNodeConfig).append(list, nodeParser).get();
    }


    /**
     * <p>build.</p>
     *
     * @param map a {@link java.util.Map} object
     * @param rootId a E object
     * @return {@link java.util.List}<{@link com.chensoul.tree.Tree}<{@link E}>>
     * @param <E> a E class
     */
    public static <E> List<Tree<E>> build(final Map<E, Tree<E>> map, final E rootId) {
        return buildSingle(map, rootId).getChildren();
    }


    /**
     * <p>buildSingle.</p>
     *
     * @param map a {@link java.util.Map} object
     * @param rootId a E object
     * @return {@link com.chensoul.tree.Tree}<{@link E}>
     * @param <E> a E class
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
     * <p>getFirstNoneNull.</p>
     *
     * @param iterable a {@link java.lang.Iterable} object
     * @return {@link T}
     * @param <T> a T class
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
     * <p>getNode.</p>
     *
     * @param node a {@link com.chensoul.tree.Tree} object
     * @param id a T object
     * @return {@link com.chensoul.tree.Tree}<{@link T}>
     * @param <T> a T class
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
     * <p>getParentsName.</p>
     *
     * @param node a {@link com.chensoul.tree.Tree} object
     * @param includeCurrentNode a boolean
     * @return {@link java.util.List}<{@link java.lang.CharSequence}>
     * @param <T> a T class
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
     * <p>createEmptyNode.</p>
     *
     * @param id a E object
     * @return {@link com.chensoul.tree.Tree}<{@link E}>
     * @param <E> a E class
     */
    public static <E> Tree<E> createEmptyNode(final E id) {
        return new Tree<E>().setId(id);
    }

}
