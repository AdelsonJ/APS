package strategies;

import persistencia.AulaDAO;
import persistencia.NoticiasDAO;
import persistencia.TarefasDAO;

public class Estrategia2 implements EstrategiaTabelas{

    public Object[][] textoTabelas(int tipo){
        switch (tipo) {
            case 0:
                return (AulaDAO.getInstancia()).textoTabelas();
                
            
            case 1:
                return (NoticiasDAO.getInstancia()).textoTabelas();

            default:
                return (TarefasDAO.getInstancia()).textoTabelas();
        }
    }
}
