package com.sce.data.gaia;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sce.data.gaia.dao.domain.QCustomUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QuerydslTest {
    @Autowired
    JPAQueryFactory queryFactory;
    @Test
    public void getDayPVUVPageTest(){
        QCustomUser user = QCustomUser.customUser;
        List<String> list =  queryFactory.select(user.userName.max()).from(user).fetch();
        System.out.println(1);
    }

}
