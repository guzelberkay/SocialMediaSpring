package com.berkay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostListResponseDto {
    Long postId;
    Long userId;
    String userName;
    String avatar;
    String photo;
    String comment;
    Long likeCount;
    Long commentCount;
    Long sharedDate;
    List<CommentResponseDto> commentList;
}
