package Course.demo.Mapper;

import Course.demo.Dto.Request.CreateChapterReq;
import Course.demo.Dto.Request.UpdateChapterReq;
import Course.demo.Entity.Chapter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ChapterMapper {

    Chapter toChapter(CreateChapterReq chapter);
    Chapter toChapterUpdate(UpdateChapterReq chapter);

}
