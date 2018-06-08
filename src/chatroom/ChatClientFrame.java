/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import GUI.BtnImagen;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 *
 * @author JavierLeon
 */
public class ChatClientFrame extends JFrame{
    private static ChatClient ChatClient;
    public static String UserName="Anónimo";
    
    private BtnImagen btnEnviar;
    private BtnImagen btnConectar;
    private BtnImagen btnDesconectar;
    private JLabel lblRoom;
    private JLabel lblOnline;
    public static JTextField txtMsg;
    public static JTextArea txtArea;
    private JScrollPane scllConver;
    private JScrollPane scllOnline;
    public static JList lstOnline;
    
    public static void main(String[] args) {
        new ChatClientFrame();
    }
    
    public ChatClientFrame(){
        super();
        super.setSize(500, 500);
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.getContentPane().setLayout(new BorderLayout());
        
        lblRoom=new JLabel("ChatRoom");
        lblRoom.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblOnline=new JLabel("Conectados");
        lblOnline.setHorizontalAlignment(SwingConstants.CENTER);
        
        Color letraColor=obtenerColor();
        txtMsg=new JTextField(20);
        txtMsg.setPreferredSize(new Dimension(100, 60));
        txtMsg.setFont(new Font("Courier",Font.BOLD,12));
        txtMsg.setForeground(letraColor);
        
        txtArea= new JTextArea();
        txtArea.setSize(300, 250);
        txtArea.setColumns(20);
        txtArea.setFont(new Font("Courier",Font.BOLD,12));
        txtArea.setForeground(letraColor);
        txtArea.setLineWrap(true);
        txtArea.setRows(5);
        txtArea.setEditable(false);
        
        scllConver= new JScrollPane();
        scllConver.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scllConver.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scllConver.setViewportView(txtArea);
        super.getContentPane().add(scllConver);
        
        lstOnline = new JList();
        lstOnline.setSize(180, 250);
        lstOnline.setForeground(Color.GREEN);
        lstOnline.setVisible(true);
        
        scllOnline=new JScrollPane();
        scllOnline.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scllOnline.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scllOnline.setViewportView(lstOnline);
        super.getContentPane().add(scllOnline);
        
        
        btnEnviar=new BtnImagen("/img/008-send.png");
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtMsg.getText().equals("")){
                    ChatClient.send(txtMsg.getText());
                    txtMsg.requestFocus();
                }
            }
        });
        btnConectar=new BtnImagen("/img/003-server.png");
        btnConectar.setEnabled(true);
        btnConectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ChatClientFrame.this.inNombreUser();
            }
        });
        btnDesconectar=new BtnImagen("/img/004-exit.png");
        btnDesconectar.setEnabled(false);
        btnDesconectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChatClient.disconnect();
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        });
        
        
        
        JPanel pnlBtns = new JPanel();
        pnlBtns.add(btnConectar);
        pnlBtns.add(btnDesconectar);
        
        
        JPanel pnlText = new JPanel();
        pnlText.add(txtArea);
        pnlText.add(lstOnline);
        
        
        JPanel pnlMsg = new JPanel();
        pnlMsg.add(btnEnviar);
        pnlMsg.add(txtMsg);
        
        super.add(pnlBtns,BorderLayout.NORTH);
        super.add(pnlText,BorderLayout.CENTER);
        super.add(pnlMsg,BorderLayout.SOUTH);
        super.setVisible(true);
    }
    
    
    
    private void inNombreUser(){
        String input=JOptionPane.showInputDialog(this, "NickName: ", "Ingresa tu nombre de usuario", JOptionPane.QUESTION_MESSAGE);
        if(!"".equals(input)){
            UserName = input.trim();
            ChatServer.CurrentUsers.add(UserName);
            super.setTitle("Chat de "+this.UserName);
            btnEnviar.setEnabled(true);
            btnDesconectar.setEnabled(true);
            btnConectar.setEnabled(false);
            Connect();
        }
    }
    
    private void Connect(){
        try {
            final int port=1234;
            final String host="127.0.0.1";
            Socket sock = new Socket(host, port);
            System.out.println("Te haz conectado a: "+host);
            
            ChatClient = new ChatClient(sock);
            
            PrintWriter out= new PrintWriter(sock.getOutputStream());
            out.println(UserName);
            out.flush();
            
            Thread hilo= new Thread(ChatClient);
            hilo.start();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "No responde el servidor");
            System.exit(0);
        }
    }
    
    private Color obtenerColor(){
        Color color;
        Random random= new Random();
        Integer r=random.nextInt(255)+1;
        Integer g=random.nextInt(255)+1;
        Integer b=random.nextInt(255)+1;
        return color=new Color(r,g,b);
    }
}
