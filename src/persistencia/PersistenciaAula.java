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

import modelo.*;

public class PersistenciaAula implements AulaDAO{
    /* Caminho para salvar no arquivo */
    private String file = "src/arquivo/arquivoAula.json";

    protected int ANO_ATUAL = 2023;

    private static PersistenciaAula instancia;

    // Método estático para obter a instância única da classe
    public static PersistenciaAula getInstancia() {
        if (instancia == null) {
            instancia = new PersistenciaAula();
        }
        return instancia;
    }


    public void insere(Entidade entidade){

        /* Cria um conversor de JSON para texto para que seja possível escrever o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {

            /* Chama uma função que confere se o caminho existe */
            caminhoExiste();

            /* Atribui a variavel local, dias, a String dos dias da semana. Em seguida cria um vetor e 
             * para cada elemento nao vazio dessa string, adiciona no vetor */
            String[] dias = ((Aula)entidade).getDias();
            JSONArray diasArray = new JSONArray();

            for(int i = 0; i < dias.length ;i++){
                if(!(dias[i].equals("")))
                    diasArray.add(dias[i]);
            }


            /* Cria uma Hash que armazena os dados em String em um objeto */
            HashMap<String,Object> hashJSON = new HashMap<String,Object>();
            hashJSON.put("id",devolveMaiorID()+1);
            hashJSON.put("materia",((Aula)entidade).getMateria());
            hashJSON.put("capacidade", ((Aula)entidade).getCapacidade());
            hashJSON.put("duracao", ((Aula)entidade).getDuracao());
            hashJSON.put("frequencia", ((Aula)entidade).getFrequencia());
            hashJSON.put("dias", diasArray);

            hashJSON.put("professor",(((Aula)entidade).getProfessor()).getId());

            /* Se existir alguma aluno na aula, cria um vetor JSON q armazena elas e depois 
             * coloca no objeto. Em caso negativo salva um vetor vazio */
            if(((Aula) entidade).getAlunos() != null){
                Aluno[] vetor_alunos = ((Aula) entidade).getAlunos();

                JSONArray vetorAulas = new JSONArray();

                for(int j = 0; j < vetor_alunos.length ;j++)
                    vetorAulas.add(vetor_alunos[j].getId());
                
                hashJSON.put("alunos", vetorAulas);
            }
            else
                hashJSON.put("alunos", new Aluno[0]);


            if(((Aula)entidade).getId()==0)
                hashJSON.put("id", devolveMaiorID()+1);
            else
                hashJSON.put("id", ((Aula)entidade).getId());
                
            if(((Aula)entidade).getDescricao() == null)
                hashJSON.put("descricao", null);
            else
                hashJSON.put("descricao", ((Aula)entidade).getDescricao());


            /* Cria um objeto JSON que vai armazenar o objeto Hash */
            JSONObject insereObj = new JSONObject(hashJSON);

            /* Cria um objeto que armazena o objeto que contém todos os usuários do sistema */
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));

            /* Cria um vetor JSON que vai pegar o vetor contendo os usuários que já estão no arquivo,
            em seguida, adiciona o novo usuário criado */
            JSONArray vetorJSON = (JSONArray) aula.get("aula");          
            vetorJSON.add(insereObj);

            /* Cria uma Hash que armazena os dados em String em um objeto */
            HashMap<String,Object> hashGuarda = new HashMap<String,Object>();
            hashGuarda.put("aula",vetorJSON);

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

    /* Funcao que remove a aula */
    public void remove(Entidade entidade, boolean condicao){
        /* Variavel auxiliar */
        String aux;
        int aula_individual, id_professor;
        Aula[] vetor_aluno, vetor_professor;

        /* Classes usadas */
        Professor professor = new Professor();
        PersistenciaProfessor pProfessor = PersistenciaProfessor.getInstancia();
        Aluno aluno = new Aluno();
        PersistenciaAluno pAluno = PersistenciaAluno.getInstancia();

        /* Cria um conversor de JSON para texto para que seja possível escrever o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");
            
            JSONArray vetorJSONAluno;

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id que deseja excluir, exclui e depois  */
                if(Integer.parseInt(aux) == ((Aula)entidade).getId()){
                    vetorJSONAluno = (JSONArray) elemento.get("alunos");

                    /* Procura entre os alunos, aqueles que estao incritos. Quando acha, exclui a 
                     * aula do registro e salva no sistema */
                    if(vetorJSONAluno != null && condicao){
                        for(int j = 0; j < vetorJSONAluno.size(); j++){
                            aula_individual = Integer.valueOf(vetorJSONAluno.get(j).toString());
                            
                            aluno = pAluno.buscaIDParcial(aula_individual);

                            vetor_aluno = aluno.getAulaInscritas();

                            for(int k = 0; k < vetor_aluno.length;k++){
                                if(vetor_aluno[k].getId() == ((Aula)entidade).getId())
                                    vetor_aluno[k] = null;
                            }

                            aluno.setAulaInscritas(vetor_aluno);

                            pAluno.remove(aluno, false);
                            pAluno.insere(aluno);
                        }
                    }

                    /* Procurao professor. Quando acha, exclui a aula do registro e salva no sistema */
                    id_professor = Integer.valueOf(elemento.get("professor").toString());
                    if(id_professor != 0 && condicao){
                        professor = pProfessor.buscaID(id_professor);

                        vetor_professor = professor.getAulaMinistradas();

                        for(int k =0; k < vetor_professor.length;k++){
                            if(vetor_professor[k].getId() == ((Aula)entidade).getId())
                                vetor_professor[k] = null;
                        }

                        professor.setAulaMinistradas(vetor_professor);

                        pProfessor.remove(aluno, false);
                        pProfessor.insere(aluno);
                    }
                    vetorJson.remove(elemento);
                }
            }

            /* Hash que converte o texto em um objeto */
            HashMap<String,Object> hashJSON = new HashMap<String,Object>();
            hashJSON.put("aula",vetorJson);

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
                bw.write("{\"aula\": []}"); 
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
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");
            
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
    public Aula buscaID(int id){
        Aula mAula = new Aula();
        PersistenciaAluno pAluno = PersistenciaAluno.getInstancia();
        PersistenciaProfessor pProfessor = PersistenciaProfessor.getInstancia();

        /* Variavel auxiliar */
        String aux;
        Aluno[] vetor_alunos;
        String[] vetor_dias;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");
            
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

                    /* Pega o vetor dentro de dias no arquivo JSON e o copia. Caso esteja vazio retorna null */
                    JSONArray vetorJSONAluno_2 = (JSONArray) elemento.get("dias");

                    if(vetorJSONAluno_2 != null){
                        vetor_dias = new String[vetorJSONAluno_2.size()];

                        for(int k = 0; k < vetorJSONAluno_2.size();k++)
                            vetor_dias[k] = vetorJSONAluno_2.get(k).toString();
                    }
                    else
                        vetor_dias = null;

                    mAula.setMateria(elemento.get("materia").toString());
                    mAula.setCapacidade(Integer.parseInt(elemento.get("capacidade").toString()));
                    mAula.setDuracao(Integer.parseInt(elemento.get("duracao").toString()));
                    mAula.setFrequencia(Boolean.parseBoolean(elemento.get("frequencia").toString()));
                    mAula.setAlunos(vetor_alunos);
                    mAula.setProfessor(pProfessor.buscaID(Integer.parseInt(elemento.get("professor").toString())));
                    mAula.setDias(vetor_dias);
                    mAula.setId(Integer.parseInt(elemento.get("id").toString()));

                    if(elemento.get("descricao") != null)
                        mAula.setDescricao(elemento.get("descricao").toString());
                    return mAula;
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
        return mAula;
    }

    public Aula buscaIDParcial(int id){
        Aula mAula = new Aula();
        PersistenciaProfessor pProfessor = PersistenciaProfessor.getInstancia();

        /* Variavel auxiliar */
        String aux;
        Aluno[] vetor_alunos;
        String[] vetor_dias;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id no banco de dados, retorna-o */
                if(id == Integer.parseInt(aux)){
                    vetor_alunos = null;

                    /* Pega o vetor dentro de dias no arquivo JSON e o copia. Caso esteja vazio retorna null */
                    JSONArray vetorJSONAluno_2 = (JSONArray) elemento.get("dias");

                    if(vetorJSONAluno_2 != null){
                        vetor_dias = new String[vetorJSONAluno_2.size()];

                        for(int k = 0; k < vetorJSONAluno_2.size();k++)
                            vetor_dias[k] = vetorJSONAluno_2.get(k).toString();
                    }
                    else
                        vetor_dias = null;

                    mAula.setMateria(elemento.get("materia").toString());
                    mAula.setCapacidade(Integer.parseInt(elemento.get("capacidade").toString()));
                    mAula.setDuracao(Integer.parseInt(elemento.get("duracao").toString()));
                    mAula.setFrequencia(Boolean.parseBoolean(elemento.get("frequencia").toString()));
                    mAula.setAlunos(vetor_alunos);
                    mAula.setProfessor(pProfessor.buscaIDParcial(Integer.parseInt(elemento.get("professor").toString())));
                    mAula.setDias(vetor_dias);
                    mAula.setId(Integer.parseInt(elemento.get("id").toString()));

                    if(elemento.get("descricao") != null)
                        mAula.setDescricao(elemento.get("descricao").toString());
                    return mAula;
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
        return mAula;
    }
    
    /* Funcao que retorna uma String com os elementos JSON */
    public Object[][] textoTabelas(){
        /* Classes que sao usadas no programa */
        PersistenciaProfessor pProfessor = PersistenciaProfessor.getInstancia();
        Professor professor = new Professor();

        /* Variavel auxiliar */
        String id_professor;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");

            /* Cria um objeto que vai guardar os dados dos elementos no JSON */
            Object[][] objeto = new String[vetorJson.size()][6];

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                id_professor = elemento.get("professor").toString();

                professor = pProfessor.buscaID(Integer.parseInt(id_professor));

                objeto[i][0] = elemento.get("id").toString();
                objeto[i][1] = elemento.get("materia").toString();
                objeto[i][2] = professor.getNome();
                objeto[i][3] = elemento.get("capacidade").toString();
                objeto[i][4] = elemento.get("duracao").toString();
                objeto[i][5] = elemento.get("frequencia").toString();
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

    /* Funcao que retorna uma String com os elementos JSON */
    public Object[][] aulasProfessor(Entidade  entidade){
        /* Variavel auxiliar */
        String id_professor;
        int contador = 0;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");

            /* Cria um objeto que vai guardar os dados dos elementos no JSON */
            Object[][] objeto = new String[vetorJson.size()][6];

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id do professor daquele elemento para String */

                id_professor = elemento.get("professor").toString();
                if(Integer.parseInt(id_professor) == ((Professor)entidade).getId()){
                    objeto[contador][0] = elemento.get("id").toString();
                    objeto[contador][1] = elemento.get("materia").toString();
                    objeto[contador][2] = elemento.get("capacidade").toString();
                    contador ++;
                }
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

    /* Texto de alunos para tabelas */
    public Object[][] textoAlunos(Entidade entidade){

        /* Classes usadas */
        Aluno aluno = new Aluno();
        PersistenciaAluno pAluno = PersistenciaAluno.getInstancia();

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");

            /* Cria um objeto que vai guardar os dados dos elementos no JSON */
            Object[][] objeto = new Object[vetorJson.size()][3];

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){

                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                if(((Aula)entidade).getId() == Integer.valueOf(elemento.get("id").toString())){

                    JSONArray vetorAux = (JSONArray) elemento.get("alunos");

                    for(int j =0 ; j < vetorAux.size();j++){
                        if(Integer.valueOf(vetorAux.get(j).toString())!=0){
                            aluno = pAluno.buscaID(Integer.valueOf(vetorAux.get(j).toString()));

                            objeto[j][0] = aluno.getId();
                            objeto[j][1] = aluno.getNome();
                            objeto[j][2] = ANO_ATUAL-aluno.getAnoNasc();
                        }
                    }
                }
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
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);
    
                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();
    
                /* Se achar o id no banco de dados, retorna-o */
                if(((Aula)entidade).getId() == Integer.parseInt(aux)){
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

    public int jaInscrito(Entidade entidade, int id_aluno){
        String aux;
        int[] vetor_alunos = null; // Inicializa o vetor aqui
    
        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject aula = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) aula.get("aula");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);
    
                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();
    
                /* Se achar o id no banco de dados, retorna-o */
                if(((Aula)entidade).getId() == Integer.parseInt(aux)){
                    /* Pega o vetor dentro de alunos no arquivo JSON e o copia.*/
                    JSONArray vetorJSONAluno = (JSONArray) elemento.get("alunos");
    
                    /* Confere se ele nao eh null e compara o numero de alunos com id diferente de 0 */
                    if(vetorJSONAluno != null){
                        vetor_alunos = new int[vetorJSONAluno.size()];
                        for(int j = 0; j < vetorJSONAluno.size();j++){
                            if(vetorJSONAluno.get(j) != null)
                                if(Integer.parseInt(vetorJSONAluno.get(j).toString()) == id_aluno){
                                    System.out.println(Integer.parseInt(vetorJSONAluno.get(j).toString()) + "==" + id_aluno);
                                    return 1;
                                }
                        }
                        return 0;
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
