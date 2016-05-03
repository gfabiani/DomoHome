package com.fabiani.domohome.app.model;

/***************************************************************************
 * 			              GestioneSocketMonitor.java                       *
 * 			              --------------------------                       *
 *   date          : Sep 6, 2004                                          *
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

import com.bticino.openwebnet.OpenWebNetUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;


/**
 * Description:
 * Gestione della socket Monitor, apertura monitor, chiusura monitor
 * 
 */
public class GestioneSocketMonitor extends Observable {
	static final String socketMonitor = "*99*1##";
	static ReadThread readThMon = null; //thread per la ricezione dei caratteri inviati dal webserver
	static NewThread timeoutThreadMon = null; //thread per la gestione dei timeout
	static int  statoMonitor = 0;  //stato socket monitor
	public static Socket socketMon = null;
	public static String responseLineMon = null; //stringa in ricezione dal Webserver
	BufferedReader inputMon = null;
	PrintWriter outputMon = null;
	Monitorizza monThread = null;

	/**
	 * Tentativo di apertura socket monitor verso il webserver
	 *
	 * @param ip Ip del webserver al quale connettersi
	 * @param port porta sulla quale aprire la connessione
	 * @param passwordOpen password open del webserver
	 * @return true se la connessione va a buon fine, false altrimenti
	 */
	public boolean connect(String ip, int port, int passwordOpen){ //tipo rappresenta socket comandi o monitor
		try {
			System.out.println("Mon: Tentativo connessione a " + ip + "  Port: " + port);
			socketMon = new Socket(ip, port);
			inputMon= new BufferedReader(new InputStreamReader(socketMon.getInputStream()));
			System.out.println("Mon: Buffer reader creato");
			outputMon = new PrintWriter(socketMon.getOutputStream(),true);
			System.out.println("Mon: Print Writer creato");
		}catch (IOException e){
			setChanged();
			notifyObservers(e);
			System.out.println("Mon: Impossibile connettersi con host " + ip + "\n");
			this.close();
		}

		if(socketMon != null){
		    while(true){
		    	readThMon = null;
				readThMon = new ReadThread(socketMon,inputMon,1);
				readThMon.start();
				try{
					readThMon.join();
				}catch (InterruptedException e1) {

					System.out.println("Mon: ----- ERRORE readThread.join() durante la connect:");
					e1.printStackTrace();
				}

				if(responseLineMon != null){
		    		if (statoMonitor == 0 ){
			        	System.out.println("\nMon: ----- STATO 0 ----- ");
			        	System.out.println("Mon: Rx: " + responseLineMon);
			            if (responseLineMon.equals(OpenWebNet.MSG_OPEN_OK)) {
			            	System.out.println("Mon: Tx: " + socketMonitor);
			            	outputMon.write(socketMonitor); //comandi
			            	outputMon.flush();
			            	statoMonitor = 1; //setto stato autenticazione
			            	setTimeout(1);
			            }else{
			            	//se non mi connetto chiudo la socket
			                System.out.println("Mon: Chiudo la socket verso il server ");
			                this.close();
			            	break; 
			            }
					} else if (statoMonitor == 1){ 
						System.out.println("\nMon: ----- STATO 1 -----");
						System.out.println("Mon: Rx: " + responseLineMon);						 
						
					    	//applico algoritmo di conversione
							System.out.println("Controllo sulla password");
							//long risultato = gestPassword.applicaAlgoritmo(passwordOpen, responseLine);
							Long seed = Long.valueOf(responseLineMon.substring(2, responseLineMon.length() - 2));
							System.out.println("Tx: " + "seed=" + seed);
							Long risultato = OpenWebNetUtils.passwordFromSeed(seed, passwordOpen);
							System.out.println("Tx: " + "*#" + risultato + "##");
							outputMon.write("*#" + risultato + "##");
					    	outputMon.flush();
					    	statoMonitor = 2; //setto stato dopo l'autenticazione 
				        	setTimeout(1);

				    } else if(statoMonitor == 2){
				    	System.out.println("\nMon: ----- STATO 2 -----");
				    	System.out.println("Mon: Rx: " + responseLineMon);
				    	if (responseLineMon.equals(OpenWebNet.MSG_OPEN_OK)) {
			               	System.out.println("Mon: Monitor attivata con successo");
			               	statoMonitor = 3;
			               	break;
			            }else{
			            	System.out.println("Mon: Impossibile attivare la monitor");
			               	//se non mi connetto chiudo la socket
			               	System.out.println("Mon: Chiudo la socket monitor\n");
			               	this.close();
			               	break;			               	
			            }
				    } else break; //non dovrebbe servire (quando passo per lo stato tre esco dal ciclo con break)
		    	}else{
		    		System.out.println("Mon: Risposta dal webserver NULL");
		    		this.close();
		    		break;//ramo else della funzione riceviStringa()
		    	}
		    }//chiude while(true)
		}else{
			//System.out.println("$$$$$$$");
		}
		
		if(statoMonitor == 3){
			monThread = null;
			monThread = new Monitorizza(socketMon, inputMon);
			monThread.start();		
		}   
		
		if (statoMonitor == 3) return true;
		else return false;
		
	}//chiude connect()
	
	
	/**
	 * Chiude la socket monitor ed imposta statoMonitor = 0
	 *
	 */
	public void close(){
		if(socketMon != null){
			try { 
				socketMon.close();
				socketMon = null;
				statoMonitor = 0;
				System.out.println("MON: Socket monitor chiusa correttamente-----\n");				
			} catch (IOException e) {

				System.out.println("MON: Errore chiusura Socket: <GestioneSocketMonitor>");
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Attiva il thread per il timeout sulla risposta inviata dal WebServer.
	 * 
	 * @param tipoSocket: 0 se è socket comandi, 1 se è socket monitor
	 */
	public void setTimeout(int tipoSocket){
		timeoutThreadMon = null;
		timeoutThreadMon = new NewThread("timeout",tipoSocket);
		timeoutThreadMon.start();
	}
}




