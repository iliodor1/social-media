package ru.eldar.socialmedia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.dto.AuthenticationResponse;
import ru.eldar.socialmedia.entity.Account;
import ru.eldar.socialmedia.exeption.EmailNotUniqueException;
import ru.eldar.socialmedia.repository.AccountRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;

    @Override
    public AuthenticationResponse signIn(AuthenticationRequest authenticationRequest) {
        return null;
    }

    @Override
    public Account signUp(Account account) {
        checkUniqueEmail(account.getEmail());
        account.setCreated();
        var createdAccount = accountRepository.save(account);

        return createdAccount;
    }

    public void checkUniqueEmail(String email){
        var account = accountRepository.findByEmail(email);

        if(account.isPresent()){
            log.error("user with email '{}' already exists", email);
            throw new EmailNotUniqueException(String.format("user with email '%s' already exists", email));
        }
    }
}
