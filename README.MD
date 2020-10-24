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