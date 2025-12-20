package br.com.sistema.livros.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.sistema.livros.dto.BookRequestDTO;
import br.com.sistema.livros.dto.BookResponseDTO;
import br.com.sistema.livros.exception.EntityNotFoundException;
import br.com.sistema.livros.mapper.BookMapper;
import br.com.sistema.livros.model.Book;
import br.com.sistema.livros.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    // Injeção de dependências via construtor
    public BookService(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Cria um novo livro
     * Valida se o ISBN já existe antes de salvar
     */
    @Transactional
    public BookResponseDTO criarLivro(BookRequestDTO dto) {
        // Validação: ISBN deve ser único
        if (repository.existsByIsbn(dto.getIsbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado: " + dto.getIsbn());
        }

        // Converte DTO para entidade
        Book book = mapper.toEntity(dto);
        
        // Salva no banco
        Book savedBook = repository.save(book);
        
        // Converte entidade para DTO de resposta
        return mapper.toResponse(savedBook);
    }

    /**
     * Atualiza um livro existente
     */
    @Transactional
    public BookResponseDTO atualizarLivro(Long id, BookRequestDTO dto) {
        // Busca o livro ou lança exceção se não existir
        Book book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado com ID: " + id));

        // Validação: se o ISBN mudou, verifica se já existe outro livro com esse ISBN
        if (!book.getIsbn().equals(dto.getIsbn()) && repository.existsByIsbn(dto.getIsbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado: " + dto.getIsbn());
        }

        // Atualiza os campos
        book.setTitulo(dto.getTitulo());
        book.setAutor(dto.getAutor());
        book.setIsbn(dto.getIsbn());
        book.setPublicadoEm(dto.getPublicadoEm());

        // Salva as alterações
        Book updatedBook = repository.save(book);
        
        return mapper.toResponse(updatedBook);
    }

    /**
     * Busca um livro por ID
     */
    public BookResponseDTO getById(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado com ID: " + id));
        
        return mapper.toResponse(book);
    }

    /**
     * Lista todos os livros
     */
    public List<BookResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deleta um livro por ID
     */
    @Transactional
    public void deletar(Long id) {
        // Verifica se existe antes de deletar
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Livro não encontrado com ID: " + id);
        }
        
        repository.deleteById(id);
    }
}