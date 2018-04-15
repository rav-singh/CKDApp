var queryString = decodeURIComponent(window.location.search);
      queries = queryString.substring(1).split("=");
      var ref = firebase.database().ref("Admins/Tech/" + queries[1]);
      var techName, promise;

      var htmlValuesForSocial;

      promise = ref.once("value", function(snap) {
        if (!snap.exists())
        {
          firebase.auth().signOut();
          alert("You are not technical personel.\nWe are signing you out.\nPlease log in with your appropriate credentials.");
          var timer = setTimeout(function() {
            window.location.href = "index.html";
          }, 1000);
        }

        snap.forEach(function(snappy){
          if (strcmp(snappy.key, "Name")==0)
            techName = snappy.val();
        });
      });

      Promise.resolve(promise);

      function placeButtonForSocialTopics()
      {
        var socialRef = firebase.database().ref("Data/Social/");

        htmlValuesForSocial = "<div style=\"text-align: center; margin: 16px auto 16px;\">";

        Promise.resolve(socialRef.once("value", function(snapshot) {

          snapshot.forEach(function(topics) {

            htmlValuesForSocial += "<button id=\"" + topics.key.replace(" ", "") + "\" onClick=\"presentData('" + topics.key + "')\">" + topics.key + "</button>";
          });
        })).then(function() {
          htmlValuesForSocial += "</div>";
        }).then(function() {
          document.getElementById("socially").innerHTML = htmlValuesForSocial;
        });

      }

      function presentData (topic)
      {
        var topicRef = firebase.database().ref("Data/Social/" + topic + "/");
        var topicHTML = "<table id=\"table" + topic + "\">";
        var show, nshow;
        Promise.resolve(topicRef.once("value", function(snapshot) {
          snapshot.forEach(function(threadUID) {
            show = "display: block;";
            nshow = "display: none;";

            if (strcmp(typeof threadUID.val().Comments,"undefined") == 0)
            {
                firebase.database().ref("Data/Social/" + topic + "/" + threadUID.key + "/").set({
                  Comments: "No comments",
                  author: threadUID.val().author,
                  authorUID: threadUID.val().authorUID,
                  body: threadUID.val().body,
                  category: threadUID.val().category,
                  date: threadUID.val().date,
                  likes: threadUID.val().likes,
                  title: threadUID.val().title
                });
            }

            if (strcmp(typeof threadUID.val().Comments,"undefined") == 0 || strcmp(threadUID.val().Comments, "No comments") == 0)
            {
              show = "display: none;";
              nshow = "display: block;";
            }

            topicHTML += "<tr> <th>Author</th> <th>Title</th> <th>Body</th> <th>Date</th> <th>Delete</th> <th>Comments on Thread</th> </tr>";
            topicHTML += "<tr> <td>" + threadUID.val().author + "</td> <td>"
              + threadUID.val().title + "</td> <td>" + threadUID.val().body
              + "</td> <td>" + threadUID.val().date + "</td> <td> <button onClick=\"deleteRecord('"
              + topic + "', '" + threadUID.key + "', '" + threadUID.val().title
              + "', '" + threadUID.val().author + "')\">Delete!</button></td>"
              + "<td><button style=\"" + show + "\" onClick=\"showComments('" + topic + "', '"
              + threadUID.key + "')\">See Them!</button><p style=\"text-align:center; " + nshow + "\">N/A</p></td></tr>";
          });
        })).then(function() {
          topicHTML += "</table>";
        }).then(function() {
          document.getElementById("socially").innerHTML = htmlValuesForSocial + topicHTML;
        });
      }

      function showComments(topic, thread)
      {
        commentsRef = firebase.database().ref("Data/Social/" + topic + "/" + thread + "/Comments/");

        var topicHTML = "<button onClick=\"presentData('" + topic + "')\">Go Back to Threads</button><table id=\"table" + topic + "\">";

        Promise.resolve(commentsRef.once("value", function(snaps) {
          snaps.forEach(function(uid) {
            topicHTML += "<tr> <th>Name</th> <th>Comment</th> <th>Date</th> <th>Delete</th> </tr>";
            topicHTML += "<tr> <td>" + uid.val().userName + "</td> <td>" + uid.val().comment
              + "</td> <td>" + uid.val().date + "</td> <td>"
              + "<button onClick=\"deleteComment('" + topic + "', '" + thread
              + "', '" + uid.key + "', '" + uid.val().userName + "', '" + uid.val().date + "')\">Delete!</button></td></tr>";
          });
        })).then(function() {
          topicHTML += "</table>";
        }).then(function() {
          document.getElementById("socially").innerHTML = htmlValuesForSocial + topicHTML;
        });
      }

      function deleteComment (topic, thread, uid, name, date)
      {
        var deleteRef = firebase.database().ref("Data/Social/" + topic + "/" + thread + "/Comments/" + uid + "/");
        if (confirm("Are you sure you want to permanently delete this comment written by " +  name + " on " + date + " ?"))
        {
          alert("Record has been permanently deleted");
          deleteRef.remove();
          showComments(topic, thread);
        }
      }

      function deleteRecord (thread, uid, title, name)
      {
        var deleteRef = firebase.database().ref("Data/Social/" + thread + "/" + uid + "/");
        if (confirm("Are you sure you want to permanently delete this record?\n\n'" + title +"' by " + name))
        {
          alert("Record has been permanently deleted");
          deleteRef.remove();
        }
        presentData(thread);
      }

      function togglePinnedPostOn()
      {
        firebase.database().ref('Admins/PinnedPost/title').on("value", function(blogsnap) {
          document.getElementById("pinnedPostTitle").placeholder = "CURRENT TITLE: " + blogsnap.val();
        });

        firebase.database().ref('Admins/PinnedPost/body').on("value", function(blogsnap) {
          document.getElementById("pinnedBody").placeholder = "CURRENT POST CONTENT:\n" + blogsnap.val();
        });

        document.getElementById("bloggy").style.display = "none";
        document.getElementById("socially").style.display = "none";
        document.getElementById("actively").style.display = "none";
        document.getElementById("pinny").style.display = "block";
      }

      function submitExerciseBlog()
      {
        firebase.database().ref('Admins/ExerciseBlog/').set({
          AuthorUID: queries[1],
          Author: techName,
          Title: document.querySelector('#exerciseBlogTitle').value,
          Body: document.querySelector('#exerciseBlogBody').value
        }).then(function() {
          alert("Post submitted!");
          document.querySelector('#exerciseBlogTitle').value ="";
          document.querySelector('#exerciseBlogBody').value ="";
        });
      }

      function submitPinnedPost()
      {
        firebase.database().ref('Admins/PinnedPost/').set({
          authorUID: queries[1],
          author: techName,
          title: document.querySelector('#pinnedPostTitle').value,
          body: document.querySelector('#pinnedBody').value
        }).then(function() {
          alert("Post submitted!");
          document.querySelector('#pinnedPostTitle').value ="";
          document.querySelector('#pinnedBody').value ="";
        });
      }

      function submitDietBlog()
      {
        firebase.database().ref('Admins/DietBlog/').set({
          AuthorUID: queries[1],
          Author: techName,
          Title: document.querySelector('#dietBlogTitle').value,
          Body: document.querySelector('#dietBlogBody').value
        }).then(function() {
          alert("Post submitted!");
          document.querySelector('#dietBlogTitle').value ="";
          document.querySelector('#dietBlogBody').value ="";
        });
      }

      function toggleActivitiesOn() {
        firebase.database().ref('Admins/ExerciseBlog/Title').on("value", function(blogsnap) {
          document.getElementById("exerciseBlogTitle").placeholder = "CURRENT TITLE: " + blogsnap.val();
        });

        firebase.database().ref('Admins/ExerciseBlog/Body').on("value", function(blogsnap) {
          document.getElementById("exerciseBlogBody").placeholder = "CURRENT POST CONTENT:\n" + blogsnap.val();
        });

        document.getElementById("bloggy").style.display = "none";
        document.getElementById("socially").style.display = "none";
        document.getElementById("actively").style.display = "block";
        document.getElementById("pinny").style.display = "none";
      }

      function toggleSocialOn() {
        document.getElementById("bloggy").style.display = "none";
        document.getElementById("socially").style.display = "block";
        document.getElementById("actively").style.display = "none";
        document.getElementById("pinny").style.display = "none";

        placeButtonForSocialTopics();
      }

      function toggleDietOn() {
        firebase.database().ref('Admins/DietBlog/Title').on("value", function(blogsnap) {
          document.getElementById("dietBlogTitle").placeholder = "CURRENT TITLE: " + blogsnap.val();
        });

        firebase.database().ref('Admins/DietBlog/Body').on("value", function(blogsnap) {
          document.getElementById("dietBlogBody").placeholder = "CURRENT POST CONTENT:\n" + blogsnap.val();
        });

        document.getElementById("bloggy").style.display = "block";
        document.getElementById("socially").style.display = "none";
        document.getElementById("actively").style.display = "none";
        document.getElementById("pinny").style.display = "none";
      }

      function strcmp(a, b)
      {
          a = a.toString(), b = b.toString();

          for (var i=0,n=Math.max(a.length, b.length); i<n && a.charAt(i) === b.charAt(i); ++i);
            if (i == n) return 0;

          return a.charAt(i) > b.charAt(i) ? -1 : 1;
      }