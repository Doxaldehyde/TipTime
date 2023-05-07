package com.dox.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dox.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

@Composable
fun TipTimeScreen(){
    var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    var tipInput by remember { mutableStateOf("")}
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    var roundup by remember { mutableStateOf(false)}
    val tip = calculateTip(amount, tipPercent, roundup)
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(text = stringResource(id = R.string.Calculate_Tip),
        modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        Spacer(modifier = Modifier.height(23.dp))
        EditNumberField(label = R.string.bill_amount,value = amountInput, onValueChange = { amountInput = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
            )
        EditNumberField(label = R.string.how_the_service, value = tipInput, onValueChange = {tipInput = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()})
            )
        RoundupTipRow(roundup = roundup, onRoundUpChange = {roundup = it} )
        Spacer(modifier = Modifier.height(23.dp))
        Text(text = stringResource(id = R.string.Tip_Amount, tip),
        modifier = Modifier.align(Alignment.CenterHorizontally),
        fontWeight = FontWeight.Bold )
    }

}

@Composable
fun EditNumberField(@StringRes label: Int, value: String, onValueChange: (String) -> Unit,
                    keyboardOptions:KeyboardOptions, keyboardActions: KeyboardActions,
                    modifier: Modifier = Modifier ) {

    TextField(
        value = value, onValueChange = onValueChange,
        label = { Text(text = stringResource(id = label), modifier = Modifier.fillMaxWidth()) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )
}
@Composable
fun RoundupTipRow(roundup: Boolean, onRoundUpChange: (Boolean) -> Unit ){
    Row(modifier = Modifier
        .fillMaxWidth()
        .size(48.dp), verticalAlignment = Alignment.CenterVertically){
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(checked = roundup, onCheckedChange = onRoundUpChange,modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
            )
    }
}
@VisibleForTesting
 fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundup: Boolean): String {
    var tip = tipPercent/100 * amount
    if(roundup){
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}