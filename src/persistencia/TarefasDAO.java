package persistencia;

import modelo.Entidade;

public interface TarefasDAO extends TextoDAO{

    // Método estático para obter a instância única da implementação da interface
    static TarefasDAO getInstancia() {
        return PersistenciaTarefa.getInstancia();
    }

    public abstract int numeroAlunos(Entidade entidade);
}
