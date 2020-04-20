<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Top Bar Start -->
<div class="topbar">
	<div class="topbar-left">
		<div class="logo">
			<h1>
				<a href="/index.jsp"><img src="assets/img/logo.png" alt="Logo"></a>
			</h1>
		</div>
		<button class="button-menu-mobile open-left">
			<i class="fa fa-bars"></i>
		</button>
	</div>
	<!-- Button mobile view to collapse sidebar menu -->
	<div class="navbar navbar-default" role="navigation">
		<div class="container">
			<div class="navbar-collapse2">
				<!-- 
				<ul class="nav navbar-nav hidden-xs">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><i class="icon-th"></i></a>
						<div class="dropdown-menu grid-dropdown">
							<div class="row stacked">
								<div class="col-xs-4">
									<a href="javascript:;" data-app="notes-app"
										data-status="inactive"><i class="icon-edit"></i>Anotações</a>
								</div>
								<div class="col-xs-4">
									<a href="javascript:;" data-app="todo-app" data-status="inactive"><i
										class="icon-check"></i>Todo List</a>
								</div>
								<div class="col-xs-4">
									<a href="javascript:;" data-app="calc" data-status="inactive"><i
										class="fa fa-calculator"></i>Calculadora</a>
								</div>
							</div>
							<div class="row stacked">
								<div class="col-xs-4">
									<a href="javascript:;" data-app="weather-widget"
										data-status="inactive"><i class="icon-cloud-3"></i>Temperatura</a>
								</div>
								<div class="col-xs-4">
									<a href="javascript:;" data-app="calendar-widget2"
										data-status="inactive"><i class="icon-calendar"></i>Calendário</a>
								</div>
								<div class="col-xs-4">
									<a href="javascript:;" data-app="stock-app"
										data-status="inactive"><i class="icon-chart-line"></i>Gráficos</a>
								</div>
							</div>
							<div class="clearfix"></div>
						</div></li>
				</ul>
				-->
				<ul class="nav navbar-nav navbar-right top-navbar">

					<li class="dropdown topbar-profile"><a href="#"
						class="dropdown-toggle" data-toggle="dropdown"><span
							class="rounded-image topbar-profile-image"><img
								class="userPhoto" src="images/user.png"></span> <!-- Nome do usuário -->
							<sec:authentication property="principal.username" /> <i
							class="fa fa-caret-down"></i></a>
						<ul class="dropdown-menu">
							<sec:authorize access="hasRole('ROLE_USER_UPDATE')">
								<li><a class="editUser">Perfil</a></li>
							</sec:authorize>
							<li><a href="changepassword.jsp">Alterar Senha</a></li>
							<li><a href="#">Sobre o ZEM</a></li>
							<li class="divider"></li>
							<li><a class="md-trigger" data-modal="logout-modal"><i
									class="icon-logout-1"></i>Sair</a></li>
						</ul></li>

				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
</div>
<!-- Top Bar End -->