package persistencia;

import modelo.Entidade;

public interface MensagemDAO extends PersistenciaDAO{

    // Método estático para obter a instância única da implementação da interface
    static MensagemDAO getInstancia() {
        return PersistenciaMensagem.getInstancia();
    }

    public abstract Object[][] textoMensagem(Entidade entidade, int tipo);
}
