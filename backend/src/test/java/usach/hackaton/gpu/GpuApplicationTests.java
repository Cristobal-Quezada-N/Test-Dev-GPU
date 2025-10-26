package usach.hackaton.gpu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import usach.hackaton.gpu.config.JwtUtil;

@SpringBootTest
class GpuApplicationTests {
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
    }
}
