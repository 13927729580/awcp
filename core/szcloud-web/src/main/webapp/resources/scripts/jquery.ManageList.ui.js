// JavaScript Document
$(function() {

	// nav bar of listpage
	$('.navbar-collapse > .nav > li > a').each(function() {
		var $this = $(this);
		var href = $this.attr('href');
		var target = href.substring(href.indexOf('#'), href.length);
		$this.attr('data-target', target);
	});
	$('body').scrollspy({
		target : '#navbar-collapse'
	});

	// navbar collapse
	$('.navbar-collapsed .nav > .nav-heading').click(
			function(event) {
				var $nav = $(this).closest('.nav');
				if ($nav.hasClass('collapsed')) {
					if ($(window).width() < 767) {
						$('.navbar-collapsed .nav').not($nav).children(
								'li:not(.nav-heading)').slideUp(
								'fast',
								function() {
									$(this).closest('.nav').addClass(
											'collapsed');
								});

					}
					$nav.removeClass('collapsed').children(
							'li:not(.nav-heading)').slideDown('fast');
				} else {
					$nav.children('li:not(.nav-heading)').slideUp('fast',
							function() {
								$nav.addClass('collapsed');
							});
				}
			});

});

$(document)
		.ready(
				function() {

					// initial page frame when loading
					if ($(window).width() < 767) {
						initialHeight(42);
						initialWidth(0);
					} else {
						initialHeight(0);
						initialWidth(200);
					}

					// Page Frame initial height
					function initialHeight(sHeight) {

						if ($(window).width() < 767) {
							$(".C-contentFrame").css("top", sHeight);
							$(".C-contentFrame").css("height",
									$(window).height() - sHeight);
							$(".C-contentFrame").css("position", "fixed");
							$(".C-contentFrame").css("overflow", "hidden");
							$(".C-contentFrame").css("box-sizing",
									"content-box");
						} else {
							$(".C-contentFrame").css("top", 0);
							$(".C-contentFrame").css("height",
									$(window).height() - sHeight);
							$(".C-contentFrame").css("position", "fixed");
							$(".C-contentFrame").css("overflow", "hidden");
						}

					}
					// Page Frame initial width
					function initialWidth(sWidth) {
						if ($(window).width() < 767) {
							$(".C-contentFrame").css("width", "100%");
							$(".C-contentFrame").css("margin-left", 0);
						} else {
							$(".C-contentFrame").css("width",
									$(window).width() - sWidth);
							$(".C-contentFrame").css("margin-left",
									sWidth + "px");

						}

					}

					// change frame of page when resize the window
					$(window).resize(
						function() {
							$('#main').css('min-height',$(window).height() - 86);
							$("#navbar").css("display") == "none" ?initialWidth(0):initialWidth(200); 
							if ($(window).width() < 767) {
								$('#LogoBar').css("padding", 0);
								$('#LogoBar')
										.html(
												'<img src="./resources/images/szCloudlogo_mobile.png" border="0">');
								initialHeight(42);
								$(".settingBtn").css("position",
										"static").removeClass(
										"btn-group");
							} else {
								$('#LogoBar').html('后台管理');
								initialHeight(0);
								$(".C-contentFrame").css(
										"overflow", "hidden");
								$(".settingBtn,#LogoBar")
										.removeAttr("style");
								$(".settingBtn").addClass(
										"btn-group");
	
							}
						}).resize();

				});