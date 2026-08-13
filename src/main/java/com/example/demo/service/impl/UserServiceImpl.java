package com.example.demo.service.impl;

import com.example.demo.constant.RedisConstant;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
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
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
//注册
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
        redisTemplate.delete(RedisConstant.USER_LIST);
        return "注册成功";
    }

//登录
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
        //查询redis
        String key = RedisConstant.USER_LIST;

        List<UserVO> cacheUsers =
                (List<UserVO>) redisTemplate.opsForValue().get(key);

        // 2. Redis有数据，直接返回
        if (cacheUsers != null) {
            System.out.println("Redis查询");
            return cacheUsers;
        }

        // 3. Redis没有，查询数据库
        System.out.println("MySQL查询");
        List<User> users = userRepository.findAll();

        //4.user转换userVO
        List<UserVO> userVOList = users.stream()
                .map(user -> {

                    UserVO vo = new UserVO();

                    vo.setId(user.getId());

                    vo.setUsername(user.getUsername());

                    vo.setCreateTime(user.getCreateTime());

                    return vo;

                })
                .toList();

        //5.放入Redis，设置过期时间
        redisTemplate.opsForValue()
                .set(
                        key,
                        userVOList,
                        30,
                        TimeUnit.MINUTES
                );


        return userVOList;
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
        //删除reids缓存
        redisTemplate.delete(RedisConstant.USER_LIST);
        return "修改成功";
    }
}
