package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;


    // 注册接口
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "用户名已存在";
        }
        user.setCreateTime(LocalDateTime.now());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "注册成功";
    }

    // 登录接口
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser == null) {
            return "用户不存在";
        }
        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return "密码错误";
        }
        String token = jwtUtil.generateToken(dbUser.getUsername());
        return token;
    }
        // 查询所有用户
        @GetMapping("/list")
        public List<User> list() {
            return userRepository.findAll();
        }
    // 修改用户信息
    @PutMapping("/update")
    public String update(@RequestBody User user) {
        User existing = userRepository.findByUsername(user.getUsername());
        if (existing == null) {
            return "用户不存在";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        existing.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(existing);
        return "修改成功";
    }

}

