package org.mjulikelion.engnews.authentication;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.UnauthorizedException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@AllArgsConstructor
public class JwtEncoder {

    public static final String TOKEN_TYPE = "Bearer ";

    public static String encode(String token) {
        String cookieValue = TOKEN_TYPE + token;
        return URLEncoder.encode(cookieValue, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public static String decode(String cookieValue) {
        String value = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
        if (value.startsWith(TOKEN_TYPE)) {
            return value.substring(TOKEN_TYPE.length());
        }
        throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "token decode 실패");
    }
}
