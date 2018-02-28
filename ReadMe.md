## SpringBoot로 웹서비스 개발해보는 첫 프로젝트 [![Build Status](https://travis-ci.org/HyunjunJeon/springboot_first_webservice.svg?branch=master)](https://travis-ci.org/HyunjunJeon/springboot_first_webservice) #####

####1. Spring Boot 만나보기

####2. Spring Boot로 DB접근 해보기 + Time 주입 
      ( DB 에서 하지말고 애플리케이션 단에서 )

####3. 화면 만들어보기
    Handlebars는 흔히 사용하시는 Freemarker, Velocity와 같은 서버 템플릿 엔진
    JSP는 서버 템플릿 역할만 하지 않기 때문에 JSP와 완전히 똑같은 역할을 한다고 볼순 없지만, 
    순수하게 JSP를 View 용으로만 사용하실때는 똑같은 역할
    결국 URL 요청시, 파라미터와 상태에 맞춰 적절한 HTML 화면을 생성해 전달하는 역할을 하는것정도로...

    '''
    JSP, Freemarker, Velocity가 몇년동안 업데이트가 안되고 있어 사실상 SpringBoot에선 권장하지 않는 템플릿 엔진
    현재까지도 꾸준하게 업데이트 되고 있는 템플릿 엔진은 Thymeleaf, Handlebars 이며, 선택!!
    (Spring 진영에선 Thymeleaf를 밀고 있음.)
    '''
    Handlebars 는...
    (1) 문법이 다른 템플릿엔진보다 간단하고
    (2) 로직 코드를 사용할 수 없어 View의 역할과 서버의 역할을 명확하게 제한할 수 있으며
    (3) Handlebars.js와 Handlebars.java 2가지가 다 있어, 하나의 문법으로 클라이언트 템플릿/서버 템플릿을 모두 사용가능.

#### 4. AWS EC2(Ubuntu) & RDS (MariaDB) 설정

#### 5. EC2에 배포하기
    ./gradlew test

    작성한 코드를 실제 서버에 반영하는것 => 배포
    > git clone 혹은 git pull을 통해 새 버전의 프로젝트 받음
    > Gradle / Maven을 통해 프로젝트 Test & Build
    > EC2 서버에서 해당 프로젝트 실행 및 재실행

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
    
    프로젝트 디렉토리 주소는 스크립트 내에서 자주 사용하는 값이기 때문에 이를 변수로 저장

#### 6. TravisCI & AWS CodeDeploy로 배포 자동화 구축하기

    CI(지속적 통합)
     모든 소스 코드가 살아있고(현재 실행되고) 어느 누구든 현재의 소스를 접근할 수 있는 단일 지점을 유지할 것
     빌드 프로세스를 자동화시켜서 어느 누구든 소스로부터 시스템을 빌드하는 단일 명령어를 사용할 수 있게 할 것
     테스팅을 자동화시켜서 단일 명령어를 통해서 언제든지 시스템에 대한 건전한 테스트 수트를 실핼할 수 있게 할 것
     누구나 현재 실행 파일을 얻으면 지금까지 최고의 실행파일을 얻었다는 확신을 하게 만들 것

     지속적으로 통합하기 위해선 무엇보다 이 프로젝트가 완전한 상태임을 보장하기 위해 테스트 코드가 구현되어 있어야만한다!!

     * Jenkins는 설치형이라서 EC2 instance가 하나 더 필요하기때문에 (배포서버) 오픈소스 웹 서비스인 Travis CI를 이용해보자!!
     (AWS에 CodeBuild라는 서비스가 있지만...빌드시간만큼 과금되기때문에 ㅎㅎ 알아만두자...)

      CodeDeploy는 저장 기능없음 -> AWS S3를 이용
      >> 압축(zip)형태로 전달, 매번 파일로 복사하면 시간이 오래걸리니..
      
      "EC2 java 버전에서 1.8로 바꿧다고 생각했으나 문제 발생"
      다시 yum list java*jdk-devel 로 리스트를 확인
      yum install -y java-1.8.0-openjdk-devel.x86_64 다운로드
      sudo /usr/sbin/alternatives --config java 적용
      sudo yum remove java-1.7.0-openjdk 깔끔하게 삭제
      java -version 으로 한번 더 확인!
      
#### 7. Nginx 서버를 이용한 무중단 배포환경 구축하기

      1) AWS 블루그린 배포 ( 돈이 많다면....^^)
      2) Docker 환경을 이용한 웹서비스 무중단 배포 구축
      3) Nginx를 이용한 무중단 배포환경 구축 ( Reverse Proxy pass 이용 )       
      
    외부에 있는 이 파일을 프로젝트가 호출할 수 있도록 Application.java 코드 변경
    
    <배포 스크립트 작성>
    1번째 배포 디렉토리로 git (Github Pull 용)
    2번째 배포 디렉토리로 travis (Travis CI Auto Deploy용)
    3번째는 nonstop (Nginx Non-stop Deploy용)
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
    
    >> Docker 환경으로 재 구축 진행.
    

    
      
      
      


    





