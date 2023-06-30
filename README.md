# Radius-Agent
MVP, Retrofit, RoomDB, Dagger-Hilt, Data Binding, Coroutines, Dark Mode.

This project is an offline-first Android application. 

- As soon as the app starts and if the data in local database has not been refreshed for the past 24 hours then the app will retrieve fresh data from network call and re-populate the database. If the data has been refreshed in the past 24 hours then app will retrieve data from local database.

- Coroutines, Data binding has been used profoundly. 

- Dagger-Hilt has been used for handling dependency injections. 

- App is also compatible for both Light and Dark modes.

- Also a space & text sizing unit dependency called as sdp and ssp is used. This dependency helps to have consistent UI accross all screen sizes including tablets.


E.g.

App's first launch & Offline mode respectively



https://github.com/yadavshashankr/Radius-Agent/assets/81215694/e8bc985d-5370-4468-824d-dfbb9ee4d751



https://github.com/yadavshashankr/Radius-Agent/assets/81215694/7880250a-2d8a-4a2f-92d9-48febecbef5e


