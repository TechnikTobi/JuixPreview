# JuixPreview
A Luix Preview written in Java (because GTK and iced are stupid)

## Build locally
```
./gradlew build
jpackage --input build/libs/ --dest . --main-jar *.jar --main-class juixPreview.main.JuixPreview --name JuixPreview
```