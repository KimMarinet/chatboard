package org.maengle.banner.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.maengle.file.entities.FileInfo;
import org.maengle.global.entities.BaseEntity;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_banner_basic", columnList = "listOrder DESC, createdAt ASC"))
public class Banner extends BaseEntity {
    @Id
    @GeneratedValue
    private Long seq;

    @Column(length = 60, nullable = false)
    private String bName; // 배너명

    private String bLink; // 배너 링크
    private String target = "_self"; // 배너 타겟

    private long listOrder; // 진열 순서

    private boolean active; // 노출 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupCode")
    private BannerGroup bannerGroup;

    @Transient
    private FileInfo bannerImage;
}
