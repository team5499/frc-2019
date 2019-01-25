# frc-2019
## Getting started
### Pre-commit
First, install the project's [pre-commit](http://pre-commit.com/) hooks (**do this immediately after cloning!**)
```
./gradlew install_hooks
```
Pre-commit hooks are programs that check the files you've changed each time you make a new commit. They will prevent you from checking in things like invalid JSON, code that fails the [Kotlin linter](https://github.com/shyiko/ktlint/), and more.

### Testing
To test your code, run:
```
./gradlew test
```
Test frequently and always test before deploying code to a roboRIO. Running tests will also run pre-commit on all of the files in the repo.

### Deploying
To deploy your code, run:
```
./gradlew deploy
```

## Troubleshooting
### False positives from the detekt-wrapper hook
[detekt](https://github.com/arturbosch/detekt/) helps catch common [code smells](https://en.wikipedia.org/wiki/Code_smell) in your program. If you encounter a false positive, you have two options:
* [Suppress the issue using the @Suppress annotation](https://arturbosch.github.io/detekt/suppressing-rules.html)
* Disable the issue across the whole project. To do this, you'll need to edit `.detekt-config.yml` as desired.
