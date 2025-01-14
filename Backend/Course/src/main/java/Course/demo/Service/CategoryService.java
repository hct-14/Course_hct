package Course.demo.Service;

import Course.demo.Dto.Response.CategoryReponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Dto.Request.CategoryReq;
import Course.demo.Entity.Category;
import Course.demo.Mapper.CategoryMaper;
import Course.demo.Repository.CategoryRepository;
import Course.demo.Util.error.IdInvaldException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMaper categoryMaper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMaper categoryMaper) {
        this.categoryRepository = categoryRepository;
        this.categoryMaper = categoryMaper;
    }

    public Category createCategory(CategoryReq categoryReq)throws IdInvaldException{
        Category category = this.categoryMaper.toCategory(categoryReq);
        boolean checkCate = this.categoryRepository.existsByName(categoryReq.getName());
        if(checkCate){
            throw new IdInvaldException("Tên Category đã tồn tại!");
        }
        return this.categoryRepository.save(category);

    }
    public Category updateCategory(CategoryReq categoryReq)throws IdInvaldException{
        Category category = this.categoryMaper.toCategory(categoryReq);
        Optional<Category> optionalCategory = this.categoryRepository.findById(category.getId());
        if(!optionalCategory.isPresent()){
            throw new IdInvaldException("Category không tồn tại");
        }
        boolean checkCate = this.categoryRepository.existsByName(categoryReq.getName());
        if(checkCate){
            throw new IdInvaldException("tên Category đã tồn tại");
        }
        if(optionalCategory.get().getName() != null){
            category.setName(categoryReq.getName());
        }
        return this.categoryRepository.save(category);
    }
    public Category fetchById(int id)throws IdInvaldException{
        Optional<Category> optionalCategory = this.categoryRepository.findById(id);
        if(!optionalCategory.isPresent()){
            throw new IdInvaldException("Category không tồn tại");
        }
        return optionalCategory.get();
    }
    public ResultPaginationDTO fetchAll(Specification<Category> spec, Pageable pageable){
        Page<Category> page = this.categoryRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+ 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        List<CategoryReponse> categoryReponses = page.stream().map(
                item->this.converToCategoryReponse(item))
                .collect(Collectors.toList());

    res.setMeta(meta);
    res.setResult(categoryReponses);
    return res;
    }
    public void deleteCategory(int id)throws IdInvaldException{
        Optional<Category> optionalCategory = this.categoryRepository.findById(id);
        if(!optionalCategory.isPresent()){
            throw new IdInvaldException("Category không tồn tại");
        }
        this.categoryRepository.deleteById(id);
    }


    public CategoryReponse converToCategoryReponse(Category category){
        CategoryReponse categoryReponse = new CategoryReponse();
        categoryReponse.setId(category.getId());
        categoryReponse.setName(category.getName());
        return categoryReponse;
    }
}
