package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.demo.service.UserService;
import com.example.demo.dto.UpdateDTO;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    // 注册接口
    @PostMapping("/register")
    public Result<String> register(
            @RequestBody  @Valid RegisterDTO registerDTO) {

        return Result.success(
                userService.register(registerDTO)
        );
    }
    // 登录接口
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        String token=userService.login(loginDTO);
     return Result.success(token);
}
        // 查询所有用户
        @GetMapping("/list")
        public Result<List<UserVO>> list() {
            return  Result.success(userService.list());
        }
    // 修改用户信息
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Valid UpdateDTO updateDTO) {

        String result = userService.update(updateDTO);

        if (result.equals("修改成功")) {

            return Result.success(result);

        }


        return Result.error(result);
    }

}

