<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Modal Logout -->
<div class="md-modal md-just-me" id="logout-modal">
	<div class="md-content">
		<div align="center">
			<img src="assets/login/images/login-logo.png" alt=""/>
		</div>
		<h3>
			Confirmação de <strong>Logout</strong>
		</h3>
		<div>
			<p class="text-center">Tem certeza que deseja sair?</p>
			
				<form class="form-logout" action="<c:url value='/logout'/>" method='POST' name="logoutForm">
					<p class="text-center">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<button type="submit" name="submit" id="submit" value="submit" class="btn btn-success">Sim</button>
						<a class="btn btn-danger md-close" data-dismiss="modal" role="button">Não</a>

					</p>
				</form>
			
		</div>
	</div>
</div>
<!-- Modal End -->
