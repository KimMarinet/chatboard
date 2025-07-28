package org.maengle.file.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.global.entities.BaseEntity;
import org.springframework.data.annotation.CreatedBy;

import java.io.Serializable;

@Data
@Entity
@Table(indexes = {
        @Index(name="idx_fileinfo_gid1", columnList = "gid,createdAt"),
        @Index(name="idx_fileinfo_gid2", columnList = "gid,done,createdAt"),
        @Index(name="idx_fileinfo_location1", columnList = "gid,location,createdAt"),
        @Index(name="idx_fileinfo_location2", columnList = "gid,location,done,createdAt")
})
public class FileInfo extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long seq;

    @Column(length = 45, nullable = false)
    private String gid;

    @Column(length = 45)
    private String location;

    @Column(nullable = false, length = 150)
    private String fileName;

    @Column(length = 60)
    private String extension;

    @Column(length = 100)
    private String contentType;

    @CreatedBy
    private String createdBy;

    private boolean done;

    @Transient
    private String filePath;

    @Transient
    private String fileUrl;

    @Transient
    private String thumbBaseUrl;

    @Transient
    private String thumbBasePath;

    @Transient
    private boolean image;

}
