package persistencia;

public interface NoticiasDAO extends TextoDAO{

    // Método estático para obter a instância única da implementação da interface
    static NoticiasDAO getInstancia() {
        return PersistenciaNoticias.getInstancia();
    }

}
