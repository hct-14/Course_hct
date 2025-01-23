package Course.demo.Service;

import Course.demo.Dto.Request.ProveCreateReq;
import Course.demo.Dto.Request.ProveUpdateReq;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Dto.Response.PermissionReponse;
import Course.demo.Dto.Response.ProveResponse;
import Course.demo.Entity.Prove;
import Course.demo.Entity.User;
import Course.demo.Mapper.ProveMapper;
import Course.demo.Repository.ProveRepository;
import Course.demo.Repository.UserRepository;
import Course.demo.Util.SecurityUtil;
import Course.demo.Util.error.IdInvaldException;
import org.mapstruct.ap.internal.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ProveService {
    private ProveRepository proveRepository;
    private ProveMapper proveMapper;
    private UserRepository userRepository;
public ProveService(ProveRepository proveRepository, ProveMapper proveMapper, UserRepository userRepository) {
        this.proveRepository = proveRepository;
        this.proveMapper = proveMapper;
        this.userRepository = userRepository;
    }

    public Prove createProve(ProveCreateReq proveReq)throws IdInvaldException {
        Prove prove = this.proveMapper.toProve(proveReq);
        String currentUserEmail = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User is not logged in"));

        // Tìm người dùng hiện tại bằng email
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + currentUserEmail);
        }

        // Gắn người dùng hiện tại vào Prove
        prove.setUser(currentUser);
        return this.proveRepository.save(prove);
    }

    public Prove updateProve(ProveUpdateReq proveReq) throws IdInvaldException {
    Prove prove = proveMapper.toProveUpdate(proveReq);
    Optional<Prove> proveOptional = proveRepository.findById(prove.getId());
    if (!proveOptional.isPresent()) {
        throw new IdInvaldException("Prove not found");
    }
    Prove proveToUpdate = proveOptional.get();

    if (prove.getNameFacility()!=null) {
        proveToUpdate.setNameFacility(prove.getNameFacility());
    }
    if(prove.getCountry()!=null) {
        proveToUpdate.setCountry(prove.getCountry());
    }
    if (prove.getCity()!=null) {
        proveToUpdate.setCity(prove.getCity());
    }
    if (prove.getExpertise()!=null) {
        proveToUpdate.setExpertise(prove.getExpertise());
    }
    if (prove.getImage()!=null) {
        proveToUpdate.setImage(prove.getImage());
    }
        if (prove.getType()!=null) {
            proveToUpdate.setType(prove.getType());
        }
    if (prove.getType()!=null) {
        proveToUpdate.setType(prove.getType());
    }
    return proveRepository.save(proveToUpdate);
    }

    public Prove getProveById(int id) {
    Optional<Prove> proveOptional = proveRepository.findById(id);
    if (!proveOptional.isPresent()) {
        throw new RuntimeException("Prove not found");
    }
    return proveOptional.get();
    }
    public void deleteProveById(int id) {
    Optional<Prove> proveOptional = proveRepository.findById(id);
    if (!proveOptional.isPresent()) {
        throw new RuntimeException("Prove not found");
    }
    if (proveOptional.get().getUser()!=null){
        proveOptional.get().setUser(null);
    }
    proveRepository.deleteById(id);

    }

    public ResultPaginationDTO fetchAllProves(Specification<Prove> spec, Pageable pageable) {
        Page<Prove> provePage = proveRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+ 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(provePage.getTotalPages());
        meta.setTotal(provePage.getTotalElements());
        rs.setMeta(meta);

        List<ProveResponse> list = provePage.getContent().stream().map(item -> this.convertToProveDTO(item)
        ).collect(Collectors.toList());

        rs.setResult(list);
        return rs;


    }

    public ProveResponse convertToProveDTO(Prove prove) {
    ProveResponse res = new ProveResponse();
    ProveResponse.UserProve userProve = new ProveResponse.UserProve();

    if (prove.getUser() != null) {
        userProve.setId(prove.getUser().getId());
        userProve.setName(prove.getUser().getName());
        userProve.setEmail(prove.getUser().getEmail());
        userProve.setPhone(prove.getUser().getPhone());

    }
    res.setId(prove.getId());
    res.setNameFacility(prove.getNameFacility());
    res.setCountry(prove.getCountry());
    res.setCity(prove.getCity());
    res.setImage(prove.getImage());
    res.setType(prove.getType());
    res.setExpertise(prove.getExpertise());
    res.setUser(userProve);
    return res;

    }

}
