package persistencia;

import modelo.Entidade;
import modelo.Aluno;

public interface AlunoDAO extends PersistenciaDAO{
    // Método estático para obter a instância única da implementação da interface
    static AlunoDAO getInstancia() {
        return PersistenciaAluno.getInstancia();
    }

    public abstract Aluno buscaIDParcial(int id);
    public abstract int devolveIdPerdido(String email, int dia, int mes, int ano);
    public abstract Object[][] textoAlunos(Entidade aluno);
}
