
package com.BackEnd.service;

import com.BackEnd.repository.CartItemRepository;
import com.BackEnd.repository.ProductRepository;

import jakarta.transaction.Transactional;

import com.BackEnd.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private final CartItemRepository cartItemRepo;

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Autowired
    public ProductService(ProductRepository productRepo, CartItemRepository cartItemRepo) {
        this.productRepository = productRepo;
        this.cartItemRepo = cartItemRepo;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<String> getImageUrls(Long productId) {
        List<String> imageUrls;
        imageUrls = productRepository.findImageByProductId(productId)
                .orElseThrow(() -> new RuntimeException("No image urls found " +
                        "for product id " + productId));
        return imageUrls;

    }

    public String getImageUrl(Long productId) {
        List<String> imageUrls = productRepository.findImageByProductId(productId)
                .orElseThrow(() -> new RuntimeException("No image urls found for product id " + productId));

        if (imageUrls.isEmpty()) {
            throw new RuntimeException("No image urls found for product id " + productId);
        }
        return imageUrls.get(0);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByProductId(id);
    }

    public boolean updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDetails.getName());
            product.setBrand(productDetails.getBrand());
            product.setCategory(productDetails.getCategory());
            product.setDescription(productDetails.getDescription());
            product.setCompatibleVehicles(productDetails.getCompatibleVehicles());
            product.setYearOfManufacture(productDetails.getYearOfManufacture());
            product.setSize(productDetails.getSize());
            product.setMaterial(productDetails.getMaterial());
            product.setWeight(productDetails.getWeight());
            product.setImages(productDetails.getImages());
            product.setDiscount(productDetails.getDiscount());
            product.setWarranty(productDetails.getWarranty());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            productRepository.save(product);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        cartItemRepo.deleteByProductId(id);
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

}