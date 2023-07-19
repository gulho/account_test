package ee.gulho.account.api;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class TransactionTestIT {

    private static final String TRANSACTION_PATH = "/transaction/";
    private static final String TRANSACTION_ID = "065c6828-1cbe-4be3-bd74-cfb831faefdb";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTransaction_success() throws Exception {
        mockMvc.perform(post(TRANSACTION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountId": "df604273-4286-4b05-afa8-820860f9dbc5",
                                  "amount": 100,
                                  "currency":"USD",
                                  "direction": "IN",
                                  "description": "Add start money"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void createInternalTransaction_success() throws Exception {
        mockMvc.perform(post(TRANSACTION_PATH + "internal")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "accountOut": "df604273-4286-4b05-afa8-820860f9dbc5",
                          "accountIn": "378a0210-fa56-11ed-be56-0242ac120002",
                          "currency": "USD",
                          "amount": 5,
                          "description": "internal transaction"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void getTransaction_success() throws Exception {
        mockMvc.perform(get(TRANSACTION_PATH + TRANSACTION_ID))
                .andExpect(status().isOk());
    }
}
