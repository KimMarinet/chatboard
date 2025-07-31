package org.maengle.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.file.entities.FileInfo;
import org.maengle.global.entities.BaseEntity;
import org.maengle.model.constants.ModelStatus;

import java.util.List;

@Data
@Entity
@Table(indexes = {
        @Index(name="idx_model_created_at", columnList = "createdAt DESC"),
        @Index(name = "idx_model_count", columnList = "count DESC")
})
public class Model extends BaseEntity {

    @Id
    @GeneratedValue
    private Long seq;

    @Column(length = 45, nullable = false)
    private String gid;

    @Column(length = 150, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ModelStatus modelStatus;

    @Column(length = 60)
    private String category;

    @Column(length = 60)
    private String subcategory;

    private Long count; // 조회수

    @Lob
    private String description;

    @Transient
    private List<FileInfo> listImages;

    @Transient
    private List<FileInfo> mainImages;

}
