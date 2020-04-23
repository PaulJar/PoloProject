<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Test</title>
</head>
<body>
	<%@ include file="menu.jsp"%>

	<h2>Java on HTML</h2>
	<p>MVC = Model View Controller</p>
	<p>Model = java beans / BDD</p>
	<p>View = jsp</p>
	<p>Controller = servlet</p>
	<h2>Java on HTML</h2>
	<p>
		Ceci est une mauvaise pratique (mélange code Java / code html dans une
		jsp) : <br />
		<%
			String name = (String) request.getAttribute("name");
		out.println(name);
		%>
	</p>

	<h2>Expression Language (EL)</h2>
	<p>
		Ceci est une bonne pratique (Expression Language = EL, avec "$ + { +
		EL + }" ) : <br /> Bonjour ${ !empty name ? name : '' }
	</p>
	<p>${ noms[2] }</p>

	<p>Bonjour ${ auteur.prenom } ${ auteur.nom }</p>
	<p>${ auteur.actif ? 'Vous êtes très actif !' : 'Vous êtes inactif !' }</p>

	<h2>JSTL</h2>
	<h3>JSTL - Mise en place</h3>
	<p>JSTL = JavaServer Standard Tag Library</p>
	<p>5 sous bibliothèques (core, format, xml, sql, function)</p>
	<p>Protection faille XSS</p>
	<p>
		<c:out value="Bonjour !" />
	</p>
	<h3>JSTL & Variables</h3>
	<p>
		<c:out value="${ variable }">Valeur par défaut</c:out>
	</p>
	<c:set var="pseudo" value="Polo" scope="page" />
	<p>Variable définie dans le tag JSTL (ici scope = page / aussi :
		request, session, application)</p>
	<p>
		<c:out value="${ pseudo }">Valeur par défaut</c:out>
	</p>
	<p>Modification de Beans :</p>
	<c:set target="${ auteur }" property="prenom" value="Polo2" />
	<p>
		<c:out value="${ auteur.prenom }" />
	</p>
	<p>Supprimer une variable</p>
	<c:remove var="pseudo" scope="page" />
	<p>
		<c:out value="${ pseudo }">Valeur par défaut</c:out>
	</p>
	<h3>JSTL & Conditions</h3>
	<p>Défaut du if : pas de else ni de esle if</p>
	<c:if test="${ 50 > 10 }" var="variable">
    	C'est vrai !
	</c:if>
	<br />
	<c:choose>
		<c:when test="${ variable }">Du texte 1</c:when>
		<c:when test="${ autreVariable }">Du texte 2</c:when>
		<c:when test="${ encoreUneAutreVariable }">Du texte 3</c:when>
		<c:otherwise>Par défaut</c:otherwise>
	</c:choose>
	<h3>JSTL & Boucles</h3>
	<c:forEach var="i" begin="0" end="10" step="2">
		<p>
			Un message n°
			<c:out value="${ i }" />
			!
		</p>
	</c:forEach>
	<c:forEach items="${ titres }" var="titre" varStatus="status">
		<p>
			N°
			<c:out value="${ status.count }" />
			:
			<c:out value="${ titre }" />
			!
		</p>
	</c:forEach>
	<p>status.count, status.index, status.current, status.first,
		status.last</p>
	<c:forTokens var="morceau"
		items="Un élément/Encore un autre élément/Un dernier pour la route"
		delims="/ ">
		<p>${ morceau }</p>
	</c:forTokens>
	<p>delims="/ " : Découpe à chaque fois qu'il y a un '/' et un
		espace</p>

	<h2>Formulaires</h2>
	<c:if test="${ !empty nomForm }">
		<p>
			<c:out value="Bonjour, vous vous appelez ${ nomForm }" />
		</p>
	</c:if>
	<form method="post" action="bonjour">
		<label for="nomForm">Nom : </label> <input type="text" name="nomForm"
			id="nomForm" /> <input type="submit" />
	</form>
	<c:if test="${ !empty connectionForm.resultat }">
		<p>
			<c:out value="${ connectionForm.resultat }" />
		</p>
	</c:if>
	<form method="post" action="bonjour">
		<p>
			<label for="login">Identifiant : </label> <input type="text"
				name="login" id="login" />
		</p>
		<p>
			<label for="pass">Password (123) : </label> <input type="text"
				name="pass" id="pass" />
		</p>
		<input type="submit" />
	</form>

	<h2>Envoyer des fichiers</h2>
	<c:if test="${ !empty fichier }">
		<p>
			<c:out
				value="Le fichier ${ fichier } (${ description }) a été uploadé !" />
		</p>
	</c:if>
	<form method="post" action="bonjour" enctype="multipart/form-data">
		<p>
			<label for="description">Description du fichier : </label> <input
				type="text" name="description" id="description" />
		</p>
		<p>
			<label for="fichier">Fichier à envoyer : </label> <input type="file"
				name="fichier" id="fichier" />
		</p>

		<input type="submit" />
	</form>

	<h2>Gérer les sessions</h2>
	<p>Stockés côté serveur, le temps de la session</p>
	<c:if test="${ !empty sessionScope.prenom && !empty sessionScope.nom }">
		<p>Vous êtes ${ sessionScope.prenom } ${ sessionScope.nom } !</p>
	</c:if>
	<form method="post" action="bonjour">
		<p>
			<label for="nom">Nom : </label> <input type="text" name="nom"
				id="nom" />
		</p>
		<p>
			<label for="prenom">Prénom : </label> <input type="text"
				name="prenom" id="prenom" />
		</p>

		<input type="submit" />
	</form>

	<h2>Gérer les cookies</h2>
	<p>Stockés côté client, sur son navigateur</p>
	<c:out value="${ prenom }" />
	<form method="post" action="bonjour">
		<p>
			<label for="nom">Nom : </label> <input type="text" name="nom"
				id="nom" />
		</p>
		<p>
			<label for="prenom">Prénom : </label> <input type="text"
				name="prenom" id="prenom" />
		</p>

		<input type="submit" />
	</form>

	<h2>Travailler avec JDBC (BDD)</h2>
	<p>But : se connecter à n'importe quelle BDD</p>
	<form method="post" action="bonjour">
		<p>
			<label for="nomJdbc">Nom : </label> <input type="text" name="nomJdbc"
				id="nomJdbc" />
		</p>
		<p>
			<label for="prenomJdbc">Prénom : </label> <input type="text"
				name="prenomJdbc" id="prenomJdbc" />
		</p>
		<input type="submit" />
	</form>
	<ul>
		<c:forEach var="utilisateur" items="${ utilisateurs }">
			<li><c:out value="${ utilisateur.prenom }" /> <c:out
					value="${ utilisateur.nom }" /></li>
		</c:forEach>
	</ul>

	<h2>Utiliser le modèle DAO (Data Access Object)</h2>
	<p>But : se connecter à n'importe quelle BDD</p>
	<p>Modèle <-> Interface DAO <-> Implémentation DAO A (système de
		stockage A) / Implémentation DAO B (système de stockage B)</p>
	<c:if test="${ !empty erreur }">
		<p style="color: red;">
			<c:out value="${ erreur }" />
		</p>
	</c:if>
	<form method="post" action="bonjour">
		<p>
			<label for="nomDao">Nom : </label> <input type="text" name="nomDao"
				id="nomDao" />
		</p>
		<p>
			<label for="prenomDao">Prénom : </label> <input type="text"
				name="prenomDao" id="prenomDao" />
		</p>

		<input type="submit" />
	</form>

	<ul>
		<c:forEach var="utilisateur" items="${ utilisateurs }">
			<li><c:out value="${ utilisateur.prenom }" /> <c:out
					value="${ utilisateur.nom }" /></li>
		</c:forEach>
	</ul>

</body>
</html>