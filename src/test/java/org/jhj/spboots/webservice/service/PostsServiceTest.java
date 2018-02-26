package org.jhj.spboots.webservice.service;

import org.jhj.spboots.webservice.domain.posts.Posts;
import org.jhj.spboots.webservice.domain.posts.PostsRepository;
import org.jhj.spboots.webservice.dto.posts.PostsSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsServiceTest {
    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void savePostsDto(){
        //given
        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .author("jeonhj920@gmail.com")
                .content("테스트")
                .title("테스트 해보는 타이틀")
                .build();
        //when
        postsService.save(dto);

        //then
        Posts posts = postsRepository.findAll().get(0);
        assertThat(posts.getAuthor()).isEqualTo(dto.getAuthor());
        assertThat(posts.getContent()).isEqualTo(dto.getContent());
        assertThat(posts.getTitle()).isEqualTo(dto.getTitle());
    }

}
