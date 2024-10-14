package modelo;

public class Usuario extends Entidade {
    /* Atributos da classe Usuario */
    protected String nome;
    protected String sobrenome;
    protected String email;
    protected int diaNasc;
    protected int mesNasc;
    protected int anoNasc;
    protected int id;
    protected String senha;

    /* Construtor simples para a classe Usuario */ 
    public Usuario(){
        super();
        this.nome = null;
        this.sobrenome = null;
        this.email = null;
        this.diaNasc = 0;
        this.mesNasc = 0;
        this.anoNasc = 0;
        this.id = 0;
        this.senha = null;
    }

    /* Construtor com os valores para a classe Cliente */ 
    public Usuario(String nome, String sobrenome, String email, int diaNasc, int mesNasc, int anoNasc, int id, String senha){
        super();
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.diaNasc = diaNasc;
        this.mesNasc = mesNasc;
        this.anoNasc = anoNasc;
        this.id = id;
        this.senha = senha;
    }

    /* Getters */
    public String getNome(){
        return this.nome;
    }
    public String getSobrenome(){
        return this.sobrenome;
    }
    public String getEmail(){
        return this.email;
    }
    public int getDiaNasc(){
        return this.diaNasc;
    }
    public int getMesNasc(){
        return this.mesNasc;
    }
    public int getAnoNasc(){
        return this.anoNasc;
    }
    public int getId(){
        return this.id;
    }
    public String getSenha(){
        return this.senha;
    }

    /* Setters */
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setSobrenome(String sobrenome){
        this.sobrenome= sobrenome;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setDiaNasc(int diaNasc){
        this.diaNasc = diaNasc;
    }
    public void setMesNasc(int mesNasc){
        this.mesNasc = mesNasc;
    }
    public void setAnoNasc(int anoNasc){
        this.anoNasc = anoNasc;
    }
    public void setId(int id){
        this.id= id;
    }
    public void setSenha(String senha){
        this.senha = senha;
    }

    /* Retorna o texto contendo todos os elementos (normalmente usada para testes) */
    @Override
    public String toString(){
        String texto = "Nome: "+ nome + "\nSobrenome" + sobrenome +"\nEmail" + email +
        "\nDia do Nascimento: " + diaNasc + "\nMes do Nascimento: " + mesNasc + 
        "\nAno do Nascimento: " + anoNasc + "\nUsername: " + id+ "\nSenha: " + senha;
        return super.toString() + texto;
    }
}

