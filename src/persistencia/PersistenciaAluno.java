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

public class PersistenciaAluno implements AlunoDAO{
    /* Caminho para salvar no arquivo */
    private String file = "src/arquivo/arquivoAluno.json";

    /* Variaveis a serem usadas */
    protected Aula vetor_aulas[];
       
    private static PersistenciaAluno instancia;

    // Método estático para obter a instância única da classe
    public static PersistenciaAluno getInstancia() {
        if (instancia == null) {
            instancia = new PersistenciaAluno();
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
            hashJSON.put("nome", ((Aluno)entidade).getNome());
            hashJSON.put("sobrenome", ((Aluno)entidade).getSobrenome());
            hashJSON.put("email", ((Aluno)entidade).getEmail());
            hashJSON.put("diaNasc", ((Aluno)entidade).getDiaNasc());
            hashJSON.put("mesNasc", ((Aluno)entidade).getMesNasc());
            hashJSON.put("anoNasc", ((Aluno)entidade).getAnoNasc());
            hashJSON.put("senha", ((Aluno)entidade).getSenha());


            /* Se existir alguma aula no aluno, cria um vetor JSON q armazena elas e depois 
             * coloca no objeto. Em caso negativo salva um vetor vazio */
            if(((Aluno) entidade).getAulaInscritas() != null){
                vetor_aulas = ((Aluno) entidade).getAulaInscritas();

                JSONArray vetorAulas = new JSONArray();

                for(int j = 0; j < vetor_aulas.length ;j++){
                    vetorAulas.add(vetor_aulas[j].getId());
                }
                
                hashJSON.put("aulas", vetorAulas);
            }
            else
                hashJSON.put("aulas", null);


            if(((Aluno)entidade).getId()==0)
                hashJSON.put("id", devolveMaiorID()+1);
            else
                hashJSON.put("id", ((Aluno)entidade).getId());
            
            /* Cria um objeto JSON que vai armazenar o objeto Hash */
            JSONObject insereObj = new JSONObject(hashJSON);

            /* Cria um objeto que armazena o objeto que contém todos os usuários do sistema */
            JSONObject aluno = (JSONObject) conversorJson.parse(new FileReader(file));

            /* Cria um vetor JSON que vai pegar o vetor contendo os usuários que já estão no arquivo,
            em seguida, adiciona o novo usuário criado */
            JSONArray vetorJSON = (JSONArray) aluno.get("aluno");          
            vetorJSON.add(insereObj);

            /* Armazena numa objeto da hash a string */
            HashMap<String,Object> hashGuarda = new HashMap<String,Object>();
            hashGuarda.put("aluno",vetorJSON);

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
        int aula_individual;
        Aluno[] vetor_aux;

        /* Classes usadas */
        Aula aula = new Aula();
        PersistenciaAula pAula = PersistenciaAula.getInstancia();

        /* Cria um conversor de JSON para texto para que seja possível escrever o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aluno = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aluno.get("aluno");
            
            JSONArray vetorJSONAux;

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id que deseja excluir, exclui e depois  */
                if(Integer.parseInt(aux)== ((Aluno)entidade).getId()){
                    vetorJSONAux = (JSONArray) elemento.get("aulas");
                    
                    if(vetorJSONAux != null && condicao){
                        for(int j = 0; j < vetorJSONAux.size(); j++){
                            aula_individual = Integer.valueOf(vetorJSONAux.get(j).toString());
                            
                            if(aula_individual != 0 ){
                                aula = pAula.buscaID(aula_individual);

                                vetor_aux = aula.getAlunos();

                                for(int k = 0; k < vetor_aux.length;k++){
                                    if(vetor_aux[k].getId() == ((Aluno)entidade).getId())
                                        vetor_aux[k] = null;
                                }

                                aula.setAlunos(vetor_aux);

                                pAula.remove(aula, false);
                                pAula.insere(aula);
                            }
                        }
                    }

                    vetorJson.remove(elemento);
                }
            }

            /* Hash que converte o texto em um objeto */
            HashMap<String,Object> hashJSON = new HashMap<String,Object>();
            hashJSON.put("aluno",vetorJson);

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
                bw.write("{\"aluno\": []}"); 
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
            JSONObject aluno = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aluno.get("aluno");
            
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
    public Aluno buscaID(int id){
        Aluno mAluno = new Aluno();
        PersistenciaAula pAula = PersistenciaAula.getInstancia();

        /* Variavel auxiliar */
        String aux;
        Aula[] vetor_aux;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aluno = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aluno.get("aluno");
            
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

                    mAluno.setNome(elemento.get("nome").toString());
                    mAluno.setSobrenome(elemento.get("sobrenome").toString());
                    mAluno.setEmail(elemento.get("email").toString());
                    mAluno.setDiaNasc(Integer.parseInt(elemento.get("diaNasc").toString()));
                    mAluno.setMesNasc(Integer.parseInt(elemento.get("mesNasc").toString()));
                    mAluno.setAnoNasc(Integer.parseInt(elemento.get("anoNasc").toString()));
                    mAluno.setAulaInscritas(vetor_aux);
                    mAluno.setId(Integer.parseInt(elemento.get("id").toString()));
                    mAluno.setSenha(elemento.get("senha").toString());
                    return mAluno;
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
        return mAluno;
    }

    /* Funcao que busca um id no banco de dados e retorna o usuario*/
    public Aluno buscaIDParcial(int id){
        Aluno mAluno = new Aluno();

        /* Variavel auxiliar */
        String aux;
        Aula[] vetor_aux;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aluno = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aluno.get("aluno");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id no banco de dados, retorna-o */
                if(id == Integer.parseInt(aux)){
                
                    vetor_aux = null;

                    mAluno.setNome(elemento.get("nome").toString());
                    mAluno.setSobrenome(elemento.get("sobrenome").toString());
                    mAluno.setEmail(elemento.get("email").toString());
                    mAluno.setDiaNasc(Integer.parseInt(elemento.get("diaNasc").toString()));
                    mAluno.setMesNasc(Integer.parseInt(elemento.get("mesNasc").toString()));
                    mAluno.setAnoNasc(Integer.parseInt(elemento.get("anoNasc").toString()));
                    mAluno.setAulaInscritas(vetor_aux);
                    mAluno.setId(Integer.parseInt(elemento.get("id").toString()));
                    mAluno.setSenha(elemento.get("senha").toString());
                    return mAluno;
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
        return mAluno;
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
            JSONObject aluno = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aluno.get("aluno");
            
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

    /* Texto de alunos para tabelas */
    public Object[][] textoAlunos(Entidade entidade){

        /* Classes usadas */
        Aula aula = new Aula();
        PersistenciaAula pAula = PersistenciaAula.getInstancia();

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aluno = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aluno.get("aluno");

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){

                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                if(((Aluno)entidade).getId() == Integer.valueOf(elemento.get("id").toString())){

                    JSONArray vetorAux = (JSONArray) elemento.get("aulas");

                    if(vetorAux == null)
                        return null;

                    /* Cria um objeto que vai guardar os dados dos elementos no JSON */
                    Object[][] objeto = new Object[vetorAux.size()][3];


                    for(int j =0 ; j < vetorAux.size();j++){
                        if(Integer.valueOf(vetorAux.get(j).toString())!=0){
                            aula = pAula.buscaID(Integer.valueOf(vetorAux.get(j).toString()));

                            objeto[j][0] = aula.getId();
                            objeto[j][1] = aula.getMateria();
                            objeto[j][2] = aula.getCapacidade();
                        }
                    }
                    return objeto;
                }
            }
            
            return null;
            
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

}
