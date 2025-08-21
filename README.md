# prw3jpa

Este projeto foi desenvolvido como atividade da disciplina **PRW3** no IFSP.
O objetivo é praticar o uso de **JPA** com **Hibernate** e banco **H2**.

## O que o projeto faz

* Cadastro de alunos (nome, RA, e-mail e notas).
* Operações básicas de CRUD:

  * Cadastrar aluno
  * Excluir aluno
  * Alterar dados do aluno
  * Buscar aluno pelo nome
  * Listar todos os alunos, mostrando status de aprovação/reprovação.

## Estrutura do projeto

* **src/main/java**: código fonte do sistema

  * `dao/` → classes de acesso a dados (AlunoDAO)
  * `modelo/` → entidades JPA (Aluno)
  * `util/` → utilitários de configuração (JPAUtil)
  * `Main.java` → menu interativo para executar a aplicação
* **src/main/resources/META-INF/persistence.xml**: configuração do JPA/Hibernate
* **pom.xml**: configuração do Maven (dependências)

## Observações

* O banco utilizado é o **H2 em memória**, recriado a cada execução.
