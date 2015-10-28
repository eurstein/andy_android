title: android training notes

[TOC]

## [Build System OverView](https://developer.android.com/intl/zh-cn/sdk/installing/studio-build.html#detailed-build)

## [Providing Resource](https://developer.android.com/guide/topics/resources/providing-resources.html)
1. 不同类型的资源可以同名
2. values/ 包含字符串、整型数和颜色等简单值的 XML 文件。
文件名约定
```
arrays.xml，用于资源数组（类型化数组）。
colors.xml：颜色值。
dimens.xml：尺寸值。
strings.xml：字符串值。
styles.xml：样式。
```


## [Layouts](https://developer.android.com/guide/topics/ui/declaring-layout.html)
1. ID 不需要在整个结构树中具有唯一性，但在您要搜索的结构树部分应具有唯一性（要搜索的部分往往是整个结构树，因此最好尽可能具有全局唯一性）。
```
Button myButton = (Button) findViewById(R.id.my_button);
```

## [Building a Simple User Interface](https://developer.android.com/intl/zh-cn/training/basics/firstapp/building-ui.html)
1. This button doesn't need the android:id attribute, because it won't be referenced from the activity code.
```
      <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/button_send" />
```
PS: [如果小工具没有 ID，则系统无法保存其状态。](https://developer.android.com/intl/zh-cn/guide/components/activities.html#Lifecycle)

2. layout_weight, layout_width
```
Also, assign <EditText> element's layout_width attribute a value of 0dp.
res/layout/activity_my.xml
<EditText
    android:layout_weight="1"
    android:layout_width="0dp"
    ... />
To improve the layout efficiency when you specify the weight, you should change the width of the EditText to be zero (0dp). Setting the width to zero improves layout performance because using "wrap_content" as the width requires the system to calculate a width that is ultimately irrelevant because the weight value requires another width calculation to fill the remaining space.
```
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal">
    <EditText android:id="@+id/edit_message"
        **android:layout_weight="1"**
        **android:layout_width="0dp"**
        android:layout_height="wrap_content"
        android:hint="@string/edit_message" />
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/button_send" />
</LinearLayout>

```

## [Intent和Intent过滤器](https://developer.android.com/intl/zh-cn/guide/components/intents-filters.html)
1. 强制使用应用选择器，如果多个应用可以响应Intent，采用下列方法可以强制每次都弹应用选择框（仅有一个应用宝响应intent不会弹，有多个应用响应时，不管是否有选择默认应用都会弹）
```
Intent sendIntent = new Intent(Intent.ACTION_SEND);
...

// Always use string resources for UI text.
// This says something like "Share this photo with"
String title = getResources().getString(R.string.chooser_title);
// Create intent to show the chooser dialog
Intent chooser = Intent.createChooser(sendIntent, title);

// Verify the original intent will resolve to at least one activity
if (sendIntent.resolveActivity(getPackageManager()) != null) {
    startActivity(chooser);
}
```

## [android:parentActivityName](https://developer.android.com/intl/zh-cn/training/basics/firstapp/starting-activity.html#CreateActivity)
默认的导航行为
The android:parentActivityName attribute declares the name of this activity's parent activity within the app's logical hierarchy. The system uses this value to implement default navigation behaviors
```
<application ... >
    ...
    <activity
        android:name="com.mycompany.myfirstapp.DisplayMessageActivity"
        android:label="@string/title_activity_display_message"
        android:parentActivityName="com.mycompany.myfirstapp.MyActivity" >
        <meta-data
            android:name="android.support.PARENT_ACTIVITY"
            android:value="com.mycompany.myfirstapp.MyActivity" />
    </activity>
</application>
```

## [Support Libraries](https://developer.android.com/tools/support-library/setup.html#download)
1. Note: If you're developing with Android Studio, select and install the Android Support Repository item instead.


## [Material design](https://www.google.com/design/spec/material-design/introduction.html)

## [Google Design](https://design.google.com/)

## [Use Platform Styles and Themes](https://developer.android.com/intl/zh-cn/training/basics/supporting-devices/platforms.html#style-themes)

## [Activity](https://developer.android.com/intl/zh-cn/guide/components/activities.html#Lifecycle)
1. PS: 不过，即使您什么都不做，也不实现 onSaveInstanceState()，Activity 类的 onSaveInstanceState() 默认实现也会恢复部分 Activity 状态。具体地讲，默认实现会为布局中的每个 View 调用相应的 onSaveInstanceState() 方法，让每个视图都能提供有关自身的应保存信息。Android 框架中几乎每个小工具都会根据需要实现此方法，以便在重建 Activity 时自动保存和恢复对 UI 所做的任何可见更改。例如，EditText 小工具保存用户输入的任何文本，CheckBox 小工具保存复选框的选中或未选中状态。您只需为想要保存其状态的每个小工具提供一个唯一的 ID（通过 android:id 属性）。[如果小工具没有 ID，则系统无法保存其状态。](https://developer.android.com/intl/zh-cn/guide/components/activities.html#Lifecycle)

2. 由于 onSaveInstanceState() 的默认实现有助于保存 UI 的状态， 因此如果您为了保存更多状态信息而重写该方法，应始终先调用 onSaveInstanceState() 的超类实现，然后再执行任何操作。同样，如果您替代 onRestoreInstanceState() 方法，也应调用它的超类实现，以便默认实现能够恢复视图状态。

3. 注：由于无法保证系统会调用 onSaveInstanceState()，因此您只应利用它来记录 Activity 的瞬态（UI 的状态）—切勿使用它来存储持久性数据，而应使用 onPause() 在用户离开 Activity 后存储持久性数据（例如应保存到数据库的数据）。

4. 您只需旋转设备，让屏幕方向发生变化，就能有效地测试您的应用的状态恢复能力。 当屏幕方向变化时，系统会销毁并重建 Activity，以便应用可供新屏幕配置使用的备用资源。 单凭这一理由，您的 Activity 在重建时能否完全恢复其状态就显得非常重要，因为用户在使用应用时经常需要旋转屏幕。

5. 以下是当 Activity A 启动 Activity B 时一系列操作的发生顺序：
```
	1. Activity A 的 onPause() 方法执行。
	2. Activity B 的 onCreate()、onStart() 和 onResume() 方法依次执行。（Activity B 现在具有用户焦点。）
	3. 然后，如果 Activity A 在屏幕上不再可见，则其 onStop() 方法执行。
```
您可以利用这种可预测的生命周期回调顺序管理从一个 Activity 到另一个 Activity 的信息转变。 例如，如果您必须在第一个 Activity 停止时向数据库写入数据，以便下一个 Activity 能够读取该数据，则应在 onPause() 而不是 onStop() 执行期间向数据库写入数据。


## [Starting an Activity](https://developer.android.com/intl/zh-cn/training/basics/activity-lifecycle/starting.html#launching-activity)
```
<activity android:name=".MainActivity" android:label="@string/app_name">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
1. 如果未对您的Activity之一声明 MAIN 操作或 LAUNCHER 类别，那么您的应用图标将不会出现在应用的主屏幕列表中。
2. 如果对多个Activity设置 MAIN 操作和 LAUNCHER 类别，那么在应用主屏幕列表将展示两个应用图标，分别对应具体的Activity

注意：在所有情况下，系统在调用 onPause() 和 onStop() 之后都会调用 onDestroy() ，只有一个例外：当您从 onCreate() 方法内调用 finish() 时。在有些情况下，比如当您的Activity作为临时决策工具运行以启动另一个Activity时，您可从 onCreate() 内调用 finish() 来销毁Activity。 在这种情况下，系统会立刻调用 onDestroy()，而不调用任何其他 生命周期方法。

## [Fragments](https://developer.android.com/intl/zh-cn/guide/components/fragments.html#Managing)

