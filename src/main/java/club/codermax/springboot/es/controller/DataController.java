package club.codermax.springboot.es.controller;


import club.codermax.springboot.es.entity.es.EsBlog;
import club.codermax.springboot.es.entity.mysql.MysqlBlog;
import club.codermax.springboot.es.repository.es.EsBlogRepository;
import club.codermax.springboot.es.repository.mysql.MysqlBlogRepository;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


//前后端分离，返回json
@RestController
public class DataController {

    @Autowired
    private MysqlBlogRepository mysqlBlogRepository;
    @Autowired
    private EsBlogRepository esBlogRepository;

    @GetMapping("/blogs")
    public Object blog(){
        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryAll();
        return mysqlBlogs;
    }

    @PostMapping("/search")
    public Object search(@RequestBody Param param){

        HashMap<String ,Object> map = new HashMap<>();

        //统计时间
        StopWatch watch = new StopWatch();
        watch.start();

        String type = param.getType();
        if(type.equalsIgnoreCase("mysql")){
            //mysql
            List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryBlogs(param.getKeyword());
            map.put("list",mysqlBlogs);

        }else if(type.equalsIgnoreCase("es")){
            //es
            //实现es的DSL查询语句
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.should(QueryBuilders.matchPhraseQuery("title",param.getKeyword()));
            builder.should(QueryBuilders.matchPhraseQuery("content",param.getKeyword()));
            String s = builder.toString();

            System.out.println(s);
            Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);

            List<EsBlog> content = search.getContent();

            map.put("list",content);

        }else{
            return "I don't understand";
        }
        watch.stop();
        //获取毫秒值
        long totalTimeMillis = watch.getTotalTimeMillis();
        map.put("duration",totalTimeMillis);
        return map;
    }

    @GetMapping("/blog/{id}")
    public Object blog(@PathVariable("id") Integer id){
        Optional<MysqlBlog> byId = mysqlBlogRepository.findById(id);
        MysqlBlog blog = byId.get();
        return blog;
    }




    @Data
    public static class Param{
        //获得前台传来要查询的方式，是mysql还是es
        private String type;
        //获得前台传递的关键字
        private String keyword;
    }



}
