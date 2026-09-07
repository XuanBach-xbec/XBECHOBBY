package com.example.demo.Controller;

import com.example.demo.Entity.Banner;
import com.example.demo.Repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin(origins = "*") // Kết nối mượt với VueJS
public class BannerController {

    @Autowired
    private BannerRepository bannerRepository;

    // API cho Trang chủ: Lấy danh sách banner đang hoạt động
    @GetMapping("/active")
    public List<Banner> getActiveBanners() {
        return bannerRepository.findByIsActiveTrueOrderBySortOrderAsc();
    }

    // API cho Admin: Lấy toàn bộ danh sách banner
    @GetMapping
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    // API cho Admin: Thêm Banner mới
    @PostMapping
    public Banner createBanner(@RequestBody Banner banner) {
        return bannerRepository.save(banner);
    }

    // API cho Admin: Cập nhật thông tin Banner
    @PutMapping("/{id}")
    public Banner updateBanner(@PathVariable Long id, @RequestBody Banner bannerDetails) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Banner ID: " + id));

        banner.setTitleVi(bannerDetails.getTitleVi());
        banner.setTitleEn(bannerDetails.getTitleEn());
        banner.setDescVi(bannerDetails.getDescVi());
        banner.setDescEn(bannerDetails.getDescEn());
        banner.setBtnTextVi(bannerDetails.getBtnTextVi());
        banner.setBtnTextEn(bannerDetails.getBtnTextEn());
        banner.setLink(bannerDetails.getLink());
        banner.setVideoUrl(bannerDetails.getVideoUrl());
        banner.setThumbUrl(bannerDetails.getThumbUrl());
        banner.setIsActive(bannerDetails.getIsActive());
        banner.setSortOrder(bannerDetails.getSortOrder());

        return bannerRepository.save(banner);
    }

    // API cho Admin: Xóa Banner
    @DeleteMapping("/{id}")
    public void deleteBanner(@PathVariable Long id) {
        bannerRepository.deleteById(id);
    }
}