<ul>
	<li><a href="/PoloProject/">Accueil</a></li>
	<li><a href="/PoloProject/bonjour?name=Polo">Bonjour</a></li>
	<li>Page 3</li>
	<c:if test="${ !empty sessionScope.prenom && !empty sessionScope.nom }">
		<li>Vous êtes ${ sessionScope.prenom } ${ sessionScope.nom } !</li>
	</c:if>
</ul>