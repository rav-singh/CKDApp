 // var htmlValue;
    var dateByFeeling = new Array();
    var happy, flat, nausea, anxious, fatigued, depressed;
    var queryString = decodeURIComponent(window.location.search);
    queries = queryString.substring(1).split("&");
    adminUID = queries[0].split("=")[1];
    ptUID = queries[1].split("=")[1];

    // grab that user's name
     firebase.database().ref("Users/" + ptUID + "/Name").on("value", function(name)
     {
       document.getElementById("patientName").innerHTML += name.val();
       // for each user, get their moods
       firebase.database().ref("Data/Mood/" + ptUID).orderByKey().on("value", function(user)
       {
         happy = flat = nausea = anxious = fatigued = depressed = 0;
         user.forEach(function(day)
         {
           happy = flat = nausea = anxious = fatigued = depressed = 0;
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
            dateByFeeling.push({date: day.key, h: happy, f: flat, n :nausea, a: anxious, f: fatigued, d: depressed});
         });
          myGraph(dateByFeeling);
       });
     });

     function myGraph (datesByMood)
     {
       // for (var i = 0; i < datesByMood.length; i++)
         // console.log(datesByMood[i].date + " " + datesByMood[i].h + " " + datesByMood[i].f + " " + datesByMood[i].n + " " + datesByMood[i].a + " " + datesByMood[i].f + " " + datesByMood[i].d);
         var trace1 = {
           x: [1, 2, 3],
           y: [4, 5, 6],
           name: 'yaxis1 data',
           type: 'scatter'
         };

         var trace2 = {
           x: [2, 3, 4],
           y: [40, 50, 60],
           name: 'yaxis2 data',
           yaxis: 'y2',
           type: 'scatter'
         };

         var trace3 = {
           x: [4, 5, 6],
           y: [40000, 50000, 60000],
           name: 'yaxis3 data',
           yaxis: 'y3',
           type: 'scatter'
         };

         var trace4 = {
           x: [5, 6, 7],
           y: [400000, 500000, 600000],
           name: 'yaxis4 data',
           yaxis: 'y4',
           type: 'scatter'
         };

         var data = [trace1, trace2, trace3, trace4];

         var layout = {
           title: 'multiple y-axes example',
           width: 800,
           xaxis: {domain: [0.3, 0.7]},
           yaxis: {
             title: 'yaxis title',
             titlefont: {color: '#1f77b4'},
             tickfont: {color: '#1f77b4'}
           },
           yaxis2: {
             title: 'yaxis2 title',
             titlefont: {color: '#ff7f0e'},
             tickfont: {color: '#ff7f0e'},
             anchor: 'free',
             overlaying: 'y',
             side: 'left',
             position: 0.15
           },
           yaxis3: {
             title: 'yaxis4 title',
             titlefont: {color: '#d62728'},
             tickfont: {color: '#d62728'},
             anchor: 'x',
             overlaying: 'y',
             side: 'right'
           },
           yaxis4: {
             title: 'yaxis5 title',
             titlefont: {color: '#9467bd'},
             tickfont: {color: '#9467bd'},
             anchor: 'free',
             overlaying: 'y',
             side: 'right',
             position: 0.85
           }
         };

         Plotly.newPlot('ptGraph', data, layout);
     }