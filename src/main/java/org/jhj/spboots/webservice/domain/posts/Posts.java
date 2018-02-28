package org.jhj.spboots.webservice.domain.posts;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jhj.spboots.webservice.domain.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
/*
    여기서 Posts 클래스는 실제 DB의 테이블과 매칭될 클래스이며 보통 Entity클래스라고도 합니다.
    JPA를 사용하시면 DB 데이터에 작업할 경우 실제 쿼리를 날리기 보다는, 이 Entity 클래스의 수정을 통해 작업합니다.

    Posts 클래스에는 JPA에서 제공하는 어노테이션들이 몇개 있습니다.

    @Entity
    테이블과 링크될 클래스임을 나타냅니다.
    언더스코어 네이밍(_)으로 이름을 매칭합니다.
    ex) SalesManager.java -> sales_manager table

    @Id
    해당 테이블의 PK 필드를 나타냅니다.

    @GeneratedValue
    PK의 생성 규칙을 나타냅니다.
    기본값은 AUTO 로, MySQL의 auto_increment와 같이 자동증가하는 정수형 값이 됩니다.

    @Column
    테이블의 컬럼을 나타내면, 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 컬럼이 됩니다.
    사용하는 이유는, 기본값 외에 추가로 변경이 필요한 옵션이 있을경우 사용합니다.
    문자열의 경우 VARCHAR(255)가 기본값인데, 사이즈를 500으로 늘리고 싶거나(ex: title),
    타입을 TEXT로 변경하고 싶거나(ex: content) 등의 경우에 사용됩니다.

    웬만하면 Entity의 PK는 Long 타입의 Auto_increment를 추천합니다.
    (MySQL 기준으로 이렇게 하면 bigint 타입이 됩니다.)
    주민등록번호와 같은 비지니스상 유니크키나, 여러키를 조합한 복합키로 PK를 잡을 경우 난감한 상황이 종종 발생합니다.
    (1) FK를 맺을때 다른 테이블에서 복합키 전부를 갖고 있거나, 중간 테이블을 하나더 둬야하는 상황이 발생합니다.
    (2) 인덱스에 좋은 영향을 끼치지 못합니다.
    (3) 유니크한 조건이 변경될 경우 PK 전체를 수정해야하는 일이 발생합니다.
    주민등록번호, 복합키 등은 유니크키로 별도로 추가하시는것을 추천드립니다

    (@NoArgsConstructor, @Getter, @Builder)

    이는 처음 프로젝트 생성시 추가했던 Lombok 라이브러리의 어노테이션들입니다.
    어노테이션 이름만 봐도 대략 기능을 예측할수 있습니다.

    @NoArgsConstructor : 기본 생성자 자동 추가
    access = AccessLevel.PROTECTED : 기본생성자의 접근 권한을 protected로 제한
    생성자로 protected Posts() {}와 같은 효과
    Entity 클래스를 프로젝트 코드상에서 기본생성자로 생성하는 것은 막되, JPA에서 Entity 클래스를 생성하는것은 허용하기 위해 추가
    @Getter : 클래스내 모든 필드의 Getter 메소드를 자동생성
    @Builder : 해당 클래스의 빌더패턴 클래스를 생성
    생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
    특히 서비스 구축단계에선 테이블 설계(여기선 Entity설계)가 빈번하게 변경되는데,
    이때 Lombok의 어노테이션들은 코드 변경량을 최소화시켜주기 때문에 아주 강력 추천하는 라이브러리입니다.

    Entity 클래스를 생성하실때, 주의하실것은 무분별한 setter 메소드 생성입니다.
    자바빈 규약을 생각하시면서 getter/setter를 무작정 생성하시는 분들이 계시는데요.
    이렇게 되면 해당 클래스의 인스턴스 값들이 언제 어디서 변해야하는지 코드상으로 명확히 구분할수가 없어, 차후 기능변경시 정말 복잡해집니다.
    해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가하셔야만 합니다.

    기본생성자도 AccessLevel.PROTECTED로 막아놓고, setter 메소드도 없는 이 상황에서 어떻게 값을 채워 DB에 insert 해야할까요?

    생성자 대신에 @Builder를 통해 제공되는 빌더 클래스를 사용합니다.
    생성자나 빌더나 생성시점에 값을 채워주는 역할은 똑같습니다.
    다만, 생성자의 경우 지금 채워야할 필드가 무엇인지 명확히 지정할수가 없습니다.


 */
