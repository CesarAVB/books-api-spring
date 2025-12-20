package br.com.sistema.livros;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.com.sistema.livros.model.Book;
import br.com.sistema.livros.repository.BookRepository;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}

	// Teste temporário - REMOVER depois
	@Bean
	CommandLineRunner testDatabase(BookRepository repository) {
		return args -> {
			// Salva um livro
			Book livro = new Book("Clean Code", "Robert C. Martin", "978-0132350884", LocalDate.of(2008, 8, 1));
			repository.save(livro);

			// Busca todos os livros
			System.out.println("=== Livros no banco ===");
			repository.findAll().forEach(b -> System.out.println(b.getId() + " - " + b.getTitulo()));
		};
	}

}