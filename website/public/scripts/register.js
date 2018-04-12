var createdAccountThisSession = 0;
      document.querySelector('#register').addEventListener('click', function(e)
      {
        e.preventDefault();
        e.stopPropagation();

        var email = document.querySelector('#email').value;
        var c_email = document.querySelector('#confirm_email').value;
        var password = document.querySelector('#password').value

        var e = document.getElementById("occupation");
        var strUser = e.options[e.selectedIndex].value;

        if(email == c_email && strUser != 1)
        {
          firebase.auth().setPersistence(firebase.auth.Auth.Persistence.SESSION).then(function()
          {
            createdAccountThisSession = 1;
            return firebase.auth().createUserWithEmailAndPassword(email, password);
          }).catch(function(error) {
              var errorMessage = error.message;
              alert(errorMessage);
          });
        }
        else
        {
          if (email != c_email)
            alert("Error: Your emails do not match!");
          else
            alert("Please select a occupation!");
        }
      });

    document.addEventListener('DOMContentLoaded', function() {
      firebase.auth().onAuthStateChanged(function (user)
      {
          if (user && createdAccountThisSession == 1)
          {
            firebase.database().ref('Admins/' + document.getElementById("occupation").options[document.getElementById("occupation").selectedIndex].value + "/" + firebase.auth().currentUser.uid).set({
              Name: document.querySelector('#name').value,
              Email: document.querySelector('#email').value,
              Facility: document.querySelector('#facility').value
            });
            createdAccountThisSession = 0;

            alert("Thank you for registering.\n");

            var timer = setTimeout(function() {
              firebase.auth().signOut();
              window.location.href = "index.html"
            }, 1000);

          }
      });
    });