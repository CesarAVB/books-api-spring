package br.com.sistema.livros.mapper;

import org.springframework.stereotype.Component;
import br.com.sistema.livros.dto.BookRequestDTO;
import br.com.sistema.livros.dto.BookResponseDTO;
import br.com.sistema.livros.model.Book;

@Component
public class BookMapper {

    /**
     * Converte BookRequestDTO para entidade Book
     */
    public Book toEntity(BookRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Book(
            dto.getTitulo(),
            dto.getAutor(),
            dto.getIsbn(),
            dto.getPublicadoEm()
        );
    }

    /**
     * Converte entidade Book para BookResponseDTO
     */
    public BookResponseDTO toResponse(Book book) {
        if (book == null) {
            return null;
        }

        return new BookResponseDTO(
            book.getId(),
            book.getTitulo(),
            book.getAutor(),
            book.getIsbn(),
            book.getPublicadoEm()
        );
    }
}