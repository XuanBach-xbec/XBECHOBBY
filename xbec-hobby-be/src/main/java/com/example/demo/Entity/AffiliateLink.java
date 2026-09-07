package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AffiliateLinks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "gundam_id")
    private GundamKit gundamKit;

    private String itemType;
    private String itemName;

    @Column(nullable = false, length = 1000)
    private String affiliateUrl;

    private String priceRange;
}