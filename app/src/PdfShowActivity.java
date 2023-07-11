package com.dhruvi.dhruvisonani.usersidexa2.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

public class PdfShowActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final String TAG = PdfShowActivity.class.getSimpleName();
    //    public static String SAMPLE_FILE = "";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    Uri pdf;

    public static int PG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_show);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        Intent i = getIntent();
        pdf = Uri.parse(i.getStringExtra("uri"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayFromAsset();
    }

    @Override
    public void onBackPressed() {
        setResult(12);
        finish();
    }

    private void displayFromAsset() {
        pdfView.fromUri(pdf).defaultPage(pageNumber).enableSwipe(true).swipeHorizontal(false).onPageChange(PdfShowActivity.this).enableAnnotationRendering(true).onLoad(this).scrollHandle(new DefaultScrollHandle(this)).load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        PG = pageCount;
        setTitle(String.format("%s / %s", page + 1, pageCount));

    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_close:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}


