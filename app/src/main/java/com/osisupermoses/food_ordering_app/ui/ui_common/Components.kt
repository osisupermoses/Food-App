package com.osisupermoses.food_ordering_app.ui.ui_common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow
import com.osisupermoses.food_ordering_app.ui.theme.spacing

@Composable
fun Button(
    modifier: Modifier = Modifier,
    btnColor: ButtonColors = ButtonDefaults.buttonColors(GoldYellow),
    textColor: Color = Color.Black,
    enabled: Boolean = true,
    text: String,
    onButtonClicked: () -> Unit,
) {
    Button(
        onClick = onButtonClicked,
        colors = btnColor,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        enabled = enabled
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.button,
            letterSpacing = 0.00.em
        )
    }
}

@Composable
fun SectionHeader(
    sectionTitle: String
) {

    Text(
        text = sectionTitle,
        style = MaterialTheme.typography.h2,
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.medium)
            .padding(bottom = MaterialTheme.spacing.medium)
    )
}