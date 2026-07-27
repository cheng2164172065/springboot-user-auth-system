package com.example.demo.service.impl;

import com.example.demo.util.JwtUtil;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.UpdateDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BCryptPasswordEncoder encoder;
//登录
    @Override
    public String register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(registerDTO.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        User user=new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(encoder.encode(registerDTO.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        userRepository.save(user);
        return "注册成功";
    }

//注册
    @Override
    public String login(LoginDTO loginDTO) {
        User dbUser = userRepository.findByUsername(loginDTO.getUsername());
        if (dbUser == null) {
           throw new BusinessException("用户不存在");
        }
        if (!encoder.matches(loginDTO.getPassword(), dbUser.getPassword())) {
            throw new BusinessException("密码错误");
        }
        String token = jwtUtil.generateToken(dbUser.getUsername());
        return token;
    }
    //查询
    @Override
    public List<UserVO> list() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {

                    UserVO vo = new UserVO();

                    vo.setId(user.getId());

                    vo.setUsername(user.getUsername());

                    vo.setCreateTime(user.getCreateTime());

                    return vo;

                })
                .toList();
        }
    //修改
    @Override
    public String update( UpdateDTO updateDTO) {
        User existing = userRepository.findByUsername(updateDTO.getUsername());
        if (existing == null) {
            throw new BusinessException("用户不存在");
        }
        existing.setPassword(encoder.encode(updateDTO.getPassword()));
        userRepository.save(existing);
        return "修改成功";
    }
}
