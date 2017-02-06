package com.fabiani.domohome.model;

/***************************************************************************
 * 			                 Monitorizza.java                              *
 * 			              --------------------------                       *
 *   date          : Sep 6, 2004                                           *
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

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 * Description:
 * Gestisce tramite un thread la ricezione di tutti i messaggi che passano 
 * sulla socket monitor
 * 
 */
public class Monitorizza extends Thread{
	Socket socketMon = null;
    BufferedReader inputMon = null;
    int num = 0;
    int indice = 0;
    boolean esito = false;
    char risposta[] = null;
	char c = ' ';
	int ci = 0;
	String responseString = null;
    
    //String responseLine = null; //stringa in ricezione dal Webserver
    /**
     * Costruttore
     */
	public Monitorizza(Socket sock, BufferedReader inp){
		socketMon = sock;
        inputMon = inp;
    }
	
	/**
	 * Avvia il Thread per la ricezione dei messaggi sulla monitor
	 */
	public void run(){
			do{
				GestioneSocketMonitor.responseLineMon = null;
		        num = 0;
		        indice = 0;
		        esito = false;
		        risposta = new char[1024];
		    	c = ' ';
		    	ci = 0;
		    	try{
			    	do{ 
			    		if(socketMon != null && !socketMon.isInputShutdown()){
			    			ci = inputMon.read();
			    		
				    		if (ci == -1){
					        	num = 0;    
					  			indice = 0;
					  			c = ' ';
					  			System.out.println("Mon: ----- Socket chiusa dal server -----");
					  			socketMon = null;
								GestioneSocketMonitor.statoMonitor=0;
					  			break;
					        }else{ 
					        	c = (char)ci;  
					        	//System.out.println("Carattere ricevuto:  "+c);				        				        
							    if (c == '#' && num == 0){
							    	risposta[indice] = c;      		
							    	num = indice;
							    	indice = indice +1;
							    }else if (c == '#' && indice == num + 1){
							    	risposta[indice] = c;
							    	esito = true;
							    	break;
							    }else if (c != '#'){
							    	risposta[indice] = c;
							    	num = 0;
							    	indice = indice + 1;
							    }
					        }
			    		}
			        }while(!esito);
				}catch(IOException e){
					/**System.out.println("Mon eccezione: ");
					e.printStackTrace();*/
			    }
				
				if (esito){
					responseString = new String(risposta,0,indice+1);
					GestioneSocketMonitor.responseLineMon = responseString;
				}else{
					GestioneSocketMonitor.responseLineMon = null;
					GestioneSocketMonitor.statoMonitor=0;
					break;
				}

				System.out.println("Mon: "+ GestioneSocketMonitor.responseLineMon);
				risposta = null;

			}while(GestioneSocketMonitor.statoMonitor==3);

			System.out.println("Thread Monitorizza terminato");
		}
}
