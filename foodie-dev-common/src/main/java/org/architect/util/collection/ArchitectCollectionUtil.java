package org.architect.util.collection;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;

import java.util.Collection;

/**
 * 用于操作集合判空
 *
 * @author 多宝
 * @since 2021/5/23 17:32
 */
public class ArchitectCollectionUtil {

    /**
     * 集合判空
     *
     * @param collection 集合信息
     * @return boolean
     * @since 2021/5/23 17:34
     */
    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    /**
     * 集合非空判断
     *
     * @param collection 集合类
     * @return boolean
     * @author 多宝
     * @since 2021/5/23 17:41
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

}
