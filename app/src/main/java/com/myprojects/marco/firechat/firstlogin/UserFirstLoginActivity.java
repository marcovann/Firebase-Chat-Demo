package com.myprojects.marco.firechat.firstlogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myprojects.marco.firechat.Constants;
import com.myprojects.marco.firechat.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by marco on 13/07/16.
 */

public class UserFirstLoginActivity extends AppCompatActivity
    implements View.OnClickListener {

    private static final int SELECT_PHOTO = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private Toolbar toolbar;
    private EditText nameEditText;
    private CircleImageView profileImageView;

    private boolean hasImageChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlogin);

        // TODO to implement
        String image = getIntent().getStringExtra(Constants.FIREBASE_USERS_IMAGE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.firstlogin_title);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        if (firebaseUser.getDisplayName() != null && firebaseUser.getDisplayName().length() > 0)
            nameEditText.setText(firebaseUser.getDisplayName());
        profileImageView = (CircleImageView) findViewById(R.id.profileImageView);

        profileImageView.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_firstlogin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                if (nameEditText.getText() == null) {
                    Toast.makeText(this,"Name cannot be empty",Toast.LENGTH_SHORT).show();
                    return false;
                } else if (nameEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(this,"Name cannot be empty",Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (firebaseUser != null) {
                    final HashMap<String,String> values = new HashMap<>();
                    values.put(Constants.FIREBASE_USERS_EMAIL,firebaseUser.getEmail());
                    values.put(Constants.FIREBASE_USERS_NAME,nameEditText.getText().toString());
                    values.put(Constants.FIREBASE_USERS_UID,firebaseUser.getUid());

                    if (hasImageChanged) {
                        profileImageView.setDrawingCacheEnabled(true);
                        profileImageView.buildDrawingCache();
                        Bitmap bitmap = profileImageView.getDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        final String imageRef = profileImageView.getDrawingCache().hashCode() + System.currentTimeMillis() + ".jpg";
                        StorageReference mountainsRef = storageReference.child(imageRef);
                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                values.put(Constants.FIREBASE_USERS_IMAGE,imageRef);
                                databaseReference.child(Constants.FIREBASE_USERS)
                                        .child(firebaseUser.getUid()).setValue(values);
                                setToken();

                                finish();
                            }
                        });
                    } else {
                        databaseReference.child(Constants.FIREBASE_USERS)
                                .child(firebaseUser.getUid()).setValue(values);
                        setToken();

                        finish();
                    }
                }
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profileImageView.setImageBitmap(selectedImage);
                        hasImageChanged = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.profileImageView:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
        }
    }

    private void setToken() {
        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FCM);
        tokenRef.child(firebaseUser.getUid() + "/" + Constants.FIREBASE_FCM_TOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
        tokenRef.child(firebaseUser.getUid() + "/" + Constants.FIREBASE_FCM_ENABLED).setValue(Boolean.TRUE.toString());
    }

}
