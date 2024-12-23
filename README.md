<div align="center">
    <img src="https://github.com/the-orca-app/android/assets/38408390/adfd2748-5ca3-482a-9a65-650cdea4f8bb" />
</div>

## About

Orca is a beautifully designed, fully functional Mastodon client that offers you the best experience you could have.

As it's still in development, it currently supports searching for profiles, favoriting, reposting and sharing posts from the feed. The application is divided into modules that categorize each context, which often have submodules. Screens such as the feed and the profile details can be found within the [`:feature`](https://github.com/jeanbarrossilva/Orca/tree/main/feature) module, while core-level logic regarding authorization/authentication and read/write operations on profiles or posts are located at [`:core`](https://github.com/jeanbarrossilva/Orca/tree/main/core). As you will notice, [`:core`](https://github.com/jeanbarrossilva/Orca/tree/main/core) has lots of submodules, but the one in which calls to the actual API take place is [`:core:mastodon`](https://github.com/jeanbarrossilva/Orca/tree/main/core/mastodon).

Note that Orca isn't a fork of the official app. Its overall structure has been built from the ground up to provide maximum developer-facing flexibility, making it easy for each component to be tested in isolation and allowing for a pleasant, readable core-level API that's exposed to other modules, mainly [`:feature`](https://github.com/jeanbarrossilva/Orca/tree/main/feature) ones.

## Structure

<div align="center">
    <img src="https://github.com/user-attachments/assets/5c997a7a-0b87-4d38-9529-9efb9d7ea625" />
</div>

Each module represents the context to which its underlying structures are related.

| Context | Description
----------|------------
Composite | Exposes APIs that rely on core and/or platform modules.
Core      | Coordinates and executes actions tightly related to the core purpose of the application, which is reading and writing profiles and posts. Contains two main submodules (often referred to as "core variants" in the source code): [`:core:sample`](https://github.com/jeanbarrossilva/Orca/tree/main/core/sample), which doesn't make any HTTP requests and targets offline usage for demonstration purposes, providing sample posts and profiles to browse through; and [`:core:mastodon`](https://github.com/jeanbarrossilva/Orca/tree/main/core/mastodon), whose structures call the Mastodon API and convert the DTOs it returns into [`:core`](https://github.com/jeanbarrossilva/Orca/tree/main/core) objects.
Extension | Provides extensions built upon Kotlin API (not tied to the platform) that ease development.
Feature   | Each submodule within this context represents a screen that can be navigated to. One notable exception is the authorization screen, from which the user can input the instance of their account and then sign in into the app: since it's strongly related to the Mastodon API, it is part of [`:core:mastodon`](https://github.com/jeanbarrossilva/Orca/tree/main/core/mastodon), and is shown by the [`HttpAuthorizationActivity`](https://github.com/jeanbarrossilva/Orca/blob/main/core/mastodon/src/main/java/com/jeanbarrossilva/orca/core/http/auth/authorization/HttpAuthorizationActivity.kt).
Platform  | Contains platform-dependent (that is, Android-only) utilities for general purpose use that facilitate the overall development process.
Standard  | Similarly to the platform context, provides utilities, but doesn't depend on platform-specific structures. Differs from the extension context in that it provides new APIs instead of just making it easier to use currently existing ones.

## Contributing

### Pre-requisites

- [Android Studio](https://developer.android.com/studio)

#### API keys

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

### Building

[Clone the repository](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository) to your machine, [open it in Android Studio](https://www.jetbrains.com/help/idea/import-project-or-module-wizard.html#open-project) and then [build it](https://www.jetbrains.com/help/idea/compiling-applications.html#compile_module). If the previous steps were followed, this process shouldn't throw any errors and the project should be built successfully.

### Code style

The project follows a Kotlin-adapted version of the [Google Java code style](https://google.github.io/styleguide/javaguide.html), and the [build workflow](https://github.com/jeanbarrossilva/Orca/actions/workflows/build.yml) will automatically fail if any change that isn't conformant to this specific style is pushed.
