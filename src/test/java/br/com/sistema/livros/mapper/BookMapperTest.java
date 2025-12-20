package br.com.sistema.livros.mapper;

import br.com.sistema.livros.dto.BookRequestDTO;
import br.com.sistema.livros.dto.BookResponseDTO;
import br.com.sistema.livros.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

// Classe de teste - sempre termina com "Test" no nome
// Testa a classe BookMapper
class BookMapperTest {

    // Variável que será usada em TODOS os testes
    // É a classe que queremos testar
    private BookMapper mapper;

    // @BeforeEach = Executa ANTES de CADA teste
    // É usado para preparar objetos que todos os testes vão usar
    @BeforeEach
    void setUp() {
        // Cria uma nova instância do BookMapper
        // Agora a variável "mapper" está pronta para ser usada em qualquer teste
        mapper = new BookMapper();
    }

    // @Test = Marca que este método é um teste
    // JUnit vai executar este método automaticamente
    @Test
    void deveConverterRequestDTOParaEntity() {
        // ========== ARRANGE (PREPARAR) ==========
        // Cria um DTO de requisição com dados de exemplo
        // Este é o objeto que vamos ENVIAR para o método que queremos testar
        BookRequestDTO dto = new BookRequestDTO(
            "Clean Code",                    // título do livro
            "Robert C. Martin",              // autor
            "978-0132350884",                // ISBN
            LocalDate.of(2008, 8, 1)        // data de publicação
        );

        // ========== ACT (AGIR/EXECUTAR) ==========
        // Chama o método que queremos testar
        // toEntity() converte o DTO em uma entidade Book
        Book book = mapper.toEntity(dto);

        // ========== ASSERT (VERIFICAR) ==========
        // Agora verificamos se o método funcionou corretamente
        
        // Verifica se o objeto Book foi criado (não é null)
        assertNotNull(book);
        
        // Verifica se o título foi copiado corretamente do DTO para a entidade
        // assertEquals(esperado, real)
        assertEquals("Clean Code", book.getTitulo());
        
        // Verifica se o autor foi copiado corretamente
        assertEquals("Robert C. Martin", book.getAutor());
        
        // Verifica se o ISBN foi copiado corretamente
        assertEquals("978-0132350884", book.getIsbn());
        
        // Verifica se a data foi copiada corretamente
        assertEquals(LocalDate.of(2008, 8, 1), book.getPublicadoEm());
        
        // Verifica se o ID está nulo
        // O ID só deve ser preenchido DEPOIS que salvarmos no banco de dados
        // Como estamos apenas convertendo DTO para Entity, o ID deve ser null
        assertNull(book.getId());
    }

    // Segundo teste - converte entidade para DTO de resposta
    @Test
    void deveConverterEntityParaResponseDTO() {
        // ========== ARRANGE (PREPARAR) ==========
        // Cria uma entidade Book com dados de exemplo
        // Simulamos um livro que JÁ FOI SALVO no banco de dados
        Book book = new Book(
            "Clean Architecture",           // título
            "Robert C. Martin",             // autor
            "978-0134494166",               // ISBN
            LocalDate.of(2017, 9, 12)      // data de publicação
        );
        
        // Seta o ID manualmente
        // Isso simula um livro que já está no banco de dados
        // No banco real, o ID é gerado automaticamente
        book.setId(1L);

        // ========== ACT (AGIR/EXECUTAR) ==========
        // Chama o método que converte a entidade para DTO de resposta
        BookResponseDTO dto = mapper.toResponse(book);

        // ========== ASSERT (VERIFICAR) ==========
        // Verifica se o DTO foi criado
        assertNotNull(dto);
        
        // Verifica se o ID foi copiado corretamente
        assertEquals(1L, dto.getId());
        
        // Verifica se o título foi copiado corretamente
        assertEquals("Clean Architecture", dto.getTitulo());
        
        // Verifica se o autor foi copiado corretamente
        assertEquals("Robert C. Martin", dto.getAutor());
        
        // Verifica se o ISBN foi copiado corretamente
        assertEquals("978-0134494166", dto.getIsbn());
        
        // Verifica se a data foi copiada corretamente
        assertEquals(LocalDate.of(2017, 9, 12), dto.getPublicadoEm());
    }

    // Terceiro teste - testa um caso especial (null)
    @Test
    void deveRetornarNullQuandoDTOForNull() {
        // ========== ACT (AGIR/EXECUTAR) ==========
        // Chama o método passando null como parâmetro
        // Estamos testando o que acontece quando alguém passa null
        Book book = mapper.toEntity(null);

        // ========== ASSERT (VERIFICAR) ==========
        // Verifica se o método retorna null quando recebe null
        // Isso evita que o sistema quebre (NullPointerException)
        assertNull(book);
    }

    // Quarto teste - outro caso especial (null)
    @Test
    void deveRetornarNullQuandoEntityForNull() {
        // ========== ACT (AGIR/EXECUTAR) ==========
        // Chama o método passando null como parâmetro
        BookResponseDTO dto = mapper.toResponse(null);

        // ========== ASSERT (VERIFICAR) ==========
        // Verifica se retorna null quando recebe null
        assertNull(dto);
    }
}