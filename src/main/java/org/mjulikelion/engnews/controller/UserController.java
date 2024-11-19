package org.mjulikelion.engnews.controller;

import lombok.AllArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.user.MypageDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseDto<MypageDto>> getMypage(@AuthenticatedUser User user) {
        MypageDto mypageDto = userService.getMypage(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "마이페이지 조회 완료", mypageDto), HttpStatus.OK);
    }
}
