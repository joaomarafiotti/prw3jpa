package br.edu.ifsp.joaomarafiotti.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "alunos")
public class Aluno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String ra;

    @Column(nullable = false)
    private String email;

    @Column(precision = 5, scale = 2)
    private BigDecimal nota1;
    @Column(precision = 5, scale = 2)
    private BigDecimal nota2;
    @Column(precision = 5, scale = 2)
    private BigDecimal nota3;

    public Aluno() {}
    public Aluno(String nome, String ra, String email) { this.nome = nome; this.ra = ra; this.email = email; }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getNota1() { return nota1; }
    public void setNota1(BigDecimal nota1) { this.nota1 = nota1; }
    public BigDecimal getNota2() { return nota2; }
    public void setNota2(BigDecimal nota2) { this.nota2 = nota2; }
    public BigDecimal getNota3() { return nota3; }
    public void setNota3(BigDecimal nota3) { this.nota3 = nota3; }

    @Transient
    public BigDecimal getMedia() {
        if (nota1 == null || nota2 == null || nota3 == null) return null;
        return nota1.add(nota2).add(nota3).divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
    }

    @Transient
    public String getStatusAprovacao() {
        BigDecimal m = getMedia();
        if (m == null) return "SEM NOTAS";
        if (m.compareTo(new BigDecimal("4.00")) < 0) return "Reprovado";
        if (m.compareTo(new BigDecimal("6.00")) < 0) return "Recuperação";
        return "Aprovado";
    }

    @Override
    public String toString() { return nome; }
}