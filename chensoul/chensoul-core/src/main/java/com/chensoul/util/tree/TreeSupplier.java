package com.chensoul.util.tree;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Supplier;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

/**
 * 树构建器
 *
 * @param <E> ID类型
 */
public class TreeSupplier<E> implements Supplier<Tree<E>> {

    private final Tree<E> root;

    private final Map<E, Tree<E>> idTreeMap;

    private boolean isBuild;

    /**
     * 构造
     *
     * @param rootId 根节点ID
     * @param config 配置
     */
    public TreeSupplier(final E rootId, final TreeNodeConfig config) {
        root = new Tree<>(config);
        root.setId(rootId);
        idTreeMap = new TreeMap<>(); // 使用有序map
    }

    /**
     * 创建Tree构建器
     *
     * @param rootId 根节点ID
     * @param <T>    ID类型
     * @return {@link TreeSupplier}
     */
    public static <T> TreeSupplier<T> of(final T rootId) {
        return of(rootId, null);
    }

    /**
     * 创建Tree构建器
     *
     * @param rootId 根节点ID
     * @param config 配置
     * @param <T>    ID类型
     * @return {@link TreeSupplier}
     */
    public static <T> TreeSupplier<T> of(final T rootId, final TreeNodeConfig config) {
        return new TreeSupplier<>(rootId, config);
    }

    /**
     * 增加节点列表，增加的节点是不带子节点的
     *
     * @param map 节点列表
     * @return this
     */
    public TreeSupplier<E> append(final Map<E, Tree<E>> map) {
        checkBuilt();
        idTreeMap.putAll(map);
        return this;
    }

    /**
     * 增加节点列表，增加的节点是不带子节点的
     *
     * @param trees 节点列表
     * @return this
     */
    public TreeSupplier<E> append(final Iterable<Tree<E>> trees) {
        checkBuilt();

        for (final Tree<E> tree : trees) {
            idTreeMap.put(tree.getId(), tree);
        }
        return this;
    }

    /**
     * 增加节点列表，增加的节点是不带子节点的
     *
     * @param list       Bean列表
     * @param <T>        Bean类型
     * @param nodeParser 节点转换器，用于定义一个Bean如何转换为Tree节点
     * @return this
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
     * 重置Builder，实现复用
     *
     * @return this
     */
    public TreeSupplier<E> reset() {
        idTreeMap.clear();
        root.setChildren(null);
        isBuild = false;
        return this;
    }

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
     * 构建树列表，没有顶层节点，例如：
     *
     * <pre>
     * -用户管理
     *  -用户管理
     *    +用户添加
     * - 部门管理
     *  -部门管理
     *    +部门添加
     * </pre>
     *
     * @return 树列表
     */
    public List<Tree<E>> buildList() {
        if (isBuild) {
            // 已经构建过了
            return root.getChildren();
        }
        return get().getChildren();
    }

    /**
     * 开始构建
     */
    private void buildFromMap() {
        if (MapUtils.isEmpty(idTreeMap)) {
            return;
        }

        final Map<E, Tree<E>> eTreeMap = idTreeMap;// 使用有序map
        // MapUtils.sortByValue(this.idTreeMap,
        // false);
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

    /**
     * 树剪枝
     */
    private void cutTree() {
        final TreeNodeConfig config = root.getConfig();
        final Integer deep = config.getDeep();
        if (null == deep || deep < 0) {
            return;
        }
        cutTree(root, 0, deep);
    }

    /**
     * 树剪枝叶
     *
     * @param tree        节点
     * @param currentDepp 当前层级
     * @param maxDeep     最大层级
     */
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

    /**
     * 检查是否已经构建
     */
    private void checkBuilt() {
        if (isBuild) {
            throw new RuntimeException("Current tree has been built.");
        }
    }

}
