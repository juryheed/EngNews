package org.mjulikelion.engnews.dto.response.user;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MypageDto {
    private UUID id;
    private String name;
    private String email;
}
