package Course.demo.Mapper;

import Course.demo.Dto.Request.CategoryReq;
import Course.demo.Entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMaper {

    Category toCategory(CategoryReq category);
}
