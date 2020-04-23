package com.poloproduction.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.poloproduction.bdd.Noms;
import com.poloproduction.beans.Auteur;
import com.poloproduction.beans.BeanException;
import com.poloproduction.beans.Utilisateur;
import com.poloproduction.dao.DaoException;
import com.poloproduction.dao.DaoFactory;
import com.poloproduction.dao.UtilisateurDao;
import com.poloproduction.forms.ConnectionForm;

@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final int TAILLE_TAMPON = 10240;
	public static final String CHEMIN_FICHIERS = "C:/Users/jarde/UploadsJava/"; // A changer

	private UtilisateurDao utilisateurDao;

	public Test() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		this.utilisateurDao = daoFactory.getUtilisateurDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		fillRequest(request, response);

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("prenom")) {
					request.setAttribute("prenom", cookie.getValue());
				}
			}
		}

		try {
			// JDBC
			Noms tableNoms = new Noms();
			request.setAttribute("utilisateurs", tableNoms.recupererUtilisateurs());

			// DAO
			request.setAttribute("utilisateurs", utilisateurDao.lister());
		}
		catch (DaoException | BeanException e) {
			request.setAttribute("erreur", e.getMessage());
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/bonjour.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		fillRequest(request, response);

		String nomForm = request.getParameter("nomForm");
		request.setAttribute("nomForm", nomForm);

		ConnectionForm connectionForm = new ConnectionForm();
		connectionForm.verifierIdentifiants(request);
		request.setAttribute("connectionForm", connectionForm);

		if (request.getParameterMap().containsKey("fichier")) {
			uploadFile(request);
		}

		useSession(request);

		addCookie(request, response);

		try {
			// JDBC
			if(null!=request.getParameter("nomJdbc")) {
				Utilisateur utilisateurJdbc = new Utilisateur();
				utilisateurJdbc.setNom(request.getParameter("nomJdbc"));
				utilisateurJdbc.setPrenom(request.getParameter("prenomJdbc"));
				Noms tableNoms = new Noms();
				tableNoms.ajouterUtilisateur(utilisateurJdbc);
				request.setAttribute("utilisateurs", tableNoms.recupererUtilisateurs());
			}

			// DAO
			if(null!=request.getParameter("nomDao")) {
				Utilisateur utilisateurToDao = new Utilisateur();
				utilisateurToDao.setNom(request.getParameter("nomDao"));
				utilisateurToDao.setPrenom(request.getParameter("prenomDao"));
				utilisateurDao.ajouter(utilisateurToDao);
				request.setAttribute("utilisateurs", utilisateurDao.lister());
			}
		}
		catch (Exception e) {
			request.setAttribute("erreur", e.getMessage());
		}


		this.getServletContext().getRequestDispatcher("/WEB-INF/bonjour.jsp").forward(request, response);
	}

	private void fillRequest(HttpServletRequest request, HttpServletResponse response) {

		String name = request.getParameter("name");
		request.setAttribute("name", name);
		String[] noms = {"Mathieu", "Robert", "François"};
		request.setAttribute("noms", noms);

		Auteur auteur = new Auteur();
		auteur.setPrenom("Paul");
		auteur.setNom("Jardel");
		auteur.setActif(true);
		request.setAttribute("auteur", auteur);

		String[] titres = {"GG", "Ouais, ouais", ":D"};
		request.setAttribute("titres", titres);
	}

	private void ecrireFichier( Part part, String nomFichier, String chemin ) throws IOException {
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {
			entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
			sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);

			byte[] tampon = new byte[TAILLE_TAMPON];
			int longueur;
			while ((longueur = entree.read(tampon)) > 0) {
				sortie.write(tampon, 0, longueur);
			}
		} finally {
			try {
				sortie.close();
			} catch (IOException ignore) {
			}
			try {
				entree.close();
			} catch (IOException ignore) {
			}
		}
	}

	private static String getNomFichier( Part part ) {
		for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
			if ( contentDisposition.trim().startsWith( "filename" ) ) {
				return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
			}
		}
		return null;
	}

	private void uploadFile(HttpServletRequest request) throws ServletException, IOException {
		// On récupère le champ description comme d'habitude
		String description = request.getParameter("description");
		request.setAttribute("description", description );

		// On récupère le champ du fichier
		Part part = request.getPart("fichier");

		// On vérifie qu'on a bien reçu un fichier
		String nomFichier = getNomFichier(part);

		// Si on a bien un fichier
		if (nomFichier != null && !nomFichier.isEmpty()) {
			String nomChamp = part.getName();
			// Corrige un bug du fonctionnement d'Internet Explorer
			nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1)
					.substring(nomFichier.lastIndexOf('\\') + 1);

			// On écrit définitivement le fichier sur le disque
			ecrireFichier(part, nomFichier, CHEMIN_FICHIERS);

			request.setAttribute(nomChamp, nomFichier);
		}
	}

	private void useSession(HttpServletRequest request) {
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");

		HttpSession session = request.getSession();

		session.setAttribute("nom", nom);
		session.setAttribute("prenom", prenom);
	}

	private void addCookie(HttpServletRequest request, HttpServletResponse response) {
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");

		Cookie cookie = new Cookie("prenom", prenom);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		response.addCookie(cookie);
	}   
}
