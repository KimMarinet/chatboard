package org.maengle.board.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.global.entities.BaseEntity;
import org.maengle.member.entities.Member;

import java.io.Serializable;

@Data
@Entity
public class BoardData extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long seq;

    @Column(length=45, nullable = false)
    private String gid;

    @JoinColumn(name="bid")
    @ManyToOne(fetch= FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length=60)
    private String category;

    @Column(length=60, nullable = false)
    private String poster;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    private int viewCount;

    @Column(length=20)
    private String ip;

    private String ua;
}
