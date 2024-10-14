package persistencia;


/* Import das bibliotecas necessárias para implementar o json */
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/* Import dos códigos existente no pacote desse programa */
import modelo.*;

public class PersistenciaTarefa implements TarefasDAO{

    /* Caminho para salvar no arquivo */
    private String file = "src/arquivo/arquivoTarefas.json";


    private static PersistenciaTarefa instancia;

    // Construtor privado para evitar instanciação direta
    private PersistenciaTarefa() {}

    // Método estático para obter a instância única da classe
    public static PersistenciaTarefa getInstancia() {
        if (instancia == null) {
            instancia = new PersistenciaTarefa();
        }
        return instancia;
    }

    /* Função que insere um usuário no arquivo */
    public void insere(Entidade entidade){
        /* Cria um conversor de JSON para texto para que seja possível escrever o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {

            /* Chama uma função que confere se o caminho existe */
            caminhoExiste();

            /* Cria uma Hash que armazena os dados em String em um objeto */
            HashMap<String,Object> hashJSON = new HashMap<String,Object>();
            hashJSON.put("id", ((Tarefa)entidade).getId());
            hashJSON.put("dia", ((Tarefa)entidade).getDia());
            hashJSON.put("mes", ((Tarefa)entidade).getMes());
            hashJSON.put("ano", ((Tarefa)entidade).getAno());
            hashJSON.put("dataLimite", ((Tarefa)entidade).getPrazo());
            hashJSON.put("descricao", ((Tarefa)entidade).getDescricao());
            hashJSON.put("titulo", ((Tarefa)entidade).getTitulo());
            hashJSON.put("autor", (((Tarefa)entidade).getAutor()).getId());
            hashJSON.put("aula", ((Tarefa)entidade).getAula().getId());

            if(((Tarefa) entidade).getEntregas() != null){
                Aluno[] vetor_alunos = ((Tarefa) entidade).getEntregas();

                JSONArray vetorAulas = new JSONArray();

                for(int j = 0; j < vetor_alunos.length ;j++)
                    vetorAulas.add(vetor_alunos[j].getId());
                
                hashJSON.put("alunos", vetorAulas);
            }
            else
                hashJSON.put("alunos", null);


            if(((Tarefa)entidade).getId()==0)
                hashJSON.put("id", devolveMaiorID()+1);
            else
                hashJSON.put("id", ((Tarefa)entidade).getId());
            
            /* Cria um objeto JSON que vai armazenar o objeto Hash */
            JSONObject insereObj = new JSONObject(hashJSON);

            /* Cria um objeto que armazena o objeto que contém todos os usuários do sistema */
            JSONObject Tarefas = (JSONObject) conversorJson.parse(new FileReader(file));

            /* Cria um vetor JSON que vai pegar o vetor contendo os usuários que já estão no arquivo,
            em seguida, adiciona o novo usuário criado */
            JSONArray vetorJSON = (JSONArray) Tarefas.get("tarefas");          
            vetorJSON.add(insereObj);

            /* Armazena numa objeto da hash a string */
            HashMap<String,Object> hashGuarda = new HashMap<String,Object>();
            hashGuarda.put("tarefas",vetorJSON);

            /* Cria um objeto que irá armazenar o objeto da hash */
            JSONObject guarda = new JSONObject(hashGuarda);

            /* Chama a função que escreve no arquivo */
            escreveArquivo(guarda);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    /* Funcao que remove um usuario do sistema*/
    public void remove(Entidade entidade, boolean condicao){
        /* Variavel auxiliar */
        String aux;

        /* Cria um conversor de JSON para texto para que seja possível escrever o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject Tarefas = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) Tarefas.get("tarefa");

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id que deseja excluir, exclui e depois  */
                if(Integer.parseInt(aux)== ((Tarefa)entidade).getId())
                    vetorJson.remove(elemento);
            }

            /* Hash que converte o texto em um objeto */
            HashMap<String,Object> hashJSON = new HashMap<String,Object>();
            hashJSON.put("Tarefa",vetorJson);

            /* Cria um objeto JSON que irá armazenar o objeto gerado pela hash */
            JSONObject guarda = new JSONObject(hashJSON);

            /* Chama a função que escreve no arquivo */
            escreveArquivo(guarda);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }


    /* Função que confere a existência de um caminho para a leitura do arquivo */
    public void caminhoExiste(){
        /* Variaveis que auxiliam na verificação do caminho*/
        Path caminho = Paths.get(file);
        boolean naoExiste = Files.notExists(caminho);

        /* Confere se o caminho existe e, caso não exista, cria um */
        if(naoExiste){
            try{
                BufferedWriter bw = new BufferedWriter(
                    new FileWriter(file));    
                bw.write("{\"tarefas\": []}"); 
                bw.close();
            }
            catch(Exception ex){
                System.out.println("\nNao foi possivel escrever no arquivo\n");
                return; 
            }
        }
    }


    /* Função para escrever no arquivo  */
    public void escreveArquivo(JSONObject escreve){
        try{
            BufferedWriter bw = new BufferedWriter(
                new FileWriter(file));    
            bw.write(escreve.toString()); 
            bw.close();
        }
        catch(Exception ex){
            return; 
        }
    }
    
    /* Função que percorre os elementos de um arquivo e devolve o maior ID entre eles */
    public int devolveMaiorID(){
        int maior = 0;
        String aux;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject Tarefas = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) Tarefas.get("tarefas");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se o id que deste elemento for maior que o até então maior id,
                 * este passa a ser o novo maior id */
                if(Integer.parseInt(aux) > maior)
                    maior = Integer.parseInt(aux);
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        /* Retorna o maior id */
        return maior;
    }

    /* Funcao que busca um id no banco de dados e retorna o usuario*/
    public Tarefa buscaID(int id){
        Tarefa mTarefas = new Tarefa();
        PersistenciaAluno pAluno = PersistenciaAluno.getInstancia();
        PersistenciaProfessor pProfessor = PersistenciaProfessor.getInstancia();
        PersistenciaAula pAula = PersistenciaAula.getInstancia();

        /* Variavel auxiliar */
        String aux;
        Aluno[] vetor_alunos;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject Tarefas = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) Tarefas.get("tarefas");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id no banco de dados, retorna-o */
                if(id == Integer.parseInt(aux)){
                    /* Pega o vetor dentro de alunos no arquivo JSON e o copia. Caso esteja vazio retorna null */
                    JSONArray vetorJSONAluno = (JSONArray) elemento.get("alunos");

                    if(vetorJSONAluno != null){
                        vetor_alunos = new Aluno[vetorJSONAluno.size()];

                        for(int j = 0; j < vetorJSONAluno.size();j++)
                            vetor_alunos[j] = pAluno.buscaIDParcial(Integer.valueOf(vetorJSONAluno.get(j).toString()));
                    }
                    else
                        vetor_alunos = null;

                    mTarefas.setDescricao(elemento.get("descricao").toString());
                    mTarefas.setTitulo(elemento.get("titulo").toString());
                    mTarefas.setDia(Integer.parseInt(elemento.get("dia").toString()));
                    mTarefas.setMes(Integer.parseInt(elemento.get("mes").toString()));
                    mTarefas.setAno(Integer.parseInt(elemento.get("ano").toString()));
                    mTarefas.setId(Integer.parseInt(elemento.get("id").toString()));
                    mTarefas.setPrazo((elemento.get("dataLimite").toString()));
                    mTarefas.setAutor(pProfessor.buscaIDParcial(Integer.parseInt(elemento.get("autor").toString())));
                    mTarefas.setAula(pAula.buscaIDParcial(Integer.parseInt(elemento.get("aula").toString())));
                    return mTarefas;
                }
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        /* Retorna 0 caso não encontre o id */;
        return mTarefas;
    }

    /* Texto de alunos para tabelas */
    public Object[][] textoTabelas(){
        int numero;
        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject Tarefas = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) Tarefas.get("tarefas");

            /* Cria um objeto que vai guardar os dados dos elementos no JSON */
            Object[][] objeto = new Object[vetorJson.size()][4];

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);
                numero = numeroAlunos(buscaID(Integer.parseInt(elemento.get("id").toString())));

                objeto[i][0] = elemento.get("id").toString();
                objeto[i][1] = elemento.get("titulo").toString();
                objeto[i][2] = numero + "";
                objeto[i][3] = elemento.get("dataLimite").toString();
            }
            
            return objeto;
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        /* Retorna o maior id */
        return null;
    }

    public int numeroAlunos(Entidade entidade){
        /* Variavel auxiliar */
        String aux;
        int[] vetor_alunos = null; // Inicializa o vetor aqui
        int contador = 0;
    
        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject tarefa = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) tarefa.get("tarefas");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);
    
                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();
    
                /* Se achar o id no banco de dados, retorna-o */
                if(((Tarefa)entidade).getId() == Integer.parseInt(aux)){
                    /* Pega o vetor dentro de alunos no arquivo JSON e o copia.*/
                    JSONArray vetorJSONAluno = (JSONArray) elemento.get("alunos");
    
                    /* Confere se ele nao eh null e compara o numero de alunos com id diferente de 0 */
                    if(vetorJSONAluno != null){
                        vetor_alunos = new int[vetorJSONAluno.size()];
                        for(int j = 0; j < vetorJSONAluno.size();j++){
                            if(vetorJSONAluno.get(j) != null && Integer.parseInt(vetorJSONAluno.get(j).toString()) != 0)
                                contador++;
                        }
                        return contador;
                    }
                }
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    
        /* Retorna 0 caso não encontre o id ou caso nao tenha alunos na aula*/;
        return 0;
    }
}