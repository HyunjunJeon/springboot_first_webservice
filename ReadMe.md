## SpringBoot로 웹서비스 개발해보는 첫 프로젝트 [![Build Status](https://travis-ci.org/HyunjunJeon/springboot_first_webservice.svg?branch=master)](https://travis-ci.org/HyunjunJeon/springboot_first_webservice) #####

####1. Spring Boot 만나보기

####2. Spring Boot로 DB접근 해보기 + Time 주입 
      ( DB 에서 하지말고 애플리케이션 단에서 )

####3. 화면 만들어보기
    Handlebars는 흔히 사용하시는 Freemarker, Velocity와 같은 서버 템플릿 엔진입니다.
    JSP는 서버 템플릿 역할만 하지 않기 때문에 JSP와 완전히 똑같은 역할을 한다고 볼순 없지만, 순수하게 JSP를 View 용으로만 사용하실때는 똑같은 역할이라고 보시면 됩니다.

    결국 URL 요청시, 파라미터와 상태에 맞춰 적절한 HTML 화면을 생성해 전달하는 역할을 하는것으로 보시면 됩니다.

    Tip)
    JSP, Freemarker, Velocity가 몇년동안 업데이트가 안되고 있어 사실상 SpringBoot에선 권장하지 않는 템플릿엔진입니다.
    (Freemarker는 프리뷰버전은 계속나오고 있는데 릴리즈버전이 2015년이 마지막입니다.)
    현재까지도 꾸준하게 업데이트 되고 있는 템플릿 엔진은 Thymeleaf, Handlebars 이며 이 중 하나를 선택하시면 됩니다.
    개인적으로는 Handlebars를 추천합니다.
    (Spring 진영에선 Thymeleaf를 밀고 있습니다.)

    (1) 문법이 다른 템플릿엔진보다 간단하고
    (2) 로직 코드를 사용할 수 없어 View의 역할과 서버의 역할을 명확하게 제한할 수 있으며
    (3) Handlebars.js와 Handlebars.java 2가지가 다 있어, 하나의 문법으로 클라이언트 템플릿/서버 템플릿을 모두 사용할 수 있습니다.

    개인적으로 View 템플릿엔진은 View의 역할에만 충실하면 된다고 생각합니다.
    너무 많은 기능을 제공하면 API와 View템플릿엔진, JS가 서로 로직을 나눠갖게 되어 유지보수하기가 굉장히 어렵습니다

    의존성 하나만 추가하면 기존에 다른 스타터 패키지와 마찬가지로 추가 설정없이 설치가 끝입니다.
    다른 서버 템플릿 스타터 패키지와 마찬가지로 Handlebars도 기본 경로는 src/main/resources/templates가 됩니다.

    Tip)
    스프링부트는 디폴트 설정이 굉장히 많습니다.
    기존의 스프링처럼 개인이 하나하나 설정 코드를 다 작성할 필요가 없습니다.
    스프링부트를 쓰면 많은 설정을 생략할 수 있습니다.
    영상을 참고하셔서 이런 점들은 숙지하시면 좋을것 같습니다.

#### 4. AWS EC2(Ubuntu) & RDS ( MariaDB) 설정

#### 5. EC2에 배포하기
    ./gradlew test

    작성한 코드를 실제 서버에 반영하는것을 배포라고 합니다.
    여기서는 배포라 하면 다음의 과정을 모두 합친 뜻이라고 보시면 됩니다.

    git clone 혹은 git pull을 통해 새 버전의 프로젝트 받음
    Gradle / Maven을 통해 프로젝트 Test & Build
    EC2 서버에서 해당 프로젝트 실행 및 재실행

    /home/ubuntu/app/git/deploy.sh
        변수에 현재 build 디렉토리 주소 저장
        변수 + git clone 디렉토리로 이동
        디렉토리에서 git pull 하여 최신버전으로 교체
        프로젝트 빌드
        build 파일을 변수에 저장된 주소로 복사
        기존에 실행중인 스프링부트 Process id 가져옴
        실행중인 스프링부트가 있으면 종료(kill -2 or kill -15) 5초간 대기
        복사했던 build 파일명 저장
        build 파일 실행

    프로젝트 디렉토리 주소는 스크립트 내에서 자주 사용하는 값이기 때문에 이를 변수로 저장합니다.
    쉘에선 타입 없이 선언하여 저장을 합니다.
    쉘에선 $변수명으로 변수를 사용할 수 있습니다.

    cd $REPOSITORY/springboot-webservice/
    제일 처음 git clone 받았던 디렉토리로 이동합니다.
    바로 위의 쉘 변수 설명처럼 $REPOSITORY으로 /home/ec2-user/app/git을 가져와 /springboot-webservice/를 붙인 디렉토리 주소로 이동합니다.

    git pull
    디렉토리 이동후, master브랜치의 최신 내용을 받습니다.

    ./gradlew build
    프로젝트 내부의 gradlew로 build를 수행합니다.

    cp ./build/libs/*.jar $REPOSITORY/
    build의 결과물인 jar파일을 복사해 jar파일을 모아둔 위치로 복사합니다.

    CURRENT_PID=$(pgrep -f springboot-webservice)
    기존에 수행중이던 스프링부트 어플리케이션을 종료합니다.
    pgrep은 process id만 추출하는 명령어입니다.
    -f 옵션은 프로세스 이름으로 찾습니다.
    좀 더 자세한 옵션을 알고 싶으시면 공식 홈페이지를 참고하시면 좋습니다.

    if ~ else ~ fi
    현재 구동중인 프로세스가 있는지 없는지 여부를 판단해서 기능을 수행합니다.
    process id값을 보고 프로세스가 있으면 해당 프로세스를 종료합니다.

    JAR_NAME=$(ls $REPOSITORY/ |grep 'springboot-webservice' | tail -n 1)
    새로 실행할 jar 파일명을 찾습니다.
    여러 jar파일이 생기기 때문에 tail -n로 가장 나중의 jar파일(최신 파일)을 변수에 저장합니다.
    nohup java -jar $REPOSITORY/$JAR_NAME &
    찾은 jar파일명으로 해당 jar파일을 nohup으로 실행시킵니다.
    스프링부트의 장점으로 특별히 외장 톰캣을 설치할 필요가 없습니다.
    내장 톰캣을 사용해서 jar 파일만 있으면 바로 웹 어플리케이션 서버가 실행할수 있습니다.
    좀 더 자세한 스프링부트의 장점을 알고 싶으시면 이전에 작성한 SpringBoot의 깨알같은 팁을 참고하시면 좋습니다.
    일반적으로 Java를 실행시킬때는 java -jar라는 명령어를 사용하지만, 이렇게 할 경우 사용자가 터미널 접속을 끊을 경우 어플리케이션도 같이 종료가 됩니다.
    어플리케이션 실행자가 터미널을 종료시켜도 어플리케이션은 계속 구동될 수 있도록 nohup명령어를 사용합니다.
    nohup은 실행시킨 jar파일의 로그 내용을 nohup.out 이란 파일에 남깁니다.

    수동 Test
    본인이 짠 코드가 다른 개발자의 코드에 영향을 끼치지 않는지 확인하기 위해 전체 테스트를 수행해야만 합니다.
    현재 상태에선 항상 개발자가 작업을 진행할때마다 수동으로 전체 테스트를 수행해야만 합니다.
    수동 Build
    다른 사람이 작성한 브랜치와 제가 작성한 브랜치가 합쳐졌을때(Merge) 이상이 없는지는 Build를 수행해야만 알수 있습니다.
    이를 매번 개발자가 직접 실행해봐야만 됩니다.


#### 6. TravisCI & AWS CodeDeploy로 배포 자동화 구축하기

    CI(지속적 통합)
     모든 소스 코드가 살아있고(현재 실행되고) 어느 누구든 현재의 소스를 접근할 수 있는 단일 지점을 유지할 것
     빌드 프로세스를 자동화시켜서 어느 누구든 소스로부터 시스템을 빌드하는 단일 명령어를 사용할 수 있게 할 것
     테스팅을 자동화시켜서 단일 명령어를 통해서 언제든지 시스템에 대한 건전한 테스트 수트를 실핼할 수 있게 할 것
     누구나 현재 실행 파일을 얻으면 지금까지 최고의 실행파일을 얻었다는 확신을 하게 만들 것

     지속적으로 통합하기 위해선 무엇보다 이 프로젝트가 완전한 상태임을 보장하기 위해 테스트 코드가 구현되어 있어야만한다!!

     * Jenkins는 설치형이라서 EC2 instance가 하나 더 필요하기때문에 (배포서버) 오픈소스 웹 서비스인 Travis CI를 이용해보자!!
     (AWS에 CodeBuild라는 서비스가 있지만...빌드시간만큼 과금되기때문에 ㅎㅎ 알아만두자...)

      branches
      오직 master브랜치에 push될때만 수행됩니다.

      cache
      Gradle을 통해 의존성을 받게 되면 이를 해당 디렉토리에 캐시하여, 같은 의존성은 다음 배포때부터 다시 받지 않도록 설정합니다.

      script
      master 브랜치에 PUSH 되었을때 수행하는 명령어입니다.
      여기선 프로젝트 내부에 둔 gradlew을 통해 clean & build 를 수행합니다.

      notifications
      Travis CI 실행 완료시 자동으로 알람이 가도록 설정합니다
      
      AWS EC2 (Ubuntu) 내부에서 Yum MirrorList 0 이 발생.
        > EC2 Instance 새로 만듦 ( AMI로 - 내부는 CentOS와 동일 )
        
      CodeDeploy는 저장 기능이 없습니다. 
      그래서 Travis CI가 Build 한 결과물을 받아서 CodeDeploy가 가져갈 수 있도록 보관할 수 있는 공간이 필요합니다. 
      보통은 이럴때 AWS S3를 이용합니다. 
      
      매번 Travis CI에서 파일을 하나하나 복사하는건 복사시간이 많이 걸리기 때문에 프로젝트 폴더 채로 압축해서 
      S3로 전달하도록 설정을 조금 추가하겠습니다.
      
      "EC2 java 버전에서 1.8로 바꿧다고 생각했으나 문제 발생"
      다시 yum list java*jdk-devel 로 리스트를 확인
      yum install -y java-1.8.0-openjdk-devel.x86_64 다운로드
      sudo /usr/sbin/alternatives --config java 적용
      sudo yum remove java-1.7.0-openjdk 깔끔하게 삭제
      java -version 으로 한번 더 확인!
      
#### 7. Nginx 서버를 이용한 무중단 배포환경 구축하기

      1) AWS 블루그린 배 ( 돈이 많다면....^^)
      2) Docker 환경을 이용한 웹서비스 무중단 배포 구축
      3)         
      EC2 혹은 리눅스 서버에 Nginx 1대와 스프링부트 jar를 2대를 사용하는 것입니다. 
      Nginx는 80(http), 443(https) 포트를 할당하고, 
      스프링부트1은 8081포트로, 
      스프링부트2는 8082포트로 실행
      사용자는 서비스 주소로 접속합니다 (80 혹은 443 포트)
      Nginx는 사용자의 요청을 받아 현재 연결된 스프링부트로 요청을 전달합니다.
      스프링부트1 즉, 8081 포트로 요청을 전달한다고 가정하겠습니다.
      스프링부트2는 Nginx와 연결된 상태가 아니니 요청을 받지 못한다.
      1.1 버전으로 신규 배포가 필요하면 Nginx와 연결되지 않은 스프링부트2 (8082)로 배포합니다.
      배포하는 동안에도 서비스는 중단되지 않습니다.
      Nginx는 스프링부트1을 바라보기 때문입니다.
      배포가 끝나고 정상적으로 스프링부트2가 구동중인지 확인합니다.
      스프링부트2가 정상 구동중이면 nginx reload를 통해 8081 대신에 8082를 바라보도록 합니다.
      Nginx Reload는 1초 이내에 실행완료가 됩니다.
      또다시 신규버전인 1.2 버전의 배포가 필요하면 이번엔 스프링부트1로 배포합니다.
      현재는 스프링부트2가 Nginx와 연결되있기 때문입니다.
      스프링부트1의 배포가 끝났다면 Nginx가 스프링부트1을 바라보도록 변경하고 nginx reload를 실행합니다.
      1.2 버전을 사용중인 스프링부트1로 Nginx가 요청을 전달합니다.
      만약 배포된 1.2 버전에서 문제가 발생한다? 
      그러면 바로 Nginx가 8082 포트(스프링부트2)를 보도록 변경하면 됩니다.
      
     이렇게 Nginx가 외부의 요청을 받아 뒷단 서버로 요청을 전달하는 행위를 ####리버스 프록시####라고 합니다. 
     이런 리버스 프록시 서버(Nginx)는 요청을 전달하고, 실제 요청에 대한 처리는 뒷단의 웹서버들이 처리합니다. 
     대신 외부 요청을 뒷단 서버들에게 골고루 분배한다거나, 
     한번 요청왔던 js, image, css등은 캐시하여 리버스 프록시 서버에서 바로 응답을 주거나 등의 여러 장점들이 있습니다.
     
     
     sudo vi /etc/nginx/nginx.conf
     location/{
     
     }
     proxy_pass : 요청이 오면 http://localhost:8080로 전달
     proxy_set_header XXX : 실제 요청 데이터를 header의 각 항목에 할당
     ex) proxy_set_header X-Real-IP $remote_addr: Request Header의 X-Real-IP에 요청자의 IP를 저장
     
    실제 서비스에선 로컬, 개발서버, 운영서버 등으로 환경이 분리되어 접속하는 DB 값, 외부 API 주소등이 서로 다릅니다. 
    하지만 프로젝트의 코드는 하나인데, 어떻게 로컬, 개발, 운영 환경을 구분해서 필요한 값들을 사용할까요? 
    아주 오래전에는 이를 필요한 부분에서 전부 if ~ else로 구분해서 사용했습니다. 
    하지만 최근에는 이를 개선해서 외부의 설정 파일을 통해 사용하도록 하였습니다. 
    스프링부트는 .properties, .yml 파일을 통해 여러 설정값을 관리합니다.
        
    운영 환경의 yml은 프로젝트 내부가 아닌 외부에 생성하겠습니다. 
    본인이 원하는 디렉토리에 real-application.yml을 생성합니다. 
    EC2
    /app/config/springboot-webservice/real-application.yml
        
        > 내용
        --- 
        spring: profiles: set1 
        server: port: 8081 
        --- 
        spring: profiles: set2 
        server: port: 8082
    
    절대 프로젝트 내부에 운영환경의 yml을 포함시키지 않습니다. 
    Git Push를 혹시나 한번이라도 하셨다면 프로젝트를 삭제하시는걸 추천드립니다. 
    Git은 한번이라도 커밋 되면 이력이 남기 때문에 단순히 파일 삭제만 한다고 내용이 사라지지 않습니다. 
    Github 같이 오픈된 공간에 운영환경의 설정 (Database 접속정보, 세션저장소 접속정보, 암호화 키 등등)
    현재는 크리티컬한 정보를 다루지 않기 때문에 괜찮지만, 절대 주의해야합니다.
    
    외부에 있는 이 파일을 프로젝트가 호출할 수 있도록 Application.java 코드 변경
    
    <배포 스크립트 작성>
    1번째 배포 디렉토리로 git 
    2번째 배포 디렉토리로 travis 
    3번째는 nonstop
    (1) 스크립트에 필요한 변수 할당
    (2) 빌드된 JAR 파일 nonstop/jar/ 에 복사
    (3) 현재 구동중인 set 확인
    (4) Nginx에 연결되어 있지 않은 profile 찾기
    (5) 미연결된 Jar로 신규 Jar를 심볼릭 링크 걸어주기
    (6) Nginx와 연결되지 않은 profile 강제 종료
    (7) 위의 Profile로 Jar실
    (8) 10회 반복 시작
        (9) /health 요청 결과 저장
        (10) "UP" 문자열이 있다면 for loop 종료 / 없다면 메시지 출력
    (11) 10회 실행될 동안 마무리 하지 못했다면 현 스크립트 종료 선언
    

    
      
      
      


    





