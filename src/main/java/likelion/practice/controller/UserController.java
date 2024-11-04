package likelion.practice.controller;

import likelion.practice.dto.UserDTO;
import likelion.practice.entity.User;
import likelion.practice.security.JwtTokenProvider;
import likelion.practice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
   private final UserService userService;
   private final JwtTokenProvider jwtTokenProvider;
   private final AuthenticationManager authenticationManager;

   public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager){
      this.userService = userService;
      this.jwtTokenProvider=jwtTokenProvider;
      this.authenticationManager=authenticationManager;
   }

   //회원가입 API
   @PostMapping("/register")
   public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO){
      User registeredUser= userService.registerUser(userDTO);
      return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
   }

   @GetMapping("/checkId")
   public String checkUserId(@RequestParam UserDTO userId){
      boolean isAvailable= userService.checkUserId(userId);
      if(isAvailable)
         return "사용 가능한 아이디 입니다.";
      else return "이미 존재하는 아이디입니다.";
   }

   //로그인 API
   @PostMapping("/login")
   public ResponseEntity<Map<String, String>> loginUSer(@RequestBody UserDTO userDTO){
      try{
         //사용자 인증 시도
         Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(userDTO.getUserId(),userDTO.getPassword())
         );

         //인증 성공 시 JWT 토큰 생성
         String jwtToken = jwtTokenProvider.createToken(authentication.getName());

         //응답으로 토큰을 Map 형태로 반환
         Map<String, String> response = new HashMap<>();
         response.put("token", jwtToken);

         return ResponseEntity.ok(response);
      } catch (AuthenticationException ex){
         throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
      }
   }

   @GetMapping("/mypage")
   public ResponseEntity<User> getMyPage(Authentication authentication){
      String userId= authentication.getName();
      User user = userService.MyPage(userId);
      return ResponseEntity.ok(user);
   }

   @PutMapping("/mypage/{userId}/edit")
   public ResponseEntity<User> editMyPage(@PathVariable String userId, @RequestBody UserDTO userDTO){
      User user = userService.editMyPage(userId, userDTO);
      return ResponseEntity.ok(user);
   }
}
