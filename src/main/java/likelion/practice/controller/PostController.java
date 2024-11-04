package likelion.practice.controller;

import likelion.practice.dto.PostDTO;
import likelion.practice.entity.Post;
import likelion.practice.security.JwtTokenProvider;
import likelion.practice.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostController {
   private final PostService postService;
   private final JwtTokenProvider jwtTokenProvider;
   private final AuthenticationManager authenticationManager;

   public PostController(PostService postService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager){
      this.postService=postService;
      this.jwtTokenProvider=jwtTokenProvider;
      this.authenticationManager=authenticationManager;
   }

   //게시글 작성
   @PostMapping
   public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO,  @AuthenticationPrincipal UserDetails userDetails){
      String userId= userDetails.getUsername();
      Post post= postService.creatPost(postDTO,userId);

      return ResponseEntity.status(HttpStatus.CREATED).body(post);
   }

   //게시글 검색
   //게시글 편집
   //게시글 삭제
}
