package org.maengle.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Categorys {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 64)
    private String name;

    @Column(length = 150)
    private String description; // 설명


}