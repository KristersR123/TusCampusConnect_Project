import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTableScreen(navController: NavController, vm: IgViewModel) {
    val timetableData = remember {
        mapOf(
            "Monday" to listOf(
                "Advanced Web Techniques 15B25, Carol Rainsford, 9:00am to 11:00am",
                "Mobile Application Development 1A15, Ita Kavanagh, 11:00am to 1:00pm",
                "Advanced Web Techniques 15B25, Carol Rainsford, 2:00pm to 4:00pm"
            ),
            "Tuesday" to listOf(
                "Mobile and Web Computing Project 15B26, Suzanne O'Gorman, 9:00am to 11:00am",
                "Data Structures and Algorithms 15B25, Caroline McAlister, 11:00am to 12:00pm",
                "Mobile and Web Computing Project 15B25, Suzanne O'Gorman, 1:00pm to 2:00pm",
                "Concurrent Programming 15B25, Alan Ryan, 2:00pm to 4:00pm"
            ),
            "Wednesday" to listOf(
                "Mobile Application Development 15B25, Ita Kavanagh, 11:00am to 1:00pm",
                "Concurrent Programming 15B25, Alan Ryan, 4:00pm to 6:00pm"
            ),
            "Thursday" to listOf("Free, no class/module"),
            "Friday" to listOf(
                "Advanced Web Technique 15B25, Carol Rainsford, 9:00am to 11:00am",
                "Data Structures and Algorithms 15B25, Caroline McAlister, 11:00am to 1:00pm",
                "Data Structures and Algorithms 15B25, Caroline McAlister, 1:00pm to 2:00pm"
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7E6E44))
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            timetableData.forEach { (day, classes) ->
                item {
                    Text(text = day, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 8.dp))
                }

                items(classes) { clazz ->
                    Text(text = "- $clazz", modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Spacer at the end to create space for the button
            item {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }

        // Back Button
        Button(
            onClick = {
                // Navigate back to HomeScreen
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Back to Home",
                fontSize = 18.sp
            )
        }
    }
}