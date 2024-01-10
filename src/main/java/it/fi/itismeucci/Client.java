package it.fi.itismeucci;

import java.io.*;
import java.net.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Client {
    String serverName = "localhost";
    int serverPort;
    Socket mioSocket;
    BufferedReader in;
    DataOutputStream out;
    BufferedReader tastiera;

    public Client (){
        this.serverPort = 6789;
        this.mioSocket = null;
        this.tastiera = null;
        this.in = null;
        this.out = null;
    }

    public Socket connetti(){
        System.out.println("CLIENT is running...");
        try {
            mioSocket = new Socket(serverName, serverPort);
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(mioSocket.getInputStream()));
            out = new DataOutputStream(mioSocket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Errore nella connessione");
        } 
        return mioSocket;
    }

    public void comunica(){
        for (;;){
            try {
                System.out.println("Digita 1 per xml; qualsiasi altro numero per json: ");
                String choice = tastiera.readLine();
                out.writeBytes(choice + "\n");
                Persona p = null;
                if (choice.equals("1")){
                    XmlMapper mapper = new XmlMapper();
                    String stringaDalServer = in.readLine();
                    p = mapper.readValue(stringaDalServer, Persona.class);
                }else{
                    ObjectMapper mapper = new ObjectMapper();
                    String stringaDalServer = in.readLine();
                    p = mapper.readValue(stringaDalServer, Persona.class);
                }
                
                System.out.println("Stringa ricevuta dal server: " + "\n" + p.toString());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connetti();
        client.comunica();
    }


}