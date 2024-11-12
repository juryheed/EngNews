package org.mjulikelion.engnews.authentication;

import lombok.Getter;
import lombok.Setter;
import org.mjulikelion.engnews.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@Component
@RequestScope
public class AuthenticationContext {
    private User principal;
}
