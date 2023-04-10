/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author usuario
 */
public class ChatMessage implements Serializable { 
    private String name;//nome do cliente
    private String text;//texto da mensagem
    private String nameReserved; //armazenar o nome do cliente que receberar uma mensagem do tipo reservada
    private Set<String> setOnlines = new HashSet<String>(); //lista (do tipo String) armazena todos os clientes onlines conectados no servidor
    private Action action; //para cada mensagem, mostra qual acao vai ser executada baseado no "enum"

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
       
}