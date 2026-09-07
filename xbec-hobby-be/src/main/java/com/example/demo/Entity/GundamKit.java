package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GundamKits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GundamKit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @Column(nullable = false)
    private String name;

    private String modelNumber;
    private String grade;
    private String scale;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String originDescription;

    private String coverImage;
}