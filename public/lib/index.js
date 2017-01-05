$(function() {
  var successMessage =
    "<pre class='message-code'><code>{<br>" +
    "  \"data\": {<br>" +
    "    \"conferenceUpdates\": {<br>" +
    "      \"message\": \"Thanks for subscribing!\"<br>" +
    "    }<br>" +
    "  }<br>" +
    "}</code></pre>"

  var unexpectedErrorMessage = "Oops, something unexpected happened :( We are sorry for this. Please drop us an e-mail at " + supportEmail
  var graphCoolProject = "https://api.graph.cool/relay/v1/" + graphCoolProjectKey;
  var registerQuery =
    "mutation RegisterSubscriber($name: String!, $email: String!) {" +
    "  createSubscriber(input: {clientMutationId: \"1\", name: $name, email: $email}) {" +
    "    clientMutationId" +
    "  }" +
    "}";

  function setupSmoothScrolling() {
    $('a[href*="#"]:not([href="#"])').click(function() {
      if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
        var target = $(this.hash);
        target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
        if (target.length) {
          $('html, body').animate({
            scrollTop: target.offset().top
          }, 1000);
          return true;
        }
      }
    });
  }

  function showSuccess(topForm) {
    if (!topForm) {
      $.notify({
        message: successMessage
      },{
        type: 'success',
        allow_dismiss: false,
        placement: {from: "top", align: "right"},
        animate: {
          enter: 'animated fadeInDown',
          exit: 'animated fadeOutUp'
        }
      });
    } else {
      $(".window-result").slideDown()
      $(".execute-button").hide()
    }


    $("#bottomRegisterName, #bottomRegisterEmail, #topRegisterName, #topRegisterEmail").val("")
  }

  function showError(message) {
    $.notify({
      message: message
    },{
      type: 'danger',
      allow_dismiss: false,
      placement: {from: "top", align: "right"},
      animate: {
        enter: 'animated fadeInDown',
        exit: 'animated fadeOutUp'
      }
    });
  }

  function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
  }

  function subscribe(topForm, name, email) {
    if (name.trim().length == 0) {
      showError("Name is empty.")
      return
    }

    if (email.trim().length == 0) {
      showError("Email is empty.")
      return
    }

    if (!validateEmail(email)) {
      showError("Email '" + email + "' is not a valid email address.")
      return
    }

    var variables = {
      name: name,
      email: email
    };

    var request = {
      type: "POST",
      url: graphCoolProject,
      data: JSON.stringify({query: registerQuery, variables: variables}),
      contentType:"application/json; charset=utf-8",
      dataType:"json"
    };

    $.ajax(request)
      .then(function (data) {
        if (data.errors && data.errors.length > 0) {
          var message = data.errors[0].code == 3010 ? "E-Mail '" + variables.email + "' is already registered" : unexpectedErrorMessage

          showError(message)
        } else {
          showSuccess(topForm)
        }
      })
      .catch(function (data) {
        showError(unexpectedErrorMessage)
        console.error(data)
      })
  }

  function setupHandlers() {
    $('#registerButton').on('click', function (e) {
      e.preventDefault()
      subscribe(false, $('#bottomRegisterName').val(), $('#bottomRegisterEmail').val())
    });

    $('.execute-button').on('click', function (e) {
      e.preventDefault()
      subscribe(true, $('#topRegisterName').val(), $('#topRegisterEmail').val())
    });

    $('.hidden-input').keypress(function(e) {
      if(e.which == 13) {
        subscribe(true, $('#topRegisterName').val(), $('#topRegisterEmail').val())
      }
    });
  }

  setupSmoothScrolling()
  setupHandlers()

});