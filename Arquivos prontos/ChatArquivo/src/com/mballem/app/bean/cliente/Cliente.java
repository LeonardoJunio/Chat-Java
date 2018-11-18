/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.bean.cliente;

import com.mballem.app.bean.FileMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author leonardo
 */
public class Cliente {

    private Socket socket;
    private ObjectOutputStream outputStream;

    public Cliente() throws IOException {
        this.socket = new Socket("localhost", 5555);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(new ListenerSocket(socket)).start();

        menu();
    }

    private void menu() throws IOException {
        Scanner s = new Scanner(System.in);

        String nome;
        int option = 0;

        System.out.print("Digite seu nome: ");
        nome = s.nextLine();

        this.outputStream.writeObject(new FileMessage(nome)); //cliente se conectando e seu nome colocado na lista

        while (option != -1) {
            System.out.print("1-Sair, 2-Enviar: ");
            option = s.nextInt();

            if (option == 2) {
                send(nome);
            } else if (option == 1) {
                System.exit(0);
            }
        }

    }

    private void send(String nome) throws IOException {
        int opt;

        JFileChooser fileChooser = new JFileChooser();

        opt = fileChooser.showOpenDialog(null);//abre a janela e retorna se o cliente selecionou o arquivo ou cancelou

        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            this.outputStream.writeObject(new FileMessage(nome, file));

        }
    }

    private static class ListenerSocket implements Runnable {

        private ObjectInputStream inputStream;

        public ListenerSocket(Socket socket) throws IOException {
            this.inputStream = new ObjectInputStream(socket.getInputStream());

        }

        @Override
        public void run() {
            FileMessage message = null;
            try {
                while ((message = (FileMessage) inputStream.readObject()) != null) {
                    //chamada ao metodo que vai ler, receber mensagem e exibir o conteudo do arquivo 
                    System.out.println("\n Voce recebeu um arquivo de " + message.getCliente());
                    System.out.println("O arquivo é: " + message.getFile().getName());
                    
                    //imprime(message);
                    salvar(message);
                    
                    System.out.print("1-Sair, 2-Enviar: ");
                }
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void imprime(FileMessage message) {
            try {
                FileReader fileReader = new FileReader(message.getFile());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String linha;
                while((linha = bufferedReader.readLine())!= null){
                    System.out.println(linha);                
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        public static void main(String[] args){
            try {
                new Cliente();
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void salvar(FileMessage message) {
            try {
                Thread.sleep(new Random().nextInt(1000));
                
                long time = System.currentTimeMillis();//tempo exato da execucao dessa linha
                
                FileInputStream fileInputStream = new FileInputStream(message.getFile());
                FileOutputStream fileOutputStream = new FileOutputStream("/home/leonardo/testearquivo/" + time + message.getFile().getName());
                
                FileChannel fin = fileInputStream.getChannel();
                FileChannel fout = fileOutputStream.getChannel();
                
                long size = fin.size();
                
                fin.transferTo(0, size, fout);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

    }
}
