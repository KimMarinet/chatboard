package org.maengle.model.entities;

import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.Data;
import org.maengle.chatbot.constants.ChatbotModel;
import org.maengle.file.entities.FileInfo;
import org.maengle.global.entities.BaseEntity;
import org.maengle.model.constants.ModelStatus;

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
    @Column(nullable = false)
    private ModelStatus modelStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatbotModel chatbotModel;

    @Column(length=60)
    private String category;

    @Column(length=60)
    private String subCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categorys; // 분류 하기 기 위래서 사용


    private long count; // 조회수

    @Lob
    private String description;

    @Transient
    private FileInfo mainImage;

}