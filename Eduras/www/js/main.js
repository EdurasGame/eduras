$(document).ready(function(){
  var slider = $('.bxslider').bxSlider({
  		adaptiveHeight: true,
  		autoStart: false,
		pagerCustom: $('#pageNav'),
		mode: 'fade',
		controls: false
  });
  
	$('a').on('click', function() {
		this.blur();
	});
	
	$('#impressum').on('click', function(event) {
		slider.goToSlide(4);
		event.preventDefault();
	});
});