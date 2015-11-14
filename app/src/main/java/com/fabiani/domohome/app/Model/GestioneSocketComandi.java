//package client_java_core.core;

/***************************************************************************
 * 			              GestioneSocketComandi.java                       *
 * 			              --------------------------                       *
 *   date          : Jul 19, 2004                                          *
 *   copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy 	       *
 *   				 Embedded Software Development Laboratory              *
 *   license       : GPL                                                   *
 *   email         : 		             				                   *
 *   web site      : www.bticino.it; www.myhome-bticino.it                 *
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

package com.fabiani.domohome.app.model;

import com.bticino.openwebnet.OpenWebNetUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//import bticino.GestionePassword;

/**
 * Description:
 * Gestione della socket Comandi, apertura connessione, chiusura connessione, invio comando
 * 
 */
public class GestioneSocketComandi{
	
	/*    
	 * stato 0 = non connesso.
	 * stato 1 = inviata richiesta socket comandi, in attesa di risposta.
	 * stato 2 = inviato risultato sulle operazioni della password, attesa per ack o nack. Se la     
	 *           risposta Ã¨ ack si passa allo stato 3.
	 * stato 3 = connesso correttamente.
	 */
	
	static ReadThread readTh = null; //thread per la ricezione dei caratteri inviati dal webserver
	static NewThread timeoutThread = null; //thread per la gestione dei timeout
	static int  stato = 0;  //stato socket comandi
	Socket socket = null;	
	static String responseLine = null; //stringa in ricezione dal Webserver
	static final String socketComandi = "*99*0##";
	static final String socketSuperComandi = "*99*9##";
	BufferedReader input = null;
	PrintWriter output = null;  
	OpenWebNet openWebNet = null;


	/**
	 * Tentativo di apertura socket comandi verso il webserver
	 * Diversi possibili stati:
	 * stato 0 = non connesso
	 * stato 1 = inviata richiesta socket comandi, in attesa di risposta
	 * stato 2 = inviato risultato sulle operazioni della password, attesa per ack o nack. Se la     
	 *           risposta Ã¨ ack si passa allo stato 3
	 * stato 3 = connesso correttamente
	 * 
	 * @param ip Ip del webserver al quale connettersi
	 * @param port Porta sulla quale aprire la connessione
	 * @param passwordOpen Password open del webserver
	 * @return true Se la connessione va a buon fine, false altrimenti
	 */
	public boolean connect(String ip, int port, int passwordOpen){
		try{
			System.out.println("Tentativo connessione a "+ ip +"  Port: "+ port);
			socket = new Socket(ip, port);
			setTimeout(0);
			input= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Buffer reader creato");
			output = new PrintWriter(socket.getOutputStream(),true);
			System.out.println("Print Writer creato");
		}catch (IOException e){
			System.out.println("Impossibile connettersi!");
			this.close();
			//e.printStackTrace();
		}
		
		if(socket != null){
			while(true){
				readTh = null;
				readTh = new ReadThread(socket,input,0);
				readTh.start();
				try{
					readTh.join();
				}catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					System.out.println("----- ERRORE readThread.join() durante la connect:");
					e1.printStackTrace();
				}
				
				if(responseLine != null){
		    		if (stato == 0){ //ho mandato la richiesta di connessione
			        	System.out.println("\n----- STATO 0 ----- ");
			        	System.out.println("Rx: " + responseLine);
			            if (responseLine.equals(OpenWebNet.MSG_OPEN_OK)) {
			            	System.out.println("Tx: "+socketComandi);
			            	output.write(socketComandi); //comandi
			            	output.flush();
			            	stato = 1; 
			            	setTimeout(0);
			            }else{
			            	//se non mi connetto chiudo la socket
			                System.out.println("Chiudo la socket verso il server " + ip);
			                this.close();
			            	break;
			            }
					}else if (stato == 1) { //ho mandato il tipo di servizio richiesto
						System.out.println("----- STATO 1 -----");
						System.out.println("Rx: " + responseLine);

						//applico algoritmo di conversione
						System.out.println("Controllo sulla password");
						Long seed = Long.valueOf(responseLine.substring(2, responseLine.length() - 2));
						System.out.println("Tx: " + "seed=" + seed);
						Long risultato = OpenWebNetUtils.passwordFromSeed(seed, passwordOpen);
						System.out.println("Tx: " + "*#" + risultato + "##");
						output.write("*#" + risultato + "##");
						output.flush();
						stato = 2; //setto stato dopo l'autenticazione
						setTimeout(0);
					} else if (stato == 2) {
						System.out.println("\n----- STATO 2 -----");
						System.out.println("Rx: " + responseLine);
						if (responseLine.equals(OpenWebNet.MSG_OPEN_OK)) {
							System.out.println("Connessione OK");
							stato = 3;
							break;
						} else {
							System.out.println("Impossibile connettersi!!");
							//se non mi connetto chiudo la socket
							System.out.println("Chiudo la socket verso il server " + ip);
							this.close();
							break;
						}
					} else break; //non dovrebbe servire (quando passo per lo stato tre esco dal ciclo con break)
		    	}else{
		    		System.out.println("--- Risposta dal webserver NULL");
		    		this.close();
		    		break;//ramo else di if(responseLine != null)
		    	}
		    }//chiude while(true)
		} else {
			System.out.println("--- Nessuna Risposta dal webserver");
			this.close();
			//break;//ramo else di if(responseLine != null)
		}
	if (stato == 3) return true;
		else return false;
	}//chiude connect()
	
	
	/**
	 * Chiude la socket comandi ed imposta stato = 0
	 *
	 */
	public void close(){
		if(socket != null){
			try { 
				socket.close();
				socket = null;
				stato = 0;
				System.out.println("-----Socket chiusa correttamente-----");				
			} catch (IOException e) {				
				// TODO Auto-generated catch block
				System.out.println("Errore Socket: <GestioneSocketComandi>");
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Metodo per l'invio di un comando open
	 * 
	 * @param comandoOpen comando da inviare
	 * @return 0 se il comando vine inviato, 1 se non Ã¨ possibile inviare il comando
	 */
	public int invia(String comandoOpen){
		//creo l'oggetto openWebNet con il comandoOpen
		try{
			openWebNet = new OpenWebNet(comandoOpen);
			if(openWebNet.isErrorFrame()){
				System.out.println("ERRATA frame open "+comandoOpen+", la invio comunque!!!");
			}else{
				System.out.println("CREATO oggetto OpenWebNet "+openWebNet.getFrameOpen());
			}
		}catch(Exception e){
			System.out.println("ERRORE nella creazione dell'oggetto OpenWebNet "+comandoOpen);
			System.out.println("Eccezione in GestioneSocketComandi durante la creazione del'oggetto OpenWebNet");
			e.printStackTrace();
		}

		System.out.println("Tx: "+comandoOpen);
		output.write(comandoOpen);
		output.flush();
		
		do{ 
			setTimeout(0);
			readTh = null;
			readTh = new ReadThread(socket,input,0);
			readTh.start();
			try{
				readTh.join();
			}catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				System.out.println("----- ERRORE readThread.join() durante l'invio comando:");
				e1.printStackTrace();
			}
			
	    	if(responseLine != null){
	        	if (responseLine.equals(OpenWebNet.MSG_OPEN_OK)){
	        		System.out.println("Rx: " + responseLine);
	        		System.out.println("Comando inviato correttamente");
	        		return 0;
	        		//break;
	        	}else if(responseLine.equals(OpenWebNet.MSG_OPEN_KO)){
	        		System.out.println("Rx: " + responseLine);
	        		System.out.println("Comando NON inviato correttamente");
	        		return 0;
	        		//break;
	        	}else{
		
					//RICHIESTA STATO
	        		System.out.println("Rx: " + responseLine);
	        		if(responseLine == OpenWebNet.MSG_OPEN_OK){
	        			System.out.println("Comando inviato correttamente");
		        		return 0;
		        		//break;
	        		}else if(responseLine == OpenWebNet.MSG_OPEN_KO){
	        			System.out.println("Comando NON inviato correttamente");
		        		return 0;
		        		//break;
	        		}
	        	}
	    	}else{
	    		System.out.println("Impossibile inviare il comando");
	    		return 1;
	    		//break;
	    	}
		}while(true); 
	}//chiude metodo invia(...)
		
	
	/**
	 * Attiva il thread per il timeout sulla risposta inviata dal WebServer.
	 * 
	 * @param tipoSocket: 0 se Ã¨ socket comandi, 1 se Ã¨ socket monitor
	 */
	public void setTimeout(int tipoSocket){
		timeoutThread = null;
		timeoutThread = new NewThread("timeout",tipoSocket);
		timeoutThread.start();
	}
	
}//chiuse la classe
