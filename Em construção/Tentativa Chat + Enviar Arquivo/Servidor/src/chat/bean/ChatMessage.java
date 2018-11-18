/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.bean;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author usuario
 */
public class ChatMessage implements Serializable { //classe responsavel pelo conteudo da mensagem
    private String name;//nome do cliente que invia a mensagem
    private String text;//texto da mensagem
    private String nameReserved; //armazenar o nome do cliente que receberar uma mensagem do tipo reservada
    private Set<String> setOnlines = new HashSet<String>(); //lista (do tipo String) armazena todos os clientes onlines conectados no servidor
    private Action action; //para cada mensagem, mostra qual acao vai ser executada baseado no "enum"
    
    private File file; //contem o arquivo a ser enviado
    
    /*
       Objeto da classe ChatMessage
    
    */
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNameReserved() {
        return nameReserved;
    }

    public void setNameReserved(String nameReserved) {
        this.nameReserved = nameReserved;
    }

    public Set<String> getSetOnlines() {
        return setOnlines;
    }

    public void setSetOnlines(Set<String> setOnlines) {
        this.setOnlines = setOnlines;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    
    public enum Action{
        CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USERS_ONLINE
    }
    
    
    //Onde esta atribuindo os nomes dos clientes? porque nao tem construtor no codigo acima
    
    public ChatMessage(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public ChatMessage(String name) {
        this.name = name;
    }

    public ChatMessage() {
    }

    public String getCliente() {
        return name;
    }

    public void setCliente(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}