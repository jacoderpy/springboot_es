package club.codermax.springboot.es;

import club.codermax.springboot.es.entity.es.EsBlog;
import club.codermax.springboot.es.repository.es.EsBlogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;


@SpringBootTest
class SpringbootEsApplicationTests {

    @Autowired
    private EsBlogRepository esBlogRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testEs(){
        Iterable<EsBlog> all = esBlogRepository.findAll();
        Iterator<EsBlog> iterator = all.iterator();
        EsBlog next = iterator.next();
        System.out.println("-----------"+ next.getTitle());
    }

}
