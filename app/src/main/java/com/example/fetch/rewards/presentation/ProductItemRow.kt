package com.example.fetch.rewards.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fetch.rewards.R
import com.example.fetch.rewards.domain.ProductItem

/**
 * Displays a clickable [ProductItem] row containing
 * a placeholder image icon and a text description of the product item's contents.
 */
@Composable
fun ProductItemRow(
    productItem: ProductItem,
    onClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable { onClick(productItem.id) }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(android.R.drawable.ic_menu_gallery),
            contentDescription = stringResource(R.string.product_item_image_content_description)
        )
        Text(
            text = "ID: ${productItem.id}, ListID: ${productItem.listId}, Name: ${productItem.name}",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}