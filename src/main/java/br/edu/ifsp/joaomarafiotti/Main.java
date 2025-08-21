package br.edu.ifsp.joaomarafiotti;

import br.edu.ifsp.joaomarafiotti.dao.AlunoDAO;
import br.edu.ifsp.joaomarafiotti.modelo.Aluno;
import br.edu.ifsp.joaomarafiotti.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        try (EntityManager em = JPAUtil.getEntityManager()) {
            AlunoDAO dao = new AlunoDAO(em);
            int op;
            do {
                mostrarMenu();
                op = lerInteiro("\nDigite a opção desejada: ");
                switch (op) {
                    case 1 -> cadastrarAluno(dao);
                    case 2 -> excluirAluno(dao);
                    case 3 -> alterarAluno(dao);
                    case 4 -> buscarPorNome(dao);
                    case 5 -> listarComStatus(dao);
                    case 6 -> System.out.println("Encerrando...");
                    default -> System.out.println("Opção inválida.");
                }
            } while (op != 6);
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n** CADASTRO DE ALUNOS **\n");
        System.out.println("1 - Cadastrar aluno");
        System.out.println("2 - Excluir aluno");
        System.out.println("3 - Alterar aluno");
        System.out.println("4 - Buscar aluno pelo nome");
        System.out.println("5 - Listar alunos (com status aprovação)");
        System.out.println("6 - FIM");
    }

    private static void cadastrarAluno(AlunoDAO dao) {
        System.out.println("\nCADASTRO DE ALUNO:");
        String nome  = lerObrigatorio("Digite o nome: ");

        // RA único
        String ra;
        while (true) {
            ra = lerObrigatorio("Digite o RA: ");
            if (dao.existeRa(ra)) System.out.println("RA já cadastrado. Informe outro.");
            else break;
        }

        String email = lerObrigatorio("Digite o email: ");

        Aluno a = new Aluno(nome, ra, email);
        a.setNota1(lerNotaObrigatoria("Digite a nota 1: "));
        a.setNota2(lerNotaObrigatoria("Digite a nota 2: "));
        a.setNota3(lerNotaObrigatoria("Digite a nota 3: "));

        try {
            dao.salvar(a);
        } catch (PersistenceException ex) {
            System.out.println("Erro ao salvar. Verifique os dados e tente novamente.");
        }
    }

    private static void excluirAluno(AlunoDAO dao) {
        System.out.println("\nEXCLUIR ALUNO:");
        String nome = lerObrigatorio("Digite o nome: ");
        boolean ok = dao.excluirPorNome(nome);
        System.out.println(ok ? "\nAluno removido com sucesso!" : "\nAluno não encontrado!");
    }

    private static void alterarAluno(AlunoDAO dao) {
        System.out.println("\nALTERAR ALUNO:");
        String nomeBusca = lerObrigatorio("Digite o nome: ");
        Aluno a = dao.buscarPorNomeExato(nomeBusca);
        if (a == null) { System.out.println("\nAluno não encontrado!"); return; }

        System.out.println("Dados do aluno:");
        exibirAlunoDetalhado(a);

        System.out.println("\nNOVOS DADOS:");
        a.setNome(lerObrigatorio("Digite o nome: "));

        // RA único (permite manter o mesmo)
        while (true) {
            String novoRa = lerObrigatorio("Digite o RA: ");
            if (novoRa.equals(a.getRa()) || !dao.existeRa(novoRa)) {
                a.setRa(novoRa);
                break;
            } else {
                System.out.println("RA já cadastrado. Informe outro.");
            }
        }

        a.setEmail(lerObrigatorio("Digite o email: "));
        a.setNota1(lerNotaObrigatoria("Digite a nota 1: "));
        a.setNota2(lerNotaObrigatoria("Digite a nota 2: "));
        a.setNota3(lerNotaObrigatoria("Digite a nota 3: "));

        try {
            dao.atualizar(a);
            System.out.println("\nAluno Alterado com sucesso!");
        } catch (PersistenceException ex) {
            System.out.println("Erro ao atualizar. Verifique os dados e tente novamente.");
        }
    }

    private static void buscarPorNome(AlunoDAO dao) {
        System.out.println("\nCONSULTAR ALUNO:");
        String nome = lerObrigatorio("Digite o nome: ");
        Aluno a = dao.buscarPorNomeExato(nome);
        if (a == null) { System.out.println("\nAluno não encontrado!"); return; }
        System.out.println("\nDados do aluno:");
        exibirAlunoDetalhado(a);
    }

    private static void listarComStatus(AlunoDAO dao) {
        List<Aluno> todos = dao.listarTodos();
        if (todos.isEmpty()) { System.out.println("Sem alunos cadastrados."); return; }
        System.out.println("Exibindo todos os alunos:\n");
        for (Aluno a : todos) { exibirAlunoDetalhado(a); System.out.println(); }
    }

    // ===== util =====
    private static void exibirAlunoDetalhado(Aluno a) {
        System.out.println("Nome: " + a.getNome());
        System.out.println("Email: " + a.getEmail());
        System.out.println("RA: " + a.getRa());
        System.out.printf("Notas: %s - %s - %s\n", fmt(a.getNota1()), fmt(a.getNota2()), fmt(a.getNota3()));
        BigDecimal m = a.getMedia();
        System.out.println("Media: " + (m == null ? "--" : m.toPlainString()));
        System.out.println("Situação: " + a.getStatusAprovacao());
    }

    private static String lerObrigatorio(String rotulo) {
        while (true) {
            System.out.print(rotulo);
            String s = in.nextLine();
            if (s != null && !s.isBlank()) return s.trim();
            System.out.println("Valor obrigatório. Tente novamente.");
        }
    }

    private static int lerInteiro(String rotulo) {
        while (true) {
            try {
                System.out.print(rotulo);
                return Integer.parseInt(in.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Número inteiro inválido.");
            }
        }
    }

    // Nota obrigatória (0..10)
    private static BigDecimal lerNotaObrigatoria(String rotulo) {
        while (true) {
            System.out.print(rotulo);
            String s = in.nextLine();
            if (s == null || s.isBlank()) {
                System.out.println("Nota obrigatória. Digite um número de 0 a 10.");
                continue;
            }
            try {
                BigDecimal v = new BigDecimal(s.trim().replace(',', '.'));
                if (v.compareTo(BigDecimal.ZERO) < 0 || v.compareTo(new BigDecimal("10")) > 0) {
                    System.out.println("Nota inválida. Informe um número entre 0 e 10.");
                    continue;
                }
                return v.setScale(2, java.math.RoundingMode.HALF_UP);
            } catch (Exception e) {
                System.out.println("Valor inválido. Digite número (ex.: 7.5).");
            }
        }
    }

    private static String fmt(BigDecimal v) { return v == null ? "--" : v.toPlainString(); }
}
