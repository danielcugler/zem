<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<head>

<meta charset="utf-8">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
</head>
<body>
	<div style="display: none;">
		<input type="hidden" id="username" name="username"
			value="<sec:authentication property='principal.username'/>" />
		<sec:authentication property="authorities" var="roles" scope="page" />
		<ul id="list">
			<c:forEach var="role" items="${roles}">
				<li>${role}</li>
			</c:forEach>
		</ul>
	</div>

	<div id="addForm">
		<label>Nome</label> <input type="text" id="inName" /> <label>CPF</label>
		<input type="text" id="inCpf" /> <label>Sexo</label> <select
			id="selGender">
			<option value="">Selecione o sexo</option>
			<option value="Masculino">Masculino</option>
			<option value="Feminino">Feminino</option>
		</select> <label>Telefone</label> <input type="text" id="inPhone" /> <label>Email</label>
		<input type="text" id="inEmail" /> <label>Senha</label> <input
			type="text" id="inPass" /> <label>Estado</label> <select
			id="selState">
			<option value="">Selecione o Estado</option>
			<option value="MG">MG</option>
			<option value="SP">SP</option>
			<option value="RS">RS</option>
		</select> <label>Cidade</label> <select id="selCity">
			<option value="">Selecione a Cidade</option>
			<option value="Uberlândia">Uberlândia</option>
			<option value="Patrocínio">Patrocínio</option>
			<option value="Araxá">Araxá</option>
		</select> <label>Bairro</label> <input type="text" id="inNeighboorhood" />
		<button type="button" class="btn btn-primary" id="add">
			Inserir</button>
	</div>

	<div id="loginForm">
		<br /> <br /> <label>Login</label> <input type="text" id="inLogin" />
		<label>Senha</label> <input type="text" id="inLoginPass" />
		<button type="button" class="btn btn-primary" id="login">
			Logar</button>
	</div>
	
	<script src="assets/libs/jquery/jquery-2.1.4.min.js"></script>
	<script src="js/controllerMobile.js"></script>

</body>

</html>