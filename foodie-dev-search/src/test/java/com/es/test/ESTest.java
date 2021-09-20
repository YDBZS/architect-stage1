package com.es.test;


import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试ES
 *
 * @author 多宝
 * @since 2021/9/11 13:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 不建议使用ElasticsearchTemplate对索引进行管理(创建索引，更新索引，删除索引)
     * <p>
     * 索引就相当于是数据库或者说是数据库中的表，我们平时是不会去通过java代码频繁的去创建修改删除数据库或者表的
     * 我们只会针对数据进行crud的操作
     * 在ES中也是同理，我们尽量使用ElasticsearchTemplate对文档数据做CRUD的操作
     * 1、属性(FiledType)类型不灵活
     * 2、主分片与副本分片数无法设置
     */

    /**
     * 创建一个索引
     */
    @Test
    public void createIndexStu() {
        Student student = new Student();
        student.setStuId(1001L);
        student.setName("bat man");
        student.setAge(18);

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(student).build();
        elasticsearchTemplate.index(indexQuery);
    }


    /**
     * 在原有基础上添加文档映射
     */
    @Test
    public void addMapping() {
        Student student = new Student();
        student.setStuId(1001L);
        student.setName("man");
        student.setAge(26);
        student.setMoney(18.8F);
        student.setSign("men resposibility");
        student.setDescription("as a man, go fight for forever tomorrow is a responsibility");

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(student).build();
        elasticsearchTemplate.index(indexQuery);
    }

    /**
     * 对文档进行删除操作
     */
    @Test
    public void deleteIndexStu() {
        elasticsearchTemplate.deleteIndex(Student.class);
    }


    /***********************************ElasticsearchTemplate对文档数据进行操作****************************************/

    /**
     * 对文档进行修改操作
     */
    @Test
    public void updateStuDoc() {

        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("sign", "I am not superman");
        sourceMap.put("money", 88.6f);
        sourceMap.put("age", 88);


        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(sourceMap);


        UpdateQuery updateQueryBuilder = new UpdateQueryBuilder()
                .withClass(Student.class)        // 指定文档所在索引的位置
                .withId("1002")                  // 指明文档ID
                .withIndexRequest(indexRequest)
                .build();
        // update stu set sign = 'abc',age = 33,money = 88.6 where docId = '1002';

        elasticsearchTemplate.update(updateQueryBuilder);
    }


    /**
     * 获取一条文档数据
     */
    @Test
    public void getIndexStu() {
        GetQuery query = new GetQuery();
        query.setId("1002");

        Student student = elasticsearchTemplate.queryForObject(query, Student.class);
        System.out.printf("查询文档ID为1002的数据是%s", student);

    }

    /**
     * 删除一条文档
     */
    @Test
    public void deleteDocument() {
        elasticsearchTemplate.delete(Student.class, "1002");
    }

    /**
     * 针对text类型实现分页全文检索
     */
    @Test
    public void searchStuDoc() {
        Pageable pageable = PageRequest.of(0, 10);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "save man"))
                .withPageable(pageable)
                .build();

        AggregatedPage<Student> pageStu = elasticsearchTemplate.queryForPage(searchQuery, Student.class);
        System.out.printf("检索之后的总分页数目为%s%n", pageStu.getTotalPages());
        pageStu.getContent().forEach(item -> {
            System.out.printf("每一条数据是%s%n", item);
        });
    }

    /**
     * ES高亮查询
     */
    @Test
    public void highlightSearchDoc() {

        String preTag = "<font color='red'>";
        String postTag = "</font>";

        Pageable pageable = PageRequest.of(0, 10);

        SortBuilder<FieldSortBuilder> sortBuilder = new FieldSortBuilder("money")
                .order(SortOrder.DESC);

        SortBuilder<FieldSortBuilder> sortBuilderAge = new FieldSortBuilder("age")
                .order(SortOrder.DESC);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "save man"))
                .withHighlightFields(
                        new HighlightBuilder.Field("description")
                                .preTags(preTag)
                                .postTags(postTag)
                )                           // 数据高亮显示
                .withPageable(pageable)     // 查询分页设置
                .withSort(sortBuilder)                 // 数据排序
                .withSort(sortBuilderAge)
                .build();

        AggregatedPage<Student> pageStu = elasticsearchTemplate.queryForPage(searchQuery, Student.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                List<Student> studentListHighlight = Lists.newArrayList();

                SearchHits hits = searchResponse.getHits();
                hits.forEach(h -> {
                    HighlightField highlightField = h.getHighlightFields().get("description");
                    String description = highlightField.getFragments()[0].toString();

                    Student studentHL = new Student();
                    studentHL.setDescription(description);
                    studentHL.setStuId(Long.valueOf(h.getSourceAsMap().get("stuId").toString()));
                    studentHL.setName((String) h.getSourceAsMap().get("name"));
                    studentHL.setAge((Integer) h.getSourceAsMap().get("age"));
                    studentHL.setSign((String) h.getSourceAsMap().get("sign"));
                    studentHL.setMoney(Float.valueOf(h.getSourceAsMap().get("money").toString()));

                    studentListHighlight.add(studentHL);
                });
                if (ArchitectCollectionUtil.isNotEmpty(studentListHighlight)) {
                    return new AggregatedPageImpl<>((List<T>) studentListHighlight);
                }
                return null;
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }

        });
        System.out.printf("检索之后的总分页数目为%s%n", pageStu.getTotalPages());
        pageStu.getContent().forEach(item -> {
            System.out.printf("每一条数据是%s%n", item);
        });
    }


}
