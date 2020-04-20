<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Identificação do Usuário Mobile</title>

<!-- CSS -->
<link href="assets/libs/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" />
<link href="assets/libs/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
<link rel="stylesheet"
	href="assets/public/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="assets/public/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="assets/public/css/form-elements.css">
<link rel="stylesheet" href="assets/public/css/style.css">

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

	<!-- Top content -->
	<div  id="generalModule" class="top-content">

		<div class="inner-bg">
			<div class="container">
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 form-box">
						<div class="form-top">
							<div class="form-top-left">
								<h3>Identificação do Usuário</h3>
								<p>Informe seus dados para registrar um chamado:</p>
							</div>
							<div class="form-top-right">
								<i class="fa fa-lock"></i>
							</div>
						</div>
						<div class="form-bottom">
							<form role="form" action="" method="post" class="login-form"
								id="loginForm" onkeypress="return event.keyCode != 13;">
								<div class="form-group">
									<label class="sr-only" for="form-name">Nome</label> <input
										type="text" name="inName" placeholder="Nome..."
										class="form-name form-control" id="inName" maxlength="100">
								</div>
								<div class="form-group">
									<label class="sr-only" for="form-cpf">CPF</label> <input
										type="text" name="inCpf" placeholder="CPF..."
										class="form-cpf form-control" id="inCpf">
								</div>
								<div class="form-group">
									<label class="sr-only" for="form-email">E-mail</label> <input
										type="text" name="inEmail" placeholder="E-mail..."
										class="form-email form-control" id="inEmail" maxlength="100">
								</div>
								<div class="col-sm-3"></div>
								<div class="col-sm-3">
									<button type="button" class="btn btn-green" id="btAdd">OK</button>
								</div>
								<div class="col-sm-3">
									<a href="identification.jsp">
										<button type="button" class="btn btn-danger" id="cancel">Cancelar</button>
									</a>
								</div>
								<br /> <br />
							</form>
						</div>

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
	</div>

			<script id="message-template" type="text/x-handlebars-template">
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>X</button> {{title}}
				 </div>
				<div class='modal-body'> {{body}}
				</div>
				<div class='modal-footer'>
				<a href="../entitycategory.jsp"  style="display: none;" class='btn btn-success dbOk' role='button' id='bOk1'>Ok</a>
<a class='btn btn-success dbOk' data-dismiss='modal'  style="display: none;" role='button' id='bOk2'>Ok</a>
			<a class='btn btn-danger' data-dismiss='modal' style="display: none;" id="bOk3" role='button'>Ok!</a>
&nbsp&nbsp&nbsp
				</div>
			</script>


	<!-- Javascript -->
		<script src="assets/libs/jquery/jquery-2.1.4.min.js"></script>
	<script src="assets/libs/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/libs/jqueryui/jquery-ui-1.10.4.custom.min.js"></script>
	<script src="assets/libs/jquery-ui-touch/jquery.ui.touch-punch.min.js"></script>
	
<!-- 	<script src="assets/public/js/jquery-1.11.1.min.js"></script>   -->
	<script src="assets/public/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/public/js/jquery.backstretch.min.js"></script>
<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<script src="assets/public/js/scripts.js"></script>

	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>
	<script type="text/javascript" src="js/libs/jquery.maskedinput.js"></script>
	<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>

	<script type="text/javascript" src="js/handlebarsHelpers.js"></script>
	<script type="text/javascript" src="js/ctrlWUpublic.js"></script>

	<!--[if lt IE 10]>
            <script src="assets/ js/placeholder.js"></script>
        <![endif]-->

</body>

</html>