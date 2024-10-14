package controle;

import modelo.Entidade;
import persistencia.TarefasDAO;
import strategies.EstrategiaTabelas;
import strategies.EstrategiasOpBasicas;

public class ControleTarefa extends Controle{
    
    protected EstrategiaTabelas estrategia2;

    public ControleTarefa(EstrategiasOpBasicas estrategia, EstrategiaTabelas estrategia2) {
        super(estrategia, TarefasDAO.getInstancia());
        this.estrategia2 = estrategia2;
    }

    public void setEstrategia2(EstrategiaTabelas estrategia2) {
        this.estrategia2 = estrategia2;
    }

    public Object[][] textoTabelas() {
        return estrategia2.textoTabelas(2);
    }

    public int numeroAlunos(Entidade entidade){
        return ((TarefasDAO)persistencia).numeroAlunos(entidade);
    }
}
