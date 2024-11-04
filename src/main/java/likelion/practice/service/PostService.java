package likelion.practice.service;

import jakarta.servlet.http.HttpServletRequest;
import likelion.practice.dto.PostDTO;
import likelion.practice.entity.Post;
import likelion.practice.entity.User;
import likelion.practice.repository.PostRepository;
import likelion.practice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {
   private final PostRepository postRepository;
   private final UserRepository userRepository;
   private final UserDetailsService userDetailService;
   @Autowired
   public PostService(PostRepository postRepository, UserRepository userRepository, UserDetailsService userDetailService){
      this.postRepository=postRepository;
      this.userRepository = userRepository;
      this.userDetailService=userDetailService;
   }

   //게시글 작성
   public Post creatPost(PostDTO postDTO,String userId){
      Post post = new Post();
      post.setTitle(postDTO.getTitle());
      post.setContent(postDTO.getContent());
      post.setCreatedAt(LocalDateTime.now());
      User author = userRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));;

      post.setAuthor(author);
      return postRepository.save(post);
   }
   //게시글 검색
   //게시글 편집
   //게시글 삭제

}
