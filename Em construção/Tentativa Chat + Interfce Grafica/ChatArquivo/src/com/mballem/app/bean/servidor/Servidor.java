/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.bean.servidor;

import com.mballem.app.bean.FileMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leonardo
 */
public class Servidor {
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> streamMap = new HashMap<String, ObjectOutputStream>();
    //uma lista para verificar pra quem vai enviar

    public Servidor() {
        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("Servidor on!");
            
            while (true){//quando alguem se conectar a porta 5555, cria um socket
                socket = serverSocket.accept();
                
                new Thread(new ListenerSocket(socket)).start();
                               
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    private class ListenerSocket implements Runnable {
        private ObjectOutputStream outputStream;//responsavel pelo envio de mensagem pelo servidor
        private ObjectInputStream inputStream;//quem recebe as mensagens
        
        public ListenerSocket(Socket socket) throws IOException {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() { 
            FileMessage message = null;
            try {
                while ((message = (FileMessage) inputStream.readObject()) != null){
                    streamMap.put(message.getCliente(), outputStream);
                    
                    if(message.getFile() != null){//se for nulo, so conexao, se nao, o cliente esta enviando mensagem
                        for(Map.Entry<String, ObjectOutputStream> kv: streamMap.entrySet()){
                            if(!message.getCliente().equals(kv.getKey())){//se o nome for igual, nao envia
                                kv.getValue().writeObject(message);//envia a mensagem
                                
                            }
                        }
                    }
                    
                }
            } catch (IOException ex) {
                streamMap.remove(message.getCliente());
                System.out.println(message.getCliente() + " desconectou.");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args){
        new Servidor();
    }
    
    
}
