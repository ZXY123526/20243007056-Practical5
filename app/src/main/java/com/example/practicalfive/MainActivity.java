package com.example.practicalfive;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Main screen of the Chinese Municipalities application.
 * The user types the name of one municipality; the input is validated and
 * normalised by {@link TextProcessor}, then a valid name is forwarded to the
 * {@link Municipality} activity through an explicit Intent.
 *
 * @author Zhu Xiyuan (20243007056)
 */
public class MainActivity extends AppCompatActivity {

    /** Intent extra key shared with the Municipality activity. */
    public static final String EXTRA_MUNICIPALITY = "municipality_name";

    private EditText editTextMunicipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Keep the content inside the system bars.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Locate the input field defined in activity_main.xml.
        editTextMunicipality = findViewById(R.id.editTextMunicipality);

        // Validate when the user presses Go / Done on the soft keyboard.
        editTextMunicipality.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                handleInput();
                return true;
            }
            return false;
        });
    }

    /**
     * Reads the input, normalises it with {@link TextProcessor} and either
     * shows an error on the field or opens the Municipality activity.
     */
    private void handleInput() {
        String raw = editTextMunicipality.getText().toString();
        String municipality = TextProcessor.processMunicipality(raw);

        if (municipality == null) {
            // Empty / whitespace-only input gets one message, unknown names another.
            if (raw.trim().isEmpty()) {
                editTextMunicipality.setError(getString(R.string.error_empty));
            } else {
                editTextMunicipality.setError(getString(R.string.error_unknown));
            }
            return;
        }

        // Valid input: pass the correctly capitalised name to the second screen.
        Intent intent = new Intent(this, Municipality.class);
        intent.putExtra(EXTRA_MUNICIPALITY, municipality);
        startActivity(intent);
    }
}