package com.nikitinnikita.cfdemo.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.nikitinnikita.cfdemo.R
import com.nikitinnikita.cfdemo.model.Car
import com.nikitinnikita.cfdemo.ui.theme.Blue500
import com.nikitinnikita.cfdemo.ui.theme.CarfaxDemoTheme
import com.nikitinnikita.cfdemo.utils.Utils
import com.nikitinnikita.cfdemo.viewmodel.MainViewModel
import java.util.*

class MainActivity : ComponentActivity() {

    private val vm = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CarfaxDemoTheme {
                vm.getAllCars(LocalContext.current)
                CarView(LocalContext.current, vm)
            }

        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CarView(context: Context, vm: MainViewModel) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name).uppercase(), color = Color.White) },
                actions = {

                },
                backgroundColor = Blue500
            )
        },
        content = {
            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .padding(top = 10.dp)) {
                items(vm.cars) { car ->
                    CarCard(car = car, callClick = {
                        CallPermission(callback = {
                            if (it) {
                                AlertDialog(
                                    contentText = String.format(Locale.CANADA, stringResource(R.string.call_dealer_dialog), car.dealer.phone),
                                    confirmButtonText = stringResource(R.string.call),
                                    onConfirmClick = {
                                        vm.callDealer(context, car)
                                    })

                            } else {
                                Toast.makeText(context, Utils.string(context, R.string.error_permission_call_dealer), Toast.LENGTH_LONG).show()
                            }
                        })
                    }, onCardClick = {
                        DetailsActivity.startActivity(context, it.vin)
                    })

                }
            }
        }
    )
}


/**
 * CarCard is [Column] that displays car info and act on a call dealer button click.
 *
 * @param car object of a [Car] Model
 * @param callClick callback of a Call Dealer Button.
 * @param onCardClick callback of [Card] click
 *
 *
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CarCard(car: Car, callClick: @Composable () -> Unit, onCardClick: (Car) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box {
            Card(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    onCardClick(car)
                },
                backgroundColor = Color.White
            ) {
                Column {

                    // Top Image
                    CarCardImage(car = car)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Car info, generally text and spaces
                    CarCardTextSection(car = car)

                    Divider(color = Color.LightGray, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 10.dp))

                    // Call button that takes a callback
                    CarCardCallDealerButton {
                        callClick()
                    }

                }
            }
        }

    }

}

/**
 * CarCardImage is [AsyncImage] that displays an image downloaded from network
 *
 * @param car object of a [Car] Model
 *
 */
@Composable
fun CarCardImage(car: Car) {
    AsyncImage (
        model = car.images.photoSize.large,
        contentDescription = "Car image of ${car.make}",
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.ic_no_image)
    )
}

/**
 * CarCardTextSection is [Column] that displays info of the car, generally text
 *
 * @param car object of a [Car] Model
 *
 */
@Composable
fun CarCardTextSection(car: Car) {
    Column(
        modifier = Modifier.padding(horizontal = 10.dp)) {

        Text(
            text = "${car.year} ${car.make} ${car.model} ${car.trim}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row (verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = Utils.formatMoney(car.currentPrice),
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(10.dp))

            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .height(25.dp)
                    .width(2.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "${Utils.formatMiles(car.mileage)} ${stringResource(R.string.miles_short)}",
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "${car.dealer.city.capitalize(Locale.CANADA)}, ${car.dealer.state.uppercase()}",
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(10.dp))

    }
}

/**
 * CarCardCallDealerButton are [OutlinedButton] and [mutableStateOf] by clicking on the button it
 * changes the screenState variable that has a when statement that responds to callback.
 *
 * @param callClick is a @Composable callback that acts when button is clicked
 *
 */
@Composable
fun CarCardCallDealerButton(callClick: @Composable () -> Unit) {
    var screenState by remember {mutableStateOf(Recomposer.State.Idle)}


    OutlinedButton(
        onClick = {
            screenState = Recomposer.State.Inactive

            // trick, but a bad solution
            Handler().postDelayed({
                screenState = Recomposer.State.PendingWork
            }, 2_00)
        },
        border = BorderStroke(0.dp, Color.Transparent),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.call_dealer),
                color = Blue500,
                fontWeight = FontWeight.Bold
            )
        }

    when(screenState) {
        Recomposer.State.PendingWork -> {
            callClick()
        }

        else -> {

        }
    }
}

/**
 * CallPermission is a @Composable method that handle [Manifest.permission.CALL_PHONE] permission using
 * com.google.accompanist.permissions library
 *
 * @param callback is a callback that acts when user approves or denies a permission,
 * returns a boolean - true - approved, otherwise false
 *
 */
@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CallPermission(callback: @Composable (Boolean) -> Unit) {
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.CALL_PHONE)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchPermissionRequest()
                }
                else -> {

                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    when {
        permissionState.hasPermission -> {
            callback(true)
        }
        permissionState.shouldShowRationale -> {
            callback(false)
        }
        !permissionState.hasPermission && !permissionState.shouldShowRationale -> {
            callback(false)
        }
    }
}

@Composable
fun AlertDialog(contentText: String, confirmButtonText: String, onConfirmClick: () -> Unit) {
    val openDialog = remember { mutableStateOf(true)  }
    val confirmClicked = remember { mutableStateOf(false)  }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(stringResource(R.string.app_name).uppercase(), color = Blue500)
            },
            text = {
                Text(contentText, color = Color.Black)
            },
            backgroundColor = Color.White,
            confirmButton = {
                Button(
                    onClick = {
                        confirmClicked.value = true
                        openDialog.value = false
                    }) {
                    Text(confirmButtonText, color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        confirmClicked.value = false
                        openDialog.value = false
                    }) {
                    Text(stringResource(R.string.no), color = Color.White)
                }
            }
        )
    }

    if (confirmClicked.value) {
        onConfirmClick()
    }
}