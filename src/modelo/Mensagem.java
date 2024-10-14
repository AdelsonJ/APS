package modelo;

public class Mensagem extends Entidade{
    protected String titulo;
    protected String data;
    protected String texto;
    protected Usuario remetente;
    protected Usuario destinatario;
    protected int tipoR;
    protected int tipoD;
    protected int id;

    public Mensagem(){
        super();
        this.titulo = null;
        this.data = null;
        this.texto = null;
        this.destinatario = null;
        this.remetente = null;
        this.tipoD = 0;
        this.tipoR = 0;
        this.id = 0;
    }

    public Mensagem(String titulo, String data, String texto, Usuario remetente, Usuario destinatario, int id, int tipoR, int tipoD){
        super();
        this.titulo = titulo;
        this.data = data;
        this.texto = texto;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.tipoD = tipoD;
        this.tipoR = tipoR;
        this.id = id;
    }

    /* Getters */
    public String getTitulo(){
        return this.titulo;
    }

    public String getTexto(){
        return this.texto;
    }
    public String getData(){
        return this.data;
    }
    public Usuario getRemetente(){
        return this.remetente;
    }
    public Usuario getDestinatario(){
        return this.destinatario;
    }
    public int getId(){
        return this.id;
    }
    public int getTR(){
        return this.tipoR;
    }
    public int getTD(){
        return this.tipoD;
    }

    /* Setters */
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public void setTexto(String texto){
        this.texto = texto;
    }
    public void setData(String data){
        this.data = data;
    }
    public void setRemet(Usuario remet){
        this.remetente = remet;
    }
    public void setDest(Usuario dest){
        this.destinatario = dest;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setTR(int tipoR){
        this.tipoR = tipoR;
    }
    public void setTD(int tipoD){
        this.tipoD = tipoD;
    }

}

