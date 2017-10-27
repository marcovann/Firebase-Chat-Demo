package com.myprojects.marco.firechat.navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.user.data_model.User;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by marco on 09/09/16.
 */

public class AndroidProfileNavigator implements ProfileNavigator {

    private static final int SELECT_PHOTO = 1;

    private final AppCompatActivity activity;
    private final MaterialDialog.Builder progressDialogBuilder;
    private MaterialDialog progressDialog;

    private ProfileDialogListener dialogListener;

    public AndroidProfileNavigator(AppCompatActivity activity) {
        this.activity = activity;

        this.progressDialogBuilder = new MaterialDialog.Builder(activity)
                .title(R.string.profile_dialog_upload_title)
                .content(R.string.profile_dialog_upload_message)
                .progress(true, 0);
    }

    @Override
    public void showInputTextDialog(String hint, final TextView textView, final User user) {
        final MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.profile_dialog_name_title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(activity.getString(R.string.profile_hint_name), textView.getText().toString(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .negativeText(R.string.profile_dialog_input_close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String inputText = dialog.getInputEditText().getText().toString();
                        if (inputText.length() == 0) {
                            Toast.makeText(activity,"Name cannot be empty",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialogListener.onNameSelected(inputText,user);
                        textView.setText(inputText);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showInputPasswordDialog(String hint, User user) {
        new MaterialDialog.Builder(activity)
                .title(R.string.profile_dialog_password_title)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .negativeText(R.string.profile_dialog_input_close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String password = dialog.getInputEditText().getText().toString();
                        if (password.length() < 8) {
                            Toast.makeText(activity,R.string.login_snackbar_password_short,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialogListener.onPasswordSelected(password);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void showRemoveDialog() {
        new MaterialDialog.Builder(activity)
                .content(R.string.profile_dialog_remove_content)
                .positiveText(R.string.profile_dialog_remove_positive)
                .negativeText(R.string.profile_dialog_remove_negative)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialogListener.onRemoveSelected();
                        Toast.makeText(activity,R.string.profile_toast_password_positive,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        activity.finish(); //TODO
                    }
                })
                .show();
    }

    @Override
    public void showProgressDialog() {
        this.progressDialog = this.progressDialogBuilder.show();
    }

    @Override
    public void dismissProgressDialog() {
        this.progressDialog.dismiss();
    }

    @Override
    public void attach(ProfileDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void detach(ProfileDialogListener dialogListener) {
        this.dialogListener = null;
    }


    @Override
    public void toLogin() {

    }

    @Override
    public void toMainActivity() {

    }

    @Override
    public void toParent() {
        activity.finish();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode != SELECT_PHOTO) {
            return false;
        }

        if (intent == null) {
            return false;
        }

        try {
            final Uri imageUri = intent.getData();
            final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            dialogListener.onImageSelected(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

}
