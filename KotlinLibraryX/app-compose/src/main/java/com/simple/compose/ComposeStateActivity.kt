package com.simple.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 *
 * @author jv.lee
 * @date 2021/10/20
 */
class ComposeStateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloScreen()
        }
    }

    class HelloViewModel : ViewModel() {
        private val _name = MutableLiveData("")
        val name: LiveData<String> = _name

        fun onNameChange(newName: String) {
            _name.value = newName
        }

        private val _email = MutableStateFlow("")
        val email: StateFlow<String> = _email

        fun onEmailChange(newEmail: String) {
            _email.tryEmit(newEmail)
        }
    }

    @Composable
    fun HelloScreen(helloViewModel: HelloViewModel = viewModel()) {
        val name: String by helloViewModel.name.observeAsState("")
        val email: String by helloViewModel.email.collectAsState("")
        HelloContent(
            name = name, onNameChange = { helloViewModel.onNameChange(it) },
            email = email, onEmailChange = { helloViewModel.onEmailChange(it) }
        )
    }

    @Composable
    fun HelloContent(
        name: String, onNameChange: (String) -> Unit,
        email: String, onEmailChange: (String) -> Unit
    ) {
        Surface(color = Color.White) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    if (name.isNotEmpty()) {
                        Text(
                            text = "Hello, $name!",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.h5
                        )
                    }
                    if (email.isNotEmpty()) {
                        Text(
                            text = "email:$email.",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.h5
                        )
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text(text = ("Name")) })

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text(text = ("Email")) })
            }
        }
    }
}