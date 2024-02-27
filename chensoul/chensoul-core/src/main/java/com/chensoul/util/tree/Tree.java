package com.chensoul.util.tree;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 通过转换器将你的实体转化为TreeNodeMap节点实体 属性都存在此处,属性有序，可支持排序
 *
 * @param <T> ID类型
 * @author liangbaikai
 * @since 5.2.1
 */
public class Tree<T> extends LinkedHashMap<String, Object> implements Node<T> {

    private static final long serialVersionUID = 7620659626383442013L;
    private final TreeNodeConfig treeNodeConfig;

    private Tree<T> parent;

    /**
     *
     */
    public Tree() {
        this(null);
    }

    /**
     * 构造
     *
     * @param treeNodeConfig TreeNode配置
     */
    public Tree(final TreeNodeConfig treeNodeConfig) {
        this.treeNodeConfig = ObjectUtils.defaultIfNull(treeNodeConfig, TreeNodeConfig.treeNodeConfig);
    }

    /**
     * 获取节点配置
     *
     * @return 节点配置
     * @since 5.7.2
     */
    public TreeNodeConfig getConfig() {
        return this.treeNodeConfig;
    }

    /**
     * 获取父节点
     *
     * @return 父节点
     * @since 5.2.4
     */
    public Tree<T> getParent() {
        return this.parent;
    }

    /**
     * 设置父节点
     *
     * @param parent 父节点
     * @return this
     * @since 5.2.4
     */
    public Tree<T> setParent(final Tree<T> parent) {
        this.parent = parent;
        if (null != parent) this.setParentId(parent.getId());
        return this;
    }

    /**
     * 获取ID对应的节点，如果有多个ID相同的节点，只返回第一个。<br>
     * 此方法只查找此节点及子节点，采用广度优先遍历。
     *
     * @param id ID
     * @return 节点
     * @since 5.2.4
     */
    public Tree<T> getNode(final T id) {
        return TreeUtil.getNode(this, id);
    }

    /**
     * 获取所有父节点名称列表
     *
     * <p>
     * 比如有个人在研发1部，他上面有研发部，接着上面有技术中心<br>
     * 返回结果就是：[研发一部, 研发中心, 技术中心]
     *
     * @param id                 节点ID
     * @param includeCurrentNode 是否包含当前节点的名称
     * @return 所有父节点名称列表
     * @since 5.2.4
     */
    public List<CharSequence> getParentsName(final T id, final boolean includeCurrentNode) {
        return TreeUtil.getParentsName(this.getNode(id), includeCurrentNode);
    }

    /**
     * 获取所有父节点名称列表
     *
     * <p>
     * 比如有个人在研发1部，他上面有研发部，接着上面有技术中心<br>
     * 返回结果就是：[研发一部, 研发中心, 技术中心]
     *
     * @param includeCurrentNode 是否包含当前节点的名称
     * @return 所有父节点名称列表
     * @since 5.2.4
     */
    public List<CharSequence> getParentsName(final boolean includeCurrentNode) {
        return TreeUtil.getParentsName(this, includeCurrentNode);
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

    /**
     * 获取所有子节点
     *
     * @return 所有子节点
     */
    public List<Tree<T>> getChildren() {
        return (List<Tree<T>>) this.getOrDefault(this.treeNodeConfig.getChildrenKey(), new LinkedList<>());
    }

    /**
     * 设置子节点，设置后会覆盖所有原有子节点
     *
     * @param children 子节点列表
     * @return this
     */
    public Tree<T> setChildren(final List<Tree<T>> children) {
        this.put(this.treeNodeConfig.getChildrenKey(), children);
        return this;
    }

    /**
     * 增加子节点，同时关联子节点的父节点为当前节点
     *
     * @param children 子节点列表
     * @return this
     * @since 5.6.7
     */
    @SafeVarargs
    public final Tree<T> addChildren(final Tree<T>... children) {
        final List<Tree<T>> childrenList = this.getChildren();
        if (CollectionUtils.isEmpty(childrenList)) this.setChildren(childrenList);
        for (final Tree<T> child : children) {
            child.setParent(this);
            childrenList.add(child);
        }
        // 每次添加一个记录，就排序一下
        Collections.sort(childrenList);
        return this;
    }

    /**
     * 扩展属性
     *
     * @param key   键
     * @param value 扩展值
     */
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
