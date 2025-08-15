//package com.liujunjie.appdesktopnewui.file
//
//import android.app.Dialog
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.icu.text.CollationKey
//import android.icu.text.Collator
//import android.os.Bundle
//import android.text.SpannableStringBuilder
//import android.text.Spanned
//import android.text.style.ForegroundColorSpan
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.edit
//import androidx.core.graphics.drawable.toDrawable
//import androidx.core.view.isGone
//import androidx.core.view.isVisible
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelStoreOwner
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import java.io.File
//import java.util.Locale
//import kotlin.properties.Delegates
//
//class FileSelectDialog(context: Context, intents: List<FileSelectIntent>, private val category: FileCategory) :BaseDialog<> {
//
//    constructor(context: Context, intent: FileSelectIntent, category: FileCategory = FileCategory.PlayScript) : this(context, listOf(intent), category)
//
//    private var binding: FileSelectorLayoutBinding by Delegates.notNull()
//
//    private val fileIntents = intents
//    private val supportedFormats: List<String> = intents.flatMap { it.supportedFormats.toList() }.toSet().toList()
//
//    private val whiteboardViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(WhiteboardViewModel::class.java)
//    private val fileViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(FileViewModel::class.java)
//
//    private val fileCategoryPath = FileCategoryDefaultPath(context, category)
//
//    private val fileNameCollator = Collator.getInstance(Locale.CHINA)
//
//    private val devicesAdapter: UsbDevicesAdapter = UsbDevicesAdapter(object : UsbDevicesAdapter.UsbDeviceOperation {
//        override fun onUnmount(device: MntDevice) {
//            unmountDevice(device)
//        }
//
//        override fun onOpen(device: MntDevice) {
//            if (binding.fileList.convenientOrganization.isChecked) {
//                fileViewModel.openFolder(FileRoot.ExternalFile(device, supportedFormats))
//            } else {
//                fileViewModel.openFolder(FileRoot.ExternalDir(device))
//            }
//        }
//    })
//
//    private val deviceObserver: Observer<List<MntDevice>> = Observer { devices ->
//        if (devices != null) {
//            binding.fileHome.unmountTips.isGone = devices.isEmpty()
//            devicesAdapter.updateData(devices)
//        }
//    }
//
//    private val pathAdapter: MaterialPathAdapter = MaterialPathAdapter {
//        fileViewModel.openFolder(it)
//    }
//
//    private val pathObserver: Observer<FilePath?> = Observer { path ->
//        if (path != null) {
//            pathAdapter.updatePath(path)
//            binding.fileList.path.scrollToPosition(path.childPaths.size)
//            binding.fileList.emptyToBack.isVisible = path.childPaths.isNotEmpty()
//        }
//        val root = path?.root
//        binding.fileList.root.isVisible = root != null
//        binding.fileList.convenientGroup.isVisible = root is FileRoot.External
//        if (root is FileRoot.External) {
//            binding.fileList.convenientOrganization.isChecked = root is FileRoot.ExternalFile
//        }
//        fileCategoryPath.setPath(path)
//    }
//
//    private val fileListAdapter: FileListAdapter = FileListAdapter {
//        if (it.isDirectory) {
//            fileViewModel.openFolder(it.name)
//        } else {
//            openFile(it.absolutePath, onComplete = {})
//        }
//    }
//
//    private val fileListObserver: Observer<FileList> = Observer { fileList ->
//        binding.fileList.refresh.isRefreshing = fileList is FileList.Loading
//        fileList.listFiles().forEach {
//            Debuger.printfLog("FileScanResult", it.absolutePath)
//        }
//        val files = fileList.listFiles().filter {
//            it.isDirectory || supportedFormats.contains(it.extension.lowercase())
//        }.map {
//            it to fileNameCollator.getCollationKey(it.name)
//        }.sortedWith(
//            compareBy<Pair<File, CollationKey>> { (file, _) -> !file.isDirectory }.thenBy { (_, key) -> key }
//        ).map { (file, _) ->
//            file
//        }
//        binding.fileList.emptyList.isVisible = files.isEmpty() && fileList is FileList.Complete
//        fileListAdapter.updateData(files)
//    }
//
//    private val fileListStyleObserver = Observer { style: FileListStyle ->
//        binding.fileList.arrangeStyle.isSelected = style == FileListStyle.LIST
//        binding.fileList.fileList.layoutManager = when (style) {
//            FileListStyle.GRID -> GridLayoutManager(context, 5)
//            FileListStyle.LIST -> LinearLayoutManager(context)
//        }
//        fileListAdapter.setStyle(style)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = FileSelectorLayoutBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setCanceledOnTouchOutside(true)
//        window?.apply {
//            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//            setDimAmount(0f)
//            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
//        }
//        binding.root.setOnClickListener { dismiss() }
//        initHomeLayout(binding.fileHome)
//        initFileListLayout(binding.fileList)
//        val path = fileCategoryPath.getDefaultPath()
//        if (path != null) {
//            if (path.root is FileRoot.External) {
//                val device = fileViewModel.findMountedDevice(path.root.device.uuid ?: "")
//                if (device != null) {
//                    when (path.root) {
//                        is FileRoot.ExternalFile -> {
//                            fileViewModel.openFolder(path.copy(root = FileRoot.ExternalFile(device, supportedFormats)))
//                        }
//
//                        is FileRoot.ExternalDir -> {
//                            fileViewModel.openFolder(path.copy(root = FileRoot.ExternalDir(device)))
//                        }
//                    }
//                } else {
//                    binding.fileList.convenientOrganization.isChecked = path.root is FileRoot.ExternalFile
//                    fileViewModel.closeFolder()
//                }
//            } else {
//                fileViewModel.openFolder(path)
//            }
//        } else {
//            fileViewModel.closeFolder()
//        }
//        developmentSetting()
//    }
//
//    private fun initHomeLayout(binding: FileHomeLayoutBinding) {
//        binding.root.setOnClickListener { }
//        binding.fastTransport.setOnClickListener {
//            val pkgName = ManageAppInfo.FILE_MANAGER_PLUS.packageName
//            val installed = isInstalled(pkgName, context)
//            if (installed) {
//                if (isVersionAvailable(context, pkgName, 6)) {
//                    val intent = Intent()
//                    intent.setAction("android.intent.action.synway.REQUEST_TRANSFER")
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    try {
//                        context.startActivity(intent)
//                    } catch (e: Exception) {
//                        WhiteboardToastUtil.show("快传启动失败")
//                    }
//                    dismiss()
//                } else {
//                    WhiteboardToastUtil.show("请将文件助手更新至最新版本")
//                }
//            } else {
//                WhiteboardToastUtil.show("请先安装文件助手")
//            }
//        }
//        binding.manageFiles.setOnClickListener {
//            val installed = isInstalled(context, ManageAppInfo.FILE_MANAGER_PLUS.packageName, 2)
//            if (installed) {
//                launchAPK3(context, ManageAppInfo.FILE_MANAGER_PLUS.packageName)
//            } else {
//                if (isInstalled(context, ManageAppInfo.FILE_MANAGER.packageName)) {
//                    launchAPK3(context, ManageAppInfo.FILE_MANAGER.packageName)
//                } else {
//                    WhiteboardToastUtil.show("请先安装文件助手")
//                }
//            }
//            dismiss()
//        }
//        ("选择" + category.categoryName).also { binding.title.text = it }
//        binding.localFile.icon.setImageResource(R.drawable.ic_folder)
//        binding.localFile.name.text = "本地文件"
//        binding.localFile.root.setOnClickListener {
//            fileViewModel.openFolder(FileRoot.LocalRoot)
//        }
//        binding.materialFile.icon.setImageResource(R.drawable.ic_material)
//        binding.materialFile.name.text = "素材包"
//        binding.materialFile.root.setOnClickListener {
//            if (category is FileCategory.Picture) {
//                val types = when (category) {
//                    FileCategory.ImageBackground -> listOf(Type.BACKGROUND)
//                    else -> listOf(Type.BORDER, Type.PATCH)
//                }
//                MaterialDownDialog(context, ::openFile, types).show()
//            }
//        }
//        binding.materialFile.root.isVisible = (category is FileCategory.Picture)
//        binding.baiduNetdisk.icon.setImageResource(R.drawable.ic_baidu_disk)
//        binding.baiduNetdisk.name.text = "百度网盘"
//        binding.baiduNetdisk.root.setOnClickListener {
//            BaiduDiskDialog(context, ::openFile).show()
//        }
//        binding.usbDevices.adapter = devicesAdapter
//        val tipsSpan = SpannableStringBuilder()
//        tipsSpan.append("当前支持打开的文件类型\n", ForegroundColorSpan(Color.BLACK), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        tipsSpan.append("${category.categoryName}: ", ForegroundColorSpan(Color.BLACK), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        tipsSpan.append(supportedFormats.joinToString(" | ") { it.uppercase() }, ForegroundColorSpan(0XFFA2A6A6.toInt()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.tips.text = tipsSpan
//    }
//
//    private fun initFileListLayout(binding: FileListLayoutBinding) {
//        binding.root.setOnClickListener { }
//        binding.backToHome.setOnClickListener {
//            fileViewModel.closeFolder()
//        }
//        binding.arrangeStyle.setOnClickListener {
//            if (it.isSelected) {
//                fileViewModel.setFileListStyle(FileListStyle.GRID)
//            } else {
//                fileViewModel.setFileListStyle(FileListStyle.LIST)
//            }
//        }
//        ("选择" + category.categoryName).also { binding.title.text = it }
//        binding.convenientOrganization.setOnCheckedChangeListener { _, isChecked ->
//            val root = fileViewModel.filePath.value?.root
//            var externalRoot: FileRoot.External? = (root as? FileRoot.External)
//            if (externalRoot != null) {
//                externalRoot = if (isChecked) {
//                    externalRoot.file(supportedFormats)
//                } else {
//                    externalRoot.dir()
//                }
//                fileViewModel.openFolder(externalRoot)
//            }
//        }
//        binding.refresh.setOnRefreshListener {
//            fileViewModel.refreshFileList()
//        }
//        binding.path.adapter = pathAdapter
//        binding.path.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        binding.fileList.adapter = fileListAdapter
//        binding.fileList.layoutManager = LinearLayoutManager(context)
//        "当前目录下没有${category.categoryName}".also { binding.emptyFolder.text = it }
//        binding.emptyToBack.setOnClickListener {
//            val path = fileViewModel.filePath.value
//            if (path != null && path.childPaths.isNotEmpty()) {
//                fileViewModel.openFolder(FilePath(path.root, path.childPaths.dropLast(1)))
//            }
//        }
//    }
//
//    /**
//     * 开发者演示模式后门
//     * */
//    private fun developmentSetting() {
//        val status = CameraOutputManager.isCameraDeveloperOutput()
//        binding.fileHome.displayModeText.visibility = if (status) View.VISIBLE else View.GONE
//        val combos = Combos()
//        binding.fileHome.tips.setOnClickListener {
//            combos.judgeCombosTimes(5) {
//                val sharedPreferences = context.getSharedPreferences(AppendSettingFragment.APPEND_SP, Context.MODE_PRIVATE).autoSync()
//                val enable = !sharedPreferences.getBoolean(AppendSettingFragment.DISPLAY_MODE, false)
//                sharedPreferences.edit { putBoolean(AppendSettingFragment.DISPLAY_MODE, enable) }
//                CameraOutputManager.setCameraDeveloperOutput(enable)
//                whiteboardViewModel.slaveManager.setPublishScreen(enable)
//                RecordFloatWindow.setVisibleInVirtualDisplay(enable)
//                Settings.setShowTouches(context, enable)
//                WhiteboardCommandUtil.executeForResult(KeyEnum.DISPLAY_MODE, enable)
//                val stringId = if (enable) R.string.access_display_mode else R.string.inaccessable_display_mode
//                WhiteboardToastUtil.show(AppContext.getContext().getString(stringId))
//                binding.fileHome.displayModeText.visibility = if (enable) View.VISIBLE else View.GONE
//            }
//        }
//    }
//
//    private val FileCategory.categoryName: String
//        get() = when (this) {
//            is FileCategory.Video -> "视频"
//            is FileCategory.Picture -> "图片"
//            is FileCategory.Document -> "文档"
//        }
//
//    private fun unmountDevice(mntDevice: MntDevice) {
//        toast("正在弹出…")
//        fileViewModel.unmountDevice(mntDevice)
//    }
//
//    private fun openFile(path: String, onComplete: Runnable) {
//        val responsibleIntents: MutableList<FileSelectIntent> = ArrayList()
//        for (intent in fileIntents) {
//            if (intent.isSupport(path)) {
//                if (intent.isUnique) {
//                    if (responsibleIntents.isEmpty()) {
//                        responsibleIntents.add(intent)
//                        break
//                    }
//                } else {
//                    responsibleIntents.add(intent)
//                }
//            }
//        }
//        if (responsibleIntents.isEmpty()) {
//            Log.e("FileSelectDialog", "responsibleIntents is empty for $path")
//            WhiteboardToastUtil.show("请选择${category.categoryName}文件")
//        } else if (responsibleIntents.size == 1) {
//            responsibleIntents[0].complete(path)
//            onComplete.run()
//            dismiss()
//        } else {
//            FileOpenSelectorDialog(context, responsibleIntents) { intent: FileSelectIntent ->
//                intent.complete(path)
//                onComplete.run()
//                dismiss()
//            }.show()
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        EventBus.getDefault().register(this)
//        fileViewModel.mntDevices.observeForever(deviceObserver)
//        fileViewModel.filePath.observeForever(pathObserver)
//        fileViewModel.fileList.observeForever(fileListObserver)
//        fileViewModel.fileListStyle.observeForever(fileListStyleObserver)
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//        EventBus.getDefault().unregister(this)
//        fileViewModel.mntDevices.removeObserver(deviceObserver)
//        fileViewModel.filePath.removeObserver(pathObserver)
//        fileViewModel.fileList.removeObserver(fileListObserver)
//        fileViewModel.fileListStyle.removeObserver(fileListStyleObserver)
//    }
//
//    override fun onEvent(event: String) {
//        if (event == Event.DISMISS) {
//            dismiss()
//        }
//    }
//
//}
