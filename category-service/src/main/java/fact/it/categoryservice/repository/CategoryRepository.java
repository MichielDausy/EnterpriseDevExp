package fact.it.categoryservice.repository;

import fact.it.categoryservice.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findByName(String name); //gets a category using it's name i.e. "any%"

    List<Category> findAllByNameNotNull(); //gets all categories
}
