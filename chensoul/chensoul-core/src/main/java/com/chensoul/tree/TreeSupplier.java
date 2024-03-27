package com.chensoul.tree;

import com.chensoul.collection.CollectionUtils;
import com.chensoul.collection.MapUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Supplier;
public class TreeSupplier<E> implements Supplier<Tree<E>> {

    private final Tree<E> root;

    private final Map<E, Tree<E>> idTreeMap;

    private boolean isBuild;
    public TreeSupplier(final E rootId, final TreeNodeConfig config) {
        root = new Tree<>(config);
        root.setId(rootId);
        idTreeMap = new TreeMap<>(); // 使用有序map
    }

    /**
     * <p>of.</p>
     *
     * @param rootId a T object
     * @param <T> a T class
     * @return a {@link com.chensoul.tree.TreeSupplier} object
     */
    public static <T> TreeSupplier<T> of(final T rootId) {
        return of(rootId, null);
    }

    /**
     * <p>of.</p>
     *
     * @param rootId a T object
     * @param config a {@link com.chensoul.tree.TreeNodeConfig} object
     * @param <T> a T class
     * @return a {@link com.chensoul.tree.TreeSupplier} object
     */
    public static <T> TreeSupplier<T> of(final T rootId, final TreeNodeConfig config) {
        return new TreeSupplier<>(rootId, config);
    }

    /**
     * <p>append.</p>
     *
     * @param map a {@link java.util.Map} object
     * @return a {@link com.chensoul.tree.TreeSupplier} object
     */
    public TreeSupplier<E> append(final Map<E, Tree<E>> map) {
        checkBuilt();
        idTreeMap.putAll(map);
        return this;
    }

    /**
     * <p>append.</p>
     *
     * @param trees a {@link java.lang.Iterable} object
     * @return a {@link com.chensoul.tree.TreeSupplier} object
     */
    public TreeSupplier<E> append(final Iterable<Tree<E>> trees) {
        checkBuilt();

        for (final Tree<E> tree : trees) {
            idTreeMap.put(tree.getId(), tree);
        }
        return this;
    }

    /**
     * <p>append.</p>
     *
     * @param list a {@link java.util.List} object
     * @param nodeParser a {@link com.chensoul.tree.NodeParser} object
     * @param <T> a T class
     * @return a {@link com.chensoul.tree.TreeSupplier} object
     */
    public <T> TreeSupplier<E> append(final List<T> list, final NodeParser<T, E> nodeParser) {
        checkBuilt();

        final TreeNodeConfig config = root.getConfig();
        final Map<E, Tree<E>> map = new TreeMap<>();// 使用有序map
        Tree<E> node;
        for (final T t : list) {
            node = new Tree<>(config);
            nodeParser.parse(t, node);
            map.put(node.getId(), node);
        }
        return append(map);
    }

    /**
     * <p>reset.</p>
     *
     * @return a {@link com.chensoul.tree.TreeSupplier} object
     */
    public TreeSupplier<E> reset() {
        idTreeMap.clear();
        root.setChildren(null);
        isBuild = false;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Tree<E> get() {
        checkBuilt();

        buildFromMap();
        cutTree();

        isBuild = true;
        idTreeMap.clear();

        return root;
    }

    /**
     * <p>buildList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Tree<E>> buildList() {
        if (isBuild) {
            // 已经构建过了
            return root.getChildren();
        }
        return get().getChildren();
    }

    private void buildFromMap() {
        if (MapUtils.isEmpty(idTreeMap)) {
            return;
        }

        final Map<E, Tree<E>> eTreeMap = idTreeMap;
        // MapUtils.sortByValue(this.idTreeMap,false);
        E parentId;
        for (final Tree<E> node : eTreeMap.values()) {
            if (null == node) {
                continue;
            }
            parentId = node.getParentId();
            if (Objects.equals(root.getId(), parentId)) {
                root.addChildren(node);
                continue;
            }

            final Tree<E> parentNode = eTreeMap.get(parentId);
            if (null != parentNode) {
                parentNode.addChildren(node);
            }
        }
    }

    private void cutTree() {
        final TreeNodeConfig config = root.getConfig();
        final Integer deep = config.getDeep();
        if (null == deep || deep < 0) {
            return;
        }
        cutTree(root, 0, deep);
    }

    private void cutTree(final Tree<E> tree, final int currentDepp, final int maxDeep) {
        if (null == tree) {
            return;
        }
        if (currentDepp == maxDeep) {
            // 剪枝
            tree.setChildren(null);
            return;
        }

        final List<Tree<E>> children = tree.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            for (final Tree<E> child : children) {
                cutTree(child, currentDepp + 1, maxDeep);
            }
        }
    }

    private void checkBuilt() {
        if (isBuild) {
            throw new RuntimeException("Current tree has been built.");
        }
    }

}
