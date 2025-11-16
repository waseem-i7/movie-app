# Project Overview:

"This is a Movie App built to demonstrate a modern, robust, and scalable Android application. It displays a list of trending movies, allows 
users to search for specific titles, and view movie details. The core features include a clean, reactive UI built with Jetpack Compose, 
offline caching with Room, and a well-defined architecture that ensures the app is maintainable and testable. The entire technology stack 
is based on Google's recommended best practices for modern Android development."


# 1. Core Architecture: Clean Architecture with MVVM

"For this project, I chose to implement Clean Architecture combined with the Model-View-ViewModel (MVVM) pattern. This decision was 
driven by the need for a clear separation of concerns, which makes the app more scalable, easier to debug, and highly testable."

The architecture is divided into three primary layers:

**Clean Architecture Diagram**


## a) Presentation Layer (The UI)

•What it is: This is everything the user sees and interacts with. It includes the Composable screens and the ViewModels.  
•Technologies Used:  
    ◦Jetpack Compose: I used Compose for the entire UI. It's a declarative UI framework that simplifies building and maintaining complex UIs.  
    ◦ViewModel: ViewModels act as state holders for the UI. They survive configuration changes (like screen rotation) and expose data to the UI via observable streams.  
    ◦Compose Navigation: Handles all the navigation between the MovieListScreen and MovieDetailScreen.  

•How it works: The UI observes data streams (StateFlow) from the ViewModel. When the user performs an action (like a search), the UI 
 calls a function in the ViewModel. The ViewModel processes the action, updates its state, and the UI automatically recomposes to 
 reflect the new state. This is known as Unidirectional Data Flow (UDF), which makes the UI state predictable and easy to manage.


## b) Domain Layer (The Business Logic)

•What it is: This is the heart of the application. It contains the core business rules and is completely independent of the UI and data layers. It knows nothing about Android frameworks.  
•Key Components:  
    ◦Use Cases (or Interactors): Each use case represents a single, specific business task. For example, GetTrendingMoviesUseCase or 
     SearchMoviesUseCase. This follows the Single Responsibility Principle and makes the business logic very clear and reusable.  
    ◦Repository Interface (MovieRepository): This is an abstraction that defines what data operations are possible (e.g., getTrendingMovies()), 
     but not how they are performed. The Domain layer owns this interface.  

•Why it's important: By keeping this layer pure (no Android code), the core logic can be tested with simple unit tests, and it's not 
 affected by changes in the UI or database implementation.


## c) Data Layer (The Data Sources)

•What it is: This layer is responsible for providing the data required by the application. It implements the repository interface from the Domain layer.  
•Key Components:  
    ◦Repository Implementation (MovieRepositoryImpl): This class implements the MovieRepository interface. It's the single source of 
     truth for the app's data. It contains the logic to decide whether to fetch data from the network or the local cache.  
    ◦Remote Data Source:  
        ▪Retrofit & OkHttp: Used to make network calls to the TMDB API.  
        ▪Interceptors: I've implemented two key OkHttp interceptors:  
            a.AuthInterceptor: Automatically injects the API key into every request, so I don't have to manually add it to every API 
              function call. This is cleaner and less error-prone.  
            b.HttpLoggingInterceptor: Logs all network request and response details during development, which is incredibly useful for 
              debugging.  
    ◦Local Data Source:  
        ▪Room: A persistence library that provides an abstraction layer over SQLite. It's used to cache movie data for offline access. 
         The Movie data class is annotated with @Entity, and the MovieDao interface defines the database operations.


# 2. The Working Flow: A User's Journey

Let's trace two key user flows to see how these layers work together.


## Flow 1: App Startup and Displaying Trending Movies

1.App Launch: The MainActivity is created and sets the Navigation composable as its content.  
2.UI Display: The NavHost displays the MovieListScreen. At this point, the UI is in a Loading state. The screen shows shimmering 
  placeholders to provide a good user experience.  
3.ViewModel Initialization: Hilt creates an instance of MovieListViewModel. In its init block, it calls fetchTrendingMovies().  
4.Executing Business Logic: The ViewModel calls the GetTrendingMoviesUseCase.  
5.Data Fetching: The use case calls the getTrendingMovies() function on the MovieRepository interface.  
6.The Repository's Decision:  
    ◦MovieRepositoryImpl receives the call. It first checks the NetworkMonitor utility to see if the device is online.  
    ◦If Online: It makes a network call via the MovieApiService (Retrofit). The API response is received. The repository then saves this 
     fresh list of movies into the Room database (movieDao.insertMovies()) and returns the list to the use case.  
    ◦If Offline: It skips the network call and directly fetches the cached movies from the Room database (movieDao.getMovies()).  
7.State Update: The ViewModel receives the list of movies (or an error) from the use case. It wraps this result in a Response.Success or 
  Response.Error class and updates its _movies StateFlow.  
8.UI Recomposition: The MovieListScreen is observing the movies StateFlow. It receives the new Success state, and Jetpack Compose 
  automatically recomposes the UI to display the LazyVerticalGrid of movies.


## Flow 2: User Searches for "Oppenheimer"

1.User Input: The user types "Oppenheimer" into the SearchBar composable.  
2.Event Trigger: With each character typed, the onValueChange callback is triggered, calling viewModel.onSearchQueryChanged().  
3.Reactive Search: The ViewModel updates its _searchQuery StateFlow.  
4.Debouncing: In the ViewModel's init block, a coroutine is observing this searchQuery flow. Crucially, it uses the .debounce(500) 
  operator. This means it will only proceed if the user stops typing for 500 milliseconds. This prevents an Database call for every single 
  keystroke, providing a smoother experience.  
5.Executing Search: Once the debounce time passes, the flow continues. It calls the SearchMoviesUseCase with the query "Oppenheimer".  
6.Data Fetching: The use case calls repository.searchMovies(). For search, the repository is configured to only fetch from the room database.
7.State Update & UI Recomposition: The ViewModel receives the search results, updates the _movies StateFlow, and the UI recomposes 
  to show the new list of movies matching the search query. If the user clears the search bar, the searchQuery becomes empty, and the 
  observeSearchQuery flow triggers fetchTrendingMovies() again to return to the original state.


# 3. Key Strengths & Best Practices Demonstrated

•Architectural Purity: "I successfully implemented a Clean Architecture, which separates the app into independent layers. This makes 
 the codebase robust, testable, and easy to scale."  
•Reactive and Declarative UI: "The UI is fully built with Jetpack Compose and follows a reactive, unidirectional data flow model. This 
 results in a more predictable and less error-prone UI layer."  
•Offline-First Strategy: "I implemented a network-first, cache-fallback strategy. The app provides a seamless experience whether the 
 user is online or offline, by intelligently fetching data from the network or the local Room database."  
•Efficient Networking: "The networking layer is optimized with interceptors for authentication and logging. Furthermore, the search 
 functionality is debounced to prevent excessive db requests, making the app more efficient."  
•User Experience Focus: "I paid close attention to the user experience by implementing placeholder loading states, empty state, a dark theme to 
 match the designs, and a dedicated error screen with a retry mechanism, ensuring the user is never left at a dead end."  
•Dependency Management: "I used Hilt for dependency injection, which decouples components and greatly simplifies testing and 
 managing class dependencies throughout the app."
