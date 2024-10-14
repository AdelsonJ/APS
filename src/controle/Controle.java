package controle;

import persistencia.*;
import strategies.EstrategiasOpBasicas;

import org.json.simple.JSONObject;

import modelo.*;

public abstract class Controle {
    protected EstrategiasOpBasicas estrategia;
    protected PersistenciaDAO persistencia;

    public Controle(EstrategiasOpBasicas estrategia, PersistenciaDAO persistencia) {
        this.estrategia = estrategia;
        this.persistencia = persistencia;
    }

    public void setEstrategia(EstrategiasOpBasicas estrategia) {
        this.estrategia = estrategia;
    }

    public void insere(Entidade entidade) {
        estrategia.insere(entidade);
    }

    public void remove(Entidade entidade, boolean condicao){
        estrategia.remove(entidade, condicao);
    }

    public void caminhoExiste(){
        estrategia.caminhoExiste();
    }
    
    public void escreveArquivo(JSONObject escreve){
        estrategia.escreveArquivo(escreve);
    }

    public int devolveMaiorID(){
        return estrategia.devolveMaiorID();
    }

    public Entidade buscaID(int id){
        return estrategia.buscaID(id);
    }

}
