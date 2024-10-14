package controle;

import persistencia.NoticiasDAO;
import strategies.EstrategiaTabelas;
import strategies.EstrategiasOpBasicas;

public class ControleNoticias extends Controle{
    
    protected EstrategiaTabelas estrategia2;

    public ControleNoticias(EstrategiasOpBasicas estrategia, EstrategiaTabelas estrategia2) {
        super(estrategia, NoticiasDAO.getInstancia());
        this.estrategia2 = estrategia2;
    }

    public void setEstrategia2(EstrategiaTabelas estrategia2) {
        this.estrategia2 = estrategia2;
    }

    public Object[][] textoTabelas() {
        return estrategia2.textoTabelas(1);
    }
}
