package com.BackEnd.controller;

import com.BackEnd.service.ProductService;
import com.BackEnd.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app/product")
//@CrossOrigin(origins = "*")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(
            @RequestBody Product product,
            @RequestParam("imageUrls") List<String> imageUrls
            ) {
        try {
            System.out.println("Received product: " + product.toString());
            productService.saveProduct(product);
            return ResponseEntity.ok("Product added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
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
        return product.map(p -> ResponseEntity.ok(p)) //or .map(Response::ok) _method reference:lambda expression rut gon
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody Product productDetails) {
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
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
//        boolean deleted = productService.deleteProduct(id);
//        if (deleted) {
//            return ResponseEntity.ok("Product deleted successfully");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
