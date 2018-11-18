/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.bean;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author leonardo
 */
public class FileMessage implements Serializable {//classe responsavel pelo conteudo da mensagem
    private String cliente;//nome do cliente que invia a mensagem
    private File file; //contem o arquivo a ser enviado

    public FileMessage(String cliente, File file) {
        this.cliente = cliente;
        this.file = file;
    }

    public FileMessage(String cliente) {
        this.cliente = cliente;
    }

    public FileMessage() {
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    
    

}