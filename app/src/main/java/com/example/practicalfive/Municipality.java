package com.example.practicalfive;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Detail screen shown after the user enters a valid municipality name.
 * The name, summary, photo, colour scheme and website URL are all selected
 * programmatically from the validated name passed by MainActivity.
 *
 * @author Zhu Xiyuan (20243007056)
 */
public class Municipality extends AppCompatActivity {

    // All arrays below follow the same index order:
    // 0 = Beijing, 1 = Shanghai, 2 = Chongqing, 3 = Tianjin.
    private static final String[] NAMES = {
            "Beijing", "Shanghai", "Chongqing", "Tianjin"
    };

    private static final int[] COLOUR_RES = {
            R.color.municipality_beijing,
            R.color.municipality_shanghai,
            R.color.municipality_chongqing,
            R.color.municipality_tianjin
    };

    private static final int[] IMAGE_RES = {
            R.drawable.beijing,
            R.drawable.shanghai,
            R.drawable.chongqing,
            R.drawable.tianjin
    };

    /** Official English websites, aligned with NAMES. */
    private static final String[] WEBSITE = {
            "https://english.beijing.gov.cn/",
            "https://english.shanghai.gov.cn/",
            "https://english.cq.gov.cn/",
            "https://en.tj.gov.cn/"
    };

    private TextView textName;
    private TextView textInfo;
    private ImageView image;
    private Button buttonVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_municipality);

        // Keep the content inside the system bars.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textName = findViewById(R.id.textMunicipalityName);
        textInfo = findViewById(R.id.textMunicipalityInfo);
        image = findViewById(R.id.imageMunicipality);
        buttonVisit = findViewById(R.id.buttonVisitSite);

        // Read and re-validate the name forwarded by MainActivity.
        String name = TextProcessor.processMunicipality(
                getIntent().getStringExtra(MainActivity.EXTRA_MUNICIPALITY));
        if (name == null) {
            // No safe data to show: return to the main screen.
            finish();
            return;
        }

        int index = indexOf(name);
        customiseScreen(name, index);

        // Implicit intent: open the official website in a web browser.
        buttonVisit.setOnClickListener(v -> openWebsite(WEBSITE[index]));
    }

    /** Maps a validated name to its index in the parallel resource arrays. */
    private int indexOf(String name) {
        for (int i = 0; i < NAMES.length; i++) {
            if (NAMES[i].equals(name)) {
                return i;
            }
        }
        return 0;
    }

    /** Updates the name, summary, photo and colours for the chosen municipality. */
    private void customiseScreen(String name, int index) {
        int colour = getColor(COLOUR_RES[index]);

        textName.setText(name);
        textName.setBackgroundColor(colour);
        textName.setTextColor(getColor(R.color.color_text_light));

        textInfo.setText(getResources().getStringArray(R.array.municipalities_array)[index]);
        textInfo.setTextColor(colour);

        image.setImageResource(IMAGE_RES[index]);

        buttonVisit.setBackgroundColor(colour);
        buttonVisit.setTextColor(getColor(R.color.color_text_light));
    }

    /** Fires an ACTION_VIEW implicit intent and reports failures gracefully. */
    private void openWebsite(String url) {
        try {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browser);
        } catch (ActivityNotFoundException e) {
            // No browser installed: inform the user instead of crashing.
            Toast.makeText(this, R.string.error_no_browser, Toast.LENGTH_LONG).show();
        }
    }
}