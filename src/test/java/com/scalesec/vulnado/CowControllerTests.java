import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CowController cowController;

    @Test
    public void cowsay_DefaultInput_ShouldReturnExpectedOutput() throws Exception {
        String expectedOutput = "I love Linux!";
        mockMvc.perform(MockMvcRequestBuilders.get("/cowsay"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedOutput));
    }

    @Test
    public void cowsay_CustomInput_ShouldReturnExpectedOutput() throws Exception {
        String customInput = "Hello, World!";
        String expectedOutput = "Hello, World!";
        mockMvc.perform(MockMvcRequestBuilders.get("/cowsay").param("input", customInput))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedOutput));
    }
}
