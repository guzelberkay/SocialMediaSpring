package com.berkay.controller;
import com.berkay.dto.request.AddCommentRequestDto;
import com.berkay.dto.request.GetAllCommentByPostIdRequestDto;
import com.berkay.dto.response.CommentResponseDto;
import com.berkay.dto.response.ResponseDto;
import com.berkay.service.CommentService;
import com.berkay.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST,RequestMethod.GET})
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    @PostMapping("/add-comment")
    public ResponseEntity<ResponseDto<Boolean>> addComment(@RequestBody AddCommentRequestDto dto){
        commentService.addComment(dto);
        postService.addComment(dto.getPostId());
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().data(true).code(200).message("ok").build());
    }

    @PostMapping("/get-all-comments-by-postid")
    public ResponseEntity<ResponseDto<List<CommentResponseDto>>> getAllCommentsByPosyId(@RequestBody GetAllCommentByPostIdRequestDto dto){
        return ResponseEntity.ok(
                ResponseDto.<List<CommentResponseDto>>builder()
                        .message("tüm yorumlar getirildi")
                        .code(200)
                        .data(commentService.getAllCommentsByPostId(dto))
                        .build()
        );
    }
}
