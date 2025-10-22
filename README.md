# XRP Toolkit
A set of related tools to track the liquidity movements of the digital asseet `XRP`.

<img width="439" height="916" alt="image" src="https://github.com/user-attachments/assets/69eff45e-df4a-4d63-875e-4a525f553f9b" />

## Features
### Price Watch Card
![price](https://github.com/user-attachments/assets/fb29c8a4-9e0a-4071-b638-e1abebd001e8)
- Gives the ability to auto-update a price data ticker for XRP or manually refresh.
  - Includes metrics for XRP/USD price, Market Cap, 24h volume, 24 high price, 24 low price, 24h percent change. 

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

