package controle;

import modelo.Aula;
import modelo.Entidade;
import persistencia.AulaDAO;
import strategies.EstrategiaTabelas;
import strategies.EstrategiasOpBasicas;

public class ControleAula extends Controle {

    protected EstrategiaTabelas estrategia2;
    
    public ControleAula(EstrategiasOpBasicas estrategia, EstrategiaTabelas estrategia2) {
        super(estrategia, AulaDAO.getInstancia());
        this.estrategia2 = estrategia2;
    }

    public void setEstrategia2(EstrategiaTabelas estrategia2) {
        this.estrategia2 = estrategia2;
    }

    public Object[][] textoTabelas() {
        return estrategia2.textoTabelas(0);
    }

    public Aula buscaIDParcial(int id){
        return ((AulaDAO)persistencia).buscaIDParcial(id);
    }

    public Object[][] aulasProfessor(Entidade  entidade){
        return ((AulaDAO)persistencia).aulasProfessor(entidade);
    }

    public Object[][] textoAlunos(Entidade entidade){
        return ((AulaDAO)persistencia).textoAlunos(entidade);
    }

    public int numeroAlunos(Entidade entidade){
        return ((AulaDAO)persistencia).numeroAlunos(entidade);
    }

    public int jaInscrito(Entidade entidade, int id_aluno){
        return ((AulaDAO)persistencia).jaInscrito(entidade, id_aluno);
    }
}
