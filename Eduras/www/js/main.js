$(document).ready(function(){
  var slider = $('.bxslider').bxSlider({
  		adaptiveHeight: true,
  		autoStart: false,
		pagerCustom: $('#pageNav'),
		mode: 'fade',
		speed: 300,
		controls: false,
		onSlideAfter: function($slideElement, oldIndex, newIndex){
     // $slideElement: jQuery element of the destination element
     // oldIndex: element index of the previous slide (before the transition)
     // newIndex: element index of the destination slide (after the transition)
       if (newIndex == 4) {
         showRecaptcha();
       }
       else {
         Recaptcha.destroy();
       }
     }
  });
  
	$('a').on('click', function() {
		this.blur();
	});
	
	$('#impressum').on('click', function(event) {
		slider.goToSlide(5);
		event.preventDefault();
	});
	
	$('#feedbacktoggler').on('click', function(event) {
	  slider.goToSlide(4);
    event.preventDefault();
	});

});

function showRecaptcha() {
  return;
  Recaptcha.create("6LcbPeISAAAAAKbrQtSuQqqcX-E3JOZD03usgOMR", "recaptcha_div",
    {
      theme: "white",
  		lang: 'en',
      callback: Recaptcha.focus_response_field
    }
  );

}
