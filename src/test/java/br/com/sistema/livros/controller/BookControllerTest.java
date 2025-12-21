package br.com.sistema.livros.controller;

import br.com.sistema.livros.dto.BookRequestDTO;
import br.com.sistema.livros.dto.BookResponseDTO;
import br.com.sistema.livros.exception.EntityNotFoundException;
import br.com.sistema.livros.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest - Testa apenas a camada WEB (Controller)
// Não carrega toda a aplicação, apenas o controller especificado
@WebMvcTest(BookController.class)
class BookControllerTest {

    // MockMvc - Simula requisições HTTP para o controller
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper - Converte objetos Java para JSON e vice-versa
    @Autowired
    private ObjectMapper objectMapper;

    // @MockBean - Cria um mock do Service
    // Como estamos testando apenas o Controller, não queremos executar o Service de verdade
    @MockBean
    private BookService service;

    private BookRequestDTO requestDTO;
    private BookResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Prepara os dados que serão usados nos testes
        requestDTO = new BookRequestDTO(
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
        );

        responseDTO = new BookResponseDTO(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
        );
    }

    // ========== TESTES GET ALL ==========
    
    @Test
    void deveListarTodosOsLivros() throws Exception {
        // ARRANGE
        // Cria uma lista com 2 livros
        BookResponseDTO responseDTO2 = new BookResponseDTO(
                2L,
                "Clean Architecture",
                "Robert C. Martin",
                "978-0134494166",
                LocalDate.of(2017, 9, 12)
        );
        List<BookResponseDTO> books = Arrays.asList(responseDTO, responseDTO2);

        // Configura o mock: quando chamar listarTodos(), retorna a lista
        when(service.listarTodos()).thenReturn(books);

        // ACT & ASSERT
        // Faz uma requisição GET e verifica a resposta
        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Status 200
                .andExpect(jsonPath("$", hasSize(2)))  // Array com 2 elementos
                .andExpect(jsonPath("$[0].id").value(1))  // Primeiro livro tem ID 1
                .andExpect(jsonPath("$[0].titulo").value("Clean Code"))
                .andExpect(jsonPath("$[1].id").value(2));  // Segundo livro tem ID 2

        // Verifica se o método do service foi chamado
        verify(service, times(1)).listarTodos();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverLivros() throws Exception {
        // ARRANGE
        when(service.listarTodos()).thenReturn(Arrays.asList());

        // ACT & ASSERT
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));  // Array vazio
    }

    // ========== TESTES GET BY ID ==========

    @Test
    void deveBuscarLivroPorId() throws Exception {
        // ARRANGE
        when(service.getById(1L)).thenReturn(responseDTO);

        // ACT & ASSERT
        mockMvc.perform(get("/api/v1/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Status 200
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn").value("978-0132350884"));

        verify(service, times(1)).getById(1L);
    }

    @Test
    void deveRetornar404QuandoLivroNaoExistir() throws Exception {
        // ARRANGE
        // Simula que o service lança exceção quando livro não existe
        when(service.getById(999L)).thenThrow(new EntityNotFoundException("Livro não encontrado com ID: 999"));

        // ACT & ASSERT
        mockMvc.perform(get("/api/v1/books/{id}", 999L))
                .andExpect(status().isNotFound())  // Status 404
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Livro não encontrado com ID: 999"));
    }

    // ========== TESTES POST ==========

    @Test
    void deveCriarNovoLivro() throws Exception {
        // ARRANGE
        when(service.criarLivro(any(BookRequestDTO.class))).thenReturn(responseDTO);

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        // Converte o DTO para JSON
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())  // Status 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Clean Code"));

        verify(service, times(1)).criarLivro(any(BookRequestDTO.class));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        // ARRANGE
        // Cria um DTO com dados inválidos (título vazio)
        BookRequestDTO dtoInvalido = new BookRequestDTO(
                "",  // título vazio - vai falhar na validação @NotBlank
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
        );

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());  // Status 400

        // O service NÃO deve ser chamado se a validação falhar
        verify(service, never()).criarLivro(any(BookRequestDTO.class));
    }

    @Test
    void deveRetornar400QuandoISBNDuplicado() throws Exception {
        // ARRANGE
        // Simula que o service lança exceção quando ISBN já existe
        when(service.criarLivro(any(BookRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("ISBN já cadastrado: 978-0132350884"));

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())  // Status 400
                .andExpect(jsonPath("$.message").value("ISBN já cadastrado: 978-0132350884"));
    }

    // ========== TESTES PUT ==========

    @Test
    void deveAtualizarLivro() throws Exception {
        // ARRANGE
        BookResponseDTO updatedResponse = new BookResponseDTO(
                1L,
                "Clean Code - Segunda Edição",
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
        );

        when(service.atualizarLivro(eq(1L), any(BookRequestDTO.class))).thenReturn(updatedResponse);

        // ACT & ASSERT
        mockMvc.perform(put("/api/v1/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())  // Status 200
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Clean Code - Segunda Edição"));

        verify(service, times(1)).atualizarLivro(eq(1L), any(BookRequestDTO.class));
    }

    @Test
    void deveRetornar404AoAtualizarLivroInexistente() throws Exception {
        // ARRANGE
        when(service.atualizarLivro(eq(999L), any(BookRequestDTO.class)))
                .thenThrow(new EntityNotFoundException("Livro não encontrado com ID: 999"));

        // ACT & ASSERT
        mockMvc.perform(put("/api/v1/books/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());  // Status 404
    }

    // ========== TESTES DELETE ==========

    @Test
    void deveDeletarLivro() throws Exception {
        // ARRANGE
        // O método deletar não retorna nada, apenas executa
        doNothing().when(service).deletar(1L);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/v1/books/{id}", 1L))
                .andExpect(status().isNoContent());  // Status 204

        verify(service, times(1)).deletar(1L);
    }

    @Test
    void deveRetornar404AoDeletarLivroInexistente() throws Exception {
        // ARRANGE
        // doThrow - usado para métodos void que lançam exceção
        doThrow(new EntityNotFoundException("Livro não encontrado com ID: 999"))
                .when(service).deletar(999L);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/v1/books/{id}", 999L))
                .andExpect(status().isNotFound());  // Status 404
    }
}