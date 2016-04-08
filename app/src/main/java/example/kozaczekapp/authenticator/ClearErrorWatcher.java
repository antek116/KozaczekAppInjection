package example.kozaczekapp.authenticator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ClearErrorWatcher implements TextWatcher {

    private final EditText editText;

    public ClearErrorWatcher(EditText editText){
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
            editText.setError(null);
    }
}
