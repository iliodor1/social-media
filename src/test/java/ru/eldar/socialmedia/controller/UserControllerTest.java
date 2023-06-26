package ru.eldar.socialmedia.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.eldar.socialmedia.service.user.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    void whenLoginWithExistUser_thenReturnStatusOk() throws Exception {
        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "email" : "user@mail.ru",
                                "password" : "password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void whenLoginWithNotExistUser_thenReturnForbidden() throws Exception {
        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "email" : "wrong_user@email.ru",
                                "password" : "password"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenRegisterNewUser_thenReturnStatusOk() throws Exception {
        mvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username" : "username",
                                "email" : "user@mail.ru",
                                "password" : "password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andReturn();
    }

    @Test
    void whenRefreshTokenWithInvalidRefreshToken_thenReturnStatusForbidden() throws Exception {
        mvc.perform(post("/api/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "refreshToken" : "INVALID_REFRESH_TOKEN"
                                }
                                """))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}