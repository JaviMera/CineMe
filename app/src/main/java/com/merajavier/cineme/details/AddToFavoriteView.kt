package com.merajavier.cineme.details

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.merajavier.cineme.R

class AddToFavoriteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val addToFavoriteImageView: ImageView

    init{
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.movie_add_to_favorites, this)

        addToFavoriteImageView = findViewById(R.id.details_movie_favorite)
        addToFavoriteImageView.setOnClickListener {
            addToFavoriteListener?.let{
                it()
            }
        }
    }

    private var addToFavoriteListener: (() -> Unit)? = null

    fun setListener(listener: () -> Unit){
        addToFavoriteListener = listener
    }

    var iconResource: Int = 0
    set(value){
        field = value
        addToFavoriteImageView.setImageResource(field)
    }
}