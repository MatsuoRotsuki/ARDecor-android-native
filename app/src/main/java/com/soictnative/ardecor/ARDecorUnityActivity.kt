package com.soictnative.ardecor

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.soictnative.ardecor.presentation.activities.SavePlacementActivity
import com.soictnative.ardecor.presentation.activities.ShoppingActivity
import com.soictnative.ardecor.presentation.adapters.FurnitureContextAdapter
import com.soictnative.ardecor.presentation.dialog.makeLongToast
import com.soictnative.ardecor.presentation.dialog.makeShortToast
import com.soictnative.ardecor.presentation.dialog.makeSnackbar
import com.soictnative.ardecor.presentation.dialog.setupExitDialog
import com.soictnative.ardecor.presentation.dialog.setupSavePlacementAsNewDialog
import com.soictnative.ardecor.presentation.fragments.ar.FurnitureInventoryFragment
import com.soictnative.ardecor.presentation.fragments.ar.ScreenshotDialog
import com.soictnative.ardecor.presentation.navigation.Route
import com.soictnative.ardecor.util.ScreenState
import com.soictnative.ardecor.presentation.viewmodel.ARViewModel
import com.unity3d.player.OverrideUnityActivity
import com.unity3d.player.UnityPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ARDecorUnityActivity: OverrideUnityActivity(){

    private val mainHandler = Handler(Looper.getMainLooper())

    private val arViewModel by viewModels<ARViewModel>()
    private lateinit var furnitureContextAdapter: FurnitureContextAdapter
    private lateinit var furnitureContextDialog: BottomSheetDialog

    private lateinit var settingsDialog: BottomSheetDialog

    private lateinit var screenShotDialog: DialogFragment

    private lateinit var furnitureInventoryFragment: DialogFragment

    private lateinit var userUid: String

    companion object {
        const val TAG = "ARDecorUnityActivity"
    }

    // Setup activity layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = intent
        handleIntent(intent)

        // Furniture Context Dialog
        furnitureContextDialog = BottomSheetDialog(this, R.style.DialogStyle)
        val view = layoutInflater.inflate(R.layout.furniture_context_dialog, null)
        furnitureContextDialog.setContentView(view)
        furnitureContextDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val rvFurnitureContext = view.findViewById<RecyclerView>(R.id.rvFurnitureContext)
        val btnSaveFurnitureContext = view.findViewById<Button>(R.id.btnSaveFurnitureContext)
        val btnDeleteAllContext = view.findViewById<Button>(R.id.btnDeleteAllContext)

        btnSaveFurnitureContext.setOnClickListener {
            UnityPlayer.UnitySendMessage("CommandInvoker", "OnClickSavePlacement", "A dummy text")
        }

        btnDeleteAllContext.setOnClickListener {
            UnityPlayer.UnitySendMessage("Managers", "OnClickDeleteAll", "A dummy text")
            furnitureContextDialog.dismiss()
        }

        furnitureContextAdapter = FurnitureContextAdapter()
        rvFurnitureContext.adapter = furnitureContextAdapter
        rvFurnitureContext.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                arViewModel.contextProducts.collectLatest {
                    furnitureContextAdapter.differ.submitList(it)
                }
            }
        }

        // Settings dialog
        settingsDialog = BottomSheetDialog(this, R.style.DialogStyle)
        val settingsView = layoutInflater.inflate(R.layout.settings_dialog, null)
        settingsDialog.setContentView(settingsView)
        settingsDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val switchGizmo = settingsView.findViewById<SwitchCompat>(R.id.switchGizmoSettings)
        val switchPlane = settingsView.findViewById<SwitchCompat>(R.id.switchPlaneSettings)
        val switchDimensions = settingsView.findViewById<SwitchCompat>(R.id.switchDimensionsSettings)
        val switchScaling = settingsView.findViewById<SwitchCompat>(R.id.switchScalingSettings)

        settingsDialog.lifecycleScope.launchWhenStarted {
            arViewModel.settings.collect {
                switchDimensions.isChecked = it.isDimensionsVisible
                switchGizmo.isChecked = it.isGizmoVisible
                switchPlane.isChecked = it.isPlaneVisible
                switchScaling.isChecked = it.isScalingEnabled
            }
        }

        switchGizmo.setOnCheckedChangeListener { _, isChecked ->
            arViewModel.gizmoSwitchChanged(isChecked)
        }

        switchPlane.setOnCheckedChangeListener { _, isChecked ->
            arViewModel.planeSwitchChanged(isChecked)
        }

        switchDimensions.setOnCheckedChangeListener { _, isChecked ->
            arViewModel.dimensionsSwitchChanged(isChecked)
        }

        switchScaling.setOnCheckedChangeListener { _, isChecked ->
            arViewModel.scalingSwitchChanged(isChecked)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.extras == null) return

        if (intent.extras!!.containsKey("doQuit")) {
            if (mUnityPlayer != null)
                finish()
        }

        if (intent.extras!!.containsKey("initializeProduct")) {
            val productJson = intent.extras!!.getString("initializeProduct")
            UnityPlayer.UnitySendMessage("CommandInvoker", "InitializeOnEvent", productJson)
        }

        if (intent.extras!!.containsKey("UserUid")) {
            userUid = intent.extras!!.getString("UserUid") ?: ""
            arViewModel.setUserId(userUid)
            UnityPlayer.UnitySendMessage("FirebaseManager", "SetUserUid", userUid)
        }

        if (intent.extras!!.containsKey("initializePlacement")) {
            val placementJson = intent.extras!!.getString("initializePlacement")
            UnityPlayer.UnitySendMessage("CommandInvoker", "OnClickLoadPlacement", placementJson)
        }
    }

    override fun onUnityPlayerUnloaded() {
        super.onUnityPlayerUnloaded()
    }

    override fun showShortToast(message: String?) {
        makeShortToast(message)
    }

    override fun showLongToast(message: String?) {
        makeLongToast(message)
    }

    override fun showSnackbar(message: String?) {
        makeSnackbar(message)
    }

    override fun showFurnitureInventory(message: String?) {
        try {
            mainHandler.post {
                furnitureInventoryFragment = FurnitureInventoryFragment()
                supportFragmentManager.beginTransaction()
                    .add(furnitureInventoryFragment, "furnitureInventoryFragment")
                    .commit()
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showFurnitureContext(json: String?) {
        try {
            mainHandler.post {
                arViewModel.setFurnitureContext(json)
                furnitureContextDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showSettings(json: String?) {
        mainHandler.post {
            arViewModel.setSettingsStateFromJson(json)
            settingsDialog.show()
        }
    }

    override fun returnToPrevious(message: String?) {
        mainHandler.post {
            //NOT TO DO
        }
    }

    override fun showScreenshot(firestoreRef: String?) {
        mainHandler.post {
            screenShotDialog = ScreenshotDialog.newInstance("${getExternalFilesDir(null)}${firestoreRef}")
            screenShotDialog.show(supportFragmentManager, "ScreenshotDialog")
        }
    }

    override fun showSavePlacementDialog(placementJson: String?) {
        mainHandler.post {
            furnitureContextDialog.dismiss()
            placementJson?.let {
                arViewModel.createdNewPlacement.observeForever {
                    when(it) {
                        is ScreenState.Error -> {
                            showShortToast(it.message)
                        }
                        is ScreenState.Loading -> {
                            showShortToast("Đang lưu bố trí sắp đặt...")
                        }
                        is ScreenState.Success -> {
                            showShortToast("Lưu thành công")
                        }
                        else -> Unit
                    }
                }
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Lựa chọn cách lưu")
                    .setItems(arrayOf(
                        "Lưu mới",
                        "Viết đè lên")
                    ) { _, which ->
                        when(which) {
                            0 -> {
                                setupSavePlacementAsNewDialog { placementName ->
                                    arViewModel.createNewSavePlacement(placementJson, placementName)
                                }
                            }
                            1 -> {
                                Intent(this, SavePlacementActivity::class.java).also { intent ->
                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                    intent.putExtra("destinationPage", Route.SavePlacementOverride.route)
                                    intent.putExtra("placementJson", placementJson)
                                    startActivity(intent)
                                }
                            }
                        }
                    }.setCancelable(true)
                    .create()

                dialog.show()
            }
        }
    }

    override fun showSavedPlacementList(message: String?) {
        mainHandler.post {
            Intent(this, SavePlacementActivity::class.java).also { intent ->
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                intent.putExtra("destinationPage", Route.SavedPlacementByUser.route)
                startActivity(intent)
            }
        }
    }

    override fun quitUnity(message: String?) {
        try {
            mainHandler.post {
                setupExitDialog {
                    Intent(this, ShoppingActivity::class.java).also { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}