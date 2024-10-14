package modelo;

public class Noticias extends Entidade{
    protected int id;
    protected int dia;
    protected int mes;
    protected int ano;
    protected String descricao;
    protected String titulo;
    protected Professor autor;

    public Noticias(){
        this.id = 0;
        this.dia = 0;
        this.mes = 0;
        this.ano = 0;
        this.descricao = null;
        this.titulo = null;
        this.autor = null;
    }

    public Noticias(int id, int dia, int mes, int ano, String descricao, String titulo, Professor autor){
        this.id = 0;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.descricao = descricao;
        this.titulo = titulo;
        this.autor = autor;
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
    public String getDescricao(){
        return this.descricao;
    }
    public String getTitulo(){
        return this.titulo;
    }
    public Professor getAutor(){
        return this.autor;
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
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
    public void setAutor(Professor autor){
        this.autor = autor;
    }

}
