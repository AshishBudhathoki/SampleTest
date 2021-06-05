# Coding Challenge

* An Android app as the assignment to fetch the data from the given API. 
* Designed with Repository Pattern and MVVM architectural pattern with Android Architecture Components.

## Architecture
The Application is built in MVVM Architecture.
MVVM have 3 main component called Model — View — View Model. View component will show data and manage the user interactions, View Model will handle all request
and provide all data needed for View,and Model will store the data.

![MVVM](https://upload.wikimedia.org/wikipedia/commons/8/87/MVVMPattern.png)

App also uses the Repository pattern to implement Single Source of Truth. Repository pattern implements separation of concerns by abstracting the data persistence logic.

![Repo](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

When the app is launched the view (LaunchFragment) asks LaunchesViewModel for data, LaunchesViewModel calls LaunchRepository for data as it handles all the data operations. The repository hides how data is supplied and makes apiservice call(Retrofit used) and saves data (Room used) when the api responds successfully or else send respective error message to the viewmodel. 
When the data is recieved by repository, it is communicated to viewmodel using kotlin Flow and then the view model supplies the data to view through LiveData. FLow and LiveData are observable data holder to observe changes in data. In this way data is presented to the user in UI. 
Hilt is used for providing dependencies as views are dependent to viewmodel, viewmodel dependent to repository and repository dependent on apiservice and data access objects.

For the Rocket detail and Launch Detail, the repository fetches data but doesn't cache.

## Testing
* app/test/ - Unit tests - test -> Right click on package name(com.demo.spacexdata) -> Run Test In 'com.demo.spacexdata'
* app/androidTest/ - Instrumentation tests - androidTest -> Right click on package name(com.demo.spacexdata) -> Run Test In 'com.demo.spacexdata'

## Libraries
Following are the Libraries used:
* Kotlin 💎
* Retrofit - Network Http Client
* Viewmodel - Channel between use cases and UI
* View Binding - For binding of UI components in layouts to data sources, and coroutines support.
* Gson - Data, Model & Entity JSON Parser that understands Kotlin non-nullable and default parameters
* okhttp-logging-interceptor - logs HTTP request and response data.
* Mockito - Mocking framework used in unit tests.
* kotlinx.coroutines - Library Support for coroutines, provides runBlocking coroutine builder used in tests
* picasso - For loading images