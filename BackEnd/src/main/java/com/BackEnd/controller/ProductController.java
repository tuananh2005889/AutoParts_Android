package com.BackEnd.controller;

import com.BackEnd.repository.CartItemRepository;
import com.BackEnd.repository.ProductRepository;
import com.BackEnd.service.ProductService;
import com.BackEnd.dto.ProductRequest;
import com.BackEnd.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app/product")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepo;

    public ProductController(ProductService productService,
            ProductRepository productRepo) {
        this.productService = productService;
        this.productRepo = productRepo;

    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest request) {
        try {
            Product product = request.getProduct();
            List<String> imageUrls = request.getImageUrls();

            // Nếu Product entity của bạn có field `images`, set vào:
            product.setImages(imageUrls);

            productService.saveProduct(product);
            return ResponseEntity.ok("Product added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/imageUrls")
    public ResponseEntity<List<String>> getImageUrls(@RequestParam("productId") Long productId) {
        try {
            List<String> imageUrls = productService.getImageUrls(productId);
            if (imageUrls != null && !imageUrls.isEmpty()) {
                return ResponseEntity.ok(imageUrls);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/imageUrl")
    public ResponseEntity<String> getImageUrl(@RequestParam("productId") Long productId) {
        try {
            String imageUrl = productService.getImageUrl(productId);
            return ResponseEntity.ok(imageUrl);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(p -> ResponseEntity.ok(p)) // or .map(Response::ok) _method reference:lambda expression rut
                                                      // gon
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            boolean updated = productService.updateProduct(id, productDetails);
            if (updated) {
                return ResponseEntity.ok("Product updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {

        boolean deleted = productService.deleteProduct(id);

        if (deleted) {
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
