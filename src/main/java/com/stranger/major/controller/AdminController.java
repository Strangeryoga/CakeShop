package com.stranger.major.controller;

import com.stranger.major.dto.ProductDTO;
import com.stranger.major.model.Category;
import com.stranger.major.model.Product;
import com.stranger.major.service.CategoryService;
import com.stranger.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {

    // Define the upload directory for product images
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    // Autowire CategoryService and ProductService for dependency injection
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    // Admin Home Page
    @GetMapping("/admin")
    public String adminHome() {
        return "adminHome";
    }

    // Category Section
    // Get mapping to retrieve all categories
    @GetMapping("/admin/categories")
    public String getCat(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories";
    }

    // Get mapping to display the category add form
    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    // Post mapping to handle category addition
    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    // Get mapping to delete a category by ID
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCat(@PathVariable int id) {
        Category categoryToDelete = categoryService.findById(id);
        // Check if the category exists
        if (categoryToDelete != null) {
            // Remove products associated with the category
            categoryToDelete.removeProducts();

            // Delete the category
            categoryService.delete(categoryToDelete);

            return "redirect:/admin/categories";
        } else {
            return "redirect:/admin/categories?error=not-found";
        }



    }

    // Get mapping to update a category by ID
    @GetMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if(category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        } else {
            return "404";  // Display a 404 error page if category is not found
        }

    }

    // Product Section
    // Get mapping to retrieve all products
    @GetMapping("/admin/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    // Get mapping to display the product add form
    @GetMapping("/admin/products/add")
    public String productAddGet(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }

    // Post mapping to handle product addition
    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage") MultipartFile file,
                                 @RequestParam("imgName") String imgName) throws IOException {
        // Create a new Product instance and set its properties
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDescription(productDTO.getDescription());
        String imageUUID;
        if (!file.isEmpty()) {
            // If a new image is provided, save it to the upload directory
            imageUUID = file.getOriginalFilename();
            // Stores file's path and it's name
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            // If no new image is provided, use the existing image name
            imageUUID = imgName;
        }

        // Set the image name and add the product to the database
        product.setImageName(imageUUID);
        productService.addProduct(product);


        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProductGet(@PathVariable long id, Model model) {
        Product product = productService.getProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productDTO", productDTO);


        return "productsAdd";
    }

}
