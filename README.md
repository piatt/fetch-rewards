*** PIATT FETCH REWARDS ANDROID TAKE-HOME SOLUTION ***

----------------
| INSTRUCTIONS |
----------------
* Open the fetch-rewards project with Android Studio
* Build and install the app module configuration to your target emulator or device
* Play around with the app!
* Run FetchRewardsRepositoryTest from app/src/test/com.example.fetch.rewards
* Browse the code, and please see the code comments for further explanation

---------
| NOTES |
---------
Here is a brief explanation of my choices of dependencies:

- DI: Hilt, Dagger
  I chose Hilt, which is built on top of Dagger, because Dagger is what I'm most familiar with professionally.
  I've advocated for integrating Hilt into my current team's codebase for quite some time because
  our current use of Dagger is very disorganized, presents a lot of overhead, and is a pain to maintain and augment.
  While integrating Hilt has not yet become a reality for my team, this exercise presented a great opportunity to employ it!

- Network/Data: Retrofit, OkHttp, Kotlin Serialization
  I chose Retrofit, which uses OkHttp under the hood, along with Kotlin Serialization as my converter of choice,
  because they are fairly ubiquitous in the industry, and are what I'm most familiar with professionally.
  I added a network connectivity interceptor to the network client, which facilitated better UX in the case of disconnection.
  With more time, I definitely would have integrated a local caching solution, most likely in the form of a lightweight database.

- UI: Jetpack Compose
  With more time, I definitely would have made the UI a lot more user friendly and polished, including an option to refresh data,
  a way to search and filter the data, and a way to toggle the sorting order. I did add a small navigation graph
  which allowed me to showcase a simple use case of selecting a list screen item and showing its details on a separate screen.

Here is a brief explanation of my architecture choices:

- The package structure is a microcosmic representation of what a feature module might look like upon which the app module depends.
  Separating data from domain and likewise domain from presentation creates a clean separation of concerns that facilitates
  the MVI architecture pattern employed in this exercise.

- I employed the repository pattern, separating the domain layer from the presentation layer, and used a view model,
  following the MVI architecture pattern, to convert data from the domain layer to view state to be used to create the UI,
  making sure that proper separation of concerns between the data layer and the view model was maintained.
  One of my goals was to write instrumented tests to test the view model and the UI, but decided against it for the sake of time.
  The very last thing I worked on was the UI. With the repository and view model complete, it was virtually plug-and-play,
  and all that was left to do was to make sure that the UX looked good enough for a demo!

- I used Kotlin coroutines and StateFlows in this exercise to ensure that asynchronous operations are handled by the proper dispatcher
  and within the proper context and scope, and that they are testable. The UI collects the StateFlow exposed by the view model
  and recomposes as necessary when new emissions are received, while also being lifecycle-aware.

- I spent a good portion of my time writing unit tests and making sure that the data validation was solid. 
  This involved creating test data resources, and considering edge cases that could occur from the data layer through the domain layer.

- I added detailed code comments to explain methods and properties as necessary, adding notes where applicable, and citing sources.

- Lastly, I hope you enjoy reading my code. I strive for concise and minimalistic code,
  while holding myself to a high standard of organization, efficiency, and testability.
  Although the scope of this exercise does not require a fully polished app,
  I hope I've been able to showcase some of my skills, as well as the thought process behind them.

Please let me know if you have any questions for me and I look forward to hearing your feedback!

-Benjamin Piatt