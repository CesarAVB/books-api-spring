package br.com.sistema.livros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    private String autor;

    @NotBlank(message = "ISBN é obrigatório")
    private String isbn;

    @NotNull(message = "Data de publicação é obrigatória")
    private LocalDate publicadoEm;

    // Construtor vazio
    public BookRequestDTO() {
    }

    // Construtor completo
    public BookRequestDTO(String titulo, String autor, String isbn, LocalDate publicadoEm) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.publicadoEm = publicadoEm;
    }

    // Getters e Setters
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