package fact.it.categoryservice.service;

import fact.it.categoryservice.dto.CategoryResponse;
import fact.it.categoryservice.model.Category;
import fact.it.categoryservice.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @PostConstruct
    public void loadData() {
        if (categoryRepository.count() == 0) {
            Category category0 = new Category();
            category0.setName("Any%");
            category0.setDescription("Completing the game as fast as possible using any means necessary, often exploiting glitches or shortcuts. The focus is on finishing the game with the fewest restrictions.");

            Category category1 = new Category();
            category1.setName("100%");
            category1.setDescription("Completing the game with the goal of achieving 100% completion, which involves collecting all items, completing all side quests, or achieving maximum progress within the game.");

            categoryRepository.saveAll(Arrays.asList(category0, category1));

        }
    }

    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        return mapToCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByNameNotNull();
        return categories.stream().map(this::mapToCategoryResponse).toList();
    }
}
