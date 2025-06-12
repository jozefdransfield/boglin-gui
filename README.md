This is a Kotlin Multiplatform project targeting Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that's common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple's CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

## Automated Builds

This project uses GitHub Actions to automatically build application binaries for major platforms:

* Windows (.msi)
* macOS (.dmg)
* Linux (.deb)

### How it works

The GitHub Actions workflow is defined in `.github/workflows/build.yml` and:

1. Runs on push to the main branch, pull requests to main, or manual triggering
2. Builds the application on Windows, macOS, and Linux
3. Packages the application in the appropriate format for each platform
4. Uploads the built binaries as artifacts

### Accessing the binaries

After the workflow runs successfully:

1. Go to the Actions tab in the GitHub repository
2. Select the most recent workflow run
3. Scroll down to the Artifacts section
4. Download the binary for your platform:
   - `windows-msi` for Windows
   - `macos-dmg` for macOS
   - `linux-deb` for Linux


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…