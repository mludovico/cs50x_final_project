# This is the final project of the Harvard's cs50x course

It consists of 3 projects with the same purpose: retrieve foreign exchange rates and calculate amounts in a couple of currencies.

The main global currencies like USD, EUR and GBP was used, plus BRL which is from my country and Bitcoin.

The app retrieves data from the [exchangerate api](exchangerate.host) which gives a great sort of options and symbols including crypto.
___  
   

## Android

![android gif](https://raw.githubusercontent.com/mludovico/cs50x_final_project/master/media/android.gif)

### Layout

The Android app works from api 16 and uses the simplest controls

- Relative Layuout
- Linear Layuout
- TextView
- EditText
- Button

### The MainActivity

The api requests is made using the Volley library. A request is made when the app launches and every time the user tap the reload button &#10227;. The json response is kept into a hashmap and therefore used to calculate the amounts.
Thers is also a listener for every edittext to capture text changes and make the calculations. The results is then set back to the edittext text values.
The caveat was dealing with the propagation of event triggers, because when any text is set in the edittext it triggers the `onTextChangedListener` again. The solution was to use the `hasFocus()` property getter to know if it has to change or not.

___  
  

## iOS

![ios gif](https://raw.githubusercontent.com/mludovico/cs50x_final_project/master/media/ios.gif)

### Layout

The layout was made with the XCode storyboard designer and uses:
- ViewController
- NavigationBar
- BarButton
- UITextField
- UITextView
- UIButton

### ViewController

The api requests is done with the URLSession class and happens when the app launches and every time the user tap the reload bar button. The response is parsed into a `Response` object which is kept to use in the calculations. The TextFields has listener for editingChanged that captures the changes and calls the currency conversion. Then the results is set back to the TextFields text values. 

## Web

![web gif](https://raw.githubusercontent.com/mludovico/cs50x_final_project/master/media/web.gif)

### Layout

The web layout is simple. A couple of text inputs with labels and a button to refresh data. Styles uses [bootstrap](https://getbootstrap.com/) and some additional custom styles.

### application.py

The server is a [Flask](https://palletsprojects.com/p/flask/) application that responds to requests in some routes each of which returns determinate information.

- `/` : The home view. Displays a webpage with the fields where the user types the desired amount to be converted.
- `/getExchangeRate` : `[POST]` Returns a json content with the converted values of a given input plus the last update datetime.
- `reload` : `[GET]` Call the update function which will refresh the exchange rates.

The application make a call to the api every time the user loads the home view (`/` route) and stores the rates value to a dicionary which will later be used to make the calculations as well as the update datetime.
Due to [python's float limitations](https://docs.python.org/3/tutorial/floatingpoint.html) the decimal library was used. Again a side effect happens when one try to json encode this datatype. The workaround was to create a class and override the `default()` method of the `JSONEncoder` to make it able to convert a Decimal to string. Then use `json.dumps(data, cls=DecimalEncoder)` to correctly serialize Dictionary containing Decimals.

### main.js

Some javascript is done in the client-side to add listeners to the text inputs and call `/getExchangeRate` in the background and subsequently change the inputs values.