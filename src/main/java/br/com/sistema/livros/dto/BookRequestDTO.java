package br.com.sistema.livros.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 100, message = "Título deve ter entre 3 e 100 caracteres")
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    @Size(min = 3, max = 100, message = "Autor deve ter entre 3 e 100 caracteres")
    private String autor;

    @NotBlank(message = "ISBN é obrigatório")
    @Size(min = 10, max = 17, message = "ISBN deve ter entre 10 e 17 caracteres")
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