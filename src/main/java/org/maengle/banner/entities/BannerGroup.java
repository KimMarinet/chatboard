package org.maengle.banner.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.maengle.global.entities.BaseEntity;

import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BannerGroup extends BaseEntity {
    @Id
    private String groupCode;

    @Column(length=80, nullable = false)
    private String groupName; // 배너 그룹명

    private boolean active; // 사용 여부

    @Transient
    private List<Banner> banners;
}
