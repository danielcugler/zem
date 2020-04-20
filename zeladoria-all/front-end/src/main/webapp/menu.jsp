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
				<a class="rounded-image profile-image editUser"><img class="userPhoto"
					src="images/user.png"></a>
			</div>
			<div class="col-xs-8">
				<div class="profile-text">
					<!-- Nome do usuário -->
					Bem-vindo, &nbsp<b><p>
							<sec:authentication property="principal.username" />
						</p></b>
				</div>
			</div>
		</div>
		<!--- Divider -->
		<div class="clearfix"></div>
		<br />
		<div style="display: none;">
			<sec:authentication property="authorities" var="roles" scope="page" />
			<p id="username" name="username"><sec:authentication property="principal.username" /></p>
			<p id="userEntity" name="userEntity"></p>
			<ul id="list">
				<c:forEach var="role" items="${roles}">
					<li>${role}</li>
				</c:forEach>
			</ul>
		</div>
		
		<div class="clearfix"></div>
		<!--- Divider -->
		<div id="sidebarMenu"></div>


		<div id="recent_tickets" class="widget transparent nomargin"></div>
	</div>
	<div class="clearfix"></div>
	<br> <br> <br>
</div>

<!-- Left Sidebar End -->