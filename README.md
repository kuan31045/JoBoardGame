# <img width="5%" src="art/jo_circle.png"/> JO Board Game

[<img src="art/cover.png"/>](art/cover.png) 

[<img src="https://img.shields.io/badge/version-v1.2.0-blue"/>](https://img.shields.io/badge/version-v1.1.0-blue) 
[<img src = "https://img.shields.io/badge/platform-Android-brightgreen"/>](https://img.shields.io/badge/platform-Android-brightgreen) 
[<img src="https://img.shields.io/badge/kotlin-Language-blue"/>](https://img.shields.io/badge/kotlin-Language-blue)


 
- An app developed for board game enthusiasts, allowing you to host board game parties, browse board game information, and utilize board game tools
  
- Develop with **Kotlin**, adhere to **MVVM** architecture, utilize **Jetpack** toolkit, integrate **Firestore** cloud database, and cooperate with **Coroutine & Flow** for asynchronous programming
  
 <a href='https://play.google.com/store/apps/details?id=com.kappstudio.joboardgame&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1' ><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'  width="250" height="100" /></a>

  
## Demo
  <a href='https://youtu.be/F4pF40-fW-g' ><img alt='Demo Video' src='https://firebasestorage.googleapis.com/v0/b/publisher-77e03.appspot.com/o/jodemoyt.PNG?alt=media&token=0eb17c39-e111-4d2a-a583-d7e9f0cf9ba7' /></a>


## Features
* **Host a Party** - Find companions to play with and arrange the games to play before the meetup
* **Board Game Browsing** - Quickly browse through board game information to avoid confusion during gameplay
* **Board Game Tools** - Handy tools for your use, including dice, timers, spin bottle, lottery drawing, and polygraphy
* **Collect Board** Games - Save your favorite games for quick access when hosting a party
* **Social** - View the preferences and information of other users and befriend them
* **Party Map** - Locate party near you on a map
* **Album** - Document the wonderful moments with photos
* **Game Ratings** - Rate and refer to board game scores
* **Search Feature** - Directly search for parties, games, users, or find parties related to the games you wish to play
* **Report** - Report any user that makes you feel uncomfortable; we will promptly review and take necessary actions

<img src='art/party.gif' width='24%'/>  <img src = 'art/user.gif' width='24%'/>  <img src='art/album.gif' width='24%'/>  <img src ='art/search.gif' width='24%'/>

<img src='art/dice.gif' width='24%'/>  <img src = 'art/bottle.gif' width='24%'/>  <img src='art/timer.gif' width='24%'/>  <img src ='art/polygraph.gif' width='24%'/>

 
## Tech Stacks
* Adhere **MVVM Architecture** to decouple logic, UI, and data layers, enhancing maintainability
* Design data structures and integrate **Firestore** for consistent cloud data synchronization across devices
* Utilize **SnapshotListener** + **Flow** to monitor real-time cloud data, ensuring synchronization of local updates such as comments
* Reactive UI with **LiveData** & **DataBinding** to achieve real-time UI state update
* Implement **dependency injection** and **singleton** pattern with **Koin**
* **UseCase** for encapsulating complex business logics, and can be reused by multiple ViewModels
* Adopt a waterfall **RecyclerView** for the album UI, and integrate **ViewPager** for swipe-based photo navigation
* Load images with **Coil** and enable pinch-to-zoom for photos using **BoxWithConstraints** in **Jetpack Compose**
* Integrate **Google Maps** SDK, mark the party location on the map, implement **GPS** positioning, and customize the InfoWindow
* Utilize **animation**, **Lottie library**, **Jetpack Compose**, **Canvas** and **G-Sensor**, to develop lively board game tools
* **Firebase Crashlytics** analyzes the reasons for app crashes
* **Room Database** for store user recently viewed games locally


## Architectures
![架構](art/architectures.PNG)
MVVM + UseCase
* **View** - Activities/Fragments manage and display UI interactions with users, observing UI state changes from ViewModel
* **ViewModel** - Fetches data from higher layers, handles and updates the UI state through LiveData
* **Model** - Data model and Repositories provide interfaces to retrieve data from both local and remote sources
* **UseCase** - Responsible for encapsulating complex business logics, and can be reused by multiple ViewModels

## Version
- 1.2.0
- Requirement: Android SDK 29
  
## Contact
kuan31045@gmail.com

