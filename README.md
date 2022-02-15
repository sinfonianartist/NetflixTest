# Joshua Hale - Netflix Interview

The goal of this document is to outline the choices surrounding the implementation details of the Netflix Interview question provided by the Netflix engineering team. I will attempt highlight the reasonings for using specific libraries as well as highlight parts of the system. Lastly, I will attempt to showcase some of the shortcomings of the current approach due to time constraints as well as some low hanging fruit that could be quick wins and harden the code base.

## Overall Approach

Before I got into the code I need to familiarize myself with the API documentation from TheMovieDB. I decided to go through the process of creating my own API Keys in the off-chance that there may be some sort of rate limiting and the application failed to work.

After familiarizing myself with the API documentation I needed to think of some of the considerations before writing code:

- How am I going to break down the differences between various logical breaks?
- What architecture should I implement in a sample application with minimal time?
- What are some factors I will need to consider when making a performant UI showcasing poster images
- What are some of the pitfalls of the application that I'll need to identify?
- What libraries will help me make the best use of my time?

### Logical Breaks

In Android code, we have a lot of different layers. We have distinct breaks between the UI, any domain knowledge, and the data layer. There are multiple ways to break this down, but I chose these three distinct breaks intentionally. By alleviating concerns between the different pieces, I'm able to isolate (and eventually test) all of these pieces rather easily. The logical breakdown is also simple to follow as you can see what each class is responsible for with minimal cross cutting.

### Architecture

I decided to use the Android MVVM Clean Architecture approach for this test for a few reasons.

- I feel comfortable using this architecture as I have used it in multiple applications and can speak to where it is well suited in client-facing applications
- It allows for clear separation of concerns between the UI, the domain, and the data layers.
- It has some nice integrations with the use of Hilt so I can directly inject the classes I need into a `ViewModel` without all of the boilerplate code necessary. Annotations such as `HiltViewModel` allow for clean injections and no clutter between the various layers.
- Lifecycle Awareness is rather easy to build into ViewModels with the Lifecycle libraries provided by Google. While there are other ways to do this and using Coroutines is possible, this approach was the most known to me.

### UI Performance

Instead of using `ConstraintLayout` which seems like the easiest way to handle the UI, I opted for a `LinearLayoutCompat` approach.  The main drawback with `ConstraintLayout` in something like a `RecyclerView` is that each visibility change causes an entire re-draw from the constantly changing constraints. While some of that problem can be fixed with APIs such as `setHasFixedSize(true)` the layout may be needlessly complex with constraints. Instead, I created a horizontal `LinearLayout` that had weights assigned to each section.

One potentially major to this approach without using `ConstraintLayout` may be that it can not easily be replicated on various phone screen sizes and tablets. With the use of the weighting approach, regardless of the screen size each element will take up approximately the same dp when rendered.

When it comes to showcasing lots of photos there is only so much that I am able to do in a smaller application. I am relying on Picasso as I'm the most familiar with it's API. Picasso may not be as fast as Glide when attempting to load images from memory but I wanted to call that out as an optimization that could be made. Also if there was a use-case for `GIF` formats, using Glide is recommended.

In order to ensure that we are getting the correct size image, I did some rudimentary math around the width of the device and determining the right size image to use from the `backdropSizes` and `photoSizes` given to me from the `Configuration API`. This can be found in the `ImageSizeHelper.kt` and can help to downscale the images without always loading the highest resolution for no reason.

Another UI performance upgrade would be utilizing `DiffUtils` over `notifyDataSetInserted()` and `notifyItemRangeRemoved()`. Currently we just add all elements to the UI but not if there are changes with the existing elements, or if the API changes to have pagination. There is some extra `DiffCallback` code required there and I wanted to ensure I was thinking about feature completion, knowing that it might be something to talk about in a later interview session.

### Networking

One of the approaches I took here with the networking layer is not treating the responses from the server layer as the source of truth. When I receive the data back from the server, I make sure I do some sanity checks before creating the models used within the scope of the application. This is done in the `MoviesRepository` class and maps the response from the server to the `ResponseMapper` class for data sanization and determining sane defaults.

This also allows for a separation between `Data Models` and `Network Responses` so that if one changes, the other doesn't necessarily need to. Decoupling these allows for better code isolation.

### Pitfalls

In order to time-box myself in this project versus spending an unending amount of time refactoring, there were a few things I decided to focus on, and a few things I decided to leave for documentation.

- I decided to go with the Single-Activity Architectural approach that Google has touted for some time. By including things through the `Navigation Graph` and then applying `SafeArgs`, Android will generate the `Directions` classes that give us concrete implementations for passing data between Fragments. We can also easily determine what `Actions` can be taken and it is very easy to show which Fragments have the ability to call one another.
- Currently I've created a single `ApplicationModule` that houses the dependencies for the `MoviesAPIService` as well as the networking dependencies. In the world where we are talking to multiple backends, I would most likely have a `NetworkModule` that provided the `OkHttpClient` and `Retrofit` objects but have a `Module` that handles the API so that we don't have to have every API interface as a dependency when attempting to use the networking layer.
- Testing is also not extremely robust as any error that is being passed back from the network is being bundled together. There is definitely a way to separate out some of the concerns with more robust error handling. Retrofit allows for `Result<T>` callbacks that can be wrapped in a `Single`. This would allow for us to determine the error codes and attempt retries based on specific use cases. This could be mapped to the `onErrorReturn()` methods or even put into the `onNext()` calls of the subscription.
- Currently there are a few places where things like `Application Context` or `Resources` could be injected without having to have them passed around as parameters. This is a known improvement.
- KAPT is currently in maintenance mode and could also be removed.

## Libraries Used

### RxJava2

While I know that RxJava3 exists, I have not used it enough in my day to day work to migrate to it and assume any differences in operational code, hence why RxJava2 is currently being used. Also, instead of using `suspend` functions and using Kotlin Coroutines (which I have limited experience in currently), I opted to use RxJava as the streamable approach for data observation and streaming. Coupling this with the use of the Retrofit adapters, we are able to return the `Call` responses into a `Single` which allows for consumption of the data on a single use when requesting information from the server. The reason I used a `Single` here is that the subscription should be terminated from the networking side when the REST call has completed. The server has sent us the data transmission and we will not be observing any new changes.

In the UI I opted for an `Observable` in order to consistently observe changes as we get new data fed that should be routed to the `MoviesAdapter`. These are all cleaned up when certain parts of the Android Application Lifecycle are triggered.

### Retrofit

Retrofit is the de-facto library for data transmission. Instead of building the entire HTTP layer myself, relying on this library helps with streamlining the process, as well as adds customizations for data translation when it is received by the device. By adding the `GsonConverterFactory` class, Retrofit exposes a POJO object using the `GSON` library that can easily be mapped to some sort of response object or model if preferred.

### Hilt

Hilt is a wrapper around Dagger that allows for a more clean usage of the dependency injection framework. Dagger has a lot of issues with setup and overall complexity, Hilt helps reduce that complexity and allows for class injection where it is needed. This reduces the bloat of carrying classes across multiple instantiations as well as helps with code isolation and testability. However, the largest gain by using something like Hilt for providing classes is that you can ensure the classes you want are the ones called. In this instance I created a `Singleton` object for the `Retrofit` object as it is expensive in nature to create at will. These interfaces also are extensible so if new dependences were needed to be added, the `ApplicationModule` can be modified, or a new `Module` can be created to isolate the dependencies.


### ViewBinding

Instead of using the archaic layout inflation methods and tree traversals that `findViewById` required, View Binding allows us to have type safety when assigning data to views. Instead of using the bloat heavy Data Binding library, View Binding is lightweight as it creates a single `Binding` class that creates direct references to all the views that have an ID in the layout. This greatly cleans up the code, removes the need of type casting, and increases runtime performance.

