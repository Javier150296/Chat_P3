/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author JavierLeon
 */
public class ChatClient implements Runnable{
    Socket sock;
    Scanner input;
    Scanner send=new Scanner(System.in);
    PrintWriter out;
    
    public ChatClient(Socket s){
        this.sock=s;
    }
    
    public void run(){
        try {
            try {
                input= new Scanner(sock.getInputStream());
                out=new PrintWriter(sock.getOutputStream());
                out.flush();
                CheckStream();
            } finally{
                sock.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void disconnect() throws IOException{
        out.println(ChatClientFrame.UserName+" se ha desconectado");
        out.flush();
        sock.close();
        JOptionPane.showMessageDialog(null,"Que cabroncito, te has desconectado");
        System.exit(0);
    }
    
    private void CheckStream(){
        while(true){
            recibiendo();
        }
    }
    
    private void recibiendo(){
        if(input.hasNext()){
            String message= input.nextLine();
            if(message.contains("#?!")){
                String temp1=message.substring(3);
                temp1=temp1.replace("[", "");
                temp1=temp1.replace("]", "");
                
                String[] CurrentUsers = temp1.split(", ");
                ChatClientFrame.lstOnline.setListData(CurrentUsers);
            }else{
                ChatClientFrame.txtArea.append(message+"\n");
            }
        }
    }
    
    public void send(String s){
        out.println(ChatClientFrame.UserName+": "+s);
        out.flush();
        ChatClientFrame.txtMsg.setText("");
    }
}
