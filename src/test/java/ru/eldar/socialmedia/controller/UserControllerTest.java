package ru.eldar.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    void whenLoginWithExistUser_thenReturnStatusOk() throws Exception {
        mvc.perform(post("/api/login")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("""
                       {
                       "email" : "email@mail.ru",
                       "password" : "password"
                       }
                       """))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.accessToken").isNotEmpty())
           .andExpect(jsonPath("$.refreshToken").isNotEmpty())
           .andReturn();
    }

    @Test
    @WithMockUser
    void whenLoginWithNotExistUser_thenReturnForbidden() throws Exception {
        mvc.perform(post("/api/login")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("""
                       {
                       "email" : "notexist@mail.ru",
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
                       "email" : "email1@mail.ru",
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

   /* @Test
    @WithMockUser
    void whenRefreshTokenWithValidRefreshToken_thenReturnStatusOk() throws Exception {
        String token = generateToken();
        RefreshJwtRequest req = new RefreshJwtRequest(token);

        mvc.perform(post("/api/refresh")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(asJsonString(req)))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.token").isNotEmpty())
           .andExpect(jsonPath("$.refreshToken").isNotEmpty())
           .andReturn();
    }
*/
   /* @Test
    void whenRefreshTokenWithInvalidRefreshToken_thenReturnStatusForbidden() throws Exception {
        RefreshJwtRequest req = new RefreshJwtRequest("INVALID_TOKEN", "REFRESH_TOKEN");

        mvc.perform(post("/api/refresh")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(asJsonString(req)))
           .andExpect(status().isForbidden())
           .andReturn();
    }
*/
   /* private String generateToken() {
        var token = JwtUtils.generate(userDetails);
        return token;
    }*/

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}