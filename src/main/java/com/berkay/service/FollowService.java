package com.berkay.service;

import com.berkay.config.JwtManager;
import com.berkay.dto.request.AddFollowRequestDto;
import com.berkay.entity.Follow;
import com.berkay.exception.AuthException;
import com.berkay.exception.ErrorType;
import com.berkay.repository.FollowRepository;
import com.berkay.utility.FollowState;
import com.berkay.views.VwSearchUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository repository;
    private final JwtManager jwtManager;
    /**
     * Takip etme isteği
     * 1-> token takip etmek isteyen kişiyi belirtir ve içerisinde userId vardır
     * 2-> followId takip edilmek istenilen kişiyi belirtir.
     * ------
     * kullanıcı daha önce bu kişiyi takip etmek için istekte bulunmuş mu? takip isteği
     * hangi aşamada.
     * @param dto
     */
    public void addFollow(AddFollowRequestDto dto) {
        Long userId = getUserId(dto);
        Optional<Follow> optionalFollow = repository.findOptionalByUserIdAndFollowId(userId,dto.followId());
        if (optionalFollow.isEmpty()){
            add(userId,dto.followId());
        }else {
            switch (optionalFollow.get().getState()){
                case RED, TAKIP_ETMIYOR:
                    reFollowUser(optionalFollow);
            }
        }
    }
    public void unFollow(AddFollowRequestDto dto){
        Long userId = getUserId(dto);
        Optional<Follow> optionalFollow = repository.findOptionalByUserIdAndFollowId(userId,dto.followId());
        if (optionalFollow.isPresent()){
            Follow follow = optionalFollow.get();
            follow.setState(FollowState.TAKIP_ETMIYOR);
            repository.save(follow);
        }
    }

    private Long getUserId(AddFollowRequestDto dto) {
        Optional<Long> userId = jwtManager.getAuthId(dto.token());
        if(userId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        return userId.get();
    }
    private void reFollowUser(Optional<Follow> optionalFollow) {
        Follow reFollow = optionalFollow.get();
        reFollow.setState(FollowState.BEKLEMEDE);
        repository.save(reFollow);
        return;
    }
    private void add(Long userId, Long followId){

        repository.save(Follow.builder()
                .followId(followId)
                .userId(userId)
                .state(FollowState.BEKLEMEDE)
                .build());
    }

    /**
     * Bir kullanıcının zaten takipte olduğu ya da takip isteği gönderdiği
     * ya da engellendiği tüm follow kayıtlarına ait userId bilgilerini döner.
     * @param userId
     * @return
     */
    public List<Long> findAllByUserId(Long userId) {
        List<Follow> followList = repository
                .findAllByUserIdAndStateIn(userId, List.of(FollowState.TAKIP_EDIYOR,FollowState.ENGELLE,FollowState.BEKLEMEDE));
        return followList.stream().map(Follow::getFollowId).toList();
    }

    public List<Long> getAllFollowing(String token) {
        Optional<Long> userIdOptional = jwtManager.getAuthId(token);
        if(userIdOptional.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        List<Follow> followList = repository.findAllByUserIdAndStateIn(userIdOptional.get(), List.of(FollowState.TAKIP_EDIYOR,FollowState.BEKLEMEDE));
        return followList.stream().map(Follow::getFollowId).toList();
    }
}