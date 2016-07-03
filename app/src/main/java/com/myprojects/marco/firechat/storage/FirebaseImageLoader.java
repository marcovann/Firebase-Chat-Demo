package com.myprojects.marco.firechat.storage;

/**
 * Created by marco on 01/09/16.
 */

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

/**
 * Created by marco on 21/07/16.
 */

public class FirebaseImageLoader implements StreamModelLoader<StorageReference> {

    @Override
    public DataFetcher<InputStream> getResourceFetcher(StorageReference model, int width, int height) {
        return new FirebaseStorageFetcher(model);
    }

    private class FirebaseStorageFetcher implements DataFetcher<InputStream> {

        private StorageReference mRef;

        FirebaseStorageFetcher(StorageReference ref) {
            mRef = ref;
        }

        @Override
        public InputStream loadData(Priority priority) throws Exception {
            return Tasks.await(mRef.getStream()).getStream();
        }

        @Override
        public void cleanup() {
            // No cleanup possible, Task does not expose cancellation
        }

        @Override
        public String getId() {
            return mRef.getPath();
        }

        @Override
        public void cancel() {
            // No cancellation possible, Task does not expose cancellation
        }
    }
}