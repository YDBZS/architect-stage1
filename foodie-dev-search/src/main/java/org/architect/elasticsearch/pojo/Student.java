package org.architect.elasticsearch.pojo;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 学生类
 *
 * @author 多宝
 * @since 2021/9/11 13:44
 */
@Data
@ToString
@Document(indexName = "stu", type = "_doc")
public class Student {

    @Id
    private Long stuId;

    @Field(store = true)
    private String name;

    @Field(store = true)
    private Integer age;

    @Field(store = true)
    private Float money;

    @Field(store = true,type = FieldType.Keyword)
    private String sign;

    @Field(store = true)
    private String description;


}
