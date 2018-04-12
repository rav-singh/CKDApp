var htmlValue, ptsUID = [], ptName = [];
    var happy, flat, nausea, anxious, fatigued, depressed, ptname;
    // grab facilitator's facility and hunt for it amoung the users
    var queryString = decodeURIComponent(window.location.search);
    queries = queryString.substring(1).split("=");
    var ref = firebase.database().ref("Admins/Medical/" + queries[1] + "/Patients");

    // creating grid of patients...
    ref.on("value", function(snapshot)
    {
      if (!snapshot.exists())
      {
        firebase.auth().signOut();
        alert("You are not medical personel.\nWe are signing you out.\nPlease log in with your appropriate credentials.");
        var timer = setTimeout(function() {
          window.location.href = "index.html";
        }, 1000);
      }

      htmlValue = "<tr> <th>Patient</th> <th>Happy</th> <th>Flat</th> <th>Nausea</th> <th>Anxious</th> <th>Fatigued</th> <th>Depressed</th></tr>";
      // for each patient
       snapshot.forEach(function(uid)
       {
         ptsUID.push(uid.key);
           // grab that user's name
           firebase.database().ref("Users/" + uid.key + "/Name").on("value", function(name)
           {
             ptName.push(name.val());
             // for each user, get their moods
             firebase.database().ref("Data/Mood/" + uid.key).orderByKey().limitToLast(7).on("value", function(user)
             {
               happy = flat = nausea = anxious = fatigued = depressed = 0;
               // look at each of the last 7 mood entries
                user.forEach(function(day)
                {
                  // document.getElementById("moodData").innerHTML += day.key + " ";
                  var moods = day.val().replace('[', '').replace(']','').split(',');
                  for (var i = 0; typeof moods[i] !== "undefined"; i++)
                  {
                    if (moods[i].includes("Happy")) happy++;
                    else if (moods[i].includes("Flat")) flat++;
                    else if (moods[i].includes("Nausea")) nausea++;
                    else if (moods[i].includes("Anxious")) anxious++;
                    else if (moods[i].includes("Fatigued")) fatigued++;
                    else if (moods[i].includes("Depressed")) depressed++;
                  }
                });

                if (nausea >= 4 || anxious >= 4 || fatigued >= 4 || depressed >= 4 || flat >= 4)
                  htmlValue += "<tr style=\"background-color:Tomato;\">"
                else
                  htmlValue += "<tr>";

                htmlValue += "<td>" + name.val() + "</a></td> <td>" + happy
                + "</td> <td>" + flat + "</td> <td>" + nausea + "</td> <td>" + anxious
                + "</td> <td>" + fatigued + "</td> <td>" + depressed + "</td></tr>";

                document.getElementById("usersMoodsTable").innerHTML = htmlValue;
             });
           });
         });
    }, function (error) {
       alert("Error: " + error.code + "\nPlease contact the administrator.");
       window.location.href = "index.html";
    });

    function download_CSV()
    {
      var row = [], i;
      var appNodes = [], dietNodes = [], moodNodes = [], partNodes = [];
      var appDates = [], dietDates = [], moodDates = [], partDates = [];

      var csv = 'Patient UID, Patient Name, Date, Treatment, Mood, Appetite, Fatigue, Threads Viewed, Threads Created, Comments Written, Meals, Activity \n';

      var AppAndFatigueRef, DietRef, MoodRef, PartRef;
      var promises = [], promise, dataLimit;
      var usedDateArray = [];

      ptsUID.forEach(function(uid) {
        AppAndFatigueRef = firebase.database().ref("Data/AppetiteAndFatigue/" + uid + "/");
        DietRef = firebase.database().ref("Data/Diet/" + uid + "/");
        MoodRef = firebase.database().ref("Data/Mood/" + uid + "/");
        PartRef = firebase.database().ref("Data/Participation/" + uid + "/");

        var dataAccess = firebase.database().ref("Admins/Medical/" + queries[1] + "/Patients/" + uid);

        promise = dataAccess.once("value", function(snapshot){
          dataLimit = snapshot.child("Date").val();
        });

        promises.push(promise);

        promise = AppAndFatigueRef.once("value", function(uid) {
          uid.forEach(function(date) {
            if(date.key <= dataLimit)
            {
              appNodes.push(date);
              appDates.push(date.key);
            }
          });
        });

        promises.push(promise);

        promise = DietRef.once("value", function(uid) {
          uid.forEach(function(date) {
            if(date.key <= dataLimit)
            {
              dietNodes.push(date);
              dietDates.push(date.key);
            }
          });
        });

        promises.push(promise);

        promise = MoodRef.once("value", function(uid) {
          uid.forEach(function(date) {
            if(date.key <= dataLimit)
            {
              moodNodes.push(date);
              moodDates.push(date.key);
            }
          });
        });

        promises.push(promise);

        promise = PartRef.once("value", function(uid) {
          uid.forEach(function(date) {
            if(date.key <= dataLimit)
            {
              partNodes.push(date);
              partDates.push(date.key);
            }
          });
        });

        promises.push(promise);

        // per each user, do stuff...
        Promise.all(promises).then(function () {
          var date = getDateFromArrays(appDates, moodDates, partDates, dietDates, usedDateArray);
          while (date != null && !inUsedDateArray(date, usedDateArray))
          {
            usedDateArray.push(date);
            var mood = null;
            var view = null, made = null, com = null, treat = null, act = null;
            var meals = null;
            var app = null, fat  = null;

            row.push(uid);
            row.push(ptName[ptsUID.findIndex(getname, uid)]);
            row.push(date);

            // if moodNodes is not empty and it has a valid date, go grab the mood
            if (moodNodes.length != 0 && moodDates.findIndex(getdate, date) != -1)
              mood = doTheMoodThang(moodNodes[moodDates.findIndex(getdate, date)]);

            if (partNodes.length != 0 && partDates.findIndex(getdate, date) != -1)
            {
              view = partNodes[partDates.findIndex(getdate, date)].val().ThreadsViewed;
              made = partNodes[partDates.findIndex(getdate, date)].val().ThreadsMade;
              com = partNodes[partDates.findIndex(getdate, date)].val().CommentsMade;
              act = partNodes[partDates.findIndex(getdate, date)].val().Activities;
              treat = partNodes[partDates.findIndex(getdate, date)].val().Treatment;
            }

            if (dietNodes.length != 0 && dietDates.findIndex(getdate, date) != -1)
            {
              var foods = dietNodes[dietDates.findIndex(getdate, date)].val();
              meals = 0;
              if (strcmp(typeof foods.Breakfast,"undefined") != 0)
                meals++
              if (strcmp(typeof foods.Lunch,"undefined") != 0)
                meals++
              if (strcmp(typeof foods.Dinner,"undefined") != 0)
                meals++
              if (strcmp(typeof foods.Snacks,"undefined") != 0)
                meals++
            }

            if (appNodes.length != 0 && appDates.findIndex(getdate, date) != -1)
            {
              data = appNodes[appDates.findIndex(getdate, date)].val();

              if (strcmp(typeof data.Appetite,"undefined") != 0)
              {
                app = data.Appetite;
                if (strcmp(app, "Great Appetite") == 0)
                  app = 5;

                else if (strcmp(app, "Good Appetite") == 0)
                  app = 4;

                else if (strcmp(app, "Average Appetite") == 0)
                  app = 3;

                else if (strcmp(app, "Bad Appetite") == 0)
                  app = 2;

                else if (strcmp(app, "No Appetite") == 0)
                  app = 1;
              }

              if (strcmp(typeof data.Fatigue,"undefined") != 0)
              {
                fat = data.Fatigue;
                if (strcmp(fat, "Great!") == 0)
                  fat = 5;

                else if (strcmp(fat, "Really Not Tired") == 0)
                  fat = 4;

                else if (strcmp(fat, "Not Tired") == 0)
                  fat = 3;

                else if (strcmp(fat, "Tired") == 0)
                  fat = 2;

                else if (strcmp(fat, "Really Tired") == 0)
                  fat = 1;
              }
            }

            row.push(treat);
            row.push(mood);
            row.push(app);
            row.push(fat);
            row.push(view);
            row.push(made);
            row.push(com);
            row.push(meals);
            row.push(act);

            csv += row.join(',');
            csv += '\n';

            row = [];

            date = getDateFromArrays(appDates, moodDates, partDates, dietDates, usedDateArray);
          }
        }).then(function() {
          if (ptsUID.findIndex(getname, uid) == (ptsUID.length - 1))
          {
            var d = new Date();
            var hiddenElement = document.createElement('a');
            hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
            hiddenElement.target = '_blank';
            hiddenElement.download = 'patientAndroidData_' + d.getUTCFullYear() + '-' + (d.getUTCMonth() + 1) + '-' + d.getUTCDate() + '.csv';
            hiddenElement.click();
          }

        }).then(function() {
          appNodes = [], dietNodes = [], moodNodes = [], partNodes = [];
          appDates = [], dietDates = [], moodDates = [], partDates = [];
          promises = [], usedDateArray = [];
        });
      });
    }

    function doTheMoodThang(moodJSON)
    {
      var moods = moodJSON.val().replace('[', '').replace(']','').split(',');
      var mood = "";
      for (var i = 0; typeof moods[i] !== "undefined"; i++)
      {
        if (moods[i].includes("Happy")) mood += "1.";
        else if (moods[i].includes("Flat") || moods[i].includes("Meh") ) mood += "2.";
        else if (moods[i].includes("Nausea")) mood += "3.";
        else if (moods[i].includes("Anxious")) mood += "4.";
        else if (moods[i].includes("Fatigued")) mood += "5.";
        else if (moods[i].includes("Depressed")) mood += "6.";
      }

      return mood;
    }

    function inUsedDateArray(date, usedDateArray)
    {
      for (var i = 0; i < usedDateArray.length; i++)
      {
        if (date.localeCompare(usedDateArray[i]) == 0)
          return 0 < 1;
      }

      return 0 > 1;

    }

    function getDateFromArrays(appDates, moodDates, partDates, dietDates, usedDateArray)
    {
      var date = null;

      if (0 < appDates.length)
        for (var i = 0, date = appDates[0]; i < appDates.length && inUsedDateArray(date, usedDateArray); i++)
          date = appDates[i];
      if (0 < dietDates.length && inUsedDateArray(date, usedDateArray))
        for (var i = 0, date = appDates[0]; i < dietDates.length && inUsedDateArray(date, usedDateArray); i++)
          date = dietDates[i];
      if (0 < moodDates.length && inUsedDateArray(date, usedDateArray))
        for (var i = 0, date = appDates[0]; i < moodDates.length && inUsedDateArray(date, usedDateArray); i++)
          date = moodDates[i];
      if (0 < partDates.length && inUsedDateArray(date, usedDateArray))
        for (var i = 0, date = appDates[0]; i < partDates.length && inUsedDateArray(date, usedDateArray); i++)
          date = partDates[i];

      if (inUsedDateArray(date, usedDateArray))
        return null;

      return date;
    }

    function strcmp(a, b)
    {
        a = a.toString(), b = b.toString();

        for (var i=0,n=Math.max(a.length, b.length); i<n && a.charAt(i) === b.charAt(i); ++i);
          if (i == n) return 0;

        return a.charAt(i) > b.charAt(i) ? -1 : 1;
    }

    function getdate (wantedDate)
    {
      if (strcmp(wantedDate, this) != 0)
        return 1 < 0;
      else
        return 1 > 0;
    }

    function getname (wantedUID)
    {
      return (!(wantedUID.localeCompare(this)));
    }

    function showPage() {
      document.getElementById("loader").style.display = "none";
      document.getElementById("loadingNotification").style.display = "none";
      document.getElementById("myDiv").style.display = "block";
    }