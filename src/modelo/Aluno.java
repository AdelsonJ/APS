package modelo;

public class Aluno extends Usuario {
    Aula[] idAulasInscritas;

    /* Cria um novo Aluno com os dados em branco */
    public Aluno(){
        super();
        this.idAulasInscritas = null;
    }

    /* Contrutor para o Aluno, preenchendo os dados */
    public Aluno(String nome, String sobrenome, String email, int diaNasc, int mesNasc, int anoNasc, int id, String senha, Aula[] idAulasInscritas){
        super(nome, sobrenome, email, diaNasc, mesNasc, anoNasc, id, senha);
        this.idAulasInscritas = idAulasInscritas;
    }

    /* Getters */
    public Aula[] getAulaInscritas(){
        return this.idAulasInscritas;
    }

    /* Setters */
    public void setAulaInscritas(Aula[] idAulasInscritas){
        this.idAulasInscritas = idAulasInscritas;
    }

}
