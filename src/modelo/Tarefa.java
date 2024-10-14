package modelo;

public class Tarefa extends Entidade{
    protected int id;
    protected int dia;
    protected int mes;
    protected int ano;
    protected String prazo;
    protected String descricao;
    protected String titulo;
    protected Professor autor;
    protected Aluno[] entregues;
    protected Aula materia;

    public Tarefa(){
        this.id = 0;
        this.dia = 0;
        this.mes = 0;
        this.ano = 0;
        this.descricao = null;
        this.titulo = null;
        this.autor = null;
        this.entregues = null;
    }

    public Tarefa(int id, int dia, int mes, int ano,String prazo, String descricao, String titulo, Professor autor, Aula aula, Aluno[] entregues){
        this.id = 0;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.prazo = prazo;
        this.descricao = descricao;
        this.titulo = titulo;
        this.autor = autor;
        this.materia = aula;
    }

    /* Getters */
    public int getId(){
        return this.id;
    }
    public int getDia(){
        return this.dia;
    }
    public int getMes(){
        return this.mes;
    }
    public int getAno(){
        return this.ano;
    }
    public String getPrazo(){
        return this.prazo;
    }
    public String getDescricao(){
        return this.descricao;
    }
    public String getTitulo(){
        return this.titulo;
    }
    public Professor getAutor(){
        return this.autor;
    }
    public Aula getAula(){
        return this.materia;
    }
    public Aluno[] getEntregas(){
        return this.entregues;
    }
    
    /* Setters */
    public void setId(int id){
        this.id= id;
    }
    public void setDia(int dia){
        this.dia = dia;
    }
    public void setMes(int mes){
        this.mes = mes;
    }
    public void setAno(int ano){
        this.ano = ano;
    }
    public void setPrazo(String ano){
        this.prazo = ano;
    }
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
    public void setAutor(Professor autor){
        this.autor = autor;
    }
    public void setAula(Aula autor){
        this.materia = autor;
    }
    public void setEntregas(Aluno[] entregues){
        this.entregues = entregues;
    }
}
