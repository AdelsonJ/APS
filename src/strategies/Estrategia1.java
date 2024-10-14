package strategies;

import persistencia.*;

import org.json.simple.JSONObject;

import modelo.*;


public class Estrategia1 implements EstrategiasOpBasicas {
    /* Cria um atributo do tipo persisntencia para usar dos métodos lá contidos */
    protected PersistenciaDAO persistencia;

    public Estrategia1(PersistenciaDAO persistencia) {
        this.persistencia = persistencia;
    }
    
    /* Chama o metodo insere */
    public void insere(Entidade entidade) {
        persistencia.insere(entidade);
    }

    /* Chama o metodo remove*/
    public void remove(Entidade entidade, boolean condicao){
        persistencia.remove(entidade, condicao);
    }

    /* Chama o meteodo caminhoExiste */
    public void caminhoExiste(){
        persistencia.caminhoExiste();
    }
    
    /* Chama o metodo escreveArquivo */
    public void escreveArquivo(JSONObject escreve){
        persistencia.escreveArquivo(escreve);
    }

    /* Chama o metodo escreveArquivo */
    public int devolveMaiorID(){
        return persistencia.devolveMaiorID();
    }

    /* Chama o metodo escreveArquivo */
    public Entidade buscaID(int id){
        return persistencia.buscaID(id);
    }

}
