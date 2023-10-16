<div align="center">
    <img src="https://github.com/jeanbarrossilva/Orca/assets/38408390/8f4a58e6-f67e-42af-95c4-b48c7f0ce341" />
</div>

## About

Orca is a beautifully designed, fully functional Mastodon client that offers you the best
experience you could have.

As it's still in development, it currently supports searching for profiles, favoriting, reposting and sharing posts from the feed. The application is divided into modules that categorize each context, which often have submodules. Screens such as the feed and the profile details can be found within the `:feature` module, while core-level logic regarding authorization/authentication and read/write operations on profiles or posts are located at `:core`. As you will notice, `:core` has lots of submodules, but the one in which calls to the actual API take place is `:core:http`.

Note that Orca isn't a fork of the official app. Its overall structure has been built from the ground up to provide maximum developer-facing flexibility, making it is easy for each component to be tested in isolation and allowing for a pleasant, readable core-level API that's exposed to other modules, mainly `:feature` ones.

## Structure

<div align="center">
    <img src="https://github.com/jeanbarrossilva/Orca/assets/38408390/901a8965-9b7b-44d8-b99a-2bb91409cd96" />
</div>

Each module represents the context to which its underlying structures are related. Some, such as `platform:ui`, are still a work in progress and should eventually be broken down into conciser, more specific modules.

| Context | Description
----------|------------
Core      | Coordenates and executes actions tightly related to the core purpose of the application, which is reading and writing posts and profiles. Contains two main submodules (often referred to as "core variants" in the source code): `:core:sample`, which doesn't make any HTTP requests and targets offline usage for demonstration purposes, providing sample posts and profiles to browse through; and `:core:http`, whose structures call the Mastodon API and convert the DTOs it returns into `:core` objects.
Feature   | Each submodule within this context represents a screen that can be navigated to. One notable exception is the authorization screen, from which the user can choose the instance of their account and then sign in into the app: since it's strongly related to the Mastodon API, it is part of `:core:http`, and is shown by the `HttpAuthorizerActivity`.
Platform  | Contains platform-dependent (that is, Android-only) utilities for general purpose use that facilitate the overall development process.
Standard  | Similarly to the platform context, provides utilities that facilitate development, but doesn't depend on Android-related structures.

## Contributing

### Pre-requisites

- [Android Studio](https://developer.android.com/studio)

### Building

[Clone the repository](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository) to your machine and [open it in Android Studio](https://www.jetbrains.com/help/idea/import-project-or-module-wizard.html#open-project).

In order to build the project, you have to first obtain a few API keys. Except for the GitHub personal access token, all of them are completely optional if you only intend to build the demo version of the application (which doesn't make any network calls, as stated in the [Structure](https://github.com/jeanbarrossilva/Orca#structure) section).

Each item links to a documentation explaining how to obtain the respective key, and, below, how they should be added to the root `local.properties` file.

- [GitHub personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic):

```properties
github.username=your-username
github.token=your-token
```

- [Mastodon](https://docs.joinmastodon.org/client/token) (replace the `mastodon.example` that's mentioned in the documentation by your instance):
```properties
mastodon.clientSecret=your-token
```

- [instances.social](https://instances.social/api/doc):
```properties
instancesSocial.token=your-token
```

And... ready! You can now [build it](https://www.jetbrains.com/help/idea/compiling-applications.html#compile_module) and everything should work just fine.

### Code style

The project follows a Kotlin-adapted version of the [Google Java code style](https://google.github.io/styleguide/javaguide.html), and the [build workflow](https://github.com/jeanbarrossilva/Orca/actions/workflows/build.yml) will automatically fail if any change that isn't conformant to this specific style is pushed.
