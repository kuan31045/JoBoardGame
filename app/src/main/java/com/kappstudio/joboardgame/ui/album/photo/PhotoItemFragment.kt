package com.kappstudio.joboardgame.ui.album.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kappstudio.joboardgame.databinding.FragmentPhotoItemBinding
import com.kappstudio.joboardgame.ui.theme.ComposeTheme
import com.kappstudio.joboardgame.R


class PhotoItemFragment(private val photo: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentPhotoItemBinding.inflate(inflater)
        val view = binding.root

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ComposeTheme {
                    var scale by remember {
                        mutableFloatStateOf(1f)
                    }
                    var offset by remember {
                        mutableStateOf(Offset.Zero)
                    }

                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val state =
                            rememberTransformableState { zoomChange, panChange, rotationChange ->
                                scale = (scale * zoomChange).coerceIn(1f, 5f)

                                val extraWidth = (scale - 1) * constraints.maxWidth
                                val extraHeight = (scale - 1) * constraints.maxHeight

                                val maxX = extraWidth / 2
                                val maxY = extraHeight / 2

                                offset = Offset(
                                    x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                                    y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
                                )
                            }

                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current).data(photo)
                                .crossfade(true).build(),
                            error = painterResource(id = R.drawable.image_defult),
                            placeholder = painterResource(id = R.drawable.image_defult),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    translationX = offset.x
                                    translationY = offset.y
                                }
                                .transformable(state))
                    }
                }
            }
        }

        return view
    }
}