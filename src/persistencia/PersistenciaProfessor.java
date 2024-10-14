package persistencia;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import modelo.Entidade;
import modelo.Professor;
import modelo.Aula;

public class PersistenciaProfessor implements ProfessorDAO{
    /* Caminho para salvar no arquivo */
    private String file = "src/arquivo/arquivoProfessor.json";

    /* Variaveis auxiliares */
    Aula vetor_aulas[];


    private static PersistenciaProfessor instancia;

    // Construtor privado para evitar instanciação direta
    private PersistenciaProfessor() {}

    // Método estático para obter a instância única da classe
    public static PersistenciaProfessor getInstancia() {
        if (instancia == null) {
            instancia = new PersistenciaProfessor();
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
            hashJSON.put("nome", ((Professor)entidade).getNome());
            hashJSON.put("sobrenome", ((Professor)entidade).getSobrenome());
            hashJSON.put("email", ((Professor)entidade).getEmail());
            hashJSON.put("diaNasc", ((Professor)entidade).getDiaNasc());
            hashJSON.put("mesNasc", ((Professor)entidade).getMesNasc());
            hashJSON.put("anoNasc", ((Professor)entidade).getAnoNasc());
            hashJSON.put("senha", ((Professor)entidade).getSenha());
            
            /* Se existir alguma aula no professor, cria um vetor JSON q armazena elas e depois 
             * coloca no objeto. Em caso negativo salva um vetor vazio */
            if(((Professor) entidade).getAulaMinistradas() != null){
                vetor_aulas = ((Professor) entidade).getAulaMinistradas();
                JSONArray vetorAulas = new JSONArray();

                for(int j = 0; j < vetor_aulas.length; j++) {
                    if (vetor_aulas[j] != null)
                        vetorAulas.add(vetor_aulas[j].getId());
                }
                hashJSON.put("aulas", vetorAulas);
            }
            else
                hashJSON.put("aulas", null);

            if(((Professor)entidade).getId()==0)
                hashJSON.put("id", devolveMaiorID()+1);
            else
                hashJSON.put("id", ((Professor)entidade).getId());


            /* Cria um objeto JSON que vai armazenar o objeto Hash */
            JSONObject insereObj = new JSONObject(hashJSON);

            /* Cria um objeto que armazena o objeto que contém todos os usuários do sistema */
            JSONObject professor = (JSONObject) conversorJson.parse(new FileReader(file));

            /* Cria um vetor JSON que vai pegar o vetor contendo os usuários que já estão no arquivo,
            em seguida, adiciona o novo usuário criado */
            JSONArray vetorJSON = (JSONArray) professor.get("professor");          
            vetorJSON.add(insereObj);

            /* Armazena numa objeto da hash a string */
            HashMap<String,Object> hashGuarda = new HashMap<String,Object>();
            hashGuarda.put("professor",vetorJSON);

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
        /* Variaveis auxiliares */
        String aux_id;
        int aulaIndiv;

        /* Classes usadas */
        Aula aula = new Aula();
        PersistenciaAula pAula = PersistenciaAula.getInstancia();

        /* Cria um conversor de JSON para texto para que seja possível escrever o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject professor = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) professor.get("professor");

            JSONArray vetorJSONAux;
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux_id = elemento.get("id").toString();
                
                /* Se achar o id que deseja excluir, exclui e depois  */
                if(Integer.parseInt(aux_id) == ((Professor)entidade).getId()){
                    vetorJSONAux = (JSONArray) elemento.get("aulas");

                    if(vetorJSONAux != null && condicao){
                        for(int j = 0;j < vetorJSONAux.size(); j++){
                            aulaIndiv = Integer.valueOf(vetorJSONAux.get(j).toString());
                            aula = pAula.buscaID(aulaIndiv);
                            pAula.remove(aula, true);
                        }
                    }
                    vetorJson.remove(elemento);
                }
            }

             /* Hash que converte o texto em um objeto */
             HashMap<String,Object> hashJSON = new HashMap<String,Object>();
             hashJSON.put("professor",vetorJson);
 
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
                bw.write("{\"professor\": []}"); 
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
            caminhoExiste();
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject professor = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) professor.get("professor");
            
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
    public Professor buscaID(int id){
        PersistenciaAula pAula = PersistenciaAula.getInstancia();
        Professor mProfessor = new Professor();

        /* Variavel auxiliar */
        String aux;
        Aula[] vetor_aux;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject professor = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) professor.get("professor");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id no banco de dados, retorna-o */
                if(id == Integer.parseInt(aux)){

                    JSONArray vetor_aulas =(JSONArray) elemento.get("aulas");

                    /* Se o vetor de aulas estiver vazio retorna null, senao retorna o vetor */
                    if(vetor_aulas != null){
                        vetor_aux = new Aula[vetor_aulas.size()];
                        
                        for(int j=0; j< vetor_aulas.size();j++)
                            vetor_aux[j]= pAula.buscaIDParcial(Integer.valueOf(vetor_aulas.get(j).toString()));
                    }
                    else
                        vetor_aux = null;

                    mProfessor.setNome(elemento.get("nome").toString());
                    mProfessor.setSobrenome(elemento.get("sobrenome").toString());
                    mProfessor.setEmail(elemento.get("email").toString());
                    mProfessor.setDiaNasc(Integer.parseInt(elemento.get("diaNasc").toString()));
                    mProfessor.setMesNasc(Integer.parseInt(elemento.get("mesNasc").toString()));
                    mProfessor.setAnoNasc(Integer.parseInt(elemento.get("anoNasc").toString()));
                    mProfessor.setAulaMinistradas(vetor_aux);
                    mProfessor.setId(Integer.parseInt(elemento.get("id").toString()));
                    mProfessor.setSenha(elemento.get("senha").toString());
                    return mProfessor;
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
        return mProfessor;
    }

    /* Funcao que busca um id no banco de dados e retorna o usuario*/
    public Professor buscaIDParcial(int id){
        Professor mProfessor = new Professor();

        /* Variavel auxiliar */
        String aux;
        Aula[] vetor_aux;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject professor = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) professor.get("professor");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id no banco de dados, retorna-o */
                if(id == Integer.parseInt(aux)){

                    vetor_aux = null;

                    mProfessor.setNome(elemento.get("nome").toString());
                    mProfessor.setSobrenome(elemento.get("sobrenome").toString());
                    mProfessor.setEmail(elemento.get("email").toString());
                    mProfessor.setDiaNasc(Integer.parseInt(elemento.get("diaNasc").toString()));
                    mProfessor.setMesNasc(Integer.parseInt(elemento.get("mesNasc").toString()));
                    mProfessor.setAnoNasc(Integer.parseInt(elemento.get("anoNasc").toString()));
                    mProfessor.setAulaMinistradas(vetor_aux);
                    mProfessor.setId(Integer.parseInt(elemento.get("id").toString()));
                    mProfessor.setSenha(elemento.get("senha").toString());
                    return mProfessor;
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
        return mProfessor;
    }

    /* Funcao que retorna o id em caso de perda */
    public int devolveIdPerdido(String email, int dia, int mes, int ano){
        /* Variaveis auxiliares */
        String id_perdido;
        String aux_email, aux_dia, aux_mes, aux_ano;
        boolean condicao;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject professor = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) professor.get("professor");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux_email = elemento.get("email").toString();
                aux_dia = elemento.get("diaNasc").toString();
                aux_mes = elemento.get("mesNasc").toString();
                aux_ano = elemento.get("anoNasc").toString();
                id_perdido = elemento.get("id").toString();

                /* Escreve a condicao do if */
                condicao = email.equals(aux_email) && dia == Integer.parseInt(aux_dia) && mes == Integer.parseInt(aux_mes) && ano == Integer.parseInt(aux_ano);

                /* Se os dados do email e data de nascimento baterem, retorna o id do elemento */
                if(condicao)
                    return Integer.parseInt(id_perdido);
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {

            e.printStackTrace();
        }

        /* Retorna 0 caso não encontre o id */;
        return 0;
    }
}

