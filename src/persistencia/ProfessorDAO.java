package persistencia;

import modelo.Professor;

public interface ProfessorDAO extends PersistenciaDAO {

    // Método estático para obter a instância única da implementação da interface
    static ProfessorDAO getInstancia() {
        return PersistenciaProfessor.getInstancia();
    }

    public abstract Professor buscaIDParcial(int id);
    public abstract int devolveIdPerdido(String email, int dia, int mes, int ano);
}
