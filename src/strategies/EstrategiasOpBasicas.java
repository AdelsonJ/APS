package strategies;

import org.json.simple.JSONObject;

import modelo.*;


public interface EstrategiasOpBasicas {
    public abstract void insere(Entidade entidade);
    public abstract void remove(Entidade entidade, boolean condicao);
    public abstract void caminhoExiste();
    public abstract void escreveArquivo(JSONObject escreve);
    public abstract int devolveMaiorID();
    public abstract Entidade buscaID(int id);
}
