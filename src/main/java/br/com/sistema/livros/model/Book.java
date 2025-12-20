package br.com.sistema.livros.model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(unique = true, length = 20)
    private String isbn;

    @Column(name = "publicado_em")
    private LocalDate publicadoEm;

    // Construtor vazio (obrigatório para JPA)
    public Book() {
    }

    // Construtor completo
    public Book(String titulo, String autor, String isbn, LocalDate publicadoEm) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.publicadoEm = publicadoEm;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicadoEm() {
        return publicadoEm;
    }

    public void setPublicadoEm(LocalDate publicadoEm) {
        this.publicadoEm = publicadoEm;
    }
}