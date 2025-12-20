package br.com.sistema.livros.dto;

import java.time.LocalDate;

public class BookResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private LocalDate publicadoEm;

    // Construtor vazio
    public BookResponseDTO() {
    }

    // Construtor completo
    public BookResponseDTO(Long id, String titulo, String autor, String isbn, LocalDate publicadoEm) {
        this.id = id;
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