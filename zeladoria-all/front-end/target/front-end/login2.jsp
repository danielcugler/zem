<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="description" content="Zeladoria Urbana Participativa">
<meta name="keywords"
	content="zeladoria urbaba participativa, zeladoria urbana">
<meta name="author" content="LabITec">
<link rel="shortcut icon" href="#" type="image/png">

<title>Login</title>

<link href="assets/login/css/style.css" rel="stylesheet">
<link href="assets/login/css/style-responsive.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
</head>

<body class="login-body">

	<div class="container">

		<form class="form-signin" action="index.jsp">
			<div class="form-signin-heading text-center">

				<img src="assets/login/images/login-logo.png" alt="" />
			</div>
			<div class="login-wrap">
				<input type="text" class="form-control" placeholder="Usuário"
					autofocus id="user"> <input type="password" class="form-control"
					placeholder="Senha" id="password">

				<button class="btn btn-lg btn-login btn-block" type="submit" id="submit">
					<i class="fa fa-check"></i>
				</button>


				<label class="checkbox"> <span class="pull-right"> <a
						data-toggle="modal" href="#myModal"> Esqueceu a senha?</a>

				</span>
				</label>

			</div>

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
							<p>Insira seu email para recuperar a senha.</p>
							<input type="text" name="email" placeholder="Email"
								autocomplete="off" class="form-control placeholder-no-fix">

						</div>
						<div class="modal-footer">
							<button data-dismiss="modal" class="btn btn-default"
								type="button">Cancelar</button>
							<button class="btn btn-primary" type="button">Enviar</button>
						</div>
					</div>
				</div>
			</div>
			<!-- modal -->

		</form>

	</div>



	<!-- Placed js at the end of the document so the pages load faster -->

	<!-- Placed js at the end of the document so the pages load faster -->
	<script src="assets/login/js/jquery-1.10.2.min.js"></script>
	<script src="assets/login/js/bootstrap.min.js"></script>
	<script src="assets/login/js/modernizr.min.js"></script>
	<script src="js/abstractCRUD.js"></script>
	<script src="js/userLogin.js"></script>

</body>
</html>
