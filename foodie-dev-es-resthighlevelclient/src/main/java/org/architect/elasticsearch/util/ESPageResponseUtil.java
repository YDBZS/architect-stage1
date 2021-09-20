package org.architect.elasticsearch.util;

import com.google.common.collect.Lists;
import org.architect.util.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 封装ES搜索分页信息
 *
 * @author 多宝
 * @since 2021/9/20 16:25
 */
public class ESPageResponseUtil {

    /**
     * 获取ES的分页信息封装返回
     *
     * @param response ES返回查询数据
     * @return org.architect.util.PagedGridResult
     * @author 多宝
     * @since 2021/9/20 16:28
     */
    public static PagedGridResult getEsPageResponse(SearchResponse response) {
        return new PagedGridResult();
    }

    /**
     * 封装ES分页高亮返回信息
     *
     * @param response ES查询数据
     * @return org.architect.util.PagedGridResult
     * @author 多宝
     * @since 2021/9/20 16:29
     */
    public static PagedGridResult getEsHighLightPageResponse(SearchResponse response) {
        PagedGridResult result = new PagedGridResult();
        int total = (int) response.getHits().getTotalHits().value;
        int records = response.getHits().getHits().length;
        List<?> rows = getRows(response.getHits().getHits());

        result.setTotal(total);
        result.setRows(rows);
        result.setPage(1);
        result.setRecords(records);
        return result;
    }

    /**
     * 获取分页数据
     *
     * @param hits 组合封装分页的数据
     * @return java.util.List<?>
     * @author 多宝
     * @since 2021/9/20 16:45
     */
    private static List<?> getRows(SearchHit[] hits) {
        List<Map<String, Object>> list = Lists.newArrayList();
        List<SearchHit> searchHitList = Arrays.asList(hits);

        searchHitList.forEach(item -> {
            Map<String, Object> sourceAsMap = item.getSourceAsMap();
            Map<String, HighlightField> highlightFieldsMap = item.getHighlightFields();
            highlightFieldsMap.forEach((field, value) -> {
                if (null != sourceAsMap.get(field)) {
                    String highLightFiled = value.getFragments()[0].toString();
                    sourceAsMap.put(field, highLightFiled);
                    list.add(sourceAsMap);
                }
            });
        });
        return list;
    }

}
