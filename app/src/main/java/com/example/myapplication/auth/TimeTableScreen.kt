import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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



    // Fetch selected course from Firestore
    val selectedCourse = remember {
        runBlocking {
            vm.getCourse(userId)
        }
    }

    // Call getTimetable function to fetch data based on the selected course
    val timetableData = remember {
        runBlocking {
            vm.getTimetable(userId, selectedCourse.orEmpty())
        }
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
        )

        {

            item {
                Text(
                    text = selectedCourse ?: "No Course Selected",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    color = Color.White
                )
            }

            if (timetableData != null) {
                timetableData.forEach { (day, classes) ->

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = day,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    items(classes) { clazz ->
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 8.dp)
                                .border(1.dp, Color.Black) // Set border color here
                                .background(Color.White) // Set background color here
                        ) {
                            Text(text = clazz, modifier = Modifier.padding(8.dp))
                        }
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
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFFD700), Color(0xFFFFD700))
                    )
                )
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    Color.Transparent
                ),
                modifier = Modifier
                    .width(300.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xff554800), Color(0xff554800))
                        )
                    )
            ) {
                Text(
                    text = "Back",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}