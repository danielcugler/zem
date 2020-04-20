<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Labitec">
<link rel="shortcut icon" href="#" type="image/png">

<title>Login</title>

<link href="assets/login/css/style.css" rel="stylesheet">
<link href="assets/login/css/style-responsive.css" rel="stylesheet">
<link href="assets/css/custom.css" rel="stylesheet" type="text/css" />

</head>

<body class="login-body">

	<div id="generalModule" class="container">

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>

		<form class="form-signin" action="<c:url value='/login'/>"
			method='POST' name="loginForm">

			<div class="form-signin-heading text-center">

				<img src="assets/login/images/login-logo.png" alt="" />
			</div>

			<div class="login-wrap">

				<input type="text" class="form-control" placeholder="Usuário"
					id="username" name='username' value='' minlength="3" maxlength="20">
				<div id="erroUsuario" class="vermelho"></div>
				<input type="password" class="form-control" placeholder="Senha"
					id="password" name='password' minlength="8" maxlength="12">
				<div id="erroSenha" class="vermelho"></div>
				<div style="display:none">
				<input type="checkbox" name="remember-me" /> Permanecer conectado
				</div>
				<button class="btn btn-lg btn-login btn-block" name="submit"
					type="submit" value="submit">
					<i class="fa fa-check"></i>
				</button>

				<br /> <span class="pull-right"> <a data-toggle="modal"
					href="#myModal"> <strong>Esqueceu a senha?</strong></a>
				</span> <br /> <input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />

			</div>

		</form>

		<!-- Modal -->
		<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog"
			tabindex="-1" id="myModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title">Esqueceu a senha?</h4>
					</div>
					<div class="modal-body">
						<p>Insira seu usuário para recuperar a senha:</p>
						<input type="text" required id="username" name="username"
							placeholder="Usuário" autocomplete="off"
							class="form-control placeholder-no-fix">

					</div>
					<div class="modal-footer">
						<button class="btn btn-success tamanho-botao" type="button"
							id="forgot" data-dismiss='modal'>Enviar</button>
						<button data-dismiss="modal" class="btn btn-danger tamanho-botao"
							type="button">Cancelar</button>

					</div>
				</div>
			</div>
		</div>

		<div class='modal fade in' role='dialog' id='modalE'
			aria-hidden='true'>
			<div class='modal-dialog'>
				<div class='modal-content'>
					<div id="notificacoesE"></div>
				</div>
			</div>
		</div>

		<div class='modal fade in' role='dialog' id='modalLogin'
			aria-hidden='true'>
			<div class='modal-dialog'>
				<div class='modal-content'>
					<div class='modal-header'>
						<button class='close' aria-hidden='true' data-dismiss='modal'
							type='button'>×</button>
						Erro
					</div>
					<div class='modal-body'>Login ou senha incorretos.</div>
				</div>
			</div>
		</div>
		<!-- modal -->


	</div>



	<!-- Placed js at the end of the document so the pages load faster -->
	<script id="loginerror-template" type="text/x-handlebars-template">
	<div class="modal-header"><button class="close" aria-hidden="true" data-dismiss="modal" type="button">×</button>
			Erro</div>
			<div class="modal-body">{{message}}
			</div>
			<div class="modal-footer"><a data-dismiss="modal" data-toggle="modal" href="#myModal" class="btn btn-success"  role="button">Ok!</a>
			</div>
</script>

	<script id="loginsuccess-template" type="text/x-handlebars-template">
<div class="modal-header"><button class="close" aria-hidden="true" data-dismiss="modal" type="button">×</button>
			Recuperação de Senha</div>
			<div class="modal-body">{{body}}
			</div>
			<div class="modal-footer"><a class="btn btn-success" data-dismiss="modal" role="button">Ok!</a>
			</div>
</script>

	<script id="message-template" type="text/x-handlebars-template">
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal'  type='button'>X</button> {{title}}
				 </div>
				<div class='modal-body'> {{body}}
				</div>
				<div class='modal-footer'>
				<a class='btn btn-success dbOk' role='button' data-dismiss='modal' id='bOk'>Ok</a>&nbsp&nbsp&nbsp
				</div>
			</script>


	<!-- Placed js at the end of the document so the pages load faster -->
	<script src="assets/login/js/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>
	<script src="assets/login/js/bootstrap.min.js"></script>
	<script src="assets/login/js/modernizr.min.js"></script>
	<script type="text/javascript" src="js/zemUtils.js"></script>
	
	<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<script src="js/ctrlLogin.js"></script>

</body>
</html>
