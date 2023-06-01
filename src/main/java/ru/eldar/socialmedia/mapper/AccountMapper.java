package ru.eldar.socialmedia.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.eldar.socialmedia.dto.RegistrationRequest;
import ru.eldar.socialmedia.entity.Account;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final ModelMapper mapper;

    private final PasswordEncoder encoder;

    public Account toAccount(RegistrationRequest registrationRequest){
        var account = mapper.map(registrationRequest, Account.class);

        account.setPassword(encoder.encode(registrationRequest.getPassword()));

        return account;
    }
}
