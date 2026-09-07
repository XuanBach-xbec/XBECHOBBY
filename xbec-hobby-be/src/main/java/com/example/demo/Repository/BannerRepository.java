package com.example.demo.Repository;

import com.example.demo.Entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    // Lấy danh sách banner đang bật và sắp xếp theo thứ tự
    List<Banner> findByIsActiveTrueOrderBySortOrderAsc();
}