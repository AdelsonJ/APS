package controle;

import persistencia.PersistenciaUsuario;
import strategies.EstrategiasOpBasicas;

public class ControleUsuario extends Controle {

    public ControleUsuario(EstrategiasOpBasicas estrategia) {
        super(estrategia, PersistenciaUsuario.getInstancia());
    }
}