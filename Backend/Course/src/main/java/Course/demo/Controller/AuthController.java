package Course.demo.Controller;

import Course.demo.Dto.Response.CreateUserReponse;
import Course.demo.Dto.Response.ResLoginDTO;
import Course.demo.Dto.Request.ReqLoginDTO;
import Course.demo.Dto.Request.UserReq;
import Course.demo.Entity.User;
import Course.demo.Service.UserService;
import Course.demo.Util.SecurityUtil;
import Course.demo.Util.annotation.ApiMessage;
import Course.demo.Util.error.IdInvaldException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${hct14.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @Autowired
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          SecurityUtil securityUtil,
                          UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = createLoginResponse(authentication.getName());

        String accessToken = securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(accessToken);

        String refreshToken = securityUtil.createRefreshToken(loginDto.getUsername(), res);
        userService.updateUserToken(refreshToken, loginDto.getUsername());

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        User currentUserDB = userService.handleGetUserByUsername(email);
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setRole(currentUserDB.getRole());

            userGetAccount.setUser(userLogin);
        }

        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refreshToken) throws IdInvaldException {

        if (refreshToken.equals("abc")) {
            throw new IdInvaldException("Bạn không có refresh token ở cookie");
        }

        Jwt decodedToken = securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        User currentUser = userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new IdInvaldException("Refresh Token không hợp lệ");
        }
        ResLoginDTO res = createLoginResponse(email);
        String accessToken = securityUtil.createAccessToken(email, res);
        res.setAccessToken(accessToken);

        String newRefreshToken = securityUtil.createRefreshToken(email, res);
        userService.updateUserToken(newRefreshToken, email);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws IdInvaldException {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        if (email.isEmpty()) {
            throw new IdInvaldException("Access Token không hợp lệ");
        }

        userService.updateUserToken(null, email);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(null);
    }

    @PostMapping("/auth/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<CreateUserReponse> register(@Valid @RequestBody UserReq postManUser) throws IdInvaldException {
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);

        User userSave = this.userService.createUser(postManUser);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToCreateUserReponse(userSave));
//        User userSave = userService.createUser(postManUser);
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.converToUserReponse(userSave));
    }

    private ResLoginDTO createLoginResponse(String email) {
        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getName(),
                    currentUserDB.getRole());
            res.setUser(userLogin);
        }
        return res;
    }
}
