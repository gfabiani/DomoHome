/***************************************************************************
 *                            InterfaceOpenWebNet.java                     *
 *                    --------------------------                           *
 *   date          : Jun 15, 2005                                          *
 *   copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy          *
 *                   Embedded Software Development Laboratory              *
 *   license       : GPL                                                   *
 *   email         :                                                       *
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
//package client_java_core.core;
package com.fabiani.domohome.app.model;

/**
 * Description:
 * 
 * 
 */
public interface InterfaceOpenWebNet {
    
	int MAX_LENGTH_OPEN	= 1024;
	int MAX_LENGTH			=	30;
	int MAX_NUM_VALORI		=	20;
	int MAX_INDIRIZZO		=	4;
	int ERROR_FRAME  		=	1;
	int NULL_FRAME     	=   2;
	int NORMAL_FRAME    	=	3;
	int MEASURE_FRAME   	=	4;
	int STATE_FRAME		=	5;
	int OK_FRAME			=   6;
	int KO_FRAME			=   7;
	int WRITE_FRAME		=	8;
	int PWD_FRAME			=	9;
	String MSG_OPEN_OK 	= "*#*1##";
	String MSG_OPEN_KO	    = "*#*0##";
	
	void createNullMsgOpen();
	//open normale
	void createMsgOpen(String who, String what, String where, String when);
	void createMsgOpen(String who, String what, String where, String lev, String interfac, String when);
	//richiesta stato
	void CreateStateMsgOpen(String who, String where);
	void CreateStateMsgOpen(String who, String where, String lev, String interfac);
	//richiesta grandezza
	void CreateDimensionMsgOpen(String who, String where, String dimension);
	void CreateDimensionMsgOpen(String who, String where, String lev, String interfac, String dimension);
	//scrittura grandezza
	void CreateWrDimensionMsgOpen(String who, String where, String dimension, String value[], int numValue);
	void CreateWrDimensionMsgOpen(String who, String where, String lev, String interfac, String dimension, String value[], int numValue);
	//open generale
	void createMsgOpen(String message);
	
		
}
