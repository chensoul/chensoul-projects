package com.chensoul.tree;

import java.util.Map;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 *
 */
public class DefaultNodeParser<T> implements NodeParser<TreeNode<T>, T> {
    @Override
    public void parse(final TreeNode<T> treeNode, final Tree<T> tree) {
        tree.setId(treeNode.getId());
        tree.setParentId(treeNode.getParentId());
        tree.setWeight(treeNode.getWeight());
        tree.setName(treeNode.getName());

        // 扩展字段
        final Map<String, Object> extra = treeNode.getExtra();
        if (extra != null && extra.size() > 0) {
            extra.forEach(tree::putExtra);
        }
    }

}
