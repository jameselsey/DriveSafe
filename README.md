DriveSafe
=========

![Screenshot](https://raw.github.com/jameselsey/drivesafe/master/github_assets/screenshot.png)

With a 3 hour driving commute, its quite frustrating when you get an SMS message and are unable to read it (as you're a good, legal driver of course).

This simple app attempts to solve that problem.

The app consists of a single screen, with a button to enable or disable the speaking of SMS messages

Once enabled, any incoming SMS messages will be spoken out via text-to-speech, so you would hear something like the following :

`SMS received from 07000000000, message is: What time will you be home at?`

To avoid any embarrassments, be sure to disable the app before attending meetings ;)

## How to build / run the app
1. `git clone https://github.com/jameselsey/DriveSafe.git`
2. Run `./gradlew tasks` to see the list of available run tasks, most should be self explanatory
2. Open the project in Android Studio and run on an emulator or device
3. Telnet to the device (only works for emulators) using `telnet localhost 5554` (or whatever port your emulator is running on, `adb devices` will help you there)
4. Send an sms to the device using `sms send 07843254333 Hello this is my message`

## Building the app for yourself
### Admob
Head over to [admob](www.google.com/ads/admob) and create an account
Generate an API key and put into res/values/admob.xml

### Analytics
Head over to [analytics](www.google.com/analytics/‎) and create an account
Generate an API key and put into res/values/analytics.xml

### Release keys
Add the following properties

```
storeFile = ./path/to/keystore.keystore
storePassword = mypassword
keyPassword = mypassword
```

You can either add them directly into the build.gradle file, in a gradle.properties file, or if you prefer to keep outside of the project,
then add them under ~/.gradle/gradle.properties

You should then be able to run `./gradlew clean build` which will result in a market ready APK

```
find . -name *.apk
./DriveSafe/build/apk/DriveSafe-debug-unaligned.apk
./DriveSafe/build/apk/DriveSafe-release-unaligned.apk
./DriveSafe/build/apk/DriveSafe-release.apk
```

## Future enhancements / contributions
* If the device is on silent or vibrate mode, don't attempt to speak anything, ever
* If the SMS comes from a known contact, speak the contacts name instead of phone number
* Allow the user to specify their own announcement string, using tokenisation such as SENDER and MESSAGE
* Similar process for emails?
* Test coverage (thats more of a learning thing for me)

Like it? Hate it? Let me know on my twitter [@jameselsey1986](https://twitter.com/jameselsey1986)
