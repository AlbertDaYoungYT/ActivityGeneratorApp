package threeax.productivity.ideagen.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel


class NewTaskViewModel : ViewModel() {
    var title by mutableStateOf("")
        public set

    var description by mutableStateOf("")
        public set

    var weight by mutableStateOf("1")
        public set

    var minor_act by mutableStateOf("1")
        public set

    var mayor_act by mutableStateOf("0")
        public set

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogNewTask(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    value: NewTaskViewModel,
    ) {
    var fieldsNotFilled by remember { mutableStateOf("") }

    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    value = value.title,
                    onValueChange = {
                        value.title = it
                    },
                    singleLine = true,
                    label = {
                        Text(text = "Title")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                )
                TextField(
                    value = value.description,
                    onValueChange = {
                        value.description = it
                    },
                    label = {
                        Text(text = "Description")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                )/*
                TextField(
                    value = value.weight,
                    onValueChange = {
                        if (it == "") {
                            value.weight = ""
                        } else {
                            if (it.toInt() < 100) {
                                value.weight = it
                            }
                        }
                    },
                    label = {
                        Text(text = "Weight")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = true,
                            onClick = {}
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    RadioButton(
                        selected = (if (value.minor_act == "1") true else false),
                        onClick = {
                            if (value.minor_act == "0") {
                                value.minor_act = "1"
                                value.mayor_act = "0"
                            }
                        }
                    )
                    Text(
                        text = "Minor Activity",
                        style = MaterialTheme.typography.bodyMedium.merge(),
                        modifier = Modifier
                            .padding(start = 0.dp)
                            .padding(top = 15.dp)
                            .padding(bottom = 15.dp)
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = false,
                            onClick = {}
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    RadioButton(
                        selected = (if (value.mayor_act == "1") true else false),
                        onClick = {
                            if (value.mayor_act == "0") {
                                value.mayor_act = "1"
                                value.minor_act = "0"
                            }
                        }
                    )
                    Text(
                        text = "Mayor Activity",
                        style = MaterialTheme.typography.bodyMedium.merge(),
                        modifier = Modifier
                            .padding(start = 0.dp)
                            .padding(top = 15.dp)
                            .padding(bottom = 15.dp)
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                }*/
                if (fieldsNotFilled != "") {
                    Text(text = "*Please fill in Title field", color = Color.Red)
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    if (value.title == "") {
                        fieldsNotFilled = "1"
                    } else {
                        onConfirmation()
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}