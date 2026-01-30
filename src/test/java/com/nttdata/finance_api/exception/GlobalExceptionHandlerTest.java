package com.nttdata.finance_api.exception;

import com.nttdata.finance_api.config.security.JwtAuthenticationFilter;
import com.nttdata.finance_api.controller.UserController;
import com.nttdata.finance_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserService userService;

    @Test
    void shouldHandleUserNotFoundException() throws Exception {

        when(userService.findById(99L))
                .thenThrow(new ResourceNotFoundException(
                        "User not found with id: 99"
                ));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("User not found with id: 99"))
                // ⚠️ seu ApiResponse usa data=null
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
