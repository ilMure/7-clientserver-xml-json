package it.fi.itismeucci;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    ServerSocket server;
    Socket client;
    BufferedReader in;
    DataOutputStream out;

    Persona p;

    public Server (String nome, String cognome, int eta){
        this.server = null;
        this.client = null;
        this.in = null;
        this.out = null;
        this.p = new Persona(nome, cognome, eta);
    }

    public Socket attendi(){
        try {
            System.out.println("SERVER is running");
            if (server == null) server = new ServerSocket(6789);
            client = server.accept();
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante istanza del server");
        }
        return client;
    }

    public void comunica(){
        for (;;){
            try {    
                String choice = in.readLine();
                System.out.println("Sto serializzando l'oggetto persona...");
                String toSend = "";
                if (choice.equals("1")){
                    XmlMapper mapper = new XmlMapper();
                    String xml = mapper.writeValueAsString(p);
                    toSend = xml;
                }else{
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(p);
                    toSend = json;
                }         
                
                System.out.println("Questa è la stringa che invio: " + toSend);
                out.writeBytes(toSend+"\n");
                
            } catch (Exception e) {
                System.out.println("Errore durante la comunicazione");
            }
        }
        
        
    }

    public static void main(String[] args) {
        Server server = new Server("Mario", "Rossi", 40);
        server.attendi();
        server.comunica();
    }


}
