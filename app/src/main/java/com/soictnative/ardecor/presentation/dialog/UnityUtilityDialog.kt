package com.soictnative.ardecor.presentation.dialog

import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.soictnative.ardecor.R
import com.unity3d.player.OverrideUnityAppCompatActivity

fun OverrideUnityAppCompatActivity.setupExitDialog(
    onOk: () -> Unit
) {
    AlertDialog.Builder(this).also {
        title = "Thoát khỏi chế độ xem AR"
        it.setMessage("Bạn có chắc chắn rời khỏi chế độ xem AR không?")
        it.setPositiveButton("OK") { dialog, which ->
            onOk()
            dialog.dismiss()
        }
        it.setNegativeButton("Huỷ bỏ") { dialog, which ->
            dialog.dismiss()
        }

        it.show()
    }
}

fun OverrideUnityAppCompatActivity.makeShortToast(message: String?) {
    Toast.makeText(this, message!!.toString(), Toast.LENGTH_SHORT).show()
}

fun OverrideUnityAppCompatActivity.makeLongToast(message: String?) {
    Toast.makeText(this, message!!.toString(), Toast.LENGTH_LONG).show()
}

fun OverrideUnityAppCompatActivity.makeSnackbar(message: String?) {
    if (message != null) {
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_SHORT).show()
    }
}

fun OverrideUnityAppCompatActivity.setupSavePlacementAsNewDialog(
    onSubmit: (String) -> Unit,
) {
    val dialog = BottomSheetDialog(this, R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.save_as_new_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edPlacementName = view.findViewById<EditText>(R.id.edPlacementName)
    val buttonOkSavePlacement = view.findViewById<AppCompatButton>(R.id.buttonOkSavePlacement)
    val buttonCancelSavePlacement = view.findViewById<AppCompatButton>(R.id.buttonCancelSavePlacement)

    buttonOkSavePlacement.setOnClickListener {
        val placementName = edPlacementName.text.toString().trim()
        onSubmit(placementName)
        dialog.dismiss()
    }

    buttonCancelSavePlacement.setOnClickListener {
        dialog.dismiss()
    }
}