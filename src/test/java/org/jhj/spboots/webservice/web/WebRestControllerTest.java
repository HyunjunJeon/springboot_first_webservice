package org.jhj.spboots.webservice.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jeonhj920@gmail.com on 2018. 2. 28.
 * Blog : http://HyunjunJeon.github.io
 * Github : http://github.com/HyunjunJeon
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void ProfileCheck() {
        //when
        String profile = this.restTemplate.getForObject("/profile", String.class);
        //then
        assertThat(profile).isEqualTo("local");
    }
    /*
    테스트 내용은 /profile로 요청하면 현재 활성화된 Profile값 (local이) 반환되는지 비교하는 것입니다.
    근데 왜 테스트 코드에서 local일까요?
    이는 src/test/resources/application.yml 때문입니다.
    테스트의 환경은 src/test/resources/application.yml에 의존하는데 이 yml에 spring.profile.active가 local로 되있기 때문입니다.

     */
}
