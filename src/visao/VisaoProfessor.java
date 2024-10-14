package visao;

/* Bibliotecas que serão necessárias*/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.*;

import controle.Controle;
import controle.ControleAluno;
import controle.ControleAula;
import controle.ControleMensagem;
import controle.ControleNoticias;
import controle.ControleProfessor;
import modelo.Entidade;
import modelo.Mensagem;
import modelo.Noticias;
import modelo.Aluno;
import modelo.Aula;
import modelo.Professor;
import persistencia.AlunoDAO;
import persistencia.AulaDAO;
import persistencia.MensagemDAO;
import persistencia.NoticiasDAO;
import strategies.Estrategia1;
import strategies.Estrategia2;
import strategies.EstrategiaTabelas;
import strategies.EstrategiasOpBasicas;

public class VisaoProfessor extends JFrame {
    /* Atributo que vai guardar a única instância da interface */
    //private static VisaoProfessor uniqueInstance;
    
    EstrategiasOpBasicas e1,e3;
    EstrategiaTabelas e2;

    /* Classes usadas */
    private ControleAluno cAluno;
    private Aula aula;
    private ControleAula cAula;
    private Professor professor = new Professor();
    private ControleProfessor cProfessor;
    private Noticias noticias;
    private ControleNoticias cNoticias;
    private Mensagem mensagem;
    private ControleMensagem cMensagem;
    private VisaoMain vMain;

    /* Variaveis auxiliares */
    protected int tamanho_vetor_aula;
    protected String nome, sobrenome, email, dia, mes, ano , senha, cSenha, materia, capacidade, duracao, descricao, titulo, data, texto, destinatario, papel, buscar;
    protected boolean condicao_alteracao, condicao_cadastro, frequencia;
    protected String[] dias = new String[7];
    protected String[] colunas = {"ID", "Materia", "Capacidade"};
    protected String[] botoes = { "Sim", "Nao" };
    protected String[] papelVetor = {"","Aluno","Professor"};

    /* Paineis */
    JPanel jpanel_cabecalho = new JPanel();
    JPanel jpanel_fundo = new JPanel();
    JPanel jpanel_dados = new JPanel();
    JPanel jpanel_aulas = new JPanel();
    JPanel jpanel_noticias = new JPanel();
    JPanel jpanel_mensagem = new JPanel();

    /* Botões */
    JButton bt_projeto = new JButton("PROJETO");
    JButton bt_aulas = new JButton("AULAS");
    JButton bt_noticias = new JButton("NOTICIAS");
    JButton bt_professor = new JButton("");
    JButton bt_sair = new JButton("SAIR");
    JButton bt_alterar = new JButton("ALTERAR DADOS");
    JButton bt_excluir = new JButton("EXCLUIR");
    JButton bt_confirmar = new JButton("CONFIRMAR");
    JButton bt_criar_aula = new JButton("CRIAR AULA");
    JButton bt_criar_noticias = new JButton("CRIAR NOTICIAS");
    JButton bt_continuar_cadastro = new JButton("CRIAR AULA");
    JButton bt_continuar_noticias = new JButton("CRIAR NOTICIAS");
    JButton bt_mensagem = new JButton("NOVA MENSAGEM");
    JButton bt_mensagem_menu = new JButton("MENSAGENS");
    JButton bt_continuar_mensagem = new JButton("CONTINUAR");
    JButton bt_buscar = new JButton("BUSCAR");

    /* Labels */
    JLabel label_alterar = new JLabel("ALTERAR DADOS");
    JLabel label_dados = new JLabel("DADOS PESSOAIS ");
    JLabel label_imagem_login = new JLabel("CRIAR NOVA AULA");
    JLabel label_criar_aula = new JLabel("CRIAR NOVA AULA");
    JLabel label_criar_noticias = new JLabel("CRIAR NOVA NOTICIA");
    JLabel label_nome = new JLabel();
    JLabel label_sobrenome = new JLabel("SOBRENOME: ");
    JLabel label_email = new JLabel();
    JLabel label_dataNasc = new JLabel();
    JLabel label_senha = new JLabel("SENHA:");
    JLabel label_cSenha = new JLabel("CONFIRMAR SENHA: ");
    JLabel label_materia = new JLabel("MATERIA: ");
    JLabel label_capacidade = new JLabel("CAPACIDADE: ");
    JLabel label_duracao = new JLabel("DURACAO: ");
    JLabel label_frequencia = new JLabel("FREQUENTE: ");
    JLabel label_dias = new JLabel("DIAS DA SEMANA: ");
    JLabel label_minutos = new JLabel("minutos");
    JLabel label_descricao = new JLabel("DESCRICAO: ");
    JLabel label_titulo = new JLabel("TITULO: ");
    JLabel label_texto = new JLabel("TEXTO: ");
    JLabel label_destinatario = new JLabel("DEST.: ");
    JLabel label_criar_mensagem = new JLabel("NOVA MENSAGEM");
    JLabel label_papel = new JLabel("FUNCAO: ");

    /* TextAreas */
    JTextArea tArea_nome = new JTextArea();
    JTextArea tArea_sobrenome = new JTextArea();
    JTextArea tArea_email = new JTextArea();
    JTextArea tArea_senha = new JTextArea();
    JTextArea tArea_cSenha = new JTextArea();
    JTextArea tArea_materia = new JTextArea();
    JTextArea tArea_capacidade = new JTextArea();
    JTextArea tArea_duracao = new JTextArea();
    JTextArea tArea_descricao = new JTextArea();
    JTextArea tArea_titulo = new JTextArea();
    JTextArea tArea_texto = new JTextArea();
    JTextArea tArea_destinatario = new JTextArea();
    JTextArea tArea_buscar = new JTextArea();

    /*ComboBoxes */
    JComboBox <String> cbox_dia = new JComboBox<>(preencheVetor(32, 1, true));
    JComboBox <String> cbox_mes = new JComboBox<>(preencheVetor(13, 1, true));
    JComboBox <String> cbox_ano = new JComboBox<>(preencheVetor(151, 1874, false));
    JComboBox <String> cbox_papel = new JComboBox <String> (papelVetor);
    
    /* CheckBoxes */
    JCheckBox check_frequencia = new JCheckBox("SIM");
    JCheckBox check_segunda = new JCheckBox("SEG");
    JCheckBox check_terca = new JCheckBox("TER");
    JCheckBox check_quarta = new JCheckBox("QUA");
    JCheckBox check_quinta = new JCheckBox("QUI");
    JCheckBox check_sexta = new JCheckBox("SEX");
    JCheckBox check_sabado = new JCheckBox("SAB");
    JCheckBox check_domingo = new JCheckBox("DOM");

    /* Tabelas */
    JTable tabela_aulas;

    /* ScrollPane */
    JScrollPane jScroll_aulas;

    /* Fonte e Cores */
    Font texto_padrao = new Font("ARIAL",Font.BOLD,12);
    Font texto_titulo = new Font("ARIAL",Font.BOLD,30);
    Font texto_sub_titulo = new Font("ARIAL",Font.BOLD,20);
    Color cor_fundo = new Color(194,255,240);
    Color cor_cabecalho = new Color(0,204,155);
    
    /* Imagens */
    ImageIcon imagem_login = new ImageIcon("src/imagens/login.png");

    /* Contrução do JFrame que será usado */
    public VisaoProfessor(){
        setSize(750,600);
        setTitle("TP Analise e Projeto de Software");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /* Cria uma instancia única para essa interface (Padrao Singleton) */
    /* public static VisaoProfessor getInstance(){
		if(uniqueInstance == null)
			uniqueInstance = new VisaoProfessor();
		return uniqueInstance;
	} */


    /* Interface do cabecalho */
    public void cabecalho(){
        /* Botões */
        bt_aulas.setFont(texto_padrao);
        bt_aulas.setBounds(240,30,125,40);
        bt_aulas.setBackground(Color.white);
		bt_aulas.setForeground(Color.black);
        bt_aulas.addActionListener(this::irPAula);

        bt_noticias.setFont(texto_padrao);
        bt_noticias.setBounds(370,30,125,40);
        bt_noticias.setBackground(Color.white);
		bt_noticias.setForeground(Color.black);
        bt_noticias.addActionListener(this::noticias);

        bt_sair.setFont(texto_padrao);
        bt_sair.setBounds(575,30,125,40);
        bt_sair.setBackground(Color.white);
		bt_sair.setForeground(Color.black);
        bt_sair.addActionListener(this::sair);

        bt_projeto.setFont(texto_titulo);
        bt_projeto.setBounds(20, 30,200,50);
        bt_projeto.setBackground(cor_cabecalho);
		bt_projeto.setForeground(Color.black);
        bt_projeto.setBorderPainted(false);
        bt_projeto.addActionListener(this::projeto);

        /* Paineis */
        jpanel_cabecalho.setLayout(null);
        jpanel_cabecalho.setBackground(cor_cabecalho);
        jpanel_cabecalho.setSize(750, 100);
        jpanel_cabecalho.setLocation(0, 0);
        jpanel_cabecalho.setVisible(true);
        
        jpanel_fundo.setLayout(null);
        jpanel_fundo.setBackground(cor_fundo);
        jpanel_fundo.setSize(750, 600);
        jpanel_fundo.setLocation(0, 0);
        jpanel_fundo.setVisible(true);
        
        /* Adiciona os elementos no cabecalho, em seguida adiciona-o no fundo e adiciona o fundo */
        jpanel_cabecalho.add(bt_aulas);
        jpanel_cabecalho.add(bt_noticias);
        jpanel_cabecalho.add(bt_sair);
        jpanel_cabecalho.add(bt_projeto);
        
        add(jpanel_cabecalho);
        add(jpanel_fundo);
    }

    /* Funcao que cria uma tabela de aulas */
    public void tabelaAulasProf(){
        /* Chama a funcao que devolve um objeto contendo os dados do json */
        Object[][] objeto_tabela = cAula.aulasProfessor(professor);

        
        /* Cria uma tabela de selecao unica e nao editavel */
        tabela_aulas = new JTable(objeto_tabela, colunas){   
            public boolean isCellEditable(int row, int column) {                
                return false;               
            }
        };
        tabela_aulas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        /* Cria um novo JScrollPane e coloca a tabela nele */
        jScroll_aulas = new JScrollPane(tabela_aulas);

        jScroll_aulas.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScroll_aulas.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScroll_aulas.setBounds(385,125, 315,200);
        jScroll_aulas.getVerticalScrollBar().setValue(0);
        jScroll_aulas.setVisible(true);

        /* Adiciona o ScrollPane no painel */
        jpanel_fundo.add(jScroll_aulas);
    }


    /* Pagina do professor que leva para seus dados */
    public void paginaProfessor(Controle controle, Entidade entidade){
        setVisible(true);
        
        professor = (Professor)entidade;
        cProfessor = (ControleProfessor)controle;

        e1 = new Estrategia1(AulaDAO.getInstancia());
        e3 = new Estrategia1(NoticiasDAO.getInstancia());
        e2 = new Estrategia2();

        this.cAula = new ControleAula(e1,e2);
        this.cNoticias = new ControleNoticias(e3,e2);

        cabecalho();
        tabelaAulasProf();

        /* Nomeia o botao como o nome do professor cadastrado */
        //bt_professor.setText(((professor)entidade).getNome());

        /* Botões */
        bt_alterar.setFont(texto_padrao);
        bt_alterar.setBounds(50, 335,175,40);
        bt_alterar.setBackground(Color.white);
        bt_alterar.setForeground(Color.black);
        bt_alterar.addActionListener(this::alterar);

        bt_criar_aula.setFont(texto_padrao);
        bt_criar_aula.setBounds(442, 330,200,40);
        bt_criar_aula.setBackground(Color.white);
        bt_criar_aula.setForeground(Color.black);
        bt_criar_aula.addActionListener(this::criarAula);

        bt_criar_noticias.setFont(texto_padrao);
        bt_criar_noticias.setBounds(442,380,200,40);
        bt_criar_noticias.setBackground(Color.white);
        bt_criar_noticias.setForeground(Color.black);
        bt_criar_noticias.addActionListener(this::criarNoticias);

        bt_mensagem.setFont(texto_padrao);
        bt_mensagem.setBounds(442, 430,200,40);
        bt_mensagem.setBackground(Color.white);
        bt_mensagem.setForeground(Color.black);
        bt_mensagem.addActionListener(this::criarMensagem);

        bt_mensagem_menu.setFont(texto_padrao);
        bt_mensagem_menu.setBounds(442, 480,200,40);
        bt_mensagem_menu.setBackground(Color.white);
        bt_mensagem_menu.setForeground(Color.black);
        bt_mensagem_menu.addActionListener(this::vaiPMensagem);

        /* Labels */
        label_dados.setFont(texto_padrao);
        label_dados.setBounds(15, 150,300,40);

        label_nome.setText("NOME: " + professor.getNome() + " " +professor.getSobrenome());
        label_nome.setFont(texto_padrao);
        label_nome.setBounds(25, 200,300,40);

        label_email.setText("EMAIL: " + professor.getEmail());
        label_email.setFont(texto_padrao);
        label_email.setBounds(25, 235, 300,40);

        label_dataNasc.setText("DATA DE NASCIMENTO: " + professor.getDiaNasc() + "/" + professor.getMesNasc() + "/" + professor.getAnoNasc());
        label_dataNasc.setFont(texto_padrao);
        label_dataNasc.setBounds(25, 270,420,40);

        label_imagem_login.setBounds(100,50,85,85);
        imagem_login.setImage(imagem_login.getImage().getScaledInstance(label_imagem_login.getWidth(),label_imagem_login.getHeight(),1));
        label_imagem_login.setIcon(imagem_login);

        /* Painel */
        jpanel_dados.setLayout(null);
        jpanel_dados.setBackground(Color.WHITE);
        jpanel_dados.setVisible(true);
        jpanel_dados.setSize(285, 400);
        jpanel_dados.setLocation(50, 125);

        /* Adiciona elementos no painel */
        jpanel_dados.add(label_dados);
        jpanel_dados.add(label_nome);
        jpanel_dados.add(label_email);
        jpanel_dados.add(label_dataNasc);
        jpanel_dados.add(label_imagem_login);
        jpanel_dados.add(bt_alterar);

        jpanel_fundo.add(bt_criar_aula);
        jpanel_fundo.add(bt_criar_noticias);
        jpanel_fundo.add(bt_mensagem);
        jpanel_fundo.add(bt_mensagem_menu);
        jpanel_fundo.add(jpanel_dados);
    }


    /*Acoes dos botoes */
    /* Acao do botao alterar */
    private void alterar(ActionEvent actionEvent){
        setVisible(false);

        /* Botoes */
        bt_professor.setText(professor.getNome());
        bt_professor.setFont(texto_padrao);
        bt_professor.setBounds(575,30,125,40);
        bt_professor.setBackground(Color.white);
		bt_professor.setForeground(Color.black);

        bt_excluir.setFont(new Font("ARIAL",Font.BOLD,11));
        bt_excluir.setBounds(122, 375,125,40);
		bt_excluir.setLocation(122, 375);
        bt_excluir.setBackground(Color.white);
		bt_excluir.setForeground(Color.black);
        bt_excluir.addActionListener(this::excluir);

        bt_confirmar.setBounds(253, 375, 125,40);
		bt_confirmar.setLocation(253, 375);
        bt_confirmar.setBackground(Color.white);
		bt_confirmar.setForeground(Color.black);
        bt_confirmar.addActionListener(this::confirmar);

        /* Labels */        
        label_alterar.setFont(texto_sub_titulo);
        label_alterar.setBounds(150, 25,200,50);

        label_nome.setText("NOME: ");
        label_nome.setFont(texto_padrao);
        label_nome.setBounds(37, 110, 175,50);

        label_sobrenome.setFont(texto_padrao);
        label_sobrenome.setBounds(37, 145, 175,50);

        label_email.setText("EMAIL: ");
        label_email.setFont(texto_padrao);
        label_email.setBounds(37, 180, 175,50);

        label_dataNasc.setText("DATA DE NASCIMENTO: ");
        label_dataNasc.setFont(texto_padrao);
        label_dataNasc.setBounds(37, 215, 175,50);

        label_senha.setFont(texto_padrao);
        label_senha.setBounds(37, 250, 175,50);

        label_cSenha.setFont(texto_padrao);
        label_cSenha.setBounds(37, 285, 175,50);

        /* Caixas de texto */
        tArea_nome.setFont(texto_padrao);
        tArea_nome.setBounds(218, 125, 250,25);
        tArea_nome.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_sobrenome.setFont(texto_padrao);
        tArea_sobrenome.setBounds(218, 160, 250,25);
        tArea_sobrenome.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_email.setFont(texto_padrao);
        tArea_email.setBounds(218, 195, 250,25);
        tArea_email.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_senha.setFont(texto_padrao);
        tArea_senha.setBounds(218, 265, 250,25);
        tArea_senha.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_cSenha.setFont(texto_padrao);
        tArea_cSenha.setBounds(218, 300,250,25);
        tArea_cSenha.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        /* ComboBOXes */
        cbox_dia.setBounds(218, 230,80,25);

        cbox_mes.setBounds(303, 230,80,25);

        cbox_ano.setBounds(388, 230,80,25);

        /* Paineis */
        jpanel_dados.setSize(500, 500);
        jpanel_dados.setLocation(125, 100);

        /* Remove e adiciona elementos no painel */
        jpanel_fundo.remove(bt_criar_aula);
        jpanel_fundo.remove(bt_criar_noticias);
        jpanel_fundo.remove(bt_mensagem);
        jpanel_fundo.remove(bt_mensagem_menu);
        jpanel_fundo.remove(jScroll_aulas);

        jpanel_dados.remove(bt_alterar);
        jpanel_dados.remove(label_dados);
        jpanel_dados.remove(label_imagem_login);
        jpanel_dados.add(label_alterar);
        jpanel_dados.add(label_sobrenome);
        jpanel_dados.add(label_senha);
        jpanel_dados.add(label_cSenha);
        jpanel_dados.add(tArea_nome);
        jpanel_dados.add(tArea_sobrenome);
        jpanel_dados.add(tArea_email);
        jpanel_dados.add(tArea_senha);
        jpanel_dados.add(tArea_cSenha);
        jpanel_dados.add(cbox_dia);
        jpanel_dados.add(cbox_mes);
        jpanel_dados.add(cbox_ano);
        jpanel_dados.add(bt_excluir);
        jpanel_dados.add(bt_confirmar);

        jpanel_cabecalho.remove(bt_sair);
        jpanel_cabecalho.add(bt_professor);

        setVisible(true);
    }

    /* Acao do botao excl
    uir */
    private void excluir(ActionEvent actionEvent){
        /* Recebe as senhas */
        senha = tArea_senha.getText();
        cSenha = tArea_cSenha.getText();

        /* Confere se a senha foi digitada, se sao iguais e se corresponde a do professor. Se nao for exibe mensagem de erro*/
        if((senha.equals("") || cSenha.equals("")) || (!senha.equals(cSenha)) || (!senha.equals(professor.getSenha())))
            JOptionPane.showMessageDialog(null,"As senha nao digitadas ou divergem", "ERRO",JOptionPane.ERROR_MESSAGE);
        else{
            /* Se o vetor de aulas estiver vazio exclui, senao avisa que tem dependencia */
            if(professor.getAulaMinistradas() != null){
                int resposta = JOptionPane.showOptionDialog(null,"Ainda ha aulas que sao administradas por voce, apagar esse cadastro significa apagar elas. Deseja continuar?", "ULTIMA CHANCE",JOptionPane.WARNING_MESSAGE, 0, null,botoes,botoes[0]);
                
                /* Deixa as caixas de texto em branco */
                tArea_nome.setText("");
                tArea_nome.requestFocus();
                tArea_sobrenome.setText("");
                tArea_sobrenome.requestFocus();
                tArea_email.setText("");
                tArea_email.requestFocus();
                cbox_dia.setSelectedItem("");
                cbox_mes.setSelectedItem("");
                cbox_ano.setSelectedItem("");
                tArea_senha.setText("");
                tArea_senha.requestFocus();
                tArea_cSenha.setText("");
                tArea_cSenha.requestFocus();

                /* Remove tudo do cabecalho */
                jpanel_dados.remove(label_alterar);
                jpanel_dados.remove(label_sobrenome);
                jpanel_dados.remove(label_senha);
                jpanel_dados.remove(label_cSenha);
                jpanel_dados.remove(tArea_nome);
                jpanel_dados.remove(tArea_sobrenome);
                jpanel_dados.remove(tArea_email);
                jpanel_dados.remove(tArea_senha);
                jpanel_dados.remove(tArea_cSenha);
                jpanel_dados.remove(cbox_dia);
                jpanel_dados.remove(cbox_mes);
                jpanel_dados.remove(cbox_ano);
                jpanel_dados.remove(bt_excluir);
                jpanel_dados.remove(bt_confirmar);

                jpanel_fundo.remove(bt_criar_aula);
                jpanel_fundo.remove(bt_criar_noticias);
                jpanel_fundo.remove(bt_mensagem);
                jpanel_fundo.remove(bt_mensagem_menu);
                jpanel_fundo.remove(jScroll_aulas);
                jpanel_fundo.remove(jpanel_dados);

                jpanel_cabecalho.remove(bt_professor);

                remove(jpanel_fundo);
                remove(jpanel_cabecalho);

                /* Deixa essa tela invisivel para o usuario */
                setVisible(false);

                if(resposta == 0){
                    cProfessor.remove(professor, true);

                    /* Imprime uma mensagem de sucesso */
                    JOptionPane.showMessageDialog(null,"Seu cadastrato foi excluido", "SUCESSO",JOptionPane.INFORMATION_MESSAGE);

                    /* Leva ate o menu principal */
                    this.vMain = new VisaoMain();
                    vMain.menu();
                    dispose();
                }
                else
                    paginaProfessor(cProfessor, professor);
            }
            else{
                cProfessor.remove(professor, true);

                /* Imprime uma mensagem de sucesso */
                JOptionPane.showMessageDialog(null,"Seu cadastrato foi excluido", "SUCESSO",JOptionPane.INFORMATION_MESSAGE);

                /* Leva ate o menu principal */
                this.vMain = new VisaoMain();
                vMain.menu();
                dispose();
            }
        }
    }

    /* Acao do botao confirmar */
    private void confirmar(ActionEvent actionEvent){
        /* Atribuicao  dos valores no texto para salvar nas variaveis locais*/
        nome = tArea_nome.getText();
        sobrenome = tArea_sobrenome.getText();
        email = tArea_email.getText();
        dia = cbox_dia.getSelectedItem()+"";
        mes = cbox_mes.getSelectedItem()+"";
        ano = cbox_ano.getSelectedItem()+"";
        senha = tArea_senha.getText();
        cSenha = tArea_cSenha.getText();

        condicao_alteracao = nome.equals("") && sobrenome.equals("") && email.equals("") && dia.equals("") &&
        mes.equals("") && ano.equals("") &&  senha.equals("")&& cSenha.equals("");

        /* Se todas campos estiverem vazios nao tem como alterar, entao apenas na condicao de
         * pelo menos um nao estiver vazio, ocorre a alteracao */
        if(!condicao_alteracao){
            /* Confere se as senhas digitas sao iguais, caso nao sejam, imprime uma mensagem de erro */
            if(!(senha.equals(cSenha)))
                JOptionPane.showMessageDialog(null,"Senhas divergem", "ERRO",JOptionPane.ERROR_MESSAGE);                
            else{
                /* Salva apenas aquilo que foi escrito pelo usuario */
                if(!(nome.equals("")))
                    professor.setNome(nome);
                if(!(sobrenome.equals("")))
                    professor.setSobrenome(sobrenome);
                if(!(email.equals("")))
                    professor.setEmail(email);
                if(!(dia.equals("")))
                    professor.setDiaNasc(Integer.parseInt(dia));
                if(!(mes.equals("")))
                    professor.setMesNasc(Integer.parseInt(mes));
                if(!(ano.equals("")))
                    professor.setAnoNasc(Integer.parseInt(ano));
                if(!(senha.equals("")))
                    professor.setSenha(senha);
            }

            /* Deixa as caixas de texto em branco */
            tArea_nome.setText("");
            tArea_nome.requestFocus();
            tArea_sobrenome.setText("");
            tArea_sobrenome.requestFocus();
            tArea_email.setText("");
            tArea_email.requestFocus();
            cbox_dia.setSelectedItem("");
            cbox_mes.setSelectedItem("");
            cbox_ano.setSelectedItem("");
            tArea_senha.setText("");
            tArea_senha.requestFocus();
            tArea_cSenha.setText("");
            tArea_cSenha.requestFocus();

            /* Exclui o cadastro antigo e adiciona o novo */
            cProfessor.remove(professor,true);
            cProfessor.insere(professor);

            JOptionPane.showMessageDialog(null,"Seu cadastrato foi alterado", "SUCESSO",JOptionPane.INFORMATION_MESSAGE);
        }

        /* Remove tudo do cabecalho */
        jpanel_dados.remove(label_alterar);
        jpanel_dados.remove(label_sobrenome);
        jpanel_dados.remove(label_senha);
        jpanel_dados.remove(label_cSenha);
        jpanel_dados.remove(tArea_nome);
        jpanel_dados.remove(tArea_sobrenome);
        jpanel_dados.remove(tArea_email);
        jpanel_dados.remove(tArea_senha);
        jpanel_dados.remove(tArea_cSenha);
        jpanel_dados.remove(cbox_dia);
        jpanel_dados.remove(cbox_mes);
        jpanel_dados.remove(cbox_ano);
        jpanel_dados.remove(bt_excluir);
        jpanel_dados.remove(bt_confirmar);

        jpanel_fundo.remove(bt_criar_aula);
        jpanel_fundo.remove(bt_criar_noticias);
        jpanel_fundo.remove(bt_mensagem);
        jpanel_fundo.remove(bt_mensagem_menu);
        jpanel_fundo.remove(jpanel_dados);

        jpanel_cabecalho.remove(bt_professor);

        remove(jpanel_fundo);
        remove(jpanel_cabecalho);

        this.vMain = new VisaoMain();
        vMain.professorPagina(cProfessor,professor);
        dispose();
    }


    /* Acoes do botao criar aula */
    private void criarAula(ActionEvent actionEvent){
        setVisible(false);
        /* Exclui os elementos antigos */
        jpanel_dados.remove(label_dados);
        jpanel_dados.remove(label_imagem_login);
        jpanel_dados.remove(label_nome);
        jpanel_dados.remove(label_email);
        jpanel_dados.remove(label_dataNasc);
        jpanel_dados.remove(bt_alterar);

        jpanel_fundo.remove(jpanel_dados);
        jpanel_fundo.remove(jScroll_aulas);
        jpanel_fundo.remove(bt_criar_aula);
        jpanel_fundo.remove(bt_criar_noticias);
        jpanel_fundo.remove(bt_mensagem);
        jpanel_fundo.remove(bt_mensagem_menu);

        /* Botões */
        bt_continuar_cadastro.setFont(texto_padrao);
        bt_continuar_cadastro.setBounds(187, 385, 125,40);
        bt_continuar_cadastro.setBackground(Color.white);
		bt_continuar_cadastro.setForeground(Color.black);
        bt_continuar_cadastro.addActionListener(this::continuarCadastro);

        /* Labels */
        label_criar_aula.setFont(texto_sub_titulo);
        label_criar_aula.setBounds(150, 25, 200,50);

        label_materia.setFont(texto_padrao);
        label_materia.setBounds(60, 140, 125,50);

        label_capacidade.setFont(texto_padrao);
        label_capacidade.setBounds(60, 175, 125,50);

        label_frequencia.setFont(texto_padrao);
        label_frequencia.setBounds(60, 210,125,50);

        label_duracao.setFont(texto_padrao);
        label_duracao.setBounds(60, 245,125,50);

        label_dias.setFont(texto_padrao);
        label_dias.setBounds(60, 280,125,50);

        label_minutos.setFont(texto_padrao);
        label_minutos.setBounds(295, 245,100,50);


        /* Caixas de texto */
        tArea_materia.setFont(texto_padrao);
        tArea_materia.setBounds(190, 150,250,25);
        tArea_materia.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_capacidade.setFont(texto_padrao);
        tArea_capacidade.setBounds(190, 185,250,25);
        tArea_capacidade.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_duracao.setFont(texto_padrao);
        tArea_duracao.setBounds(190, 255,100,25);
        tArea_duracao.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        /* CheckBOXes */
        check_frequencia.setBounds(190, 220, 100, 25);

        check_segunda.setBounds(190, 290, 60, 25);

        check_terca.setBounds(255, 290, 60, 25);

        check_quarta.setBounds(320, 290, 60, 25);

        check_quinta.setBounds(385, 290, 60, 25);

        check_sexta.setBounds(190, 315, 60, 25);

        check_sabado.setBounds(255, 315, 60, 25);
        
        check_domingo.setBounds(320, 315, 60, 25);

        /* Painel */   
        jpanel_aulas.setLayout(null);
        jpanel_aulas.setBackground(Color.WHITE);
        jpanel_aulas.setSize(500, 500);
        jpanel_aulas.setLocation(125, 100);
        jpanel_aulas.setVisible(true);

        /* Adiciona elementos no painel */
        jpanel_aulas.add(label_criar_aula);
        jpanel_aulas.add(label_materia);
        jpanel_aulas.add(label_capacidade);
        jpanel_aulas.add(label_frequencia);
        jpanel_aulas.add(label_duracao);
        jpanel_aulas.add(label_dias);
        jpanel_aulas.add(label_minutos);
        jpanel_aulas.add(tArea_materia);
        jpanel_aulas.add(tArea_capacidade);
        jpanel_aulas.add(tArea_duracao);
        jpanel_aulas.add(check_frequencia);
        jpanel_aulas.add(check_segunda);
        jpanel_aulas.add(check_terca);
        jpanel_aulas.add(check_quarta);
        jpanel_aulas.add(check_quinta);
        jpanel_aulas.add(check_sexta);
        jpanel_aulas.add(check_sabado);
        jpanel_aulas.add(check_domingo);
        jpanel_aulas.add(bt_continuar_cadastro);

        jpanel_fundo.add(jpanel_aulas);

        setVisible(true);
    }

    private void continuarCadastro(ActionEvent actionEvent){

        /* Atribuicao  dos valores no texto para salvar nas variaveis locais*/
        materia = tArea_materia.getText();
        capacidade = tArea_capacidade.getText();
        duracao =  tArea_duracao.getText();

        /* Expressão para saber se ao menos um dia foi selecionado */
        condicao_cadastro = check_segunda.isSelected() || check_terca.isSelected() || check_quarta.isSelected() 
        || check_quinta.isSelected() || check_sexta.isSelected() || check_sabado.isSelected() || check_domingo.isSelected();

        /* Se nenhum dia foi selecionado exibe uma mensagem de erro */
        if(!condicao_cadastro)
            JOptionPane.showMessageDialog(null,"Nenhum dia selecionado", "ERRO",JOptionPane.ERROR_MESSAGE);
        else{
            /* Se uma das caixas de texto estiver em branco, exibe mensagem de erro */
            if(materia.equals("") || capacidade.equals("") && duracao.equals(""))
                JOptionPane.showMessageDialog(null,"Informação faltando", "ERRO",JOptionPane.ERROR_MESSAGE);
            else{
                /* Atribui um valor pra frequencia de acordo com o item selecionado */
                if(check_frequencia.isSelected())
                    frequencia = true;
                else
                    frequencia = false;

                /* Escreve os dias da semana selecionados*/            
                if(check_segunda.isSelected())
                    dias[0]= "SEGUNDA";
                else
                    dias[0]= "";
                if(check_terca.isSelected())
                    dias[1]= "TERCA";
                else
                    dias[1]= "";
                if(check_quarta.isSelected())
                    dias[2]= "QUARTA";
                else
                    dias[2]= "";
                if(check_quinta.isSelected())
                    dias[3]= "QUINTA";
                else
                    dias[3]= "";
                if(check_sexta.isSelected())
                    dias[4]= "SEXTA";
                else
                    dias[4]= "";
                if(check_sabado.isSelected())
                    dias[5]= "SABADO";
                else
                    dias[5]= "";
                if(check_domingo.isSelected())
                    dias[6]= "DOMINGO";
                else
                    dias[6]= "";

                aula = new Aula(cAula.devolveMaiorID()+1, materia, null,Integer.parseInt(capacidade), new Aluno[0], professor, Integer.parseInt(duracao), frequencia, dias);
                
                Aula[] aulas_ministradas;

                if (professor.getAulaMinistradas() == null) {
                    aulas_ministradas = new Aula[1];
                    aulas_ministradas[0] = aula;
                } 
                else {
                    Aula[] aux_professor = professor.getAulaMinistradas();
                    aulas_ministradas = Arrays.copyOf(aux_professor, aux_professor.length + 1);
                    aulas_ministradas[aulas_ministradas.length - 1] = aula;
                }

                professor.setAulaMinistradas(aulas_ministradas);

                cProfessor.remove(professor,false);
                cProfessor.insere(professor);

                /* Insere a aula no sistema */
                cAula.insere(aula);

                /* Uma mensagem de sucesso aparece e apresenta o id do usuário */
                JOptionPane.showMessageDialog(null,"Parabéns, sua aula foi cadastrada com sucesso.", "SUCESSO",JOptionPane.INFORMATION_MESSAGE);
                
                this.vMain = new VisaoMain();
                vMain.professorPagina(cProfessor,professor);
                dispose();
            }
        }
    }

    private void criarNoticias(ActionEvent actionEvent){
        setVisible(false);
        /* Exclui os elementos antigos */
        jpanel_dados.remove(label_dados);
        jpanel_dados.remove(label_imagem_login);
        jpanel_dados.remove(label_nome);
        jpanel_dados.remove(label_email);
        jpanel_dados.remove(label_dataNasc);
        jpanel_dados.remove(bt_alterar);

        jpanel_fundo.remove(jpanel_dados);
        jpanel_fundo.remove(jScroll_aulas);
        jpanel_fundo.remove(bt_criar_aula);
        jpanel_fundo.remove(bt_criar_noticias);
        jpanel_fundo.remove(bt_mensagem);
        jpanel_fundo.remove(bt_mensagem_menu);

        /* Botões */
        bt_continuar_noticias.setFont(texto_padrao);
        bt_continuar_noticias.setBounds(180, 385, 150,40);
        bt_continuar_noticias.setBackground(Color.white);
		bt_continuar_noticias.setForeground(Color.black);
        bt_continuar_noticias.addActionListener(this::continuarNoticias);

        /* Labels */
        label_criar_noticias.setFont(texto_sub_titulo);
        label_criar_noticias.setBounds(125, 25, 250,50);

        label_titulo.setFont(texto_padrao);
        label_titulo.setBounds(60, 100, 125,50);

        label_dias.setFont(texto_padrao);
        label_dias.setBounds(60, 135,125,50);

        label_descricao.setFont(texto_padrao);
        label_descricao.setBounds(60, 170, 125,50);


        /* Caixas de texto */
        tArea_titulo.setFont(texto_padrao);
        tArea_titulo.setBounds(190, 110,250,25);
        tArea_titulo.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_descricao.setFont(texto_padrao);
        tArea_descricao.setBounds(190, 180,250,125);
        tArea_descricao.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));
        tArea_descricao.setLineWrap(true);
        tArea_descricao.setWrapStyleWord(true);

        /* CheckBOXes */
        cbox_dia.setBounds(190, 145, 80, 25);

        cbox_mes.setBounds(275, 145, 80, 25);

        cbox_ano.setBounds(360, 145, 80, 25);

        /* Painel */   
        jpanel_noticias.setLayout(null);
        jpanel_noticias.setBackground(Color.WHITE);
        jpanel_noticias.setSize(500, 500);
        jpanel_noticias.setLocation(125, 100);
        jpanel_noticias.setVisible(true);

        /* Adiciona elementos no painel */
        jpanel_noticias.add(label_criar_aula);
        jpanel_noticias.add(label_criar_noticias);
        jpanel_noticias.add(label_titulo);
        jpanel_noticias.add(label_descricao);
        jpanel_noticias.add(label_dias);
        jpanel_noticias.add(tArea_titulo);
        jpanel_noticias.add(tArea_descricao);
        jpanel_noticias.add(cbox_dia);
        jpanel_noticias.add(cbox_mes);
        jpanel_noticias.add(cbox_ano);
        jpanel_noticias.add(bt_continuar_noticias);

        jpanel_fundo.add(jpanel_noticias);

        setVisible(true);
    }

    private void continuarNoticias(ActionEvent actionEvent){

        descricao = tArea_descricao.getText();
        titulo = tArea_titulo.getText();
        dia = cbox_dia.getSelectedItem()+"";
        mes = cbox_mes.getSelectedItem()+"";
        ano = cbox_ano.getSelectedItem()+"";

        if(descricao.equals("") || titulo.equals("") || dia.equals("") || mes.equals("") || ano.equals(""))
            JOptionPane.showMessageDialog(null,"Informação faltando", "ERRO",JOptionPane.ERROR_MESSAGE);
        else{
            noticias = new Noticias(cNoticias.devolveMaiorID()+1, Integer.parseInt(dia), Integer.parseInt(mes),
            Integer.parseInt(ano), descricao, titulo, professor);
            cNoticias.insere(noticias);
            JOptionPane.showMessageDialog(null,"Parabéns!! Sua noticia foi adicionada com sucesso.", "SUCESSO",JOptionPane.INFORMATION_MESSAGE);
        
            this.vMain = new VisaoMain();
            vMain.professorPagina(cProfessor,professor);
            dispose();
        }
    }

    private void criarMensagem(ActionEvent actionEvent){
        this.vMain = new VisaoMain();
        vMain.professorMensagem(cProfessor,professor);
        dispose();
    }

    public void mensagemCadastro(Controle controle, Entidade entidade){
        cProfessor = (ControleProfessor)controle;
        professor = (Professor)entidade;

        /* Nomeia o botao como o nome do aluno cadastrado */
        bt_professor.setText(professor.getNome());

        cabecalho();
        /* Botões */
        bt_continuar_mensagem.setFont(texto_padrao);
        bt_continuar_mensagem.setBounds(187, 400, 125,40);
        bt_continuar_mensagem.setBackground(Color.white);
		bt_continuar_mensagem.setForeground(Color.black);
        bt_continuar_mensagem.addActionListener(this::continuarMensagem);

        bt_buscar.setFont(texto_padrao);
        bt_buscar.setBounds(150, 400, 100,40);
        bt_buscar.setBackground(Color.white);
		bt_buscar.setForeground(Color.black);
        bt_buscar.addActionListener(this::buscar);

        /* Labels */
        label_criar_mensagem.setFont(texto_sub_titulo);
        label_criar_mensagem.setBounds(137, 25, 225,50);

        label_titulo.setFont(texto_padrao);
        label_titulo.setBounds(60, 100, 125,50);

        label_dataNasc.setText("Data:");
        label_dataNasc.setFont(texto_padrao);
        label_dataNasc.setBounds(60, 135, 125,50);

        label_destinatario.setFont(texto_padrao);
        label_destinatario.setBounds(60, 170,125,50);

        label_papel.setFont(texto_padrao);
        label_papel.setBounds(60, 205,125,50);

        label_texto.setFont(texto_padrao);
        label_texto.setBounds(60, 240,125,50);

        /* Caixas de texto */
        tArea_titulo.setFont(texto_padrao);
        tArea_titulo.setBounds(190, 110,250,25);
        tArea_titulo.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_destinatario.setFont(texto_padrao);
        tArea_destinatario.setBounds(190, 180,250,25);
        tArea_destinatario.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        tArea_texto.setFont(texto_padrao);
        tArea_texto.setBounds(190, 250,250,135);
        tArea_texto.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));
        tArea_texto.setLineWrap(true);
        tArea_texto.setWrapStyleWord(true);

        tArea_buscar.setFont(texto_padrao);
        tArea_buscar.setBounds(60, 410,75,20);
        tArea_buscar.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));

        /* CheckBOXes */
        cbox_dia.setBounds(190, 145, 80, 25);

        cbox_mes.setBounds(275, 145, 80, 25);

        cbox_ano.setBounds(360, 145, 80, 25);

        cbox_papel.setBounds(190, 215, 250, 25);


        /* Painel */   
        jpanel_mensagem.setLayout(null);
        jpanel_mensagem.setBackground(Color.WHITE);
        jpanel_mensagem.setSize(500, 500);
        jpanel_mensagem.setLocation(125, 100);
        jpanel_mensagem.setVisible(true);

        /* Adiciona elementos no painel */
        jpanel_mensagem.add(label_criar_mensagem);
        jpanel_mensagem.add(label_titulo);
        jpanel_mensagem.add(label_texto);
        jpanel_mensagem.add(label_dataNasc);
        jpanel_mensagem.add(label_destinatario);
        jpanel_mensagem.add(label_papel);
        jpanel_mensagem.add(tArea_texto);
        jpanel_mensagem.add(tArea_titulo);
        jpanel_mensagem.add(tArea_destinatario);
        jpanel_mensagem.add(cbox_ano);
        jpanel_mensagem.add(cbox_dia);
        jpanel_mensagem.add(cbox_mes);
        jpanel_mensagem.add(cbox_papel);
        jpanel_mensagem.add(bt_continuar_mensagem);
        jpanel_mensagem.add(bt_buscar);
        jpanel_mensagem.add(tArea_buscar);

        jpanel_fundo.add(jpanel_mensagem);

        setVisible(true);
    }
    
    private void continuarMensagem(ActionEvent actionEvent){
        Aluno aluno_aux = new Aluno();
        Professor professor_aux = new Professor();

        e1 = new Estrategia1(AlunoDAO.getInstancia());
        e3 = new Estrategia1(MensagemDAO.getInstancia());
        
        cAluno = new ControleAluno(e1);
        cMensagem = new ControleMensagem(e3);

        data = "";

        texto = tArea_texto.getText();
        titulo = tArea_titulo.getText();
        destinatario = tArea_destinatario.getText();
        dia = cbox_dia.getSelectedItem()+"";
        mes = cbox_mes.getSelectedItem()+"";
        ano = cbox_ano.getSelectedItem()+"";
        papel = cbox_papel.getSelectedItem()+"";

        if(texto.equals("") || titulo.equals("") || dia.equals("") || mes.equals("") || ano.equals("") || destinatario.equals("") || papel.equals("")){
            JOptionPane.showMessageDialog(null,"Informação faltando", "ERRO",JOptionPane.ERROR_MESSAGE);
            return;
        }       
        
        if(Integer.parseInt(dia) < 10)
			data = "0";
		data = data + Integer.parseInt(dia) + ".";

		if(Integer.parseInt(mes) < 10)
			data = data + "0";
		data = data + Integer.parseInt(mes) + "." + Integer.parseInt(ano);

        if(papel.equals("Aluno")){
            aluno_aux = (Aluno) cAluno.buscaID(Integer.parseInt(destinatario));
            if(aluno_aux == null)
                return;
            mensagem = new Mensagem(titulo, data, texto, professor, aluno_aux, cMensagem.devolveMaiorID()+1,2,1);
        }
        else{
            professor_aux = (Professor) cProfessor.buscaID(Integer.parseInt(destinatario));
            if(professor_aux == null)
                return;
            mensagem = new Mensagem(titulo, data, texto, professor, professor_aux, cMensagem.devolveMaiorID()+1,2,2);
        }

        cMensagem.insere(mensagem);
        JOptionPane.showMessageDialog(null,"Parabéns!! Sua mensagem foi enviada com sucesso.", "SUCESSO",JOptionPane.INFORMATION_MESSAGE);

        this.vMain = new VisaoMain();
        vMain.professorPagina(cProfessor,professor);
        dispose();
    }

    private void buscar(ActionEvent actionEvent){
        buscar = tArea_buscar.getText();
        papel = cbox_papel.getSelectedItem()+"";

        e1 = new Estrategia1(AlunoDAO.getInstancia());
        
        cAluno = new ControleAluno(e1);

        if(papel.equals("")){
            JOptionPane.showMessageDialog(null,"Informe a funcao delu.", "ERRO",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(papel.equals("Aluno")){
            if(cAluno.buscaID(Integer.parseInt(buscar))==null){
                JOptionPane.showMessageDialog(null,"Usuario nao existe.", "ERRO",JOptionPane.ERROR_MESSAGE);
                return;
            }
            else{
                JOptionPane.showMessageDialog(null,"O nome deste usuario eh: " + ((Aluno)cAluno.buscaID(Integer.parseInt(buscar))).getNome(), "Sucesso",JOptionPane.INFORMATION_MESSAGE);
                return;  
            }
        }
        else{
            if(cProfessor.buscaID(Integer.parseInt(buscar))==null){
                JOptionPane.showMessageDialog(null,"Usuario nao existe.", "ERRO",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            else{
                JOptionPane.showMessageDialog(null,"O nome deste usuario eh: " + ((Professor)cProfessor.buscaID(Integer.parseInt(buscar))).getNome(), "Sucesso",JOptionPane.INFORMATION_MESSAGE);
                return;  
            }
        }
    }

    private void vaiPMensagem(ActionEvent actionEvent){
        e1 = new Estrategia1(MensagemDAO.getInstancia());

        cMensagem = new ControleMensagem(e1);

        this.vMain = new VisaoMain();
        vMain.mensagemMenu(cMensagem,professor,2);
        dispose();
    }

    /* Botao que leva para o menu da VisaoMain */
    private void projeto(ActionEvent actionEvent){
        this.vMain = new VisaoMain();
        vMain.menu();
        dispose();
    }

    private void irPAula(ActionEvent actionEvent){
        this.vMain = new VisaoMain();
        vMain.aulaMenu(cAula,professor,2);
        dispose();
    }

    private void noticias(ActionEvent actionEvent){
        this.vMain = new VisaoMain();
        vMain.noticiasMenu(cNoticias,professor,2);
        dispose();
    }

    private void sair(ActionEvent actionEvent){
        this.vMain = new VisaoMain();
        vMain.usuarioLogin();
        dispose();
    }


    /* Tamanho dos vetores para o dia, mês e ano */
    public String[] preencheVetor(int tamanho, int comeco, boolean orientacao) {

        /* Cria um vetor que vai armazenar os tempo correspondente e coloca o primeiro elemento*/
        String[] vetor = new String[tamanho];

        /* Deixa a primeira posição como null */
        vetor[0]= "";

        /* Preeche os elemetos seguintes até o fim do tamanho se acordo com a orientacao dada
         * se for TRUE é crescente, se for FALSE é descrescente*/
        if(orientacao){
            for(int i = 1; i<tamanho ;i++){
                vetor[i] = Integer.toString(comeco + i - 1);
            }
        }
        else{
            for(int i = 1; i<tamanho ;i++){
                vetor[i] = Integer.toString(comeco + tamanho - i - 1);
            }
        }
        /* Retorna o vetor */
        return vetor;
    }
}
