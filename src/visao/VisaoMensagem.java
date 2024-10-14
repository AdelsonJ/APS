package visao;

/* Bibliotecas que serão necessárias*/
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controle.*;
import modelo.*;
import strategies.EstrategiaTabelas;
import strategies.EstrategiasOpBasicas;


public class VisaoMensagem extends JFrame{

    EstrategiasOpBasicas e1;
    EstrategiaTabelas e2;
    
    private Aluno aluno;
    private Professor professor;
    private Mensagem mensagem;
    private ControleMensagem cMensagem;
    private VisaoMain vMain;

    protected int funcao, tamanho_titulo;
    protected String data, dia, mes, ano, titulo, descricao;
    protected String[] colunas_mensagem = {"ID", "Id Remet", "Id Dest"};
    protected Object[][] objeto_tabela_mensagem;

    /* Paineis */
    JPanel jpanel_cabecalho = new JPanel();
    JPanel jpanel_fundo = new JPanel();
    JPanel jpanel_mensagem = new JPanel();
    JPanel jpanel_titulo = new JPanel();

    /* Scroll Panels */
    JScrollPane jScroll_mensagem;
    JScrollPane jScroll_alunos;
    
    /* Botões */
    JButton bt_projeto = new JButton("PROJETO");
    JButton bt_usuario = new JButton("");
    JButton bt_voltar = new JButton("VOLTAR");
    
    /* Labels */
    JLabel label_remetente = new JLabel();
    JLabel label_destinatario = new JLabel();
    JLabel label_titulo = new JLabel();
    JLabel label_mensagem = new JLabel();
    JLabel label_data = new JLabel();

    /* TextAreas */
    JTextArea tArea_texto = new JTextArea();

    /* Tabelas */
    JTable tabela_mensagem;

    /* Modelo de lista de selecao */
    ListSelectionModel selecao;

    /* Fonte e Cores */
    Font texto_padrao = new Font("ARIAL",Font.BOLD,12);
    Font texto_pequeno = new Font("ARIAL",Font.BOLD,10);
    Font texto_titulo = new Font("ARIAL",Font.BOLD,30);
    Font texto_sub_titulo = new Font("ARIAL",Font.BOLD,20);
    Color cor_fundo = new Color(194,255,240);
    Color cor_cabecalho = new Color(0,204,155);
    Color cor_textos = new Color(163, 184, 204);


    /* Contrução do JFrame que será usado */
    public VisaoMensagem(){
        setSize(750,600);
        setTitle("TP Analise e Projeto de Software");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    /* Interface do cabecalho */
    public void cabecalho(){
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

        /* Botões */
        bt_usuario.setFont(texto_padrao);
        bt_usuario.setBounds(575,30,125,40);
        bt_usuario.setBackground(Color.white);
        bt_usuario.setForeground(Color.black);
        bt_usuario.addActionListener(this::usuario);

        bt_projeto.setFont(texto_titulo);
        bt_projeto.setBounds(20, 30,200,50);
        bt_projeto.setBackground(cor_cabecalho);
		bt_projeto.setForeground(Color.black);
        bt_projeto.setBorderPainted(false);
        bt_projeto.addActionListener(this::projeto);
        
        /* Adiciona os elementos no cabecalho, em seguida adiciona-o no fundo e adiciona o fundo */
        jpanel_cabecalho.add(bt_projeto);
        jpanel_cabecalho.add(bt_usuario);            
        
        add(jpanel_cabecalho);
        add(jpanel_fundo);
    }

    public void menuMensagens(Controle controle, Entidade entidade, int tipo){
        setVisible(true);

        funcao = tipo;
        mensagem = new Mensagem();
        cMensagem = (ControleMensagem)controle;

        switch (tipo) {
            case 1:                
                aluno = (Aluno)entidade;
                bt_usuario.setText(aluno.getNome());

                objeto_tabela_mensagem = cMensagem.textoMensagem(aluno,1);
                break;
            
            case 2:                
                professor = (Professor)entidade;
                bt_usuario.setText(professor.getNome());
                objeto_tabela_mensagem = cMensagem.textoMensagem(professor,2);
                break;
            
            default:
                break;
        }

        cabecalho();
        
        /* Cria uma tabela de selecao unica e nao editavel */
        tabela_mensagem = new JTable(objeto_tabela_mensagem, colunas_mensagem){   
            public boolean isCellEditable(int row, int column) {                
                return false;               
            }
        };
        tabela_mensagem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        selecao = tabela_mensagem.getSelectionModel();
        selecao.addListSelectionListener(new ListSelectionListener() {
            @Override
                public void valueChanged(ListSelectionEvent e){
                    /* Confere se um item foi selecionado, em caso positivo, pega seu index */
                    if(!selecao.isSelectionEmpty()){
                        int selecionado = selecao.getMinSelectionIndex();
                        if(tabela_mensagem.getValueAt(selecionado, 0) != null){
                            String aux = tabela_mensagem.getValueAt(selecionado, 0).toString();

                            mensagem.setId(Integer.parseInt(aux));

                            /* Apaga tabela antiga */
                            jpanel_fundo.remove(jScroll_mensagem);

                            mensagem = (Mensagem) cMensagem.buscaID(mensagem.getId());

                            setVisible(false);
                            vMain = new VisaoMain();
                            switch (funcao) {
                                case 1:
                                    vMain.mensagemPI(mensagem, aluno,funcao);
                                    break;

                                case 2:
                                    vMain.mensagemPI(mensagem, professor,funcao);
                                    break;
                            
                                default:
                                    vMain.mensagemPI(mensagem, null,funcao);
                                    break;
                            }
                            dispose();
                        }
                    }
                }
        });

        
        //Component celula = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        /* Cria um novo JScrollPane e coloca a tabela nele */
        jScroll_mensagem = new JScrollPane(tabela_mensagem);

        jScroll_mensagem.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScroll_mensagem.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScroll_mensagem.setBounds(100,150, 550,350);
        jScroll_mensagem.getVerticalScrollBar().setValue(0);
        jScroll_mensagem.setVisible(true);

        /* Adiciona o ScrollPane no painel */
        jpanel_fundo.add(jScroll_mensagem);
        
    }

    public void paginaIndividual(Controle controle, Entidade entidade, Entidade entidade2, int tipo){
        setVisible(true);

        cMensagem = (ControleMensagem)controle;
        this.mensagem = (Mensagem)entidade;

        tamanho_titulo = mensagem.getTitulo().length()*17;

        funcao = tipo;
        cabecalho();

        switch (tipo) {
            case 1:        
                aluno = (Aluno)entidade2;        
                bt_usuario.setText(aluno.getNome());
                break;
            
            case 2: 
                professor = (Professor)entidade2;               
                bt_usuario.setText(professor.getNome());
                break;
            
            default:
                break;
        }

        /* Botoes */
        bt_voltar.setFont(texto_pequeno);
        bt_voltar.setBounds(100,400,100,40);
        bt_voltar.setBackground(Color.white);
        bt_voltar.setForeground(Color.black);
        bt_voltar.setBorderPainted(true);
        bt_voltar.addActionListener(this::voltar);
        jpanel_mensagem.add(bt_voltar);

        /* JLabels */
        label_mensagem.setText(mensagem.getTitulo());
        label_mensagem.setFont(texto_sub_titulo);
        label_mensagem.setBounds((550-tamanho_titulo)/2, 50,tamanho_titulo,40);
		label_mensagem.setForeground(Color.white);

        label_remetente.setText("DE: " + (mensagem.getRemetente()).getNome() + " " + (mensagem.getRemetente()).getSobrenome());
        label_remetente.setFont(texto_padrao);
        label_remetente.setBounds(50, 110,225,40);
        
        label_destinatario.setText("PARA: " + (mensagem.getDestinatario()).getNome() + " " + (mensagem.getDestinatario()).getSobrenome());
        label_destinatario.setFont(texto_padrao);
        label_destinatario.setBounds(50, 150,225,40);

        label_titulo.setText("Assunto: " + mensagem.getTitulo());
        label_titulo.setFont(texto_padrao);
        label_titulo.setBounds(50, 190,225,40);

        label_data.setText(mensagem.getData());
        label_data.setFont(texto_padrao);
        label_data.setBounds(400, 400,225,40);

        /* TextAreas */
        tArea_texto.setText(mensagem.getTexto());
        tArea_texto.setFont(texto_padrao);
        tArea_texto.setBounds(50,230,450,155);
        tArea_texto.setBackground(Color.white);
        tArea_texto.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,cor_cabecalho));
        tArea_texto.setLineWrap(true);
        tArea_texto.setWrapStyleWord(true);
        tArea_texto.setEditable(false);

        /* Paineis */
        jpanel_mensagem.setLayout(null);
        jpanel_mensagem.setBackground(Color.WHITE);
        jpanel_mensagem.setVisible(true);
        jpanel_mensagem.setSize(550, 500);
        jpanel_mensagem.setLocation(100, 100);

        jpanel_titulo.setLayout(null);
        jpanel_titulo.setBackground(cor_cabecalho);
        jpanel_titulo.setVisible(true);
        jpanel_titulo.setSize(550, 60);
        jpanel_titulo.setLocation(0, 30);

        jpanel_mensagem.add(label_titulo);
        jpanel_mensagem.add(label_remetente);
        jpanel_mensagem.add(label_destinatario);
        jpanel_mensagem.add(label_data);
        jpanel_mensagem.add(label_mensagem);
        jpanel_mensagem.add(label_titulo);
        jpanel_mensagem.add(tArea_texto);
        jpanel_mensagem.add(jpanel_titulo);
        jpanel_mensagem.add(bt_voltar);

        jpanel_fundo.add(jpanel_mensagem);
    }


    /* Funcao que volta pro menu da Visao main */
    private void voltar(ActionEvent actionEvent){
        vMain = new VisaoMain();
        
        switch (funcao) {
            case 1:                               
                vMain.mensagemMenu(cMensagem, aluno, 1);
                dispose();
                break;
            
            case 2:                       
                vMain.mensagemMenu(cMensagem, professor, 2);
                dispose();
                break;
            
            default:       
                vMain.mensagemMenu(cMensagem, null, 0);
                dispose();
                break;
        }
    }

    /* Funcao que volta pro menu da Visao main */
    private void projeto(ActionEvent actionEvent){
        vMain = new VisaoMain();
        vMain.menu();
        dispose();
    }

    private void usuario(ActionEvent actionEvent){
        vMain = new VisaoMain();
        /* Chama a instancia unica do VisaoAluno ou do Visao Professor, redirecionando para suas
         * respectivas paginas de acordo com sua funcao*/
        if(funcao == 1)
            vMain.aulaAP(aluno,1);
        else
            vMain.aulaAP(professor,2);
        dispose();
    }

}
