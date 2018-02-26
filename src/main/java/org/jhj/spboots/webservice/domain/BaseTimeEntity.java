package org.jhj.spboots.webservice.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;


}
/*
    보통 Entity에는 해당 데이터의 생성시간과 수정시간을 포함시킵니다.
    언제 만들어졌는지, 언제 수정되었는지 등은 차후 유지보수에 있어 굉장히 중요한 정보이기 때문입니다.
    그렇다보니 매번 DB에 insert하기전, update 하기전에 날짜 데이터를 등록/수정 하는 코드가 여기저기 들어가게 됩니다.
    이 문제를 해결하기 위해 JPA Auditing를 사용하겠습니다.

    날짜 타입을 사용합니다.
    Java8 부터 LocalDate와 LocalDateTime이 등장했는데요.
    그간 Java의 기본 날짜 타입인 Date의 문제점을 제대로 고친 타입이라 Java8일 경우 무조건 써야한다고 생각하면 됌.

    현재 SpringDataJpa 버전에선 LocalDate와 LocalDateTime이 Database 저장시 제대로 전환이 안되는 이슈가 있습니다.
    이 문제를 SpringDataJpa의 코어 모듈인 Hibernate core 5.2.10부터는 해결되었어서 이 부분을 교체해보겠습니다.
    (build.gradle 참고 )


    BaseTimeEntity클래스는 모든 Entity들의 상위 클래스가 되어 Entity들의 createdDate, modifiedDate를 자동으로 관리하는 역할입니다.

    @MappedSuperclass
    JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드들(createdDate, modifiedDate)도 컬럼으로 인식하도록 합니다.

    @EntityListeners(AuditingEntityListener.class)
    BaseTimeEntity클래스에 Auditing 기능을 포함시킵니다.

    @CreatedDate
    Entity가 생성되어 저장될 때 시간이 자동 저장됩니다.

    @LastModifiedDate
    조회한 Entity의 값을 변경할 때 시간이 자동 저장됩니다.


*/