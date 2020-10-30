USEFUL RESOURCES

NAV COMPONENT
https://medium.com/androiddevelopers
https://developer.android.com/guide/navigation/navigation-getting-started


Deploy machine learning models on mobile
https://www.tensorflow.org/lite


up button with nav comp
https://stackoverflow.com/questions/57547445/using-android-navigation-component-with-single-activiy-layout-drawer-and-toolba

tool bar two ways
 (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)
 toolBar.setupWithNavController(findNavController())
 then add whatever i want to do with toolbar

second
 // And set up the ActionBar
   NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
 but i have to
 @Override
 public boolean onSupportNavigateUp() {
     return Navigation.findNavController(this, R.id.main_fragment).navigateUp()
             || super.onSupportNavigateUp();
 }



 to change the stroke color of textinput layout i can do this
 - create color resource folder
 - create selector with two states
 <selector xmlns:android="http://schemas.android.com/apk/res/android">
     <item android:color="@color/textBlue" android:state_focused="true" />
     <item android:color="@color/textBlue" android:state_focused="false" />
 </selector>
 - then use it in the stroke color attribute.

 https://stackoverflow.com/questions/56292017/textinputlayout-box-color



dismiss dialog
https://stackoverflow.com/questions/56884167/dialogfragment-does-not-dismiss-when-using-navigation-components

important tutorial about nav component
https://www.raywenderlich.com/4332831-navigation-component-for-android-part-2-graphs-and-deep-links

fragments pass-data-between
https://developer.android.com/training/basics/fragments/pass-data-between

important about dialoge and comments in this thread are important
https://stackoverflow.com/questions/50311637/navigation-architecture-component-dialog-fragments


get result with live data
https://developer.android.com/guide/navigation/navigation-programmatic#returning_a_result


Android Navigation Architecture Component - setTargetFragment for DialogFragment #
https://github.com/android/architecture-components-samples/issues/750


using-dialogfragment#custom
https://guides.codepath.com/android/using-dialogfragment#custom-view


https://stackoverflow.com/questions/50328941/navigation-architecture-component-action-navigation-with-lambda-expression


https://stackoverflow.com/questions/55027609/displaying-datepickerdialog-in-a-fragment


https://developer.android.com/guide/topics/ui/look-and-feel/themes


code to look at later
https://github.com/android/architecture-components-samples


https://www.raywenderlich.com/262-dependency-injection-in-android-with-dagger-2-and-kotlin
https://medium.com/@marco_cattaneo/integrate-dagger-2-with-room-persistence-library-in-few-lines-abf48328eaeb
https://stackoverflow.com/questions/44615986/setting-modules-and-components-with-dagger-2-for-android-mvp-activity-with-mul
https://medium.com/@MiBLT/refactoring-to-mvp-with-dagger-part-2-838c6e089a1
https://startandroid.ru/en/18-courses/dagger-2/432-lesson-1-introduction.html [[impo]]