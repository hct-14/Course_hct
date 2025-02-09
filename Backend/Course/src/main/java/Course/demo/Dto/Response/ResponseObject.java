package Course.demo.Dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ResponseObject<T> extends ResponseEntity<ResponseObject.Payload<T>> {
    public ResponseObject(HttpStatus status, String message, T data) {
        super(new Payload<>(status.value(), message, data), status);
    }

    @Getter
    @Setter
    @Builder
    public static class Payload<T> {
        private int code;
        private String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private T data;

        public Payload(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }
}
