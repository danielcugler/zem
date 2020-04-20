<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Identificação do Usuário Mobile</title>

<!-- CSS -->
<link rel="stylesheet"
	href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500" />
<link rel="stylesheet"
	href="assets/public/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="assets/public/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" href="assets/public/css/form-elements.css" />
<link rel="stylesheet" href="assets/public/css/style.css" />
<link rel="stylesheet" href="assets/libs/nifty-modal/css/component.css" />

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

<!-- Favicon and touch icons -->
<link rel="shortcut icon" href="assets/ico/favicon.png">
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="assets/public/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="assets/public/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="assets/public/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed"
	href="assets/public/ico/apple-touch-icon-57-precomposed.png">

</head>

<body>

	<!-- Modal Start -->
	<%@ include file="/logout.jsp"%>
	<!-- Modal Logout -->
	<div class="md-modal md-just-me" id="logout-modal"></div>
	<!-- Modal End -->
	<!-- Begin page -->

	<!-- Top content -->
	<div class="top-content">

		<div class="inner-bg">
			<div class="container">
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 form-box">
						<div class="form-top">
							<div class="form-top-left">
								<h3>Identificação do Usuário Mobile</h3>
								<p>Informe seu login e senha para registrar um chamado:</p>
							</div>
							<div class="form-top-right">
								<i class="fa fa-lock"></i>
							</div>
						</div>
						<div class="form-bottom">
							<form role="form" action="" method="post" class="login-form">
								<div class="form-group">
									<label class="sr-only" for="form-username">Login</label> <input
										type="text" name="form-username" placeholder="Login..."
										class="form-username form-control" id="cUsername"
										maxlength="40">
								</div>
								<div class="form-group">
									<label class="sr-only" for="form-password">Senha</label> <input
										type="password" name="form-password" placeholder="Senha..."
										class="form-password form-control" id="cPassword"
										maxlength="12">
								</div>
								<div class="col-sm-3"></div>
								<div class="col-sm-3">
									<button type="button" class="btn btn-green" id="login">OK</button>
								</div>
								<div class="col-sm-3">
									<a href="identification.jsp"><button type="button"
											class="btn btn-danger">Cancelar</button></a>
								</div>
								<br /> <br />
							</form>
						</div>

					</div>
				</div>
			</div>

		</div>

		<div class='modal fade in' role='dialog' id='modaldemensagem'
			aria-hidden='true'>
			<div class='modal-dialog'>
				<div class='modal-content'>
					<div id="notificacoes"></div>
				</div>
			</div>
		</div>
	</div>

	<!-- End of page -->
	<!-- the overlay modal element -->
	<div class="md-overlay"></div>
	<!-- End of eoverlay modal -->

	<!-- Javascript -->
	<script src="assets/public/js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>
	<script src="assets/public/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/public/js/jquery.backstretch.min.js"></script>

	<script src="assets/public/js/scripts.js"></script>
	<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<script type="text/javascript" src="js/template.js"></script>
	<script type="text/javascript" src="js/abstractCRUD.js"></script>
	<script type="text/javascript" src="js/mobileUser.js"></script>

	<!--[if lt IE 10]>
            <script src="assets/js/placeholder.js"></script>
        <![endif]-->

</body>

</html>