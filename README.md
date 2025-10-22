# XRP Toolkit
A set of related tools to track the liquidity movements of the digital asseet `XRP`.

<img width="439" height="916" alt="image" src="https://github.com/user-attachments/assets/69eff45e-df4a-4d63-875e-4a525f553f9b" />

## Features
- Price tracker card for XRP-USD pairing.

## Build
- To build native desktop app `./gradlew :composeApp:createDistributable`
- To build iOS app, run a `./gradlew clean` and then `open iosApp/iosApp.xcodeproj/` and then build from within XCode

## Notes
### Physical iOS device steps
- Had to login Apple developer account in XCode
- Had to turn phone on [`Developer Mode`](https://developer.apple.com/documentation/xcode/enabling-developer-mode-on-a-device)
- Select physical iPhone device in XCode
- Trust the developer certificate in `Settings > General > VPN & Device Management`

Should be able to build and sign the app and successfully launch on physical iOS device.

