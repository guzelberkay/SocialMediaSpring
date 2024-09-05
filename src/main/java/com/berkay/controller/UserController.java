package com.berkay.controller;

import com.berkay.config.JwtManager;
import com.berkay.dto.request.FindAllByUsernameRequestDto;
import com.berkay.dto.request.UserLoginRequestDto;
import com.berkay.dto.request.UserProfileEditRequestDto;
import com.berkay.dto.request.UserSaveRequestDto;
import com.berkay.dto.response.ResponseDto;
import com.berkay.dto.response.SearchUserResponseDto;
import com.berkay.entity.User;
import com.berkay.exception.AuthException;
import com.berkay.exception.ErrorType;
import com.berkay.service.UserService;
import com.berkay.views.VwSearchUser;
import com.berkay.views.VwUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET})
public class UserController {
    private final UserService userService;
    private final JwtManager jwtManager;
    @PostMapping("/register")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<Boolean>> save(@RequestBody UserSaveRequestDto dto){
        userService.save(dto);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                        .code(200)
                        .message("Kullanıcı başarı ile kayıt edildi.")
                        .data(true)
                .build());
    }

    @PostMapping("/login")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<String>> login(@RequestBody UserLoginRequestDto dto){
        Optional<User> user = userService.login(dto);
        if(user.isEmpty())
            throw new AuthException(ErrorType.BAD_REQUEST_USERNAME_OR_PASSWORD_ERROR);
        String token = jwtManager.createToken(user.get().getId());
        return ResponseEntity.ok(ResponseDto.<String>builder()
                        .code(200)
                        .message("Başarılı şekilde giriş yapıldı")
                        .data(token)
                .build());
    }

    @GetMapping("/search")
    @CrossOrigin("*")
    public ResponseEntity<List<SearchUserResponseDto>> getUserList(String userName){
        return ResponseEntity.ok(userService.search(userName));
    }

    @GetMapping("/get-profile")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<VwUserProfile>> getProfile(String token){
        return ResponseEntity.ok(ResponseDto.<VwUserProfile>builder()
                        .message("profile bilgileri")
                        .code(200)
                        .data(userService.getProfileByToken(token))
                .build());
    }

    @PostMapping("/edit-profile")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<Boolean>> editProfile(@RequestBody UserProfileEditRequestDto dto){
        userService.editProfile(dto);
        return  ResponseEntity.ok(ResponseDto.<Boolean>builder()
                .message("ok")
                .code(200)
                .data(true)
                .build());
    }

    @PostMapping("/search-user")
    public ResponseEntity<ResponseDto<List<VwSearchUser>>> findAllByUserName(@RequestBody FindAllByUsernameRequestDto dto){
        return  ResponseEntity.ok(
                ResponseDto.<List<VwSearchUser>>builder()
                        .code(200)
                        .message("kullanıcılar getirildi.")
                        .data(userService.getAllByUserName(dto))
                        .build()
        );
    }

}
