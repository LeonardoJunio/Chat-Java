/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.service;

import chat.bean.ChatMessage;
import chat.bean.ChatMessage.Action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

//import sun.awt.windows.ThemeReader;
/**
 *
 * @author usuario
 */
public class ServidorService {

    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>(); //todo usuario que entrar no servidor (conectados), entra nesta lista

    public ServidorService() {
        try {
            serverSocket = new ServerSocket(5555);
            //quando o servidor inicializar, vai pela porta "5555" e quando o cliente conectar, o acept libera passado o socket que o cliente criou na aplicao cliente para a variavel socket
            System.out.println("Servidor on!");

            while (true) {
                socket = serverSocket.accept();
                new Thread(new ListenerSocket(socket)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectOutputStream output; //saida de mensagem
        private ObjectInputStream input;//recebe a mensagem

        public ListenerSocket(Socket socket) {
            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void run() {
            ChatMessage message = null;
            try {
                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();
                    //verica o tipo de mensagem enviada pelo cliente
                    if (action.equals(Action.CONNECT)) { //pede parase conectar / entrar no chat
                        boolean isConnect = connect(message, output); //tem oque o cliente digitou e o objeto de saida
                        if (isConnect) {
                            mapOnlines.put(message.getName(), output);
                            sendOnlines();
                        }
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnect(message, output);
                        sendOnlines();
                        return;
                    } else if (action.equals(Action.SEND_ONE)) {
                        sendOne(message);
                    } else if (action.equals(Action.SEND_ALL)) {
                        sendAll(message);
                    }
                }
            } catch (IOException ex) {
                ChatMessage cm = new ChatMessage();
                cm.setName(message.getName());
                disconnect(cm, output);
                sendOnlines();
                System.out.println(message.getName() + " deixou o chat.");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);    
            }
        }

    }

    private boolean connect(ChatMessage message, ObjectOutputStream output) {
        /* Tres situaçoes:
         1- como fazer uma lista de clientes;
         2- nao permitir clientes com o mesmo nome;        
         */

        if (mapOnlines.size() == 0) {//nome ainda nao foi usado
            message.setText("YES");
            send(message, output);
            return true;
        }
        //lista ja possuindo pelomenos 1 cliente adicionado, ai verifica se o nome é diferente
        if (mapOnlines.containsKey(message.getName())) {
            message.setText("NO");
            send(message, output);
            return false;
        } else {
            message.setText("Yes");
            send(message, output);
            return true;
        }
    }

    private void disconnect(ChatMessage message, ObjectOutputStream output) {
        mapOnlines.remove(message.getName());

        message.setText(" até logo!");

        //manda mensagem que saiu pra todos que estao online
        message.setAction(Action.SEND_ONE);
        sendAll(message);//envia para todos a mensagem

        System.out.println("User: " + message.getName() + " sai da sala.");
    }

    private void send(ChatMessage message, ObjectOutputStream output) {
        try {
            output.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendOne(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (kv.getKey().equals(message.getNameReserved())) {
                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendAll(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (!kv.getKey().equals(message.getName())) { //key armazena o nome do cliente
                message.setAction(Action.SEND_ONE);
                try {
                    //mensagem enviada para o cliente que possui essa chave, assim a mensagem nao é enviada para ele mesmo
                    //System.out.println("user: " + message.getName());
                    kv.getValue().writeObject(message); //value armazena o objectoutput...
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendOnlines() {
        Set<String> setNames = new HashSet<String>();
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            setNames.add(kv.getKey());
        }

        ChatMessage message = new ChatMessage();
        message.setAction(Action.USERS_ONLINE);
        message.setSetOnlines(setNames);

        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            message.setName(kv.getKey());//cada mensagem com o nome do cliente
            try {
                kv.getValue().writeObject(message); //value armazena o objectoutput...
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
