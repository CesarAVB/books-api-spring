package br.com.sistema.livros.controller;

import br.com.sistema.livros.dto.BookRequestDTO;
import br.com.sistema.livros.dto.BookResponseDTO;
import br.com.sistema.livros.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Validated
public class BookController {

    private final BookService service;

    // Injeção via construtor
    public BookController(BookService service) {
        this.service = service;
    }

    /**
     * Lista todos os livros
     * GET /api/v1/books
     */
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> listarTodos() {
        List<BookResponseDTO> books = service.listarTodos();
        return ResponseEntity.ok(books);
    }

    /**
     * Busca um livro por ID
     * GET /api/v1/books/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
        BookResponseDTO book = service.getById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Cria um novo livro
     * POST /api/v1/books
     */
    @PostMapping
    public ResponseEntity<BookResponseDTO> criarLivro(@Valid @RequestBody BookRequestDTO request) {
        BookResponseDTO created = service.criarLivro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Atualiza um livro existente
     * PUT /api/v1/books/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> atualizarLivro(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO request) {
        BookResponseDTO updated = service.atualizarLivro(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deleta um livro
     * DELETE /api/v1/books/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}