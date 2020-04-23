package com.poloproduction.forms;

import javax.servlet.http.HttpServletRequest;

public class ConnectionForm {
	
	private String resultat;
	
	public void verifierIdentifiants( HttpServletRequest request ) {
		String login = request.getParameter("login");
		String pass = request.getParameter("pass");
		
		if(pass!=null && pass.equals("123")) {
			resultat = "OK, vous êtes indentifié";
		} else {
			resultat = "Non, vous n'êtes pas identifié";
		}
	}

	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

}
