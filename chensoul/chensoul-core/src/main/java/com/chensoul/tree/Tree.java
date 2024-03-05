package com.chensoul.tree;

import com.chensoul.collection.CollectionUtils;
import com.chensoul.util.ObjectUtils;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

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

    public Tree(final TreeNodeConfig treeNodeConfig) {
        this.treeNodeConfig = ObjectUtils.defaultIfNull(treeNodeConfig, TreeNodeConfig.treeNodeConfig);
    }

    public TreeNodeConfig getConfig() {
        return this.treeNodeConfig;
    }

    public Tree<T> getParent() {
        return this.parent;
    }

    public Tree<T> setParent(final Tree<T> parent) {
        this.parent = parent;
        if (null != parent) {
            this.setParentId(parent.getId());
        }
        return this;
    }

    public Tree<T> getNode(final T id) {
        return TreeUtils.getNode(this, id);
    }

    public List<CharSequence> getParentsName(final T id, final boolean includeCurrentNode) {
        return TreeUtils.getParentsName(this.getNode(id), includeCurrentNode);
    }

    public List<CharSequence> getParentsName(final boolean includeCurrentNode) {
        return TreeUtils.getParentsName(this, includeCurrentNode);
    }

    @Override
    public T getId() {
        return (T) this.get(this.treeNodeConfig.getIdKey());
    }

    @Override
    public Tree<T> setId(final T id) {
        this.put(this.treeNodeConfig.getIdKey(), id);
        return this;
    }

    @Override
    public T getParentId() {
        return (T) this.get(this.treeNodeConfig.getParentIdKey());
    }

    @Override
    public Tree<T> setParentId(final T parentId) {
        this.put(this.treeNodeConfig.getParentIdKey(), parentId);
        return this;
    }

    @Override
    public CharSequence getName() {
        return (CharSequence) this.get(this.treeNodeConfig.getNameKey());
    }

    @Override
    public Tree<T> setName(final CharSequence name) {
        this.put(this.treeNodeConfig.getNameKey(), name);
        return this;
    }

    @Override
    public Comparable<?> getWeight() {
        return (Comparable<?>) this.get(this.treeNodeConfig.getWeightKey());
    }

    @Override
    public Tree<T> setWeight(final Comparable<?> weight) {
        this.put(this.treeNodeConfig.getWeightKey(), weight);
        return this;
    }

    public List<Tree<T>> getChildren() {
        return (List<Tree<T>>) this.getOrDefault(this.treeNodeConfig.getChildrenKey(), new LinkedList<>());
    }

    public Tree<T> setChildren(final List<Tree<T>> children) {
        this.put(this.treeNodeConfig.getChildrenKey(), children);
        return this;
    }

    @SafeVarargs
    public final Tree<T> addChildren(final Tree<T>... children) {
        final List<Tree<T>> childrenList = this.getChildren();
        if (CollectionUtils.isEmpty(childrenList)) {
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

    public void putExtra(final String key, final Object value) {
        if (key.length() == 0) throw new RuntimeException("Key must be not empty !");
        this.put(key, value);
    }

    @Override
    public int compareTo(final Node node) {
        final Comparable weight = this.getWeight();
        if (null != weight) {
            final Comparable weightOther = node.getWeight();
            return this.treeNodeConfig.getReversed() ? weightOther.compareTo(weight) : weight.compareTo(weightOther);
        } else return 0;
    }

}
