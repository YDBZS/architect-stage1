package org.architect.elasticsearch.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 商品索引
 *
 * @author 多宝
 * @since 2021/9/20 14:44
 */
@Document(indexName = "foodie-items-ik", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {

    /**
     * 商品ID
     */
    @Id
    @Field(store = true, type = FieldType.Text, index = false)
    private String itemId;

    /**
     * 商品名称
     */
    @Field(store = true, type = FieldType.Text)
    private String itemName;

    /**
     * 商品图片
     */
    @Field(store = true, type = FieldType.Text, index = false)
    private String imgUrl;

    /**
     * 商品价格
     */
    @Field(store = true, type = FieldType.Integer)
    private Integer price;

    /**
     * 销量
     */
    @Field(store = true, type = FieldType.Integer)
    private Integer sellCounts;


}
