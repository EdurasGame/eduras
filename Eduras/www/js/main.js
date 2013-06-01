$(document).ready(function(){
  var slider = $('.bxslider').bxSlider({
  		adaptiveHeight: true,
  		autoStart: false,
		pagerCustom: $('#pageNav'),
		mode: 'fade',
		speed: 300,
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
