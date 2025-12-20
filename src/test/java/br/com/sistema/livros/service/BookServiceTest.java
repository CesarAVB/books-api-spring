package br.com.sistema.livros.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.sistema.livros.dto.BookRequestDTO;
import br.com.sistema.livros.dto.BookResponseDTO;
import br.com.sistema.livros.exception.EntityNotFoundException;
import br.com.sistema.livros.mapper.BookMapper;
import br.com.sistema.livros.model.Book;
import br.com.sistema.livros.repository.BookRepository;

// Habilita o Mockito para criar mocks
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    // @Mock = Cria um objeto "falso" do Repository
    // Não vai acessar o banco de dados de verdade
    @Mock
    private BookRepository repository;

    // @Mock = Cria um objeto "falso" do Mapper
    @Mock
    private BookMapper mapper;

    // @InjectMocks = Cria o BookService e injeta os mocks acima
    @InjectMocks
    private BookService service;

    private BookRequestDTO requestDTO;
    private Book book;
    private BookResponseDTO responseDTO;

    // Prepara dados que serão usados em vários testes
    @BeforeEach
    void setUp() {
        // Cria um DTO de requisição
        requestDTO = new BookRequestDTO(
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
        );

        // Cria uma entidade Book
        book = new Book(
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
        );
        book.setId(1L);

        // Cria um DTO de resposta
        responseDTO = new BookResponseDTO(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
        );
    }

    @Test
    void deveCriarLivroComSucesso() {
        // ARRANGE
        // Configura o comportamento dos mocks
        when(repository.existsByIsbn(requestDTO.getIsbn())).thenReturn(false);
        when(mapper.toEntity(requestDTO)).thenReturn(book);
        when(repository.save(any(Book.class))).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(responseDTO);

        // ACT
        BookResponseDTO result = service.criarLivro(requestDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitulo());
        
        // Verifica se os métodos foram chamados
        verify(repository, times(1)).existsByIsbn(requestDTO.getIsbn());
        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    void deveLancarExcecaoAoCriarLivroComISBNDuplicado() {
        // ARRANGE
        // Simula que já existe um livro com esse ISBN
        when(repository.existsByIsbn(requestDTO.getIsbn())).thenReturn(true);

        // ACT & ASSERT
        // Verifica se lança a exceção esperada
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.criarLivro(requestDTO)
        );

        // Verifica a mensagem da exceção
        assertTrue(exception.getMessage().contains("ISBN já cadastrado"));
        
        // Verifica que o save NÃO foi chamado
        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void deveAtualizarLivroComSucesso() {
        // ARRANGE
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        //when(repository.existsByIsbn(requestDTO.getIsbn())).thenReturn(false);
        when(repository.save(any(Book.class))).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(responseDTO);

        // ACT
        BookResponseDTO result = service.atualizarLivro(1L, requestDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitulo());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarLivroInexistente() {
        // ARRANGE
        // Simula que o livro não existe
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.atualizarLivro(999L, requestDTO)
        );

        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void deveBuscarLivroPorIdComSucesso() {
        // ARRANGE
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(mapper.toResponse(book)).thenReturn(responseDTO);

        // ACT
        BookResponseDTO result = service.getById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarLivroInexistente() {
        // ARRANGE
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void deveListarTodosOsLivros() {
        // ARRANGE
        Book book2 = new Book("Clean Architecture", "Robert C. Martin", "978-0134494166", LocalDate.of(2017, 9, 12));
        book2.setId(2L);
        
        List<Book> books = Arrays.asList(book, book2);
        
        when(repository.findAll()).thenReturn(books);
        when(mapper.toResponse(any(Book.class))).thenReturn(responseDTO);

        // ACT
        List<BookResponseDTO> result = service.listarTodos();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveDeletarLivroComSucesso() {
        // ARRANGE
        when(repository.existsById(1L)).thenReturn(true);

        // ACT
        service.deletar(1L);

        // ASSERT
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarLivroInexistente() {
        // ARRANGE
        when(repository.existsById(999L)).thenReturn(false);

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> service.deletar(999L));
        verify(repository, never()).deleteById(any());
    }
}