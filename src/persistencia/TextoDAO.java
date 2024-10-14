package persistencia;

public interface TextoDAO  extends PersistenciaDAO{
    // Método estático para obter a instância única da implementação da interface
    static TextoDAO getInstancia() {
        return PersistenciaAula.getInstancia();
    }

    public abstract Object[][] textoTabelas();
}
