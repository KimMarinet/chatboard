package org.maengle.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.file.entities.FileInfo;
import org.maengle.global.entities.BaseEntity;
import org.maengle.member.constants.Authority;
import org.maengle.member.constants.Gender;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(indexes = {
        @Index(name = "idx_member_created_at", columnList = "createdAt DESC")
})
public class Member extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userUuid;

    @Column(length=65, nullable = false)
    private String gid;

    @Column(nullable = false, length = 50, unique = true)
    private String userId;

    @Column(length = 65)
    private String password;

    @Column(nullable = false, length = 45)
    private String name;

    // 주민등록번호
    @Column(length = 15)
    private String idNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 75)
    private String email;

    @Column(nullable = false, length = 15)
    private String mobile;

    // 회원 권한 Default : MEMBER (일반회원)
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.MEMBER;

    private boolean termsAgree;

    private boolean accountLocked; // 계정 정지 상태
    private LocalDateTime expired; // 계정 만료 일자
    private LocalDateTime credentialChangedAt; // PW 변경일

    //test
    @Transient
    private FileInfo profileImage;
}