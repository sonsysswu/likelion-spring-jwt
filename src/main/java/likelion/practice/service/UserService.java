package likelion.practice.service;

import likelion.practice.dto.UserDTO;
import likelion.practice.entity.User;
import likelion.practice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserId())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
    //회원가입 기능
    public User registerUser(UserDTO userDTO){
        if(userRepository.existsByUserId(userDTO.getUserId())){ //아이디 중복체크 기능
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID already exists.");
        }
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        user.setProfileImage(userDTO.getProfileImage());

        return userRepository.save(user);
    }
    //아이디 중복 체크
    public boolean checkUserId(UserDTO userDTO){
        return !userRepository.existsByUserId(userDTO.getUserId());
    }
    //로그인 기능
    public User loginUser(String userId, String password){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with ID: "+userId));
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password.");
        }
        return user;
    }

    //마이페이지 조회 기능
    public User MyPage(String userId){
        return userRepository.findByUserId(userId)
              .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
    }

    //마이페이지 수정 기능
    public User editMyPage(String userId, UserDTO userDTO){
        // 아이디는 수정 못하게

        User user = userRepository.findByUserId(userId)
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        user.setProfileImage(userDTO.getProfileImage());

        return userRepository.save(user);
    }
}
