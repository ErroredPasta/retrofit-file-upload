package com.example.retrofitupload

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.retrofitupload.ui.theme.RetrofitUploadTheme
import java.io.File
import java.io.FileNotFoundException

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitUploadTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainBody(onUploadButtonClick = viewModel::uploadFile)
                }
            }
        }
    }
}

@Composable
fun MainBody(
    onUploadButtonClick: (File) -> Unit,
) {
    var uri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            if (it == null) return@rememberLauncherForActivityResult
            uri = it
        }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        if (uri != null) {
            AsyncImage(
                model = uri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .width(300.dp)
                    .scale(4 / 3f)
            )
        }

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image")
        }

        if (uri != null) {
            Button(onClick = { onUploadButtonClick(uri!!.toFile(context)) }) {
                Text("Upload")
            }
        }
    }
}

fun Uri.toFile(context: Context): File {
    val contentResolver = context.contentResolver

    contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndexOrThrow("_display_name")
        cursor.moveToFirst()
        val name = cursor.getString(nameIndex)
        val file = File(context.cacheDir, name)
        file.outputStream().use { outputStream ->
            contentResolver.openInputStream(this)?.use { inputStream ->
                outputStream.write(inputStream.readBytes())
            }
        }
        return file
    }
    throw FileNotFoundException()
}

@Preview(showBackground = true)
@Composable
private fun MainBodyPreview() {
    RetrofitUploadTheme {
        MainBody(onUploadButtonClick = {})
    }
}
