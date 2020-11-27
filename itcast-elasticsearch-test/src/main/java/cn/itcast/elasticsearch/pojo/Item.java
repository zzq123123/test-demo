package cn.itcast.elasticsearch.pojo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
/**
 * Package: cn.itcast.elasticsearch.pojo
 * Description：
 * Author: wude
 * Date:  2020-11-25 12:43
 * Modified By:
 */
@Data
@Document(indexName = "item",type="docs",shards = 1,replicas = 0)
public class Item {
//字段类型 存储(自动保存_search)  索引 分词
    @Id
    Long id;
    //默认创建索引
    @Field(type = FieldType.Text,index = true,analyzer = "ik_max_word")
    String title;
    @Field(type=FieldType.Keyword)
    String category;
    @Field(type=FieldType.Keyword)
    String brand;
    @Field(type=FieldType.Double)
    Double price;
    @Field(type=FieldType.Keyword,index = false)
    String images;
}