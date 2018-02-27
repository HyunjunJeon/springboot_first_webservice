package org.jhj.spboots.webservice.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){
        // 이후 테스트 코드에 영향을 끼치지 않기 위해서 테스트 메서드 끝날떄마다 Repository 전체를 비워줌
        postsRepository.deleteAll();
    }

    @Test
    public void boardSaveAndRead(){
        //given
        /*
            테스트 기반 환경 구축
         */
        postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .author("jeonhj920@gmail.com")
                .build());

        //when
        /*
            테스트 하고자 하는 행위 선언
        */
        List<Posts> postsList = postsRepository.findAll();

        //then
        /*
            테스트 결과 검증
         */
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle(), is("테스트 게시글"));
        assertThat(posts.getContent(), is("테스트 본문"));
    }

    @Test
    public void BaseTimeEntity_Test(){
        //given
        LocalDateTime now1 = LocalDateTime.now();
        postsRepository.save(Posts.builder()
                .title("테스트 게시글2")
                .content("테스트 본문2")
                .author("jeonhj920@gmail.com")
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();
        //then
        Posts posts = postsList.get(0);
        assertTrue(posts.getCreatedDate().isAfter(now1));
        assertTrue(posts.getModifiedDate().isAfter(now1));
    }

}