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
            cancel = { paintViewModel.clear() },
            paintSelectEvent = object : PaintSelectEvent {
                override fun setSelectedPaint(item: PaintItem) {
                    paintViewModel.setSelectedPaint(item)
                }

                override fun eraserSetting(item: PaintItem, archView: View) {
                    setLocation(archView)
                    // paintViewModel.setShapeList()
                }

                override fun smartLineSetting(item: PaintItem, archView: View) {
                    setLocation(archView)
                    paintViewModel.clear()
                    paintViewModel.initSmartLineUiData()
                }

                override fun commonLineSetting(item: PaintItem, archView: View) {
                    setLocation(archView)
                    //paintViewModel.setShapeList()
                }

                override fun setLocation(archView: View) {
                    val location = IntArray(2)
                    archView.getLocationOnScreen(location)
                    val left = location[0]
                    val top = location[1]
                    val right = left + archView.width
                    val bottom = top + archView.height
                    val rect = Rect(left, top, right, bottom)
                    paintViewModel.setRect(rect)
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
            onCancel = { },
            paintClickEvent = object : PaintClickEvent {
                override fun setSelectedShape(item: PaintShape) {
                    paintViewModel.setSelectedShape(item)
                }

                override fun setSelectedThick(item: PaintThick) {
                    paintViewModel.setSelectedThick(item)
                }

                override fun setSelectedColor(item: PaintColor) {
                    paintViewModel.setSelectedColor(item)
                }

                override fun setCurrentPosition(position: Rect) {
                    paintViewModel.setRect(position)
                }

                override fun setColorToEditPop(item: PaintColor) {
                    Log.d("MainUIActivity", "clearUp")
                }

                override fun addColorToEditPop() {
                    paintViewModel.addColor()
                }

                override fun complete() {
                    Log.d("MainUIActivity", "clearUp")
                }

                override fun changeColorToDel() {
                    Log.d("MainUIActivity", "clearUp")
                }

                override fun delColor(item: PaintColor) {
                    Log.d("MainUIActivity", "clearUp")
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
                if (it == SideBarItems.DrawPaintItem) paintViewModel.initPaints()
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

        }

    }


}


fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}