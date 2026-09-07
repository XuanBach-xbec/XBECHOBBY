package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleVi;
    private String titleEn;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String descVi;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String descEn;

    private String btnTextVi;
    private String btnTextEn;

    private String link;
    private String videoUrl;
    private String thumbUrl;

    private Boolean isActive = true;
    private Integer sortOrder = 0;
}