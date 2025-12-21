package br.com.sistema.livros;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Startup.class) // ou simplesmente @SpringBootTest
@ActiveProfiles("dev")
class StartupTests {

	@Test
	void contextLoads() {
	}

}
