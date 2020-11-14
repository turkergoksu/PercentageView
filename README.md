[![](https://jitpack.io/v/turkergoksu/PercentageView.svg)](https://jitpack.io/#turkergoksu/PercentageView)

# PercentageView
A Kotlin based adjustable custom view to show rating of a movie for [Kefilm](https://github.com/turkergoksu/Kefilm) project.

![intro](screenshots/intro.gif?raw=true)

## 🛠️Setup
#### 1. Add the JitPack repository to your build file if you didn't add before.
```gradle
allprojects {
    repositories {
        ...
		maven { url 'https://jitpack.io' }
    }
}
```

#### 2. Add the dependency
```gradle
dependencies {
    implementation 'com.github.turkergoksu:PercentageView:1.0.1'
}
```

## 🕹️Usage
```xml
<me.turkergoksu.lib.PercentageView
        android:id="@+id/percentageView"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:fontFamily="@font/orbitron_bold"
        app:animDuration="1500"
        app:centerColor="#f7efe2"
        app:percentage="99"
        app:percentageWidth="50"
        app:progressBackgroundColor="#4d648d"
        app:progressColor="#283655"
        app:textColor="#283655"
        app:textSize="42sp" />
```

If you prefer to set percentage value dynamically you may use code below:
```kotlin
var percentage = 100
percentageView.setPercentage(percentage)
```

## 📝Attributes
| Name | Format | Default | Description |
| ---- | ------ | ------- | ----------- |
|`percentage`|`integer`|`0`|Value of the percentage animation at the end.|
|`animDuration`|`integer`|`1000`|Animation duration in milliseconds.|
|`percentageWidth`|`float`|`50`|Width of the percentage bar.|
|`centerColor`|`color`|`white`|Background color of percentage value at the center. I recommend you to do same color as root view's background color. |
|`progressColor`|`color`|`black`|Animated fill color of the percentage bar.|
|`progressBackgroundColor`|`color`|`gray`|Background color of the percentage bar.|
|`textColor`|`color`|`black`|Text color of the percentage value at the center.|
|`textSize`|`dimension`|`32sp`|Text size of the percentage value.|
|`fontFamily`|`font`|`-`|Font family of the percentage value.|