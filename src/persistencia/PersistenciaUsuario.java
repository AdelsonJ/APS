package persistencia;

import org.json.simple.JSONObject;

/* Import dos códigos existente no pacote desse programa */
import modelo.*;

public class PersistenciaUsuario implements PersistenciaDAO{
    private static PersistenciaUsuario instancia;

    // Construtor privado para evitar instanciação direta
    private PersistenciaUsuario() {}

    // Método estático para obter a instância única da classe
    public static PersistenciaUsuario getInstancia() {
        if (instancia == null) {
            instancia = new PersistenciaUsuario();
        }
        return instancia;
    }


    /* Função que insere um usuário no arquivo */
    public void insere(Entidade entidade){

    }

    /* Funcao que remove um usuario do sistema*/
    public void remove(Entidade entidade, boolean condicao){
        
    }

    /* Função que confere a existência de um caminho para a leitura do arquivo */
    public void caminhoExiste(){

    }
    
    /* Função para escrever no arquivo  */
    public void escreveArquivo(JSONObject escreve){

    }

    public int devolveMaiorID(){
        return 0;
    }

    public Usuario buscaID(int id){
        return null;
    }
}
