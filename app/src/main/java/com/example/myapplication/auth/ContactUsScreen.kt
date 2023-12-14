import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.DestinationScreen
import com.example.myapplication.IgViewModel
import com.example.myapplication.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(navController: NavController, vm: IgViewModel) {
    Image(
        painter = painterResource(id = R.drawable.login_background),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Name TextField
        TextField(
            value = vm.userName,
            onValueChange = { vm.userName = it },
            label = { Text("Your Name") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                placeholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            )
        )

        // Email TextField
        TextField(
            value = vm.userEmail,
            onValueChange = { vm.userEmail = it },
            label = { Text("Your Email") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                placeholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            )
        )

        // Message TextField
        TextField(
            value = vm.userMessage,
            onValueChange = { vm.userMessage = it },
            label = { Text("Message") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                placeholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            )
        )

        // Submit Button
        Button(
            onClick = {
                // Handle form submission
                if (vm.userName.isNotBlank() && vm.userEmail.isNotBlank() && vm.userMessage.isNotBlank()) {
                    // Launch a coroutine to call the suspend function
                    vm.viewModelScope.launch {
                        vm.storeContactInFirestore(vm.userName, vm.userEmail, vm.userMessage)
                        // Reset the contact information after form submission
                        vm.userName = ""
                        vm.userEmail = ""
                        vm.userMessage = ""

                        // Introduce a delay to ensure the state is updated before navigating back
                        kotlinx.coroutines.delay(50)
                        // Navigate back to the home screen
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                Color.Transparent
            )
        ) {
            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text("Submit", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                Color.Transparent
            ),
            modifier = Modifier
                .width(300.dp)
                .align(Alignment.CenterHorizontally)
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