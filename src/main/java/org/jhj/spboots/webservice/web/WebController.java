package org.jhj.spboots.webservice.web;

import lombok.AllArgsConstructor;
import org.jhj.spboots.webservice.service.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class WebController {

    private PostsService postsService;

    @GetMapping("/")
    public String moveMain(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        return "main";
    }
    /*
        handlebars-spring-boot-starter 덕분에 컨트롤러에서 문자열을 반환할때 앞의 path와 뒤의 파일 확장자는 자동으로 지정됩니다.
        (prefix: src/main/resources/templates, suffix: .hbs)
        ViewResolver는 URL 요청의 결과를 전달할 타입과 값을 지정
     */
}
