package likelion.practice.dto;

import lombok.Data;

@Data
public class PostDTO {
   private String title;
   private String content;
   private String createdAt;
   private String updatedAt;
}
