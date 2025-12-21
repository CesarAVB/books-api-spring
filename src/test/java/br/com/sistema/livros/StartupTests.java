package br.com.sistema.livros;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootConfiguration
@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
class StartupTests {

	@Test
	void contextLoads() {
	}

}
