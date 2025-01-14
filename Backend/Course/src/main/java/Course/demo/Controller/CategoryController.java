package Course.demo.Controller;

import Course.demo.Dto.Response.CategoryReponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Dto.Request.CategoryReq;
import Course.demo.Entity.Category;
import Course.demo.Service.CategoryService;
import Course.demo.Util.error.IdInvaldException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryReponse> createCategory(@RequestBody CategoryReq categoryReq)throws IdInvaldException {
        Category category = this.categoryService.createCategory(categoryReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.converToCategoryReponse(category));
    }

    @PutMapping("/update")
    public ResponseEntity<CategoryReponse> updateCategory(@RequestBody CategoryReq categoryReq)throws IdInvaldException {
        Category category = this.categoryService.updateCategory(categoryReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.converToCategoryReponse(category));
    }
    @GetMapping("/fetch-by-id/{id}")
    public ResponseEntity<CategoryReponse> fetchCategoryById(@PathVariable int id)throws IdInvaldException {
        Category category = this.categoryService.fetchById(id);
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.converToCategoryReponse(category));
    }
    @GetMapping("/fetch-all")
    public ResponseEntity<ResultPaginationDTO> fetchAllCategory(@Filter Specification<Category> spec, Pageable pageable)throws IdInvaldException {
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.fetchAll(spec, pageable));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id)throws IdInvaldException {
        this.categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body("Xoá thành công");
    }

}
