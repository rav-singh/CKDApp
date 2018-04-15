 // logs user in
      document.querySelector('#sign-in').addEventListener('click', function(e)
      {
        e.preventDefault();
        e.stopPropagation();
        var email = document.querySelector('#email').value;
        var password = document.querySelector('#password').value

          firebase.auth().setPersistence(firebase.auth.Auth.Persistence.SESSION).then(function() {
            return firebase.auth().signInWithEmailAndPassword(email, password);
          }).catch(function(error) {
            var errorMessage = error.message;
            alert(errorMessage);
          });
      });

      document.querySelector('#register').addEventListener('click', function(e)
      {
        e.preventDefault();   // if we dont have these two lines,
        e.stopPropagation();  // the browser will entirely skip over the html file
        firebase.auth().signOut();
        window.location.href = "register.html";
      });
      /*
      // logs user out
      document.querySelector('#sign-out').addEventListener('click', function(e)
      {
        e.preventDefault();
        e.stopPropagation();
        firebase.auth().signOut();
        alert("You have been signed out.");
      });
      */

      document.addEventListener('DOMContentLoaded', function() {
        firebase.auth().onAuthStateChanged(function (user)
        {
          if (user)
          {
            var type = document.getElementById("occupation").options[document.getElementById("occupation").selectedIndex].value;

            var timer = setTimeout(function() {
              if (strcmp(type, "Medical") == 0)
                window.location.href = "data.html" + "?uid=" + user.uid;
              else
                window.location.href = "technical.html" + "?uid=" + user.uid;
            }, 1000);
          }
          else {
          }
        });

        // // The Firebase SDK is initialized and available here!
        // firebase.auth().onAuthStateChanged(user => { });
        // firebase.database().ref('/path/to/ref').on('value', snapshot => { });
        // firebase.messaging().requestPermission().then(() => { });
        // firebase.storage().ref('/path/to/ref').getDownloadURL().then(() => { });
      });

      function strcmp(a, b)
      {
          a = a.toString(), b = b.toString();

          for (var i=0,n=Math.max(a.length, b.length); i<n && a.charAt(i) === b.charAt(i); ++i);
            if (i == n) return 0;

          return a.charAt(i) > b.charAt(i) ? -1 : 1;
      }