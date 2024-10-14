package controle;

import modelo.Entidade;
import modelo.Aluno;
import persistencia.AlunoDAO;
import strategies.EstrategiasOpBasicas;

public class ControleAluno extends Controle{

    public ControleAluno(EstrategiasOpBasicas estrategia) {
        super(estrategia, AlunoDAO.getInstancia());
    }

    // Métodos específicos para ControleAluno
    public Aluno buscaIDParcial(int id){
        return ((AlunoDAO)persistencia).buscaIDParcial(id);
    }

    public int devolveIdPerdido(String email, int dia, int mes, int ano){
        return ((AlunoDAO)persistencia).devolveIdPerdido(email, dia, mes, ano);
    }

    public Object[][] textoAlunos(Entidade entidade){
        return ((AlunoDAO)persistencia).textoAlunos(entidade);
    }
    
}
