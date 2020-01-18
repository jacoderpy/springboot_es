package club.codermax.springboot.es.entity.mysql;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_blog")
public class MysqlBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //主键生成策略
    private Integer id;
    private String title;
    private String author;
    //这一列的字段类型比较大
    @Column(columnDefinition = "mediumtext")
    private String content;

    private Date createTime;
    private Date updateTime;

}
