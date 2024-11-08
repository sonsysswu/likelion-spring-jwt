package likelion.practice.controller;

import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import likelion.practice.dto.PostDTO;
import likelion.practice.entity.Post;
import likelion.practice.security.JwtTokenProvider;
import likelion.practice.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {
   private final PostService postService;
   private final JwtTokenProvider jwtTokenProvider;
   private final AuthenticationManager authenticationManager;
   private static final Logger logger = LoggerFactory.getLogger(PostController.class);
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
   @GetMapping("/search")
   public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword){
      List<Post> posts= postService.searchPosts(keyword);
      return ResponseEntity.ok(posts);
   }

   //게시글 편집
   @PutMapping("/{postId}")
   public ResponseEntity<Post> editPost(@PathVariable Long postId, @RequestBody PostDTO postDTO){
      Post post = postService.editPost(postId,postDTO);
      return ResponseEntity.ok(post);
   }

   //게시글 삭제
   @DeleteMapping("/{postId}")
   public ResponseEntity<Void> deletePost(@PathVariable Long postId, HttpServletRequest request){
//      logger.info("Received DELETE request for postId: {}, Headers: {}", postId, Collections.list(request.getHeaderNames()).stream()
//            .map(header -> header + ": " + request.getHeader(header))
//            .collect(Collectors.joining(", ")));

      String token = jwtTokenProvider.resolveToken(request);
      if(token == null || !jwtTokenProvider.validateToken(token)){
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      String userId= jwtTokenProvider.getUserId(token);
      postService.deletePost(postId, userId);
      return ResponseEntity.noContent().build();
   }


}
