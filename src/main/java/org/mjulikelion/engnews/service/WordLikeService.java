package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.dto.request.wordLike.CreateWordLikeDto;
import org.mjulikelion.engnews.dto.response.wordLike.WordLikeListResponseData;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.entity.WordLike;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.UserRepository;
import org.mjulikelion.engnews.repository.WordLikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WordLikeService {
    private final UserRepository userRepository;
    private final WordLikeRepository wordLikeRepository;

    // 단어 찜하기
    public void createWordLike(User user, CreateWordLikeDto createWordLikeDto){
        WordLike newWordLike = WordLike.builder()
                .word(createWordLikeDto.getWord())
                .translate(createWordLikeDto.getTranslate())
                .user(user)
                .build();
        wordLikeRepository.save(newWordLike);
    }

    // 단어 조회하기
    public WordLikeListResponseData getAllWordLike(User user){
        List<WordLike> wordLike = wordLikeRepository.findAllByUser(user);
        return WordLikeListResponseData.from(wordLike);
    }

    // 단어 삭제하기
    public void deleteWordLikeById(User user, UUID wordLikeId){
        WordLike deleteWordLike = findWordLike(user.getId(), wordLikeId);
        wordLikeRepository.delete(deleteWordLike);
    }

    private WordLike findWordLike(UUID userId, UUID wordLikeId) {
        return wordLikeRepository.findByUserIdAndId(userId, wordLikeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WORD_LIKE_NOT_FOUND));
    }
}
