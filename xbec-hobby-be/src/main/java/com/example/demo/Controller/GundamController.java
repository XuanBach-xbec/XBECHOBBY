package com.example.demo.Controller;

import com.example.demo.Entity.GundamKit;
import com.example.demo.Repository.GundamRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/gundams")
@CrossOrigin(origins = "http://localhost:5173")
public class GundamController {

    @Autowired
    private GundamRepository gundamRepository;

    // Lấy toàn bộ danh sách Gundam
    @GetMapping
    public List<GundamKit> getAllGundams() {
        return gundamRepository.findAll();
    }

    // Lấy thông tin 1 Gundam theo ID
    @GetMapping("/{id}")
    public GundamKit getGundamById(@PathVariable Integer id) {
        return gundamRepository.findById(id).orElse(null);
    }
}