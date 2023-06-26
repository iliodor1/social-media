package ru.eldar.socialmedia.util;

import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;
import ru.eldar.socialmedia.entity.user.JwtAuthentication;

@NoArgsConstructor
public final class JwtUtils {
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }
}
