package com.example.fastbilling.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastbilling.R;
import com.example.fastbilling.model.ItemsModel;
import com.example.fastbilling.util.services;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BillingCheckoutActivity extends AppCompatActivity {

    private ArrayList<ItemsModel> dataHolder=new ArrayList<>();
    private ArrayList<String> title= new ArrayList<>();
    private ArrayList<String> price= new ArrayList<>();
    private static String TAG="BILLINGACTIVITY";
    private TextView gTotalPrice,totalAmount;
    private TableLayout tableLayout;
    private RelativeLayout createPDFBtn;
    private int grandtotal = 0;
    private String customerName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_checkout);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();

        gTotalPrice = findViewById(R.id.gTotalPrice);
        totalAmount = findViewById(R.id.totalAmount);
        tableLayout = findViewById(R.id.tableLayoutItems);
        createPDFBtn = findViewById(R.id.createPDFBtn);

         title = bundle.getStringArrayList("title_list");
         price = bundle.getStringArrayList("price_list");
        Log.d(TAG,"title List: "+title);
        Log.d(TAG,"price List: "+price);



        for (String strVal : price) {
            int intVal = Integer.parseInt(strVal);
            grandtotal += intVal;
        }
        totalAmount.setText(String.valueOf(grandtotal));
        gTotalPrice.setText(String.valueOf(grandtotal));
        System.out.println("Total Amount"+grandtotal);

        getTableData(title,price);

        createPDFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDailog();
            }
        });

    }

    private void getTableData(ArrayList<String> title,ArrayList<String> price)
    {
        for (int i=0;i<title.size();i++)
        {
            TableRow c1 = new TableRow(this);
            TextView ctitle = new TextView(this);
            TextView cprice = new TextView(this);

            ctitle.setText(title.get(i));
            ctitle.setTextColor(Color.BLACK);
            ctitle.setGravity(Gravity.CENTER);

            cprice.setText(price.get(i));
            cprice.setTextColor(Color.BLACK);
            cprice.setGravity(Gravity.CENTER);

            c1.addView(ctitle);
            c1.addView(cprice);
            tableLayout.addView(c1);
        }

    }


    public void createPDFItems() {
        Document document = new Document();

        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "MyPDFs");
            File destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            // Create the directory if it doesn't exist
            if (!dir.exists()) {
                dir.mkdir();
            }
            // Create a new file at the specified path
            //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());

            String dateTime = services.getCurrentDateTimePDF();
            File file = new File(destination, "fastbilling_"+dateTime+".pdf");

            // Create a new PDF writer with the file path
            PdfWriter.getInstance(document, new FileOutputStream(file));

            // Open the document
            document.open();

            // Add the invoice details
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph titlepdf = new Paragraph("Invoice", titleFont);
            titlepdf.setAlignment(Element.ALIGN_CENTER);
            document.add(titlepdf);

            if (customerName != "")
            {
                Paragraph customer = new Paragraph("Customer: "+customerName);
                customer.setAlignment(Element.ALIGN_LEFT);
                document.add(customer);
            }
            else
            {
                Paragraph customer = new Paragraph("Customer: NA");
                customer.setAlignment(Element.ALIGN_LEFT);
                document.add(customer);
            }

            Paragraph date = new Paragraph(services.getCurrentDateTime());
            date.setAlignment(Element.ALIGN_LEFT);
            document.add(date);

            // Add the product details
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell c1 = new PdfPCell(new Paragraph("Product Name"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);


            PdfPCell c3 = new PdfPCell(new Paragraph("Price ₹"));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c3);


            table.setHeaderRows(1);

            for (int i=0;i<title.size();i++)
            {
                table.addCell(title.get(i));
                table.addCell(price.get(i));
            }

            document.add(table);
            // Add the total
            Paragraph total = new Paragraph("Total "+grandtotal);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // Close the document
            document.close();

            // Show a message indicating that the PDF was successfully created
            Toast.makeText(this, "successful Please check PDF in Document folder !", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            // Show an error message if there was a problem creating the PDF
            Log.e(TAG,"Error creating PDF invoice: " + e.getMessage());
            Toast.makeText(this, "Error creating PDF invoice: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getDailog()
    {
        Dialog dialog=new Dialog(BillingCheckoutActivity.this);
        dialog.setContentView(R.layout.createpdf_dialog_img);
        dialog.setTitle("Fast Billing");
        dialog.show();

        EditText custmerN = dialog.findViewById(R.id.customerName);
        RelativeLayout createpdfbtn = dialog.findViewById(R.id.createPDFBtn);


        createpdfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName = custmerN.getText().toString();
                createPDFItems();
                dialog.dismiss();

            }
        });
    }
}