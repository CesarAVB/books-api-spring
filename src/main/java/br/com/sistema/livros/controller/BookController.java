package br.com.sistema.livros.controller;

import br.com.sistema.livros.dto.BookRequestDTO;
import br.com.sistema.livros.dto.BookResponseDTO;
import br.com.sistema.livros.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Validated
@Tag(name = "Books", description = "Endpoints para gerenciamento de livros")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os livros", description = "Retorna uma lista com todos os livros cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> listarTodos() {
        List<BookResponseDTO> books = service.listarTodos();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Busca livro por ID", description = "Retorna um livro específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
        BookResponseDTO book = service.getById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Cria um novo livro", description = "Cadastra um novo livro no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ISBN duplicado")
    })
    @PostMapping
    public ResponseEntity<BookResponseDTO> criarLivro(@Valid @RequestBody BookRequestDTO request) {
        BookResponseDTO created = service.criarLivro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Atualiza um livro", description = "Atualiza os dados de um livro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> atualizarLivro(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO request) {
        BookResponseDTO updated = service.atualizarLivro(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deleta um livro", description = "Remove um livro do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}