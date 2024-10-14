package controle;

import modelo.Professor;
import persistencia.ProfessorDAO;
import strategies.EstrategiasOpBasicas;

public class ControleProfessor extends Controle{
    public ControleProfessor(EstrategiasOpBasicas estrategia) {
        super(estrategia, ProfessorDAO.getInstancia());
    }

    public Professor buscaIDParcial(int id){
        return ((ProfessorDAO)persistencia).buscaIDParcial(id);
    }

    public int devolveIdPerdido(String email, int dia, int mes, int ano){
        return ((ProfessorDAO)persistencia).devolveIdPerdido(email, dia, mes, ano);
    }

}
