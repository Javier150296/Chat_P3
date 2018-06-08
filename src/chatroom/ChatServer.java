/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author JavierLeon
 */
public class ChatServer {
    public static ArrayList<Socket> ConnectionArray= new ArrayList<>();
    public static ArrayList<String> CurrentUsers= new ArrayList<>();
    
    public static void main(String[] args) {
        try {
            final int puerto = 1234;
            ServerSocket servidor = new ServerSocket(puerto);
            System.out.println("Esperando clientes...");
            while(true){
                Socket sock=servidor.accept();
                ConnectionArray.add(sock);
                System.out.println("Cliente conectado desde: "+sock.getLocalAddress().getHostName());
                AddUserName(sock);
                
                ChatServerReturn CHAT= new ChatServerReturn(sock);
                Thread hilo = new Thread(CHAT);
                hilo.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void AddUserName(Socket s) throws IOException{
        Scanner input = new Scanner (s.getInputStream());
        String UserName = input.nextLine();
        CurrentUsers.add(UserName);
        
        for(int i=1; i<= ChatServer.ConnectionArray.size(); i++){
            Socket temp_sock=(Socket) ChatServer.ConnectionArray.get(i-1);
            PrintWriter out= new PrintWriter(temp_sock.getOutputStream());
            out.println("#?!"+CurrentUsers);
            out.flush();
        }
    }
    
}
