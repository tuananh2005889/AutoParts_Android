
package com.BackEnd.Service;

import com.BackEnd.Repository.ProductRepository;
import com.BackEnd.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public boolean updateProduct(String id, Product productDetails) {
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

    public boolean deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}