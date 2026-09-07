package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Series")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    private String timeline;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    private String coverUrl;
}