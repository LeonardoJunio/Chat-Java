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
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>(); //todo usuario que entrar no servidor (conectados), entra nesta lista; usado pra mandar mensagem para todos
    //private Map<String, ObjectOutputStream> streamMap = new HashMap<String, ObjectOutputStream>();
    
    public ServidorService() {
        try {
            serverSocket = new ServerSocket(5555);
            //quando o servidor inicializar, vai pela porta "5555" e quando o cliente conectar, o acept libera passado o socket que o cliente criou na aplicao cliente para a variavel socket
            System.out.println("Servidor on!");

            while (true) {
                socket = serverSocket.accept();
                new Thread(new ListenerSocket(socket)).start();//é executado para cada cliente que entrou
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ListenerSocket implements Runnable { //serviddor

        private ObjectOutputStream output; //saida de mensagem
        private ObjectInputStream input;//recebe a mensagem dos clientes

        public ListenerSocket(Socket socket) {//ouvinte do servidor; 
            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());//reserva cada par para cada cliente que entra
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void run() {
            ChatMessage message = null;
            try {
                while ((message = (ChatMessage) input.readObject()) != null) { //read object le as mensagens enviada pelo cliente
                    Action action = message.getAction();//recebe a acao que está sendo feita
                    //verica o tipo de mensagem enviada pelo cliente

                    mapOnlines.put(message.getCliente(), output);
                    if (action.equals(Action.CONNECT)) { //tipo de mensagens enviada pelo cliente, ou seja, analiza cada opcao
                        boolean isConnect = connect(message, output); //varialvel message tem tem oque o usuario digitou e saida de mensagem que é o output
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

        if (mapOnlines.size() == 0) {//tamanho zero pois o nome ainda nao foi usado
            message.setText("YES");
            send(message, output);
            return true;
        }
        //lista ja possuindo pelomenos 1 cliente adicionado, ai verifica se o nome é diferente
        if (mapOnlines.containsKey(message.getName())) {
            message.setText("NO");
            send(message, output);
            return false;
        } else {//se nome for diferente do que ja existe na lista
            message.setText("Yes");
            send(message, output);
            return true;
        }
    }

    private void disconnect(ChatMessage message, ObjectOutputStream output) {
        mapOnlines.remove(message.getName());

        message.setText(" deixou o chat!");

        //manda mensagem que saiu pra todos que estao online
        message.setAction(Action.SEND_ONE);
        sendAll(message);//envia para todos a mensagem

        System.out.println("User: " + message.getName() + " sai da sala.");
    }

    private void send(ChatMessage message, ObjectOutputStream output) {
        try {
            output.writeObject(message);//atravez da variavel output, envia a mensagem com objeto write
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendOne(ChatMessage message) {
        //if(message.getFile() != null){
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            /*if (!message.getCliente().equals(kv.getKey())) {//se o nome for igual, nao envia
                kv.getValue().writeObject(message);//envia a mensagem
            }*/
            
            if (kv.getKey().equals(message.getNameReserved()) || !message.getCliente().equals(kv.getKey())) {
            //if (kv.getKey().equals(message.getNameReserved())) {
                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    mapOnlines.remove(message.getCliente());
                    System.out.println(message.getCliente() + " desconectou.");
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendAll(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {//Saber cada mensagem de saida
            //String - key; object-value
            if (!kv.getKey().equals(message.getName())) { //key armazena o nome do cliente do for, que é de cliente em cliente
                message.setAction(Action.SEND_ONE);
                try {
                    //mensagem enviada para o cliente que possui essa chave, assim a mensagem nao é enviada para ele mesmo
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
