package Course.demo.Dto.PaymentRes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class PaymentResponse {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class VNPayResponse {
        private String code;
        private String message;
        private String paymentUrl;
    }
}
