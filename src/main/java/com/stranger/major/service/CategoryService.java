package com.stranger.major.service;

import com.stranger.major.model.Category;
import com.stranger.major.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }
    public void addCategory(Category category) {
        categoryRepo.save(category);
    }
    public void removeCategoryById(int categoryId) {
        // Check if the category with the given ID exists
        if (categoryRepo.existsById(categoryId)) {
            // If it exists, then delete it
            categoryRepo.deleteById(categoryId);
        } else {
            // If it doesn't exist, you might want to handle this situation
            throw new EntityNotFoundException("Category with ID " + categoryId + " not found");
        }
    }
    public Optional<Category> getCategoryById(int id) {
        return categoryRepo.findById(id);
    }

    @Autowired
    public CategoryService( CategoryRepo categoryRepository) {
        this.categoryRepo = categoryRepository;
    }

    // Add the findById method to find a category by its ID
    public Category findById(int categoryId) {
        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);
        return optionalCategory.orElse(null);
    }

    // Add the delete method to delete a category
    public void delete(Category category) {
        categoryRepo.delete(category);
    }

}
