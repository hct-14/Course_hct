package Course.demo.Service;

import Course.demo.Config.VNPAYConfig;
import Course.demo.Dto.PaymentRes.PaymentResponse;
import Course.demo.Entity.Course;
import Course.demo.Entity.Payment;
import Course.demo.Entity.User;
import Course.demo.Entity.UserCourse;
import Course.demo.Repository.CourseRepository;
import Course.demo.Repository.PaymentRepository;
import Course.demo.Repository.UserCourseRepository;
import Course.demo.Repository.UserRepository;
import Course.demo.Util.SecurityUtil;
import Course.demo.Util.VNPAY.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPAYConfig vnPayConfig;
    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;


    // Tạo URL thanh toán
    public PaymentResponse.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        String courseIdStr = request.getParameter("courseId");
        if (courseIdStr == null || courseIdStr.isEmpty()) {
            throw new IllegalArgumentException("Course ID is missing");
        }

        int courseId = Integer.parseInt(courseIdStr);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }

        Course course = optionalCourse.get();
        long amount = (long) (course.getPrice() * 100); // Chuyển giá thành tiền cent

        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }

        // Lấy thông tin người dùng hiện tại
        String currentUserEmail = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User is not logged in"));

        // Tìm người dùng theo email
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + currentUserEmail);
        }

        // Thêm thông tin người dùng vào URL (ví dụ: email)
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Thêm courseId và email người dùng vào vnp_ReturnUrl
        String returnUrl = "http://localhost:8080/api/payment/vn-pay-callback?courseId=" + courseId + "&email=" + currentUserEmail;
        vnpParamsMap.put("vnp_ReturnUrl", returnUrl);

        // Xây dựng URL thanh toán
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return PaymentResponse.VNPayResponse.builder()
                .code("00")
                .message("Success")
                .paymentUrl(paymentUrl)
                .build();
    }

    // Xử lý callback từ VNPay
    public PaymentResponse.VNPayResponse handleVnPayCallback(HttpServletRequest request) {
        // Kiểm tra mã phản hồi từ VNPay
        String responseCode = request.getParameter("vnp_ResponseCode");
        if (!"00".equals(responseCode)) {
            return new PaymentResponse.VNPayResponse("01", "Failed", null);
        }

        // Lấy thông tin khóa học từ callback
        String courseIdStr = request.getParameter("courseId");
        if (courseIdStr == null || courseIdStr.isEmpty()) {
            throw new IllegalArgumentException("Course ID is missing in callback");
        }

        int courseId = Integer.parseInt(courseIdStr);

        // Lấy email của người dùng
        String currentUserEmail = request.getParameter("email");
        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            throw new RuntimeException("User email is missing");
        }

        // Tìm kiếm người dùng theo email
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + currentUserEmail);
        }

        // Kiểm tra xem khóa học có tồn tại không
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }

        Course course = optionalCourse.get();
        long amount = (long) (course.getPrice() * 100); // Tiền thanh toán, chuyển sang cent

        // Lưu thông tin thanh toán vào database
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setBankCode(request.getParameter("vnp_BankCode"));
        payment.setUser(currentUser);
        payment.setCourse(course);
        payment.setDate(LocalDate.now());
        paymentRepository.save(payment);

        // Cập nhật thu nhập cho người dùng
        updateUserIncome(currentUser, amount);

        // Thêm khóa học vào danh sách khóa học của người dùng
        addCourseToUser(currentUser, course);

        return new PaymentResponse.VNPayResponse("00", "Success", "Payment has been saved successfully");
    }

    // Cập nhật thu nhập cho người dùng
    private void updateUserIncome(User user, long amount) {
        user.setIncome(user.getIncome() + amount/100);
        userRepository.save(user);  // Lưu lại thông tin người dùng
    }

    // Thêm khóa học vào người dùng
    private void addCourseToUser(User user, Course course) {
        // Tạo mới đối tượng UserCourse và lưu vào cơ sở dữ liệu
        UserCourse userCourse = new UserCourse();
        userCourse.setUser(user);    // Gắn user
        userCourse.setCourse(course); // Gắn course
        userCourse.setRating(0);     // Giá trị mặc định cho rating
        userCourse.setTeacher(false); // Giá trị mặc định cho isTeacher
        userCourseRepository.save(userCourse); // Lưu vào cơ sở dữ liệu
    }
}
