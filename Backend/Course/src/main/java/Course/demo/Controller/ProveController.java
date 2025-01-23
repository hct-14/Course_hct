package Course.demo.Controller;

import Course.demo.Dto.Request.ProveCreateReq;
import Course.demo.Dto.Request.ProveUpdateReq;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Dto.Response.ProveResponse;
import Course.demo.Entity.Prove;
import Course.demo.Service.ProveService;
import Course.demo.Util.annotation.ApiMessage;
import Course.demo.Util.error.IdInvaldException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prove")
public class ProveController {

    private final ProveService proveService;
    public ProveController(ProveService proveService) {
        this.proveService = proveService;
    }

    @PostMapping("/create")
    @ApiMessage("success")
    public ResponseEntity<ProveResponse> create(@Valid @RequestBody ProveCreateReq proveReq)  throws IdInvaldException {
        Prove prove = this.proveService.createProve(proveReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.proveService.convertToProveDTO(prove));
    }

    @PutMapping("/update")
    @ApiMessage("success")
    public ResponseEntity<ProveResponse> update(@Valid @RequestBody ProveUpdateReq proveReq) throws IdInvaldException {
        Prove prove = this.proveService.updateProve(proveReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.proveService.convertToProveDTO(prove));
    }

    @GetMapping("/fetch-by-id/{id}")
    @ApiMessage("success")
    public ResponseEntity<ProveResponse> fetchById(@PathVariable int id) throws IdInvaldException {
        Prove prove = this.proveService.getProveById(id);
        return ResponseEntity.ok(this.proveService.convertToProveDTO(prove));
    }

    @GetMapping ("/fetch-all")
    @ApiMessage("success")

    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Prove> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.proveService.fetchAllProves(spec, pageable));
    }
    @DeleteMapping("delete/{id}")
    @ApiMessage("success")
    public ResponseEntity<String> delete(@PathVariable int id) throws IdInvaldException {
        this.proveService.deleteProveById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Xoá thành công");
    }

}
