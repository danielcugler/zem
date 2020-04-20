<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Opção de Identificação</title>

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
	href="assets/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="assets/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="assets/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed"
	href="assets/ico/apple-touch-icon-57-precomposed.png">

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
								<h3>Opção de Identificação</h3>
								<p>Como você deseja se identificar?</p>
							</div>
							<div class="form-top-right">
								<img src="assets/login/images/login-logo.png" alt="" />
							</div>
						</div>
						<div class="form-bottom">
							<table style="width: 100%">
								<tr>
									<td><button class="btn"
											onclick="location.href='/mobile.jsp'">Sou um usuário
											mobile</button>
										<p></p></td>
									<td><div align="justify">Se você já é um usuário
											mobile, e deseja acompanhar o seu chamado também pelo
											celular, clique em "Sou um usuário mobile".</div></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td><button class="btn"
											onclick="location.href='/useridentification.jsp'">
											Fornecer meus dados</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
									<td><div align="justify">Se você não é um usuário
											mobile, mas deseja acompanhar o seu chamado pelo portal,
											clique em "Fornecer meus dados".</div></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>
										<button class="btn"
											onclick="location.href='/newcall.jsp'">
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Anônimo&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										</button>&nbsp;&nbsp;
									</td>
									<td><div align="justify">Se você deseja apenas
											cadastrar o chamado sem se identificar ou acompanhá-lo,
											clique em "Anônimo".</div></td>

								</tr>
							</table>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 social-login"></div>
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