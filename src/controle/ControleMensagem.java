package controle;

import modelo.Entidade;
import persistencia.MensagemDAO;
import strategies.EstrategiasOpBasicas;

public class ControleMensagem extends Controle {
    public ControleMensagem(EstrategiasOpBasicas estrategia) {
        super(estrategia, MensagemDAO.getInstancia());
    }

    public Object[][] textoMensagem(Entidade entidade, int tipo){
        return ((MensagemDAO)persistencia).textoMensagem(entidade, tipo);
    }
}
