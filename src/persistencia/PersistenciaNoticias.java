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


public class PersistenciaNoticias implements NoticiasDAO {
    /* Caminho para salvar no arquivo */
    private String file = "src/arquivo/arquivoNoticias.json";
    

    private static PersistenciaNoticias instancia;

    // Construtor privado para evitar instanciação direta
    private PersistenciaNoticias() {}

    // Método estático para obter a instância única da classe
    public static PersistenciaNoticias getInstancia() {
        if (instancia == null) {
            instancia = new PersistenciaNoticias();
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
            hashJSON.put("dia", ((Noticias)entidade).getDia());
            hashJSON.put("mes", ((Noticias)entidade).getMes());
            hashJSON.put("ano", ((Noticias)entidade).getAno());
            hashJSON.put("descricao", ((Noticias)entidade).getDescricao());
            hashJSON.put("titulo", ((Noticias)entidade).getTitulo());
            hashJSON.put("autor", (((Noticias)entidade).getAutor()).getId());

            if(((Noticias)entidade).getId()==0)
                hashJSON.put("id", devolveMaiorID()+1);
            else
                hashJSON.put("id", ((Noticias)entidade).getId());
            
            /* Cria um objeto JSON que vai armazenar o objeto Hash */
            JSONObject insereObj = new JSONObject(hashJSON);

            /* Cria um objeto que armazena o objeto que contém todos os usuários do sistema */
            JSONObject noticias = (JSONObject) conversorJson.parse(new FileReader(file));

            /* Cria um vetor JSON que vai pegar o vetor contendo os usuários que já estão no arquivo,
            em seguida, adiciona o novo usuário criado */
            JSONArray vetorJSON = (JSONArray) noticias.get("noticias");          
            vetorJSON.add(insereObj);

            /* Armazena numa objeto da hash a string */
            HashMap<String,Object> hashGuarda = new HashMap<String,Object>();
            hashGuarda.put("noticias",vetorJSON);

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
            JSONObject noticias = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) noticias.get("noticias");

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id que deseja excluir, exclui e depois  */
                if(Integer.parseInt(aux)== ((Noticias)entidade).getId())
                    vetorJson.remove(elemento);
            }

            /* Hash que converte o texto em um objeto */
            HashMap<String,Object> hashJSON = new HashMap<String,Object>();
            hashJSON.put("noticias",vetorJson);

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
                bw.write("{\"noticias\": []}"); 
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
            JSONObject noticias = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) noticias.get("noticias");
            
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
    public Noticias buscaID(int id){
        Noticias mNoticias = new Noticias();
        PersistenciaProfessor pProfessor = PersistenciaProfessor.getInstancia();

        /* Variavel auxiliar */
        String aux;

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject noticias = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) noticias.get("noticias");
            
            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                /* Converte o id daquele elemento para String */
                aux = elemento.get("id").toString();

                /* Se achar o id no banco de dados, retorna-o */
                if(id == Integer.parseInt(aux)){
                    mNoticias.setDescricao(elemento.get("descricao").toString());
                    mNoticias.setTitulo(elemento.get("titulo").toString());
                    mNoticias.setDia(Integer.parseInt(elemento.get("dia").toString()));
                    mNoticias.setMes(Integer.parseInt(elemento.get("mes").toString()));
                    mNoticias.setAno(Integer.parseInt(elemento.get("ano").toString()));
                    mNoticias.setId(Integer.parseInt(elemento.get("id").toString()));
                    mNoticias.setAutor(pProfessor.buscaIDParcial(Integer.parseInt(elemento.get("autor").toString())));
                    return mNoticias;
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
        return mNoticias;
    }

    /* Texto de noticias para tabelas */
    public Object[][] textoTabelas(){
        String aux = " ";

        /* Cria um conversor de JSON para texto para que seja possível percorrer o arquivo */
        JSONParser conversorJson = new JSONParser();
        try {
            caminhoExiste();
            
            /* Converte os elementos no arquivo para um objeto JSON*/
            JSONObject noticias = (JSONObject) conversorJson.parse(new FileReader(file));
            
            /* Pega o vetor dentro do objeto JSON e o guarda em um vetor JSON */
            JSONArray vetorJson = (JSONArray) noticias.get("noticias");

            /* Cria um objeto que vai guardar os dados dos elementos no JSON */
            Object[][] objeto = new Object[vetorJson.size()][4];

            /* Loop for que percorre os elementos do vetor até o seu fim */
            for (int i = 0; i < vetorJson.size() ; i++){
                /* Cria um objeto para aquele elemento que será analisado */
                JSONObject elemento = (JSONObject) vetorJson.get(i);

                if(Integer.parseInt(elemento.get("dia").toString()) < 10)
                    aux = "0";
                aux = aux + elemento.get("dia").toString() + "/";
    
                if(Integer.parseInt(elemento.get("mes").toString()) < 10)
                    aux = aux + "0";
                aux = aux + elemento.get("mes").toString() + "/" + elemento.get("ano").toString();


                objeto[i][0] = elemento.get("id").toString();
                objeto[i][1] = elemento.get("titulo").toString();
                objeto[i][2] = elemento.get("descricao").toString();
                objeto[i][3] = aux;
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

}
