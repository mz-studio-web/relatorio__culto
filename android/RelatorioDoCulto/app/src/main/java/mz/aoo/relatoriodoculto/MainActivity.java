package mz.aoo.relatoriodoculto;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);          // permite o histórico local (localStorage)
        s.setAllowFileAccess(true);
        s.setDefaultTextEncodingName("utf-8");

        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new PdfBridge(), "AndroidPDF");

        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // Ponte usada pela app web para guardar o PDF em Transferências
    private class PdfBridge {
        @JavascriptInterface
        public void save(final String filename, final String base64) {
            runOnUiThread(new Runnable() {
                @Override public void run() {
                    try {
                        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
                        Uri uri = writePdf(filename, bytes);
                        Toast.makeText(MainActivity.this,
                                "PDF guardado em Transferências", Toast.LENGTH_LONG).show();
                        if (uri != null) openPdf(uri);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Erro ao guardar o PDF", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private Uri writePdf(String filename, byte[] bytes) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, filename);
            values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
            values.put(MediaStore.Downloads.IS_PENDING, 1);
            Uri item = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            if (item == null) return null;
            try (OutputStream os = resolver.openOutputStream(item)) {
                os.write(bytes);
            }
            values.clear();
            values.put(MediaStore.Downloads.IS_PENDING, 0);
            resolver.update(item, values, null, null);
            return item;
        } else {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!dir.exists()) dir.mkdirs();
            File f = new File(dir, filename);
            try (FileOutputStream fos = new FileOutputStream(f)) {
                fos.write(bytes);
            }
            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", f);
        }
    }

    private void openPdf(Uri uri) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(uri, "application/pdf");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (Exception ignored) {
        }
    }
}
