package com.BackEnd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BackEnd.dto.AddReviewRequest;
import com.BackEnd.dto.ReviewDTO;
import com.BackEnd.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    private final ReviewService srv;

    public ReviewController(ReviewService srv) {
        this.srv = srv;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody AddReviewRequest req) {
        ReviewDTO out = srv.addReview(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> listByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(srv.getByProduct(productId));
    }
}
