package org.architect.elasticsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.architect.elasticsearch.common.Constant;
import org.architect.elasticsearch.service.ItemService;
import org.architect.elasticsearch.util.ESPageResponseUtil;
import org.architect.util.PagedGridResult;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author 多宝
 * @since 2021/9/20 14:56
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl implements ItemService {

    private final RestHighLevelClient client;

    @Override
    @SneakyThrows
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        SearchRequest request = new SearchRequest("foodie-items-ik");

        // 高亮查询构建
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("itemName");
        highlightBuilder.preTags(Constant.ES_HIGHLIGHT_PRE_TAGS);
        highlightBuilder.postTags(Constant.ES_HIGHLIGHT_POST_TAGS);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        QueryBuilder query = QueryBuilders.matchQuery("itemName", keywords);
        // 查询的索引
        builder.query(query);
        // 查询超时时间
        builder.timeout(new TimeValue(10, TimeUnit.SECONDS));
        // 高亮查询
        builder.highlighter(highlightBuilder);
        // ES分页查询
        builder.from(page);
        builder.size(pageSize);

        // 将查询条件构建请求
        request.source(builder);
        // 请求ES查询
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return ESPageResponseUtil.getEsHighLightPageResponse(response);
    }
}
