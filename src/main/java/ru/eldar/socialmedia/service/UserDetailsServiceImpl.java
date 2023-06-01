package ru.eldar.socialmedia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return accountRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                        String.format("Account %s not found", username))
                                );
    }


}
