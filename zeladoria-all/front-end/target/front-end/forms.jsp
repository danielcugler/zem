<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulários | zUP - Projeto de Zeladoria Urbana
	Participativa</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="description" content="">
<meta name="keywords"
	content="coco bootstrap template, coco admin, bootstrap,admin template, bootstrap admin,">
<meta name="author" content="Huban Creative">

<!-- Base Css Files -->
<link
	href="assets/libs/jqueryui/ui-lightness/jquery-ui-1.10.4.custom.min.css"
	rel="stylesheet" />
<link href="assets/libs/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" />
<link href="assets/libs/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<link href="assets/libs/fontello/css/fontello.css" rel="stylesheet" />
<link href="assets/libs/animate-css/animate.min.css" rel="stylesheet" />
<link href="assets/libs/nifty-modal/css/component.css" rel="stylesheet" />
<link href="assets/libs/magnific-popup/magnific-popup.css"
	rel="stylesheet" />
<link href="assets/libs/ios7-switch/ios7-switch.css" rel="stylesheet" />
<link href="assets/libs/pace/pace.css" rel="stylesheet" />
<link href="assets/libs/sortable/sortable-theme-bootstrap.css"
	rel="stylesheet" />
<link href="assets/libs/bootstrap-datepicker/css/datepicker.css"
	rel="stylesheet" />
<link href="assets/libs/jquery-icheck/skins/all.css" rel="stylesheet" />
<!-- Code Highlighter for Demo -->
<link href="assets/libs/prettify/github.css" rel="stylesheet" />

<!-- Extra CSS Libraries Start -->
<link href="assets/libs/bootstrap-select/bootstrap-select.min.css"
	rel="stylesheet" type="text/css" />
<link href="assets/libs/summernote/summernote.css" rel="stylesheet"
	type="text/css" />
<link href="assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Extra CSS Libraries End -->
<link href="assets/css/style-responsive.css" rel="stylesheet" />

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->

<link rel="shortcut icon" href="assets/img/favicon.ico">
<link rel="apple-touch-icon" href="assets/img/apple-touch-icon.png" />
<link rel="apple-touch-icon" sizes="57x57"
	href="assets/img/apple-touch-icon-57x57.png" />
<link rel="apple-touch-icon" sizes="72x72"
	href="assets/img/apple-touch-icon-72x72.png" />
<link rel="apple-touch-icon" sizes="76x76"
	href="assets/img/apple-touch-icon-76x76.png" />
<link rel="apple-touch-icon" sizes="114x114"
	href="assets/img/apple-touch-icon-114x114.png" />
<link rel="apple-touch-icon" sizes="120x120"
	href="assets/img/apple-touch-icon-120x120.png" />
<link rel="apple-touch-icon" sizes="144x144"
	href="assets/img/apple-touch-icon-144x144.png" />
<link rel="apple-touch-icon" sizes="152x152"
	href="assets/img/apple-touch-icon-152x152.png" />
</head>
<body class="fixed-left">
	<!-- Modal Start -->
	<%@ include file="/logout.jsp"%>
	<!-- Modal Logout -->
	<div class="md-modal md-just-me" id="logout-modal"></div>
	<!-- Modal End -->
	<!-- Begin page -->
	<div id="wrapper">

		<!-- Top Bar Start -->
		<div class="topbar">
			<%@ include file="/topbar.jsp"%>

		</div>
		<!-- Top Bar End -->
		<!-- Left Sidebar Start -->
		<div class="left side-menu">
			<%@ include file="/menu.jsp"%>
		</div>
		<!-- Left Sidebar End -->

		<!-- Start right content -->
		<div class="content-page">
			<!-- ============================================================== -->
			<!-- Start Content here -->
			<!-- ============================================================== -->
			<div class="content">
				<!-- Page Heading Start -->
				<div class="page-heading">
					<h1>
						<i class='fa fa-check'></i> Forms
						<%=request.getRequestURI()%>
					</h1>
				</div>
				<!-- Page Heading End-->

				<!-- Your awesome content goes here -->
				<div class="row">

					<div class="col-sm-6 portlets">

						<div class="widget">
							<div class="widget-header transparent">
								<h2>
									<strong>Default</strong> Form
								</h2>
								<div class="additional-btn">
									<a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
									<a href="#" class="widget-toggle"><i
										class="icon-down-open-2"></i></a> <a href="#" class="widget-close"><i
										class="icon-cancel-3"></i></a>
								</div>
							</div>
							<div class="widget-content padding">
								<div id="basic-form">
									<form role="form">
										<div class="form-group">
											<label for="exampleInputEmail1">Email address</label> <input
												type="email" class="form-control" id="exampleInputEmail1"
												placeholder="Enter email">
										</div>
										<div class="form-group">
											<label for="exampleInputPassword1">Password</label> <input
												type="password" class="form-control"
												id="exampleInputPassword1" placeholder="Password">
										</div>
										<div class="form-group">
											<input type="file" class="btn btn-default"
												title="Search for a file to add">
											<p class="help-block">Example block-level help text here.</p>
										</div>
										<button type="submit" class="btn btn-default">Submit</button>
									</form>
								</div>
							</div>
						</div>

					</div>

					<div class="col-sm-6 portlets">

						<div class="widget">
							<div class="widget-header transparent">
								<h2>
									<strong>Horizontal</strong> Form
								</h2>
								<div class="additional-btn">
									<a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
									<a href="#" class="widget-toggle"><i
										class="icon-down-open-2"></i></a> <a href="#" class="widget-close"><i
										class="icon-cancel-3"></i></a>
								</div>
							</div>
							<div class="widget-content padding">
								<div id="horizontal-form">
									<form class="form-horizontal" role="form">
										<div class="form-group">
											<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
											<div class="col-sm-10">
												<input type="email" class="form-control" id="inputEmail3"
													placeholder="Email">
												<p class="help-block">Example block-level help text
													here.</p>
											</div>
										</div>
										<div class="form-group">
											<label for="inputPassword3" class="col-sm-2 control-label">Password</label>
											<div class="col-sm-10">
												<input type="password" class="form-control"
													id="inputPassword3" placeholder="Password">
											</div>
										</div>
										<div class="form-group">
											<div class="col-sm-offset-2 col-sm-10">
												<div class="checkbox">
													<label> <input type="checkbox"> Remember me
													</label>
												</div>
											</div>
										</div>
										<div class="form-group">
											<div class="col-sm-offset-2 col-sm-10">
												<button type="submit" class="btn btn-default">Sign
													in</button>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>

					</div>

				</div>

				<div class="widget">
					<div class="widget-header transparent">
						<h2>
							<strong>Inline</strong> Form
						</h2>
						<div class="additional-btn">
							<a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
							<a href="#" class="widget-toggle"><i class="icon-down-open-2"></i></a>
							<a href="#" class="widget-close"><i class="icon-cancel-3"></i></a>
						</div>
					</div>
					<div class="widget-content padding">
						<form class="form-inline" role="form">
							<div class="form-group">
								<label class="sr-only" for="exampleInputEmail2">Email
									address</label> <input type="email" class="form-control"
									id="exampleInputEmail2" placeholder="Enter email">
							</div>
							<div class="form-group">
								<label class="sr-only" for="exampleInputPassword2">Password</label>
								<input type="password" class="form-control"
									id="exampleInputPassword2" placeholder="Password">
							</div>
							<div class="checkbox">
								<label> <input type="checkbox"> Remember me
								</label>
							</div>
							<button type="submit" class="btn btn-default">Sign in</button>
							<a class="btn btn-primary md-trigger" data-modal="form-modal">Form
								in Modal</a>
						</form>
					</div>
				</div>


				<div class="widget">
					<div class="widget-header transparent">
						<h2>
							<strong>Form</strong> Element
						</h2>
						<div class="additional-btn">
							<a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
							<a href="#" class="widget-toggle"><i class="icon-down-open-2"></i></a>
							<a href="#" class="widget-close"><i class="icon-cancel-3"></i></a>
						</div>
					</div>
					<div class="widget-content padding">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<label for="input-text" class="col-sm-2 control-label">Input
									text</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="input-text"
										placeholder="Input text">
								</div>
							</div>
							<div class="form-group">
								<label for="input-text-help" class="col-sm-2 control-label">Input
									text with help</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="input-text-help"
										placeholder="Input text placeholder">
									<p class="help-block">Example block-level help text here.</p>
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword" class="col-sm-2 control-label">Password</label>
								<div class="col-sm-10">
									<input type="password" class="form-control" id="inputPassword"
										placeholder="Password">
								</div>
							</div>
							<div class="form-group">
								<label for="input-text-disabled" class="col-sm-2 control-label">Disabled</label>
								<div class="col-sm-10">
									<input type="text" class="form-control"
										id="input-text-disabled" placeholder="Input text" disabled>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Static Control</label>
								<div class="col-sm-10">
									<p class="form-control-static">email@example.com</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Radio</label>
								<div class="col-sm-10">
									<div class="radio iradio">
										<label> <input type="radio" name="optionsRadios"
											id="optionsRadios1" value="option1" checked> Option
											one is this and that&mdash;be sure to include why it's great
										</label>
									</div>
									<div class="radio iradio">
										<label> <input type="radio" name="optionsRadios"
											id="optionsRadios2" value="option2"> Option two can
											be something else and selecting it will deselect option one
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Checkbox</label>
								<div class="col-sm-10">
									<div class="checkbox">
										<label> <input type="checkbox" value="">
											Option one is this and that&mdash;be sure to include why it's
											great
										</label>
									</div>
									<div class="checkbox">
										<label> <input type="checkbox" value="">
											Option one is this and that&mdash;be sure to include why it's
											great
										</label>
									</div>
									<div class="checkbox">
										<label> <input type="checkbox" value="">
											Option one is this and that&mdash;be sure to include why it's
											great
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Inline Checkbox</label>
								<div class="col-sm-10">
									<label class="checkbox-inline icheckbox"> <input
										type="checkbox" id="inlineCheckbox1" value="option1">
										1
									</label> <label class="checkbox-inline icheckbox"> <input
										type="checkbox" id="inlineCheckbox2" value="option2">
										2
									</label> <label class="checkbox-inline icheckbox"> <input
										type="checkbox" id="inlineCheckbox3" value="option3">
										3
									</label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Select</label>
								<div class="col-sm-10">
									<select class="form-control">
										<option>1</option>
										<option>2</option>
										<option>3</option>
										<option>4</option>
										<option>5</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<select multiple class="form-control">
										<option>1</option>
										<option>2</option>
										<option>3</option>
										<option>4</option>
										<option>5</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Custom Select</label>
								<div class="col-sm-10">
									<select class="form-control selectpicker">
										<option>1</option>
										<option>2</option>
										<option>3</option>
										<option>4</option>
										<option>5</option>
									</select>
								</div>
							</div>
							<div class="form-group has-success">
								<label for="input-text-has-success"
									class="col-sm-2 control-label">Input Success</label>
								<div class="col-sm-10">
									<input type="text" class="form-control"
										id="input-text-has-success">
								</div>
							</div>
							<div class="form-group has-warning">
								<label for="input-text-has-warning"
									class="col-sm-2 control-label">Input Warning</label>
								<div class="col-sm-10">
									<input type="text" class="form-control"
										id="input-text-has-warning">
								</div>
							</div>
							<div class="form-group has-error">
								<label for="input-text-has-error" class="col-sm-2 control-label">Input
									Error</label>
								<div class="col-sm-10">
									<input type="text" class="form-control"
										id="input-text-has-error">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Input Size</label>
								<div class="col-sm-10">
									<input type="text" class="form-control input-sm"
										placeholder="input-sm">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<input type="text" class="form-control" placeholder="default">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<input type="text" class="form-control input-lg"
										placeholder="input-lg">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Column</label>
								<div class="col-sm-10">
									<div class="row">
										<div class="col-xs-2">
											<input type="text" class="form-control"
												placeholder=".col-xs-2">
										</div>
										<div class="col-xs-3">
											<input type="text" class="form-control"
												placeholder=".col-xs-3">
										</div>
										<div class="col-xs-4">
											<input type="text" class="form-control"
												placeholder=".col-xs-4">
										</div>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Input group</label>
								<div class="col-sm-10">
									<div class="input-group">
										<span class="input-group-addon">@</span> <input type="text"
											class="form-control" placeholder="Username">
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<div class="input-group">
										<input type="text" class="form-control"> <span
											class="input-group-addon">.00</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<div class="input-group">
										<span class="input-group-addon">$</span> <input type="text"
											class="form-control"> <span class="input-group-addon">.00</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<div class="row">
										<div class="col-lg-6">
											<div class="input-group">
												<span class="input-group-addon"> <input
													type="checkbox">
												</span> <input type="text" class="form-control">
											</div>
											<!-- /input-group -->
										</div>
										<!-- /.col-lg-6 -->
										<div class="col-lg-6">
											<div class="input-group">
												<span class="input-group-addon"> <input type="radio">
												</span> <input type="text" class="form-control">
											</div>
											<!-- /input-group -->
										</div>
										<!-- /.col-lg-6 -->
									</div>
									<!-- /.row -->
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<div class="row">
										<div class="col-lg-6">
											<div class="input-group">
												<span class="input-group-btn">
													<button class="btn btn-default" type="button">Go!</button>
												</span> <input type="text" class="form-control">
											</div>
											<!-- /input-group -->
										</div>
										<!-- /.col-lg-6 -->
										<div class="col-lg-6">
											<div class="input-group">
												<input type="text" class="form-control"> <span
													class="input-group-btn">
													<button class="btn btn-default" type="button">Go!</button>
												</span>
											</div>
											<!-- /input-group -->
										</div>
										<!-- /.col-lg-6 -->
									</div>
									<!-- /.row -->
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<div class="row">
										<div class="col-lg-6">
											<div class="input-group">
												<div class="input-group-btn">
													<button type="button"
														class="btn btn-default dropdown-toggle"
														data-toggle="dropdown">
														Action <span class="caret"></span>
													</button>
													<ul class="dropdown-menu">
														<li><a href="#">Action</a></li>
														<li><a href="#">Another action</a></li>
														<li><a href="#">Something else here</a></li>
														<li class="divider"></li>
														<li><a href="#">Separated link</a></li>
													</ul>
												</div>
												<!-- /btn-group -->
												<input type="text" class="form-control">
											</div>
											<!-- /input-group -->
										</div>
										<!-- /.col-lg-6 -->
										<div class="col-lg-6">
											<div class="input-group">
												<input type="text" class="form-control">
												<div class="input-group-btn">
													<button type="button"
														class="btn btn-default dropdown-toggle"
														data-toggle="dropdown">
														Action <span class="caret"></span>
													</button>
													<ul class="dropdown-menu pull-right">
														<li><a href="#">Action</a></li>
														<li><a href="#">Another action</a></li>
														<li><a href="#">Something else here</a></li>
														<li class="divider"></li>
														<li><a href="#">Separated link</a></li>
													</ul>
												</div>
												<!-- /btn-group -->
											</div>
											<!-- /input-group -->
										</div>
										<!-- /.col-lg-6 -->
									</div>
									<!-- /.row -->
								</div>
							</div>


							<div class="form-group">
								<label class="col-sm-2 control-label">File Input</label>
								<div class="col-sm-10">
									<input type="file" class="btn btn-default" title="Select file">
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">Datepicker</label>
								<div class="col-sm-3">
									<input type="text" class="form-control datepicker-input">
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">Datepicker with
									mask</label>
								<div class="col-sm-3">
									<input type="text" class="form-control datepicker-input"
										data-mask="9999-99-99" placeholder="yyyy-mm-dd">
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">Input mask</label>
								<div class="col-sm-10">
									<div class="row">
										<div class="col-sm-3">
											<input type="text" class="form-control" data-mask="999999"
												placeholder="999999">
										</div>
										<div class="col-sm-3">
											<input type="text" class="form-control" data-mask="aaaaaa"
												placeholder="******">
										</div>
										<div class="col-sm-3">
											<input type="text" class="form-control"
												data-mask="999.999.999" placeholder="999.999.999">
										</div>
										<div class="col-sm-3">
											<input type="text" class="form-control" data-mask="99:99:99"
												placeholder="99:99:99">
										</div>
									</div>

								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">WYSWYG</label>
								<div class="col-sm-10">
									<textarea class="summernote"></textarea>
								</div>
							</div>
						</form>
					</div>

				</div>
				<!-- End of your awesome content -->
				<!-- Footer Start -->
				<div id="footer">
					<%@ include file="/footer.jsp"%>
				</div>
				<!-- Footer End -->

			</div>
			<!-- ============================================================== -->
			<!-- End content here -->
			<!-- ============================================================== -->

		</div>
		<!-- End right content -->

	</div>
	<!-- End of page -->
	<!-- the overlay modal element -->
	<div class="md-overlay"></div>
	<!-- End of eoverlay modal -->
	<script>
		var resizefunc = [];
	</script>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="assets/libs/jquery/jquery-1.11.1.min.js"></script>
	<script src="assets/libs/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/libs/jqueryui/jquery-ui-1.10.4.custom.min.js"></script>
	<script src="assets/libs/jquery-ui-touch/jquery.ui.touch-punch.min.js"></script>
	<script src="assets/libs/jquery-detectmobile/detect.js"></script>
	<script
		src="assets/libs/jquery-animate-numbers/jquery.animateNumbers.js"></script>
	<script src="assets/libs/ios7-switch/ios7.switch.js"></script>
	<script src="assets/libs/fastclick/fastclick.js"></script>
	<script src="assets/libs/jquery-blockui/jquery.blockUI.js"></script>
	<script src="assets/libs/bootstrap-bootbox/bootbox.min.js"></script>
	<script src="assets/libs/jquery-slimscroll/jquery.slimscroll.js"></script>
	<script src="assets/libs/jquery-sparkline/jquery-sparkline.js"></script>
	<script src="assets/libs/nifty-modal/js/classie.js"></script>
	<script src="assets/libs/nifty-modal/js/modalEffects.js"></script>
	<script src="assets/libs/sortable/sortable.min.js"></script>
	<script src="assets/libs/bootstrap-fileinput/bootstrap.file-input.js"></script>
	<script src="assets/libs/bootstrap-select/bootstrap-select.min.js"></script>
	<script src="assets/libs/bootstrap-select2/select2.min.js"></script>
	<script src="assets/libs/magnific-popup/jquery.magnific-popup.min.js"></script>
	<script src="assets/libs/pace/pace.min.js"></script>
	<script
		src="assets/libs/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
	<script src="assets/libs/jquery-icheck/icheck.min.js"></script>

	<!-- Demo Specific JS Libraries -->
	<script src="assets/libs/prettify/prettify.js"></script>

	<script src="assets/js/init.js"></script>
	<!-- Page Specific JS Libraries -->
	<script src="assets/libs/bootstrap-select/bootstrap-select.min.js"></script>
	<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>
	<script src="assets/libs/summernote/summernote.js"></script>
	<script src="assets/js/pages/forms.js"></script>
</body>
</html>