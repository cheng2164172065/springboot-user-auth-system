package com.example.demo.service;
import com.example.demo.dto.UpdateDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.vo.UserVO;
import java.util.List;


public interface UserService {
    String register(RegisterDTO registerDTO);
    String login (LoginDTO loginDTO);
    List<UserVO> list();
    String update(UpdateDTO updateDTO);
}
