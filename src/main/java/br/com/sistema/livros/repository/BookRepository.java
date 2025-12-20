package br.com.sistema.livros.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.sistema.livros.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	// Método customizado para buscar por ISBN
    Optional<Book> findByIsbn(String isbn);
    
    // Método para verificar se existe livro com determinado ISBN
    boolean existsByIsbn(String isbn);
	
}
