package com.BackEnd.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.BackEnd.dto.AddReviewRequest;
import com.BackEnd.dto.ReviewDTO;
import com.BackEnd.model.Product;
import com.BackEnd.model.Review;
import com.BackEnd.model.User;
import com.BackEnd.repository.ProductRepository;
import com.BackEnd.repository.ReviewRepository;
import com.BackEnd.repository.UserRepository;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public ReviewService(ReviewRepository rr,
            ProductRepository pr,
            UserRepository ur) {
        this.reviewRepo = rr;
        this.productRepo = pr;
        this.userRepo = ur;
    }

    public ReviewDTO addReview(AddReviewRequest req) {
        Product p = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User u = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review r = new Review();
        r.setProduct(p);
        r.setUser(u);
        r.setRating(req.getRating());
        r.setComment(req.getComment());
        r.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepo.save(r);
        return toDto(saved);
    }

    public List<ReviewDTO> getByProduct(Long productId) {
        return reviewRepo.findAllByProductProductIdOrderByCreatedAtDesc(productId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    private ReviewDTO toDto(Review r) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(r.getReviewId());
        dto.setComment(r.getComment());
        dto.setRating(r.getRating());
        dto.setCreatedAt(r.getCreatedAt()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        dto.setProductId(r.getProduct().getProductId());
        dto.setUserId(r.getUser().getUserId());
        dto.setUserName(r.getUser().getFullName());
        dto.setUserAvatarUrl(r.getUser().getAvatarUrl());
        return dto;
    }

}
