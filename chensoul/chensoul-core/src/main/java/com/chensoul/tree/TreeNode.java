package com.chensoul.tree;

import java.util.Map;
import java.util.Objects;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 * @version $Id: $Id
 */
public class TreeNode<T> implements Node<T> {

    private static final long serialVersionUID = 8397997835881121953L;
    /**
     * ID
     */
    private T id;

    /**
     * 父节点ID
     */
    private T parentId;

    /**
     * 名称
     */
    private CharSequence name;

    /**
     * 顺序 越小优先级越高 默认0
     */
    private Comparable<?> weight = 0;

    private final Boolean reversed = false;

    /**
     * 扩展字段
     */
    private Map<String, Object> extra;

    /**
     * 空构造
     */
    public TreeNode() {
    }

    /**
     * 构造
     *
     * @param id       ID
     * @param parentId 父节点ID
     * @param name     名称
     * @param weight   权重
     */
    public TreeNode(final T id, final T parentId, final String name, final Comparable<?> weight) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        if (weight != null) {
            this.weight = weight;
        }

    }

    /** {@inheritDoc} */
    @Override
    public T getId() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public TreeNode<T> setId(final T id) {
        this.id = id;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public T getParentId() {
        return parentId;
    }

    /** {@inheritDoc} */
    @Override
    public TreeNode<T> setParentId(final T parentId) {
        this.parentId = parentId;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CharSequence getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public TreeNode<T> setName(final CharSequence name) {
        this.name = name;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Comparable<?> getWeight() {
        return weight;
    }

    /** {@inheritDoc} */
    @Override
    public TreeNode<T> setWeight(final Comparable<?> weight) {
        this.weight = weight;
        return this;
    }

    /**
     * 获取扩展字段
     *
     * @return 扩展字段Map
     * @since 5.2.5
     */
    public Map<String, Object> getExtra() {
        return extra;
    }

    /**
     * 设置扩展字段
     *
     * @param extra 扩展字段
     * @return this
     * @since 5.2.5
     */
    public TreeNode<T> setExtra(final Map<String, Object> extra) {
        this.extra = extra;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return Boolean.TRUE;
        }
        if (o == null || getClass() != o.getClass()) {
            return Boolean.FALSE;
        }
        final TreeNode<?> treeNode = (TreeNode<?>) o;
        return Objects.equals(id, treeNode.id);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
