<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Left Sidebar Start -->
<div class="left side-menu">
	<div class="sidebar-inner slimscrollleft">
		<div class="clearfix"></div>
		<!--- Profile -->
		<br />
		<div class="profile-info">
			<div class="col-xs-4">
				<a href="profile.jsp" class="rounded-image profile-image"><img
					src="images/users/user-100.jpg"></a>
			</div>
			<div class="col-xs-8">
				<div class="profile-text">
					<!-- Nome do usu�rio -->
				</div>
			</div>
		</div>
		<!--- Divider -->
		<div class="clearfix"></div>
		<br />
		<form role="search" class="navbar-form">
			<div class="form-group">
				<input type="text" placeholder="Pesquisar" class="form-control">
				<button type="submit" class="btn search-button">
					<i class="fa fa-search"></i>
				</button>
			</div>
		</form>
		<div class="clearfix"></div>
		<!--- Divider -->
		<div id="sidebar-menu">
			<ul>
				<li class='has_sub'><a href='javascript:void(0);'> <i
						class='icon-home-3'></i> <span>Home</span> <span
						class="pull-right"> <i class="fa fa-angle-down"></i>
					</span></a>
					<ul>
						<li><a href='index.jsp'
							class="item1${pageContext.request.requestURI eq '/index.jsp' ? ' active' : ''}"><span>Dashboard</span></a></li>
					</ul></li>

				<sec:authorize
					access="hasAnyRole('ROLE_ENTITY_READ', 'ROLE_ENTITYCATEGORY_READ')">
					<li class="has_sub"><a href="javascript:void(0);"><i
							class="fa fa-bars"></i><span>Entities</span><span
							class="pull-right"><i class="fa fa-angle-down"></i></span></a>
						<ul id="submenu">
							<sec:authorize access="hasAnyRole( 'ROLE_ENTITY_READ')">
								<li><a href="http://localhost:8080/entity.jsp"
									class="item2${pageContext.request.requestURI eq '/entity.jsp' ? ' active' : ''}"><span>"Entidades"</span></a></li>
							</sec:authorize>
							<sec:authorize access="hasAnyRole( 'ROLE_ENTITYCATEGORY_READ')">
								<li><a href="http://localhost:8080/entitycategory.jsp"
									class="item3${pageContext.request.requestURI eq '/entitycategory.jsp' ? ' active' : ''}"><span>"Categoria
											de Entidade"</span></a></li>
							</sec:authorize>
						</ul></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_MESSAGEMODEL_READ')">
					<li><a href="http://localhost:8080/messagemodel.jsp"
						class="item4${pageContext.request.requestURI eq '/messagemodel.jsp' ? ' active' : ''}"><i
							class="fa fa-envelope-o"></i><span>Modelos de Mensagem</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USER_READ')">
					<li><a href="http://localhost:8080/user.jsp"
						class="item5${pageContext.request.requestURI eq '/user.jsp' ? ' active' : ''}"><i
							class="fa fa-user"></i><span>Usuários</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_CITIZEN_READ')">
					<li><a href="http://localhost:8080/citizen.jsp"
						class="item6${pageContext.request.requestURI eq '/citizen.jsp' ? ' active' : ''}"><i
							class="fa fa-group"></i><span>Cidadãos</span></a></li>
				</sec:authorize>
				<sec:authorize
					access="hasAnyRole('ROLE_CALL_READ', 'ROLE_FOLLOWCALL_READ')">
					<li class="has_sub"><a href="javascript:void(0);"><i
							class="icon-bell-1"></i><span>Chamados</span><span
							class="pull-right"><i class="fa fa-angle-down"></i></span></a>
						<ul id="submenu">
							<sec:authorize access="hasRole('ROLE_CALL_READ')">
								<li><a href="http://localhost:8080/call.jsp"
									class="item8${pageContext.request.requestURI eq '/call.jsp' ? ' active' : ''}"><span>"Moderação
											de CHamados"</span></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('ROLE_FOLLOWCALL_READ')">
								<li><a href="http://localhost:8080/callfollow.jsp"
									class="item9${pageContext.request.requestURI eq '/callfollow.jsp' ? ' active' : ''}"><span>"Acompanhamento
											de Chamados"</span></a></li>
							</sec:authorize>
						</ul></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_COMMUNICATION_READ')">
					<li><a href="http://localhost:8080/broadcastmessage.jsp"
						class="item10${pageContext.request.requestURI eq '/broadcastmessage.jsp' ? ' active' : ''}"><i
							class="icon-megaphone"></i><span>Comunicado em massa</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_INTERNALCALL_READ')">
					<li><a href="http://localhost:8080/internalcall.jsp"
						class="teste"><i class="fa fa-comment"></i><span>Chamado
								Interno</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_PROFILE_READ')">
					<li><a href="http://localhost:8080/sup.jsp"
						class="item11${pageContext.request.requestURI eq '/sup.jsp' ? ' active' : ''}"><i
							class="icon-vcard"></i><span>Perfis</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_LOG_READ')">
					<li><a href="http://localhost:8080/log.jsp"
						class="item12${pageContext.request.requestURI eq '/log.jsp' ? ' active' : ''}"><i
							class="icon-database"></i><span>Log de Operações</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_REPORT_READ')">
					<li><a href="http://localhost:8080/report.jsp"
						class="item13${pageContext.request.requestURI eq '/report.jsp' ? ' active' : ''}"><i
							class="icon-docs"></i><span>Gerar Relatório</span></a></li>
				</sec:authorize>
			</ul>

			<div class="clearfix"></div>
		</div>


		<div id="recent_tickets" class="widget transparent nomargin"></div>
	</div>
	<div class="clearfix"></div>
	<br> <br> <br>
</div>
</div>
<!-- Left Sidebar End -->