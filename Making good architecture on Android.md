# Making good architecture on Android.

what good architecture should be:

1. Independent
2. Testable
3. Re-usable
4. Extendable

# View-Presenter-Model(Entity)-Repository 
- Activity & Fragment — these two would be only doing UI and glue different parts of the app together, after all these are screens that user will see, Views.
- Somebody needs to do the logic for screens, like handling button clicks, doing analytics tracking, talking to network services. We could make a Controller, but on Android screens are created by the system, so we can’t really control them. That’s why need a Presenter. It’s job is to present things to the View.
- The MVP’s Model in our architecture could be represented by Entities — these would some simple objects that we receive from our database or server. In Java they are called POJO(Plain Old Java Objects), because they don’t contain any logic, only fields.
- Entities need to be retrieved, either from the database or the network, and we can call a thing that does that and keeps them in memory a Repository — something, who knows someone, who talks to the network/database.

- View would be tricky to unit test — it depends on things being displayed on the screen, so this we will have to skip
- Presenter, on the other hand, only does the logic needed for the screen to work, and View always passes all it’s events to the Presenter by calling some method. And all this can be covered by unit tests
- Entities have only fields and are completely dump, thus don’t need to be tested
- Repository can be a completely independent layer of our application not even knowing that UI(View) exists — it can be tested completely in isolation from other things

