package com.liujunjie.appdesktopnewui.popwindow.paint

import android.graphics.Color
import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.databinding.PaintColorPickerLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.BasePopupWindow
import com.liujunjie.appdesktopnewui.util.DisplayUtil
import com.liujunjie.appdesktopnewui.util.isValidColor
import com.liujunjie.appdesktopnewui.util.preMeasure
interface ColorSettingEvent{

}

class ColorSettingPopWindow(
    val content: View,
    val onCancel: () -> Unit
) : BasePopupWindow<ColorConfig>(content, onCancel) {
    private var colorConfig = ColorConfig()
    private val binding = PaintColorPickerLayoutBinding.inflate(LayoutInflater.from(content.context))
    var colorPickerChanged: ((colorConfig: ColorConfig) -> Unit)? = null


    init {
        binding.root.preMeasure()
        contentView = binding.root
        val rootWidth = 150
        val rootHeight = binding.root.measuredHeight
        height = rootHeight
        width = rootWidth
        isFocusable = true
        isOutsideTouchable = true
        colorFormatSpinnerSetting()
        colorConfigValueUi(colorConfig.color, true)
        applyColorInInputHide()
        initView()
    }

    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }

    private var isFirstShow = true

    override fun showAtLocation(anchor: Rect) {
        val popupWidth = DisplayUtil.dp2px(content.context, 150)
        if (isFirstShow) {
            val offscreenX = -10000
            val offscreenY = -10000
            showAtLocation(content, Gravity.NO_GRAVITY, offscreenX, offscreenY)

            contentView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    val popupHeight = contentView.height
                    val x = anchor.right
                    val y = anchor.top

                    update(x, y, popupWidth, popupHeight)
                    isFirstShow = false
                }
            })

        } else {
            contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val popupHeight = contentView.measuredHeight
            val x = anchor.right
            val y = anchor.top

            showAtLocation(content, Gravity.NO_GRAVITY, x, y)
        }
    }



    override fun onStateChanged(state: ColorConfig) {
        colorConfig = state
    }


    private fun initView() {
        binding.colorPicker.setColorSelect { color, fromUser ->
            if (fromUser) {
                colorConfigValueUi(color, false)
            }
            colorConfig.color = color
            colorPickerChanged?.invoke(colorConfig)
        }
    }

    private fun applyColorInInputHide() {
        binding.hexFormat.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateColorConfig(ColorFormat.HEX)
                v.clearFocus()
            }
            false
        }
        binding.rgbR.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateColorConfig(ColorFormat.RGB)
                v.clearFocus()
            }
            false
        }
        binding.rgbG.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateColorConfig(ColorFormat.RGB)
                v.clearFocus()
            }
            false
        }
        binding.rgbB.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateColorConfig(ColorFormat.RGB)
                v.clearFocus()
            }
            false
        }
    }

    private fun colorConfigValueUi(color: Int, resetColorPicker: Boolean) {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        binding.rgbR.setText(r.toString())
        binding.rgbG.setText(g.toString())
        binding.rgbB.setText(b.toString())
        val hexString = color.toString(16)
        val hexFormat = if (hexString.isValidColor()) hexString.substring(2) else "FFFFFF"
        binding.hexFormat.setText(hexFormat)
        binding.invalidColor.isInvisible = true
        if (resetColorPicker) {
            binding.colorPicker.resetColor(color)
        }
    }


    private fun updateColorConfig(format: ColorFormat) {
        when (format) {
            ColorFormat.RGB -> {
                val rString = binding.rgbR.text.toString()
                val gString = binding.rgbG.text.toString()
                val bString = binding.rgbB.text.toString()
                val rInt = if (rString.trim().isEmpty()) {
                    binding.rgbR.setText("0")
                    0
                } else rString.toInt()
                val gInt = if (gString.trim().isEmpty()) {
                    binding.rgbG.setText("0")
                    0
                } else gString.toInt()
                val bInt = if (bString.trim().isEmpty()) {
                    binding.rgbB.setText("0")
                    0
                } else bString.toInt()
                val validColor = rInt.isValidColor() && gInt.isValidColor() && bInt.isValidColor()
                binding.invalidColor.isInvisible = validColor
                if (validColor) {
                    val rgb = Color.rgb(rInt, gInt, bInt)
                    binding.colorPicker.resetColor(rgb)
                    binding.hexFormat.setText(rgb.toString(16).substring(2))
                }
            }

            ColorFormat.HEX -> {
                val hexColorString = binding.hexFormat.text.toString()
                val validColor = hexColorString.isValidColor()
                binding.invalidColor.isInvisible = validColor
                if (validColor) {
                    val color = Color.parseColor("#$hexColorString")
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)
                    binding.colorPicker.resetColor(color)
                    binding.rgbR.setText(r.toString())
                    binding.rgbG.setText(g.toString())
                    binding.rgbB.setText(b.toString())
                }
            }
        }
    }

    private fun colorFormatSpinnerSetting() {
        val selections = arrayListOf(
            SelectionPopup.Selection(ColorFormat.HEX.name, ColorFormat.HEX),
            SelectionPopup.Selection(ColorFormat.RGB.name, ColorFormat.RGB)
        )
        var usedSelection = selections[0]
        binding.colorFormat.setOnClickListener {
            SelectionPopup(binding.root.context, it, selections, object : SelectionPopup.OnItemSelection {
                override fun onSelected(selection: SelectionPopup.Selection) {
                    usedSelection = selection.copy()
                    binding.colorFormat.text = selection.name
                    when (selection.value as ColorFormat) {
                        ColorFormat.RGB -> {
                            binding.rgbGroup.isVisible = true
                            binding.hexFormat.isVisible = false
                            binding.hexHash.isVisible = false
                        }

                        ColorFormat.HEX -> {
                            binding.rgbGroup.isVisible = false
                            binding.hexFormat.isVisible = true
                            binding.hexHash.isVisible = true
                        }
                    }
                }
            }, usedSelection.name).show()
        }
        binding.colorFormat.text = usedSelection.name
    }

    enum class ColorFormat {
        HEX, RGB
    }
}