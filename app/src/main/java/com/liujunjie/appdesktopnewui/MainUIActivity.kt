package com.liujunjie.appdesktopnewui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.liujunjie.appdesktopnewui.adapter.*
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.databinding.ActivityMainUiLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.PopupState
import com.liujunjie.appdesktopnewui.popwindow.paint.ColorSettingEvent
import com.liujunjie.appdesktopnewui.popwindow.paint.ColorSettingPopWindow
import com.liujunjie.appdesktopnewui.popwindow.paint.PaintSelectPopWindow
import com.liujunjie.appdesktopnewui.popwindow.paint.PaintSettingPopWindow
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem
import com.liujunjie.appdesktopnewui.uimodel.SideBarItems
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem
import com.liujunjie.appdesktopnewui.viewModel.SideBarViewModel
import com.liujunjie.appdesktopnewui.viewModel.paint.PaintViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainUIActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainUIActivity"
    }

    private val binding by lazy {
        ActivityMainUiLayoutBinding.inflate(layoutInflater)
    }

    private val sideBarViewModel by lazy {
        ViewModelProvider(this)[SideBarViewModel::class.java]

    }
    private val sideBarAdapter by lazy {
        SideBarAdapter(object : SideBarEvent {
            override fun onSideBarItemClick(item: SideBarItem) {
                sideBarViewModel.setSelectedItem(item)
            }
        })
    }

    private val paintViewModel by lazy {
        ViewModelProvider(this)[PaintViewModel::class.java]
    }

    private val paintSelectPopWindow by lazy {
        PaintSelectPopWindow(
            content = binding.root,
            cancel = {},
            paintSelectEvent = object : PaintSelectEvent {
                override fun onItemClickOnce(item: PaintItem) {
                    paintViewModel.selectPaint(item)
                }

                override fun eraserSetting(item: PaintItem, archView: View) {
                    setLocation(archView)
                    paintViewModel.setShapeList()
                }

                override fun smartLineSetting(item: PaintItem, archView: View) {
                    setLocation(archView)
                    paintViewModel.setShapeList()
                }

                override fun commonLineSetting(item: PaintItem, archView: View) {
                    setLocation(archView)
                    paintViewModel.setShapeList()
                }

                override fun setLocation(archView: View) {
                    val location = IntArray(2)
                    archView.getLocationOnScreen(location)
                    val left = location[0]
                    val top = location[1]
                    val right = left + archView.width
                    val bottom = top + archView.height
                    val rect = Rect(left, top, right, bottom)
                    paintViewModel.setSelectedRect(rect)
                    Log.d("MainUIActivity", "setLocation: $rect")
                }

            },
            paintOperateEvent = object : PaintOperateEvent {
                override fun clearUp() {
                    Log.d("MainUIActivity", "clearUp")
                }

                override fun revoke() {
                    Log.d("MainUIActivity", "revoke")
                }

                override fun restore() {
                    Log.d("MainUIActivity", "restore")
                }

                override fun exit() {
                    Log.d("MainUIActivity", "exit")
                }

                override fun retract() {
                    Log.d("MainUIActivity", "retract")
                }

            }
        )
    }

    private val paintSettingPop by lazy {
        PaintSettingPopWindow(
            content = binding.root,
            onCancel = { paintViewModel.clearShapeList() },
            paintClickEvent = object : PaintClickEvent {
                override fun colorSetting(item: PaintColor) {
                    paintViewModel.setColorToSetting(ColorConfig(item.color))
                }

                override fun setCurrentPosition(position: Rect) {
                    paintViewModel.setSelectedRect(position)
                }

                override fun colorClick(item: PaintColor) {
                    paintViewModel.setPaintUsingColor(item)
                }
            }
        )
    }

    private val colorSettingPop by lazy {
        ColorSettingPopWindow(
            content = binding.root,
            onCancel = {},
            colorSettingEvent = object : ColorSettingEvent {
                override fun setColor(color: Int) {
                    paintViewModel.setColor(ColorConfig(color))
                }
            })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.sidebarRecycleview.adapter = sideBarAdapter
        binding.sidebarRecycleview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lifecycleScope.launch {
            sideBarViewModel.sideBarItems.collectLatest {
                sideBarAdapter.submitList(it)
            }
        }
        lifecycleScope.launch {
            sideBarViewModel.selectedItem.collectLatest {
                if (it == SideBarItems.DrawPaintItem) paintViewModel.addPaints()
                else paintViewModel.clear()
            }
        }
        lifecycleScope.launch {
            paintSelectPopWindow.collectState(
                PopupState<List<PaintItem>>(
                    paintViewModel.paintList,
                    paintViewModel.selectedPaintRect
                )
            )
        }

        lifecycleScope.launch {
            paintSettingPop.collectState(
                PopupState<List<PaintEditItem>>(
                    paintViewModel.smartLineUiData,
                    paintViewModel.selectedPaintRect
                )
            )
        }

        lifecycleScope.launch {
            colorSettingPop.collectState(PopupState<ColorConfig>(paintViewModel.currentSettingColor, paintViewModel.selectedPaintRect))
        }

    }


}


fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}