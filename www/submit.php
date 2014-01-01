<?php

  require_once('recaptchalib.php');
  $privatekey = "6LcbPeISAAAAAKoPA2S4v6m3SPxTBv5wjm9J7G8b";
  $resp = recaptcha_check_answer ($privatekey,
                                $_SERVER["REMOTE_ADDR"],
                                $_POST["recaptcha_challenge_field"],
                                $_POST["recaptcha_response_field"]);

  if (!$resp->is_valid) {
    // What happens when the CAPTCHA was entered incorrectly
    die ("The reCAPTCHA wasn't entered correctly. Go back and try it again." .
         "(reCAPTCHA said: " . $resp->error . ")");
  } else {
    $name = $_POST['name'];
    $text = $_POST['text'];
    $email = $_POST['email'];

    // Your code here to handle a successful verification
  }

