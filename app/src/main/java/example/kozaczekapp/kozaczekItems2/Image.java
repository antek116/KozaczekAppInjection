package example.kozaczekapp.kozaczekItems2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Image class implementation of Image objects in Articles.
 */
public class Image implements Parcelable {

    String imageUrl;
    String imageSize;

    /**
     * @param imageUrl  url to image.
     * @param imageSize size of image.
     */
    public Image(String imageUrl, String imageSize) {
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }

    /**
     * Parcelable method.
     *
     * @param in Parcel
     */
    public Image(Parcel in) {
        imageUrl = in.readString();
        imageSize = in.readString();
    }

    /**
     * Class creator of Parcelable.
     */
    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    /**
     * @return image url as a String
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's marshalled representation.
     *
     * @return 0 ?
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  dest The Parcel in which the object should be written
     * @param flags flags Additional flags about how the object should be written. May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(imageSize);
    }
}
