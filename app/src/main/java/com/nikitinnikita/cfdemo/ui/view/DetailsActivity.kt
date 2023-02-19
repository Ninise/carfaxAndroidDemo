package com.nikitinnikita.cfdemo.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nikitinnikita.cfdemo.R
import com.nikitinnikita.cfdemo.model.Car
import com.nikitinnikita.cfdemo.ui.theme.Blue500
import com.nikitinnikita.cfdemo.utils.Utils
import com.nikitinnikita.cfdemo.viewmodel.DetailsViewModel
import com.nikitinnikita.cfdemo.viewmodel.MainViewModel
import java.util.*

class DetailsActivity : ComponentActivity() {

    private val vm = DetailsViewModel()

    companion object {

        private const val CAR_VIN = "CAR_VIN"

        @JvmStatic
        fun startActivity(context: Context, vin: String) {
            val starter = Intent(context, DetailsActivity::class.java)
                .putExtra(CAR_VIN, vin)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // overlay the status bar with a car image
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {

            intent.getStringExtra(CAR_VIN)?.also {vin ->
                vm.getCar(applicationContext, vin)

                CarDetailsView(car = vm.car, onBackClick = {
                    finish()
                }, onCallClick = {car ->
                    CallPermission(callback = { granted ->
                        if (granted) {
                            AlertDialog(
                                contentText = String.format(Locale.CANADA, stringResource(R.string.call_dealer_dialog), car.dealer.phone),
                                confirmButtonText = stringResource(R.string.call),
                                onConfirmClick = {
                                    vm.callDealer(applicationContext, car)
                                })
                        } else {
                            Toast.makeText(applicationContext, getString(R.string.error_permission_call_dealer), Toast.LENGTH_LONG).show()
                        }
                    })

                })
            }?: kotlin.run { // if car vin is null show error text (better to have an appropriate screen)
                Text(text = getString(R.string.car_is_not_found))
            }

        }
    }
}


@Preview
@Composable
fun previewView() {
    val car = Car()
    CarDetailsView(car = car, onBackClick = {

    }, onCallClick = {

    })
}

/**
 * CarDetailsView is [Column] that displays a car info and act on a call dealer button click.
 *
 * @param car object of a [Car] Model
 * @param onCallClick callback of a Call Dealer Button.
 * @param onBackClick callback of [IconButton] click, returns user to previos screen
 *
 *
 */
@Composable
fun CarDetailsView(car: Car, onCallClick: @Composable (Car) -> Unit, onBackClick: () -> Unit) {

    Column {
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) {

            CarImage(car)

            CarMainText(car)

            Spacer(Modifier.height(25.dp))

            Divider(
                color = Color.LightGray,
                thickness = 0.5.dp
            )

            CarDetailedText(car)

            Spacer(Modifier.height(40.dp))

            Divider(
                color = Color.DarkGray,
                thickness = 1.dp
            )

            Spacer(Modifier.height(70.dp))
        }

        CarCallDealerButton(car, onCallClick)
        Spacer(Modifier.height(50.dp))
    }



    IconButton(onClick = {
        onBackClick()
    }, modifier = Modifier.padding(top = 25.dp)) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Back Button",
            tint = Color.White
        )
    }



}

/**
 * CarImage is [AsyncImage] that displays an image downloaded from network
 *
 * @param car object of a [Car] Model
 *
 */
@Composable
fun CarImage(car: Car) {
    AsyncImage (
        model = car.images.photoSize.large,
        contentDescription = "Image of a car ${car.make}",
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        error = painterResource(id = R.drawable.ic_no_image),
        contentScale = ContentScale.Crop

    )
}

/**
 * CarMainText is [Column] that displays 2 lines of bold text
 *
 * @param car object of a [Car] Model
 *
 */
@Composable
fun CarMainText(car: Car) {
    Column(modifier = Modifier.padding(horizontal = 40.dp)) {
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "${car.year} ${car.make} ${car.model} ${car.trim}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row (verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = Utils.formatMoney(car.currentPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .height(18.dp)
                    .width(3.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "${Utils.formatMiles(car.mileage)} ${stringResource(R.string.miles_short)}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

/**
 * CarDetailedText is [Column] that shows generally a car info and uses CarTextInfo to generate row for text
 *
 * @param car object of a [Car] Model
 *
 */
@Composable
fun CarDetailedText(car: Car) {
    Column(modifier = Modifier.padding(horizontal = 40.dp)) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = stringResource(R.string.vehicle_info), fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(15.dp))

        CarTextInfo(title = stringResource(R.string.location), value = "${car.dealer.city.capitalize(Locale.CANADA)}, ${car.dealer.state.uppercase()}")
        CarTextInfo(title = stringResource(R.string.exterion_color), value = car.exteriorColor)
        CarTextInfo(title = stringResource(R.string.interion_color), value = car.interiorColor)
        CarTextInfo(title = stringResource(R.string.drive_type), value = car.drivetype)
        CarTextInfo(title = stringResource(R.string.transmission), value = car.transmission)
        CarTextInfo(title = stringResource(R.string.body_style), value = car.bodytype)
        CarTextInfo(title = stringResource(R.string.engine), value = car.engine)
        CarTextInfo(title = stringResource(R.string.fuel), value = car.fuel)

    }
}

/**
 * CarTextInfo is [Row] generated by 2 params
 *
 * @param title is [String] on the left side
 * @param value is [String] on the right side
 *
 */
@Composable
fun CarTextInfo(title: String, value: String) {
    Row {
        Text(text = title, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.weight(1f))

        Text(text = value, color = Color.Black, fontSize = 14.sp, modifier = Modifier
            .weight(1f), textAlign = TextAlign.Start)
    }
}

/**
 * CarCallDealerButton are [Button] and mutableStateOf variable that handles click on the button
 *
 * @param car is [Car] object
 * @param onCallClick Composable callback that takes [Car] object
 *
 */
@Composable
fun CarCallDealerButton(car: Car, onCallClick:  @Composable (Car) -> Unit) {
    var screenState by remember{ mutableStateOf(Recomposer.State.Idle) }

    Button(
        onClick = {
            screenState = Recomposer.State.Inactive

            // trick, but a bad solution
            Handler().postDelayed({
                screenState = Recomposer.State.PendingWork
            }, 2_00)
        },
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Blue500),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)) {

        Text(
            text = stringResource(R.string.call_dealer),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

    }

    when(screenState) {
        Recomposer.State.Idle -> {

        }

        else -> {
            onCallClick(car)
        }
    }
}






