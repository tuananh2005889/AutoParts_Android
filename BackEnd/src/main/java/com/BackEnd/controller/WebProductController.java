package com.BackEnd.controller;

import com.BackEnd.dto.ProductDTO;
import com.BackEnd.model.Product;
import com.BackEnd.repository.ProductRepository;
import com.BackEnd.service.CloudinaryyService;
import com.BackEnd.service.ProductService;
import com.BackEnd.utils.FIleToBase64;
import com.BackEnd.utils.ImageDownloader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web/product")
public class WebProductController {
    private final ProductService productService;
    private final CloudinaryyService cloudinaryyService;
    private final ProductRepository productRepository;

    public WebProductController(ProductService productService, CloudinaryyService cloudinaryyService,
                                ProductRepository productRepository) {
        this.productService = productService;
        this.cloudinaryyService = cloudinaryyService;
        this.productRepository = productRepository;
    }

    @GetMapping("/all")
    public String getAllProducts(Model model) {
        try {
            model.addAttribute("products", productService.getAllProducts());
            return "product-list";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/detail/{id}")
    public String getProductDetails(
            @PathVariable Long id,
            Model model

    ) throws Exception {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        List<String> base64ImageList = new ArrayList<>();
        if (product != null) {
            for(String url : product.getImages()){
               MultipartFile file = ImageDownloader.urlToMultipartFile(url);
               base64ImageList.add(FIleToBase64.convertToBase64(file));
            }

            model.addAttribute("product", product);
            model.addAttribute("base64s", base64ImageList);
            return "product-detail";
        }
        return "error";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        return "product-add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductDTO productDTO,
                             @RequestParam("images") List<MultipartFile> images,
                             RedirectAttributes redirectAttributes
                             ) {
        try {
            if (images == null || images.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please upload at least one image.");
                return "redirect:/web/product/add";
            }

            List<String> imageUrls = cloudinaryyService.uploadImages(images);

            Product product = new Product(productDTO.getName(), productDTO.getBrand(),
                    productDTO.getCategory(),
                    productDTO.getPrice(),
                    productDTO.getQuantity(), productDTO.getDescription(),
                    productDTO.getCompatibleVehicles(),
                    productDTO.getYearOfManufacture(), productDTO.getSize(),
                    productDTO.getMaterial(),
                    productDTO.getWeight(),
                    productDTO.getDiscount(), productDTO.getWarranty() );

            product.setImages(imageUrls);
            productService.saveProduct(product);

            //redirectAttribute giup truyen du lieu tam thoi qua tung request
            //du lieu chi duoc du lai mot lan duy nhat sau khi redirect, sau
            // do bi xoa di
            redirectAttributes.addFlashAttribute("successMessage", "Added product successfully");
            return "redirect:/web/product/all";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                    "Error when add product: " + e.getMessage());
            return "redirect:/web/product/add";
        }
    }



    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product);
            return "product-edit";
        }
        return "error";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product) {
        try {
            product.setProductId(id);
            productService.saveProduct(product);
            return "redirect:/web/product/all";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return "redirect:/web/product/all";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }





}
