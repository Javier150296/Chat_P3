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

/**
 *
 * @author JavierLeon
 */
public class ChatServerReturn implements Runnable{
    
    Socket sock;
    private Scanner input;
    private PrintWriter out;
    String message="";
    
    public ChatServerReturn(Socket s){
        this.sock=s;
    }
    
    public void CheckConnection() throws IOException{
        if(!sock.isConnected()){
            for(int i=1; i<= ChatServer.ConnectionArray.size(); i++){
                Socket tempSock = (Socket) ChatServer.ConnectionArray.get(i-1);
                PrintWriter tempOut = new PrintWriter(tempSock.getOutputStream());
                tempOut.println(tempSock.getLocalAddress().getHostName()+" desconectado");
                tempOut.flush();
                //mostrar el socket desconectado al servidor
                System.out.println(tempSock.getLocalAddress().getHostName()+" desconectado");
                
            }
        }
    }
    
    public void run(){
        try {
            try {
                input=new Scanner(sock.getInputStream());
                out=new PrintWriter(sock.getOutputStream());
                while(true){
                    CheckConnection();
                    if(!input.hasNext()){
                        return ;
                    }
                    message=input.nextLine();
                    System.out.println("Cliente dice: "+message);
                    for(int i=1; i<=ChatServer.ConnectionArray.size(); i++){
                        Socket tempSock = (Socket) ChatServer.ConnectionArray.get(i-1);
                        PrintWriter tempOut = new PrintWriter(tempSock.getOutputStream());
                        tempOut.println(message);
                        tempOut.flush();
                        System.out.println("Enviado a: "+tempSock.getLocalAddress().getHostName());
                    }
                }
            }finally{
                sock.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
