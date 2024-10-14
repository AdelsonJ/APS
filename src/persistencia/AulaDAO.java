package persistencia;

import modelo.Aula;
import modelo.Entidade;

public interface AulaDAO extends TextoDAO{

    // Método estático para obter a instância única da implementação da interface
    static AulaDAO getInstancia() {
        return PersistenciaAula.getInstancia();
    }

    public abstract Aula buscaIDParcial(int id);
    public abstract Object[][] aulasProfessor(Entidade entidade);
    public abstract Object[][] textoAlunos(Entidade entidade);
    public abstract int numeroAlunos(Entidade entidade);
    public abstract int jaInscrito(Entidade entidade, int id_aluno);
}
