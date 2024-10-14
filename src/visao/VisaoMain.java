package visao;

/* Bibliotecas que serão necessárias*/
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import controle.*;
import modelo.*;
import persistencia.*;
import strategies.*;

public class VisaoMain extends JFrame {

    EstrategiasOpBasicas e1,e3;
    EstrategiaTabelas e2;

    private Usuario usuario;
    private Aula aula;
    private Aluno aluno;
    private Professor professor;
    private Noticias noticias;
    private Mensagem mensagem;
    private Tarefa tarefa;

    private VisaoUsuario vUsuario;
    private VisaoAula vAula;
    private VisaoAluno vAluno;
    private VisaoProfessor vProfessor;
    private VisaoNoticias vNoticias;
    private VisaoMensagem vMensagem;
    private VisaoTarefa vTarefa;

    /* Cria um atributo do tipo Controle para acessar as funções */
    private ControleUsuario cUsuario;
    private ControleAula cAula;
    private ControleAluno cAluno;
    private ControleProfessor cProfessor;
    private ControleNoticias cNoticias;
    private ControleMensagem cMensagem;
    private ControleTarefa cTarefa;

    /* Atributo que vai guardar a única instância da interface */
    //private static VisaoMain uniqueInstance;

    /* Paineis */
    JPanel jpanel_cabecalho = new JPanel();
    JPanel jpanel_fundo = new JPanel();
    JPanel jpanel_aulas = new JPanel();
    JPanel jpanel_noticias = new JPanel();

    /* Botões */
    JButton bt_login = new JButton("LOGIN");
    JButton bt_juntese = new JButton("JUNTE-SE");
    JButton bt_aula = new JButton("AULA");
    JButton bt_noticias = new JButton("NOTICIAS");
    JButton bt_projeto = new JButton("PROJETO");

    /* Labels */
    JLabel label_bem_vindos = new JLabel("SEJAM BEM-VINDES");
    JLabel label_aulas = new JLabel("CONHECA AS AULAS");
    JLabel label_noticias = new JLabel("CONFIRA AS  NOVIDADES");
    JLabel label_imagem = new JLabel("CONFIRA AS  NOVIDADES");

    /* Text Areas */
    JTextArea tArea_aulas = new JTextArea();
    JTextArea tArea_noticias = new JTextArea();

    /* Fonte e Cores */
    Font texto_padrao = new Font("ARIAL",Font.BOLD,12);
    Font texto_sub_titulo = new Font("ARIAL",Font.BOLD,15);
    Font texto_titulo = new Font("ARIAL",Font.BOLD,30);
    Color cor_fundo = new Color(194,255,240);
    Color cor_cabecalho = new Color(0,204,155);

    /* Imagens */
    ImageIcon imagem_login = new ImageIcon("src/imagens/aula.jpg");

    /* Contrução do JFrame que será usado */
    public VisaoMain(){
        setSize(750,600);
        setTitle("TP Analise e Projeto de Software");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /* Cria uma instancia única para essa interface (Padrao Singleton) */
    /*public static VisaoMain getInstance(){
		if(uniqueInstance == null)
			uniqueInstance = new VisaoMain();
		return uniqueInstance;
	}    */


    /* Interface do cabecalho */
    public void cabecalho(){
        /* Botões */
        bt_login.setFont(texto_padrao);
        bt_login.setBounds(520,30,100,40);
        bt_login.setBackground(Color.white);
		bt_login.setForeground(Color.black);
        bt_login.addActionListener(this::login);

        bt_juntese.setFont(texto_padrao);
        bt_juntese.setBounds(625,30,100,40);
        bt_juntese.setBackground(Color.white);
		bt_juntese.setForeground(Color.black);
        bt_juntese.addActionListener(this::cadastro);

        bt_projeto.setFont(texto_titulo);
        bt_projeto.setBounds(20, 30,200,50);
        bt_projeto.setBackground(cor_cabecalho);
		bt_projeto.setForeground(Color.black);
        bt_projeto.setBorderPainted(false);

        /* Paineis */
        jpanel_fundo.setLayout(null);
        jpanel_fundo.setBackground(cor_fundo);
        jpanel_fundo.setVisible(true);
        jpanel_fundo.setSize(750, 600);
        jpanel_fundo.setLocation(0, 0);

        jpanel_cabecalho.setLayout(null);
        jpanel_cabecalho.setBackground(cor_cabecalho);
        jpanel_cabecalho.setVisible(true);
        jpanel_cabecalho.setSize(750, 100);
        jpanel_cabecalho.setLocation(0, 0);

        /* Adiciona os elementos no cabecalho */
        jpanel_cabecalho.add(bt_login);
        jpanel_cabecalho.add(bt_juntese);
        jpanel_cabecalho.add(bt_projeto);
        
        add(jpanel_cabecalho);
        add(jpanel_fundo);
;
    }
    
    public void menu(){
        setVisible(true);

        /* instancia ControleAula e Usuario */
        e1 = new Estrategia1(PersistenciaUsuario.getInstancia());
        e3 = new Estrategia1(AulaDAO.getInstancia());
        e2 = new Estrategia2();

        this.cUsuario = new ControleUsuario(e1);
        this.cAula = new ControleAula(e3,e2);

        /* BOTOES */
        bt_aula.setFont(texto_padrao);
        bt_aula.setBounds(75, 210,100,30);
        bt_aula.setBackground(cor_cabecalho);
		bt_aula.setForeground(Color.white);
		bt_aula.setBorderPainted(false);
        bt_aula.addActionListener(this::aula);

        bt_noticias.setFont(texto_padrao);
        bt_noticias.setBounds(75, 210,100,30);
        bt_noticias.setBackground(cor_cabecalho);
		bt_noticias.setForeground(Color.white);
		bt_noticias.setBorderPainted(false);
        bt_noticias.addActionListener(this::noticias);

        /* LABELS */
        label_bem_vindos.setFont(texto_titulo);
        label_bem_vindos.setBounds(207,125,335,50);

        label_aulas.setFont(texto_sub_titulo);
        label_aulas.setBounds(40,20,170,20);

        label_noticias.setFont(texto_sub_titulo);
        label_noticias.setBounds(19,20,215,20);

        label_imagem.setBounds(0,100,750,500);
        imagem_login.setImage(imagem_login.getImage().getScaledInstance(label_imagem.getWidth(),label_imagem.getHeight(),1));
        label_imagem.setIcon(imagem_login);

        /* Text Areas */
        tArea_aulas.setText("Temos uma grande variedade de aulas disponiveis, venha conferir, se increver e crescer com a gente");
        tArea_aulas.setFont(texto_padrao);
        tArea_aulas.setBounds(25,60,200,135);
        tArea_aulas.setLineWrap(true);
        tArea_aulas.setWrapStyleWord(true);
        tArea_aulas.setEditable(false);

        tArea_noticias.setText("Venha acompanhas as ultimas noticias e avisos da semana.");
        tArea_noticias.setFont(texto_padrao);
        tArea_noticias.setBounds(25,60,200,135);
        tArea_noticias.setLineWrap(true);
        tArea_noticias.setWrapStyleWord(true);
        tArea_noticias.setEditable(false);

        /* Paineis */
        jpanel_aulas.setLayout(null);
        jpanel_aulas.setBackground(Color.WHITE);
        jpanel_aulas.setVisible(true);
        jpanel_aulas.setSize(260, 275);
        jpanel_aulas.setLocation(83, 300);

        jpanel_noticias.setLayout(null);
        jpanel_noticias.setBackground(Color.WHITE);
        jpanel_noticias.setVisible(true);
        jpanel_noticias.setSize(250, 275);
        jpanel_noticias.setLocation(417, 300);

        jpanel_fundo.setLayout(null);
        jpanel_fundo.setBackground(cor_fundo);
        jpanel_fundo.setVisible(true);
        jpanel_fundo.setSize(750, 600);
        jpanel_fundo.setLocation(0, 0);
        
        /* Adiciona no Layout */
        jpanel_aulas.add(bt_aula);        
        jpanel_aulas.add(label_aulas);  
        jpanel_aulas.add(tArea_aulas);

        jpanel_noticias.add(bt_noticias);
        jpanel_noticias.add(label_noticias);
        jpanel_noticias.add(tArea_noticias);
        
        jpanel_fundo.add(label_bem_vindos);
        jpanel_fundo.add(jpanel_aulas);
        jpanel_fundo.add(jpanel_noticias);
        jpanel_fundo.add(label_imagem);


        /* Chama a funcao que adiciona o cabecalho */
        cabecalho();
    }

    /* Faz com que ao apertar o botao seja redirecionado para o login */
    private void login(ActionEvent actionEvent){
        /* REMOVE O LAYOUT ANTERIOR */	
        jpanel_aulas.remove(bt_aula);        
        jpanel_aulas.remove(label_aulas);  
        jpanel_aulas.remove(tArea_aulas);

        jpanel_noticias.remove(bt_noticias);
        jpanel_noticias.remove(label_noticias);
        jpanel_noticias.remove(tArea_noticias);
        
        jpanel_cabecalho.remove(bt_login);
        jpanel_cabecalho.remove(bt_juntese);
        jpanel_cabecalho.remove(bt_projeto);

        jpanel_fundo.remove(label_bem_vindos);
        jpanel_fundo.remove(label_imagem);
        jpanel_fundo.remove(jpanel_aulas);
        jpanel_fundo.remove(jpanel_noticias);

        remove(jpanel_cabecalho);
        remove(jpanel_fundo);

        /* Chama a instancia unica do VisaoUsuario e vai até ela na funcao login */
        vUsuario = new VisaoUsuario();
        vUsuario.login(cUsuario);
        dispose();
    }

    /* Faz com que ao apertar o botao seja redirecionado para o cadastro */
    private void cadastro(ActionEvent actionEvent){
        /* REMOVE O LAYOUT ANTERIOR */
        jpanel_aulas.remove(bt_aula);        
        jpanel_aulas.remove(label_aulas);  
        jpanel_aulas.remove(tArea_aulas);

        jpanel_noticias.remove(bt_noticias);
        jpanel_noticias.remove(label_noticias);
        jpanel_noticias.remove(tArea_noticias);
        
        jpanel_cabecalho.remove(bt_login);
        jpanel_cabecalho.remove(bt_juntese);
        jpanel_cabecalho.remove(bt_projeto);

        jpanel_fundo.remove(label_bem_vindos);
        jpanel_fundo.remove(label_imagem);
        jpanel_fundo.remove(jpanel_aulas);
        jpanel_fundo.remove(jpanel_noticias);

        remove(jpanel_cabecalho);
        remove(jpanel_fundo);

        vUsuario = new VisaoUsuario();
        vUsuario.cadastro(cUsuario);
        dispose();
    }

     /* Faz com que ao apertar o botao seja redirecionado para o cadastro */
    private void aula(ActionEvent actionEvent){
        this.usuario = new Usuario();

        usuario.setId(0);
        /* Chama a instancia unica do VisaoUsuario e vai até ela na funcao cadastro */
        
        e1 = new Estrategia1(AulaDAO.getInstancia());
        e2 = new Estrategia2();

        this.cAula = new ControleAula(e1,e2);
        this.vAula = new VisaoAula();
        vAula.menuAulas(cAula,usuario,0);
        dispose();
    }

    private void noticias(ActionEvent actionEvent){
        this.usuario = new Usuario();

        usuario.setId(0);

        e1 = new Estrategia1(NoticiasDAO.getInstancia());
        e2 = new Estrategia2();

        /* Chama a instancia unica do VisaoUsuario e vai até ela na funcao cadastro */
        this.cNoticias = new ControleNoticias(e1,e2);
        this.vNoticias = new VisaoNoticias();
        vNoticias.menuNoticias(cNoticias,usuario,0);
        dispose();
    }


    public void usuarioLogin(){

        e1 = new Estrategia1(PersistenciaUsuario.getInstancia());
        
        this.cUsuario = new ControleUsuario(e1);
        this.vUsuario= new VisaoUsuario();
        vUsuario.login(cUsuario);
        dispose();
    }

    public void usuarioCadastro(){

        e1 = new Estrategia1(PersistenciaUsuario.getInstancia());

        this.cUsuario = new ControleUsuario(e1);
        this.vUsuario= new VisaoUsuario();
        vUsuario.cadastro(cUsuario);
        dispose();
    }

    /* Funcao que redireciona o usuario para a sua pagina apos o login */
    public void usuarioContinuar(Boolean tipoUsuario, Entidade entidade){
        e1 = new Estrategia1(AlunoDAO.getInstancia());
        e3 = new Estrategia1(ProfessorDAO.getInstancia());

        /* Considerando true para Aluno e false para professor, uma comparacao eh feita e 
         * leva o usuario para sua pagina correspondente de acordo com seu papel */
        if(tipoUsuario){
            this.cAluno = new ControleAluno(e1);
            this.vAluno = new VisaoAluno();
            vAluno.paginaAluno(cAluno,(Aluno)entidade);
            dispose();
        }
        else{
            this.cProfessor = new ControleProfessor(e3);
            this.vProfessor = new VisaoProfessor();
            vProfessor.paginaProfessor(cProfessor,(Professor)entidade);
            dispose();
        }
    }   

    public void alunoPagina(Controle controle, Entidade entidade){
        this.cAluno = (ControleAluno)controle;
        this.aluno = (Aluno)entidade;
        this.vAluno = new VisaoAluno();
        vAluno.paginaAluno(cAluno,aluno);
        dispose();
    }

    public void alunoMensagem(Controle controle, Entidade entidade){
        this.cAluno = (ControleAluno)controle;
        this.aluno = (Aluno)entidade;
        this.vAluno = new VisaoAluno();
        vAluno.mensagemCadastro(cAluno,aluno);
        dispose();
    }

    public void professorPagina(Controle controle, Entidade entidade){
        this.cProfessor = (ControleProfessor)controle;
        this.professor = (Professor)entidade;
        this.vProfessor = new VisaoProfessor();
        vProfessor.paginaProfessor(cProfessor,professor);
        dispose();
    }

    public void professorMensagem(Controle controle, Entidade entidade){
        this.cProfessor = (ControleProfessor)controle;
        this.professor = (Professor)entidade;
        this.vProfessor = new VisaoProfessor();
        vProfessor.mensagemCadastro(cProfessor,professor);
        dispose();
    }

    public void aulaMenu(Controle controle, Entidade entidade, int tipo){
        this.cAula = (ControleAula)controle;
        this.vAula = new VisaoAula();

        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade;
                vAula.menuAulas(cAula,aluno,1);
                break;

            case 2:
                this.professor = (Professor)entidade;
                vAula.menuAulas(cAula,professor,2);
                break;
        
            default:
                vAula.menuAulas(cAula,null,0);
                break;
        }
        dispose();
    }

    public void aulaPI(Entidade entidade, Entidade entidade2, int tipo){

        e1 = new Estrategia1(AulaDAO.getInstancia());
        e2 = new Estrategia2();

        this.cAula = new ControleAula(e1,e2);
        this.aula = (Aula)entidade;
        this.vAula = new VisaoAula();
        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade2;
                vAula.paginaIndividual(cAula,aula, aluno, tipo);
                break;

            case 2:
                this.professor = (Professor)entidade2;
                vAula.paginaIndividual(cAula,aula, professor, tipo);
                break;
        
            default:
                vAula.paginaIndividual(cAula,aula, null, tipo);
                break;
        }
        dispose();
    }

    public void aulaAP(Entidade entidade, int funcao){

        e1 = new Estrategia1(AlunoDAO.getInstancia());
        e3 = new Estrategia1(ProfessorDAO.getInstancia());

        if(funcao == 1){   
            this.cAluno = new ControleAluno(e1);
            this.aluno = (Aluno)entidade;
            this.vAluno = new VisaoAluno();
            vAluno.paginaAluno(cAluno,aluno);
            dispose();
        }
        else{
            this.cProfessor = new ControleProfessor(e3);
            this.professor = (Professor)entidade;
            this.vProfessor = new VisaoProfessor();
            vProfessor.paginaProfessor(cProfessor,professor);
            dispose();
        }
    }

    public void aulaTarefa(Entidade entidade, Entidade entidade2, int tipo){
        e1 = new Estrategia1(AulaDAO.getInstancia());
        e2 = new Estrategia2();

        this.cAula = new ControleAula(e1,e2);
        this.aula = (Aula)entidade;
        this.vAula = new VisaoAula();
        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade2;
                vAula.tarefas(cAula,aula, aluno, tipo);
                break;

            case 2:
                this.professor = (Professor)entidade2;
                vAula.tarefas(cAula,aula, professor, tipo);
                break;
        
            default:
                vAula.tarefas(cAula,aula, null, tipo);
                break;
        }
        dispose();
    }

    public void noticiasMenu(Controle controle, Entidade entidade, int tipo){
        this.cNoticias = (ControleNoticias)controle;
        this.vNoticias = new VisaoNoticias();

        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade;
                vNoticias.menuNoticias(cNoticias,aluno,1);
                break;

            case 2:
                this.professor = (Professor)entidade;
                vNoticias.menuNoticias(cNoticias,professor,2);
                break;
        
            default:
                vNoticias.menuNoticias(cNoticias,null,0);
                break;
        }
        dispose();
    }

    public void noticiasPI(Entidade entidade, Entidade entidade2, int tipo){

        e1 = new Estrategia1(NoticiasDAO.getInstancia());
        e2 = new Estrategia2();

        this.cNoticias = new ControleNoticias(e1,e2);
        this.noticias = (Noticias)entidade;
        this.vNoticias = new VisaoNoticias();
        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade2;
                vNoticias.paginaIndividual(cNoticias,noticias, aluno, tipo);
                break;

            case 2:
                this.professor = (Professor)entidade2;
                vNoticias.paginaIndividual(cNoticias,noticias, professor, tipo);
                break;
        
            default:
                vNoticias.paginaIndividual(cNoticias,noticias, null, tipo);
                break;
        }
        dispose();
    }

    public void noticiasAlterar(Entidade entidade, Entidade entidade2){
        e1 = new Estrategia1(NoticiasDAO.getInstancia());
        e2 = new Estrategia2();

        this.cNoticias = new ControleNoticias(e1,e2);
        this.noticias = (Noticias)entidade;
        this.vNoticias = new VisaoNoticias();
        this.professor = (Professor)entidade2;
        vNoticias.alterar(cNoticias,noticias, professor);
        dispose();
    }

    public void mensagemMenu(Controle controle, Entidade entidade, int tipo){
        this.cMensagem = (ControleMensagem)controle;
        this.vMensagem = new VisaoMensagem();

        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade;
                vMensagem.menuMensagens(cMensagem,aluno,1);
                break;

            case 2:
                this.professor = (Professor)entidade;
                vMensagem.menuMensagens(cMensagem,professor,2);
                break;
        
            default:
                vMensagem.menuMensagens(cMensagem,null,0);
                break;
        }
        dispose();
    }

    public void mensagemPI(Entidade entidade, Entidade entidade2, int tipo){
        e1 = new Estrategia1(MensagemDAO.getInstancia());

        this.mensagem = (Mensagem)entidade; 
        this.cMensagem = new ControleMensagem(e1);
        this.vMensagem = new VisaoMensagem();

        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade2;
                vMensagem.paginaIndividual(cMensagem, mensagem, aluno,tipo);
                break;

            case 2:
                this.professor = (Professor)entidade2;
                vMensagem.paginaIndividual(cMensagem, mensagem, professor,tipo);
                break;
        
            default:
                vMensagem.paginaIndividual(cMensagem, mensagem, null,tipo);
                break;
        }
        dispose();
    }

    public void tarefaMenu(Controle controle, Entidade entidade, Entidade entidade2, int tipo){
        aula = (Aula)entidade;
        this.cTarefa = (ControleTarefa)controle;
        this.vTarefa = new VisaoTarefa();

        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade2;
                vTarefa.menuTarefas(cTarefa,aula,aluno,1);
                break;

            case 2:
                this.professor = (Professor)entidade;
                vTarefa.menuTarefas(cTarefa,aula,professor,2);
                break;
        
            default:
                vTarefa.menuTarefas(cTarefa,aula,null,0);
                break;
        }
        dispose();
    }

    public void tarefaPI(Entidade entidade, Entidade entidade2, Entidade entidade3, int tipo){
        e1 = new Estrategia1(TarefasDAO.getInstancia());
        e2 = new Estrategia2();

        this.tarefa = (Tarefa)entidade;
        this.aula = (Aula)entidade2;
        this.cTarefa = new ControleTarefa(e1,e2);
        this.vTarefa = new VisaoTarefa();
        switch (tipo) {
            case 1:
                this.aluno = (Aluno)entidade3;
                vTarefa.paginaIndividual(cTarefa,tarefa,aula, aluno, tipo);
                break;

            case 2:
                this.professor = (Professor)entidade3;
                vTarefa.paginaIndividual(cTarefa,tarefa,aula, professor, tipo);
                break;
        
            default:
                vTarefa.paginaIndividual(cTarefa,tarefa,aula, null, tipo);
                break;
        }
        dispose();
    }
}