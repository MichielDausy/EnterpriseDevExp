package fact.it.categoryservice.controller;

import fact.it.categoryservice.dto.CategoryRequest;
import fact.it.categoryservice.dto.CategoryResponse;
import fact.it.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategoryByName(@RequestParam String name) {
        return categoryService.getCategoryByName(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getAllCategories(){
        return categoryService.getAllCategories();
    }
} 
