package com.bunq.paymentapp.repository

import android.content.Context
import android.graphics.Outline
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.bunq.paymentapp.R
import kotlinx.android.synthetic.main.custom_search_view.view.*

/**Custom Search View Implemented to search the payment details of user
 **/

class CustomSearchView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(
        context
        , attrs, defStyleAttr
    ) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    val text: Editable
        get() = search_bar.text

    private var onButtonStateChange: ((Boolean, Boolean) -> Unit)? = { isBlank, focus ->
        if (!isBlank) isClearable = true
    }

    init {

        val Array = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomSearchView, 0, 0)
        val hint = Array.getDimension(R.styleable.CustomSearchView_hint, 0f)
        val elevation = Array.getDimension(R.styleable.CustomSearchView_elevation, 0f)
        Array.recycle()

        LayoutInflater.from(context).inflate(R.layout.custom_search_view, this, true).apply {
            this.elevation = elevation
            hint.let { search_bar.hint = it.toString() }

            iv_search.setOnTouchListener { _, event ->
                search_bar.onTouchEvent(event)
            }

            search_bar.setOnFocusChangeListener { view, focus ->
                val editText = (view as EditText)
                val isBlank = editText.text.isBlank()

                onButtonStateChange?.invoke(isBlank, focus)
                this.onFocusChangeListener?.onFocusChange(view, focus)
            }
        }

    }

    private var isClearable: Boolean
        get() = search_bar.text.trim().isNotEmpty()
        private set(value) {
            iv_search.isVisible = !value
        }

    public fun setOnQueryTextListener(onQueryTextListener: SearchView.OnQueryTextListener) {
        search_bar.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onQueryTextListener.onQueryTextSubmit((view as EditText).text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        search_bar.doAfterTextChanged {
            onButtonStateChange?.invoke(it!!.isBlank(), true)
            onQueryTextListener.onQueryTextChange(it.toString())
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        outlineProvider = CustomOutline(w, h)
    }

    private class CustomOutline internal constructor(var width: Int, var height: Int) :
        ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(
                0,
                0,
                width,
                height,
                view.resources.getDimension(R.dimen.default_corner_radius)
            )
        }

    }
}