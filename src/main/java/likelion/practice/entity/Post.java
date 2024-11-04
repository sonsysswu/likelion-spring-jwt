package likelion.practice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long postId;

   @Column(name="title",nullable = false)
   private String title;

   @Lob
   @Column(name="content")
   private String content;

   @ManyToOne
   @JoinColumn(name="user_id")
   private User author;

   @CreationTimestamp
   @Column(name="created_at",nullable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name="updated_at")
   private LocalDateTime updatedAt;

}
