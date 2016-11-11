# Making your Android app faster
### Startup time
- load less things in the startup
- reflection is really slow
- providers need to be initialized, be aware
- reduce the size of your bitmaps
- delay a few seconds background processes for synchronization with the server
- use launch screens
### Loading data
2.1. Cache
- load data that the user may need later in advance
- use the loader manager
- don’t forget the cache in the server side
- etags
2.2. Network requets
- the less data you request the better
- start making the request for the next page of data before you reach the last item
- http2
- don’t request full size images
- keep your endpoints simple and fast
- removed fields of your objects not relevant for the request
- use signed tokens for user authentication
2.3. Parse data
- processing data the user is not going to see in the screen then you are wasting resources and making the user wait longer
- bulk inserts
### Layouts
- custom views
- avoid nesting views
- use the android tools for the frame rate and overdraw
### Animations
- your app doesn’t only have to be fast it has to feel it is fast
- mask networks requests with activity transitions
### Ads
- data is more important than ads
- load the ads before needed if possible
- if the users don’t see the ads they hardly are going to click on them
