package com.simple.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.compose.ui.theme.KotlinLibraryXTheme

/**
 * @author jv.lee
 * @date 2022/1/6

 */
class ComposeUiActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinLibraryXTheme {
                SimpleText()
            }
        }
    }


    @Composable
    fun SimpleText() {
        Surface {
            Row(modifier = Modifier.padding(all = 8.dp)) {
                Image(
                    painter = painterResource(R.mipmap.picture),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colors.primary, CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "title",
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.subtitle2
                    )

                    Text(
                        text = "content",
                        style = MaterialTheme.typography.body2,
                        maxLines = 1
                    )
                }
            }
        }
    }

    @Preview(
        name = "Light Mode",
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        showBackground = true
    )
    @Preview(
        name = "Dark Mode",
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
    )
    @Composable
    fun PreviewSimpleText() {
        KotlinLibraryXTheme {
            SimpleText()
        }
    }

}



































