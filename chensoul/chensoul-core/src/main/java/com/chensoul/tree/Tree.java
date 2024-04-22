package com.chensoul.tree;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public class Tree<T> extends LinkedHashMap<String, Object> implements Node<T> {

    private static final long serialVersionUID = 7620659626383442013L;
    private final TreeNodeConfig treeNodeConfig;

    private Tree<T> parent;
    public Tree() {
        this(null);
    }

    /**
     * <p>Constructor for Tree.</p>
     *
     * @param treeNodeConfig a {@link com.chensoul.tree.TreeNodeConfig} object
     */
    public Tree(final TreeNodeConfig treeNodeConfig) {
        this.treeNodeConfig = ObjectUtils.defaultIfNull(treeNodeConfig, TreeNodeConfig.treeNodeConfig);
    }

    /**
     * <p>getConfig.</p>
     *
     * @return a {@link com.chensoul.tree.TreeNodeConfig} object
     */
    public TreeNodeConfig getConfig() {
        return this.treeNodeConfig;
    }

    /**
     * <p>Getter for the field <code>parent</code>.</p>
     *
     * @return a {@link com.chensoul.tree.Tree} object
     */
    public Tree<T> getParent() {
        return this.parent;
    }

    /**
     * <p>Setter for the field <code>parent</code>.</p>
     *
     * @param parent a {@link com.chensoul.tree.Tree} object
     * @return a {@link com.chensoul.tree.Tree} object
     */
    public Tree<T> setParent(final Tree<T> parent) {
        this.parent = parent;
        if (null != parent) {
            this.setParentId(parent.getId());
        }
        return this;
    }

    /**
     * <p>getNode.</p>
     *
     * @param id a T object
     * @return a {@link com.chensoul.tree.Tree} object
     */
    public Tree<T> getNode(final T id) {
        return TreeUtils.getNode(this, id);
    }

    /**
     * <p>getParentsName.</p>
     *
     * @param id a T object
     * @param includeCurrentNode a boolean
     * @return a {@link java.util.List} object
     */
    public List<CharSequence> getParentsName(final T id, final boolean includeCurrentNode) {
        return TreeUtils.getParentsName(this.getNode(id), includeCurrentNode);
    }

    /**
     * <p>getParentsName.</p>
     *
     * @param includeCurrentNode a boolean
     * @return a {@link java.util.List} object
     */
    public List<CharSequence> getParentsName(final boolean includeCurrentNode) {
        return TreeUtils.getParentsName(this, includeCurrentNode);
    }

    /** {@inheritDoc} */
    @Override
    public T getId() {
        return (T) this.get(this.treeNodeConfig.getIdKey());
    }

    /** {@inheritDoc} */
    @Override
    public Tree<T> setId(final T id) {
        this.put(this.treeNodeConfig.getIdKey(), id);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public T getParentId() {
        return (T) this.get(this.treeNodeConfig.getParentIdKey());
    }

    /** {@inheritDoc} */
    @Override
    public Tree<T> setParentId(final T parentId) {
        this.put(this.treeNodeConfig.getParentIdKey(), parentId);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CharSequence getName() {
        return (CharSequence) this.get(this.treeNodeConfig.getNameKey());
    }

    /** {@inheritDoc} */
    @Override
    public Tree<T> setName(final CharSequence name) {
        this.put(this.treeNodeConfig.getNameKey(), name);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Comparable<?> getWeight() {
        return (Comparable<?>) this.get(this.treeNodeConfig.getWeightKey());
    }

    /** {@inheritDoc} */
    @Override
    public Tree<T> setWeight(final Comparable<?> weight) {
        this.put(this.treeNodeConfig.getWeightKey(), weight);
        return this;
    }

    /**
     * <p>getChildren.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Tree<T>> getChildren() {
        return (List<Tree<T>>) this.getOrDefault(this.treeNodeConfig.getChildrenKey(), new LinkedList<>());
    }

    /**
     * <p>setChildren.</p>
     *
     * @param children a {@link java.util.List} object
     * @return a {@link com.chensoul.tree.Tree} object
     */
    public Tree<T> setChildren(final List<Tree<T>> children) {
        this.put(this.treeNodeConfig.getChildrenKey(), children);
        return this;
    }

    /**
     * <p>addChildren.</p>
     *
     * @param children a {@link com.chensoul.tree.Tree} object
     * @return a {@link com.chensoul.tree.Tree} object
     */
    @SafeVarargs
    public final Tree<T> addChildren(final Tree<T>... children) {
        final List<Tree<T>> childrenList = this.getChildren();
        if (childrenList == null || childrenList.size() == 0) {
            this.setChildren(childrenList);
        }
        for (final Tree<T> child : children) {
            child.setParent(this);
            childrenList.add(child);
        }
        // 每次添加一个记录，就排序一下
        Collections.sort(childrenList);
        return this;
    }

    /**
     * <p>putExtra.</p>
     *
     * @param key a {@link java.lang.String} object
     * @param value a {@link java.lang.Object} object
     */
    public void putExtra(final String key, final Object value) {
        if (key.length() == 0) throw new RuntimeException("Key must be not empty !");
        this.put(key, value);
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(final Node node) {
        final Comparable weight = this.getWeight();
        if (null != weight) {
            final Comparable weightOther = node.getWeight();
            return this.treeNodeConfig.getReversed() ? weightOther.compareTo(weight) : weight.compareTo(weightOther);
        } else return 0;
    }

}
