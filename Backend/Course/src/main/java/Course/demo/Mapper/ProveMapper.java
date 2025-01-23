package Course.demo.Mapper;

import Course.demo.Dto.Request.ProveCreateReq;
import Course.demo.Dto.Request.ProveUpdateReq;
import Course.demo.Entity.Prove;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProveMapper {

    @Mapping(source = "country", target = "country")
    @Mapping(source = "nameFacility", target = "nameFacility")
    @Mapping(source = "expertise", target = "expertise")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "type", target = "type")
    Prove toProve(ProveCreateReq proveReq);
    
    Prove toProveUpdate(ProveUpdateReq proveReq);
}
