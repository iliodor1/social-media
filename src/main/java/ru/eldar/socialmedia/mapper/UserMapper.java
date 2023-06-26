package ru.eldar.socialmedia.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.eldar.socialmedia.dto.user.RegistrationRequest;
import ru.eldar.socialmedia.dto.user.RegistrationResponse;
import ru.eldar.socialmedia.entity.user.User;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    private final PasswordEncoder encoder;

    public User toUser(RegistrationRequest registrationRequest) {
        var user = mapper.map(registrationRequest, User.class);

        user.setPassword(encoder.encode(registrationRequest.getPassword()));

        return user;
    }

    public RegistrationResponse toRegistrationResponse(User user) {
        return mapper.map(user, RegistrationResponse.class);
    }
}
