package com.chensoul.tree;

import com.chensoul.collection.MapUtils;
import java.util.Map;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 * @version $Id: $Id
 */
public class DefaultNodeParser<T> implements NodeParser<TreeNode<T>, T> {

    /** {@inheritDoc} */
    @Override
    public void parse(final TreeNode<T> treeNode, final Tree<T> tree) {
        tree.setId(treeNode.getId());
        tree.setParentId(treeNode.getParentId());
        tree.setWeight(treeNode.getWeight());
        tree.setName(treeNode.getName());

        // 扩展字段
        final Map<String, Object> extra = treeNode.getExtra();
        if (!MapUtils.isEmpty(extra)) {
            extra.forEach(tree::putExtra);
        }
    }

}
