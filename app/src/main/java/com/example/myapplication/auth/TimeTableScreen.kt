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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTableScreen(navController: NavController, vm: IgViewModel) {
    // Get the current user ID from Firebase Authentication
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    // Call getTimetable function to fetch data
    val timetableData = remember {
        // runBlocking is used here to block the main thread temporarily and wait for the result
        runBlocking {
            vm.getTimetable(userId)
        }
    }

    // Check if timetableData is not null
    if (timetableData != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF7E6E44))
                .padding(16.dp)
        ) {
            // ... (rest of the code for displaying timetable screen)

        }
    } else {
        // Show a message or navigate to a different screen indicating no timetable available
        Text(
            text = "No timetable available",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
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
            if (timetableData != null) {
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