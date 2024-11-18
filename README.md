# AtlantaFX Gluon

Monkey testing AtlantaFX Themes implemented on Gluon Platform ( Desktop, Android, Ios )

<p align="center">
<img src="https://github.com/CommonGrounds/AtlantaFX_Gluon/screenshots/base.png" alt="blueprints"/><br/>
<img src="https://github.com/CommonGrounds/AtlantaFX_Gluon/screenshots/card.png" alt="overview"/><br/>
<img src="https://github.com/CommonGrounds/AtlantaFX_Gluon/screenshots/chart.png" alt="page"/><br/>
<img src="https://github.com/CommonGrounds/AtlantaFX_Gluon/screenshots/left.png" alt="page"/><br/>
<img src="https://github.com/CommonGrounds/AtlantaFX_Gluon/screenshots/notification.png" alt="page"/><br/>
</p>

## Documentation

Read how to create Gluon samples step by step [here](https://docs.gluonhq.com/samples)

## Quick Instructions

We use [GluonFX plugin](https://docs.gluonhq.com/) to build a native image for platforms including desktop, android and iOS.
Please follow the prerequisites as stated [here](https://docs.gluonhq.com/#_requirements).

### Desktop

Run the application on JVM/HotSpot:

    mvn gluonfx:run

Run the application and explore all scenarios to generate config files for the native image with:

    mvn gluonfx:runagent

Build a native image using:

    mvn gluonfx:build

Run the native image app:

    mvn gluonfx:nativerun

### Android

Build a native image for Android using:

    mvn gluonfx:build -Pandroid

Package the native image as an 'apk' file:

    mvn gluonfx:package -Pandroid

Install it on a connected android device:

    mvn gluonfx:install -Pandroid

Run the installed app on a connected android device:

    mvn gluonfx:nativerun -Pandroid

### iOS

Build a native image for iOS using:

    mvn gluonfx:build -Pios

Install and run the native image on a connected iOS device:

    mvn gluonfx:nativerun -Pios

Create an IPA file (for submission to TestFlight or App Store):

    mvn gluonfx:package -Pios