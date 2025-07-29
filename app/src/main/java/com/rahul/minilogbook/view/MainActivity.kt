package com.rahul.minilogbook.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.minilogbook.data.BGUnit
import com.rahul.minilogbook.ui.theme.BGLogBookTheme
import com.rahul.minilogbook.viewModel.BGLogViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BGLogBookTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    BGScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BGScreen(modifier: Modifier, viewModel: BGLogViewModel = hiltViewModel()) {

    val entries by viewModel.entries.collectAsState()
    val unit by viewModel.unit.collectAsState()
    val input by viewModel.inputValue.collectAsState()
    val average by viewModel.average.collectAsState()
    val context = LocalContext.current

    // Hoist the expanded state for the DropdownMenu
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Unit selection
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Select Unit:")
            Spacer(modifier = Modifier.width(8.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = { viewModel.onUnitChange(BGUnit.mmol_L)
                              expanded = false},
                    text = { Text(BGUnit.mmol_L.name)})

                DropdownMenuItem(onClick = { viewModel.onUnitChange(BGUnit.mg_dL)
                                           expanded = false },
                    text = { Text(BGUnit.mg_dL.name)})
            }
            Button(onClick = { expanded = true }) {
                Text(unit.name)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input field
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { viewModel.onInputChange(it) },
                label = { Text("Blood Glucose Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(unit.name)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Save Button
        Button(
            onClick = {
                if (input.toDouble() > 0.0)
                    viewModel.save()
                else
                    Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
                 },
            enabled = input.toDoubleOrNull() != null && input.toDouble() >= 0
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Average label
        Text("Your average BG is $average ${unit.name}", fontStyle = androidx.compose.ui.text.font.FontStyle.Italic )

        Spacer(modifier = Modifier.height(24.dp))

        // List of entries
        Text("Previous Entries:")
        LazyColumn {
            items(entries) { entry ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "%.2f %s".format(
                            if (entry.unit == unit) entry.value
                            else if (unit == BGUnit.mmol_L) entry.value / 18.0182
                            else entry.value * 18.0182,
                            unit.name
                        )
                    )
                    Text(
                        text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            .format(Date(entry.timeStamp))
                    )
                }
                Divider()
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BGLogBookTheme {
        BGScreen(modifier = Modifier)
    }
}