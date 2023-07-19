package ee.gulho.account.api;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class AccountTestIT {

    private static final String ACCOUNT_PATH = "/account/";
    private static final String ACCOUNT_ID = "df604273-4286-4b05-afa8-820860f9dbc5";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAccountWithInvalidId_exception400return() throws Exception {
        mockMvc.perform(get(ACCOUNT_PATH + "wrongId"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAccount_success() throws Exception {
        mockMvc.perform(get(ACCOUNT_PATH + ACCOUNT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.equalTo(ACCOUNT_ID)));
    }

    @Test
    void createAccount_success() throws Exception {
        mockMvc.perform(post(ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "customerId": "378a0210-fa56-11ed-be56-0242ac120002",
                                    "country": "EE",
                                    "currencies":["USD"]
                                }
                                """))
                .andExpect(status().isOk());
    }

}
