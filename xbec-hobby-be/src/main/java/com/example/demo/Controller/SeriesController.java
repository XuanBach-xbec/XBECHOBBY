package com.example.demo.Controller;

import com.example.demo.Entity.Series;
import com.example.demo.Repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@CrossOrigin(origins = "http://localhost:5173")
public class SeriesController {

    @Autowired
    private SeriesRepository seriesRepository;

    @GetMapping
    public List<Series> getAllSeries() {
        return seriesRepository.findAll();
    }
}