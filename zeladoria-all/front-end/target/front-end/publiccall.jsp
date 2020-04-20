<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Registro de Chamado</title>

<!-- CSS -->
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
	<div class="top-content">

		<div class="inner-bg">
			<div class="container">
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 form-box">
						<div class="form-top">
							<div class="form-top-left">
								<h3>Registro de Chamado</h3>
								<p>Para registar um chamado, preencha os campos:</p>
							</div>
							<div class="form-top-right">
								<i class="fa fa-lock"></i>
							</div>
						</div>
						<div class="form-bottom">
							<form role="form" action="" method="post" class="login-form" onkeypress="return event.keyCode != 13;">
								<div class="form-group">
									<label for="form-description">Descrição</label>
									<textarea name="form-description" placeholder="Descrição..."
										class="form-description form-control" id="form-description"> </textarea>
								</div>
								<div class="form-group">
									<label for="form-cpf">Rua</label> <input type="text"
										name="form-cpf" placeholder="Rua..."
										class="form-cpf form-control" id="form-cpf">
								</div>
								<div class="form-group">
									<label class="" for="form-email">Número</label> <input
										type="text" name="form-email" placeholder="Número..."
										class="form-email form-control" id="form-email">
								</div>
								<div class="form-group">
									<label class="" for="form-email">Bairro</label> <input
										type="text" name="form-email" placeholder="Bairro..."
										class="form-email form-control" id="form-email"> <input
										type="checkbox" id="envio" name="envio"> Não informar
									mídias
								</div>
								<div class="col-sm-6">

									<label for="sel1">Entidade:</label> <select
										class="form-control" id="sel1">
										<option>1</option>
										<option>2</option>
										<option>3</option>
										<option>4</option>
									</select>

								</div>

								<div class="col-sm-6">
									<input type="checkbox" id="envio" name="envio"> Chamado
									Sigiloso? <br /> <br />

								</div>

								<br /> <br />
								<div class="col-sm-3"></div>
								<button type="" class="btn btn-green">OK</button>
								<button type="" class="btn">Cancelar</button>
								<div class="col-sm-3"></div>




							</form>
						</div>
					</div>
				</div>

			</div>
		</div>

	</div>


	<!-- Javascript -->
	<script src="assets/public/js/jquery-1.11.1.min.js"></script>
	<script src="assets/public/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/public/js/jquery.backstretch.min.js"></script>
	<script src="assets/public/js/scripts.js"></script>

	<!--[if lt IE 10]>
            <script src="assets/js/placeholder.js"></script>
        <![endif]-->

</body>

</html>