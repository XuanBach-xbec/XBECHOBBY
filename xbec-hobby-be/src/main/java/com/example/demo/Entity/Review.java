package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "gundam_id", unique = true)
    private GundamKit gundamKit;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String buildExperience;

    private Integer articulationScore;
    private Integer detailScore;

    private LocalDateTime createdAt = LocalDateTime.now();
}