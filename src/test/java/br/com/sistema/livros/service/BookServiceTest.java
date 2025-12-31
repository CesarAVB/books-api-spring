package br.com.sistema.livros.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

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


@ExtendWith(MockitoExtension.class) // Ativa o Mockito nessa classe de teste
class BookServiceTest {
   
    @Mock // Cria um "fake" do repository (não usa banco real)
    private BookRepository repository; // Variável que vai ser o fake

    @Mock // Cria um "fake" do mapper (não converte de verdade)
    private BookMapper mapper; // Variável que vai ser o fake
    
    @InjectMocks // Injeta automaticamente os @Mock acima no service
    private BookService service; // O service recebe os mocks automaticamente
  
    // Variáveis globais
    private BookRequestDTO requestDTO;
    private Book book;
    private BookResponseDTO responseDTO;

    
    @BeforeEach	// Prepara dados que serão usados em vários testes - Será chamado antes de todos os testes
    void setUp() {
        requestDTO = new BookRequestDTO("Clean Code", "Robert C. Martin", "978-0132350884", LocalDate.of(2008, 8, 1));			// Cria um DTO de requisição
        book = new Book("Clean Code", "Robert C. Martin", "978-0132350884", LocalDate.of(2008, 8, 1));							// Cria uma entidade Book
        book.setId(1L);																											// Define ID (como se o banco tivesse gerado)
        responseDTO = new BookResponseDTO(1L, "Clean Code", "Robert C. Martin", "978-0132350884", LocalDate.of(2008, 8, 1));	// Cria um DTO de resposta
    }
    
    
    @Test	
    void testCriarLivroComSucesso() { // Nome do teste: deve + ação + resultado esperado
    	
        // ========== ARRANGE (Preparar dados) - CONFIGURAR OS MOCKS ==========
        when(repository.existsByIsbn("978-0132350884")).thenReturn(false); 	// Quando perguntar "existe ISBN?", responde FALSE - Significa: "O ISBN não existe ainda, pode criar"
        when(mapper.toEntity(requestDTO)).thenReturn(book); 				// Quando perguntar "converta DTO em Entity?", retorna o book - Significa: "Aqui está o livro convertido"
        when(repository.save(book)).thenReturn(book); 						// Quando perguntar "salve esse livro?", retorna o book - Significa: "Livro salvo com sucesso"
        when(mapper.toResponse(book)).thenReturn(responseDTO); 				// Quando perguntar "converta Entity em DTO?", retorna o responseDTO - Significa: "Aqui está o DTO para enviar ao cliente"

        
        // ========== ACT (Executar o método que queremos testar) ==========
        BookResponseDTO result = service.criarLivro(requestDTO); 			// Chama o método criarLivro() e guarda o resultado - Aqui o service vai usar os mocks acima

        
        // ========== ASSERT (Validar o resultado) ==========
        assertNotNull(result); 												// Verifica se o resultado NÃO é nulo (existe) - Se for nulo, o teste FALHA
        assertEquals(1L, result.getId()); 									// Verifica se o ID é igual a 1 - Se for diferente, o teste FALHA
        assertEquals("Clean Code", result.getTitulo()); 					// Verifica se o título é "Clean Code" - Se for diferente, o teste FALHA
        assertEquals("Robert C. Martin", result.getAutor()); 				// Verifica se o autor é "Robert C. Martin" - Se for diferente, o teste FALHA
        assertEquals("978-0132350884", result.getIsbn()); 					// Verifica se o ISBN é "978-0132350884" - Se for diferente, o teste FALHA
        assertEquals(LocalDate.of(2008, 8, 1), result.getPublicadoEm()); 	// Verifica se o ano é 2008, 8, 1 - Se for diferente, o teste FALHA

    }
    
    
    @Test
    void deveApresentarIsbnDuplicado() {
    	// ARRANGE
    	when(repository.existsByIsbn("978-0132350884")).thenReturn(true);
   
    	// ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> service.criarLivro(requestDTO)); // Quero que lance uma IllegalArgumentException ao chamar o método criarLivro	
    }
    
    
    @Test
    void deveExcluirLivroComSucesso() {
    	//ARRANGE
    	when(repository.existsById(1L)).thenReturn(true);
    	
    	//ACT
    	service.deletar(1L);
    	
    	// ASSERT
        // Se chegou aqui sem exceção, teste PASSOU
    }
    
    
    @Test
    void deveLançarErroAoDeletarLivroInexistente() {
    	//ARRANGE
    	when(repository.existsById(1L)).thenReturn(false);
    	
    	// ACT + ASSERT
        assertThrows(						// Uma função que ESPERA uma exceção
            EntityNotFoundException.class, 	// Qual exceção você ESPERA?
            () -> service.deletar(1L)		// Uma função que você quer EXECUTAR
        );
    }
    
    
    @Test
    void deveAtualizarLivroComSucesso() {
    	// ARRANGE
        when(repository.existsById(1L)).thenReturn(true);

        // ACT
        BookResponseDTO result = service.atualizarLivro(book.getId(), requestDTO);

        // ASSERT
        assertNotNull(result);
    }
}