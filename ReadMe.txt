1. Spring Boot 만나보기

2. Spring Boot로 DB접근 해보기 + Time 주입 ( DB 에서 하지말고 애플리케이션 단에서 )

3. 화면 만들어보기
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



4. AWS EC2(Ubuntu) & RDS ( MariaDB) 설정

5. EC2에 배포하기
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




