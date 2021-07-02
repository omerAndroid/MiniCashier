package com.aoa.mini_cashier.Invoice;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.MainActivity;
import com.aoa.mini_cashier.item_classes.Settings_item;
import com.aoa.mini_cashier.update_goods_db;
import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.aoa.mini_cashier.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class createpdf_2 {
    Context mContext;
    public String PATH;
    public Databases databases ;

    Font font = FontFactory.getFont("assets/Aljazeera.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font f = FontFactory.getFont("assets/Aljazeera.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    public static String[] k;
    String oooo;

    public void create(String money_or_debt, String c_name, Databases db, Context context, String what,ArrayList<String>id_bills){

        mContext=context;
        databases=db;
        String ahent_name;
        int id_agent=databases.get_id_agent(c_name);
        if (!money_or_debt.equals("آجل")){
            ahent_name="***";
        }else ahent_name=c_name;

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator +"Mini Cashier Invoice");
        Calendar calendar1=Calendar.getInstance(Locale.getDefault());
        calendar1.setTimeInMillis(Long.parseLong(""+System.currentTimeMillis()));
        String timeAdded= ""+DateFormat.format("dd-MM-yyy hh:mm:ss",calendar1)+"";
        boolean success1 = true;
        if (!folder.exists())
            success1 = folder.mkdirs();
        if (success1) {
            oooo= money_or_debt+ " " +timeAdded+".pdf";
            //oooo = money_or_debt+".pdf";
            PATH = Environment.getExternalStorageDirectory() + "/Mini Cashier Invoice/"+oooo;
            File file = new File(PATH);
//            if (file.exists()) {
//                file.delete();
//            }

            boolean success = false;
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!success) {
                //Log.d(TAG, "创建文件" + path + "失败");
                return;
            }
        }


        try {

            Document document = new Document(PageSize.A4, 0, 0, 50, 0);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PATH));
            document.open();



//            f.setSize(15);
//
//
//            PdfPCell  cell = new PdfPCell();
//            Phrase p;
//            int g;
//            Paragraph p8;
//
//            PdfPTable table = new PdfPTable(2);
//            table.setWidthPercentage(100);
//            table.setWidthPercentage(90);
//            table.setSpacingBefore(50f);
//            table.setSpacingAfter(0f);
//            table.setPaddingTop(300f);
//
//            font.setStyle("bold");
//            font.setSize(12);
//
//            //b     الصورة
//
//            if ((databases.read_save_Market())>=1) {
//                Bitmap[] all_Bitmap = databases.get_ALLq_store_Blob_Market();
//
//                // @SuppressLint("UseCompatLoadingForDrawables") Drawable d = getResources().getDrawable(R.drawable.follow);
//                //BitmapDrawable bitDw = ((BitmapDrawable) d);
//                Bitmap bmp = all_Bitmap[0];
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                Image image = null;
//                try {
//                    image = Image.getInstance(stream.toByteArray());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                //document.add(image);
//                //PdfPCell cell7 = new PdfPCell(image,true);
//
//                cell.setFixedHeight(100);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//                ImageEvent imgEvent = new ImageEvent(image);
//                cell.setCellEvent(imgEvent);
//            }else {
//                @SuppressLint("UseCompatLoadingForDrawables") Drawable d = mContext.getResources().getDrawable(R.drawable.img);
//                BitmapDrawable bitDw = ((BitmapDrawable) d);
//                Bitmap bmp = bitDw.getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                Image image = null;
//                try {
//                    image = Image.getInstance(stream.toByteArray());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                //document.add(image);
//                //PdfPCell cell7 = new PdfPCell(image,true);
//
//                cell.setFixedHeight(100);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//                ImageEvent imgEvent = new ImageEvent(image);
//                cell.setCellEvent(imgEvent);
//            }
//
//
//
//            cell.setColspan(2);
//            table.addCell(cell);
//
//            cell = new PdfPCell();
//            cell.setPaddingTop(-100f);
//            // String[] k={"الاسم","Name","العنوان","Addres","رقم التلفون"};
//
//            if ((databases.read_save_Market())>=1) {
//                String[] all_text=databases.get_ALL_store_save_Market();
//                Bitmap[] all_Bitmap=databases.get_ALLq_store_Blob_Market();
//
//                //objectImageView.setImageBitmap(all_Bitmap[0]);
//                String copy = "";
//                if (databases.read_store_number()>0){
//
//                    String[] Settings = databases.get_ALLq_store_number();
//
//                    for (int j=0;j<databases.read_store_number();j++){
//                        copy += "هاتف : "+Settings[j];
//                        copy +="\n";
//                    }
//                }
//
//                k= new String[]{all_text[0], "Name", "العنوان : "+all_text[1], "Addres", copy};
//
//            }else{  k= new String[]{"الاسم", "Name", "العنوان", "Addres", "رقم التلفون"}; }
//
//            g=1;
//
//            for (int i=0;i<= k.length-1;i++) {
//                String ff=k[i];
//                if (g==1){
//                    p = new Phrase(ff, font);
//                    cell.addElement(p);
//                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//
//                    g=0;
//                }else {
//                    p8 = new Paragraph(ff,font);
//                    p8.setAlignment(Element.ALIGN_RIGHT);
//                    cell.addElement(p8);
//                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//                    g=1;
//                }
//
//                cell.setPaddingBottom(-18f);/// cell.setPaddingBottom(8f);
//
//            }
//
//            cell.setPaddingBottom(20f);
//            cell.setColspan(2);
//            table.addCell(cell);
//
//            Calendar calendar=Calendar.getInstance(Locale.getDefault());
//            calendar1.setTimeInMillis(Long.parseLong(""+System.currentTimeMillis()));
//            String timeAdded1="التاريخ : "+ DateFormat.format("dd/MM/yyyy",calendar)+"";
//
//            cell = new PdfPCell();
//
//            p8 = new Paragraph("بيع "+money_or_debt, font);
//            p8.setAlignment(Element.ALIGN_CENTER);
//            cell.addElement(p8);
//            cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//            cell.setColspan(2);
//            cell.setPaddingBottom(8f);
//            cell.setPaddingTop(-6f);
//            table.addCell(cell);
//
//            g=1;
//            k= new String[]{timeAdded1, "رقم الفاتوره: ***", "العميل : "+ahent_name, "النوع : "+money_or_debt};
//            for (int i=0;i<= k.length-1;i++) {
//
//                String ff=k[i];
//                cell = new PdfPCell();
//                if (g==1){
//                    p = new Phrase(ff, font);
//                    cell.addElement(p);
//                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//                    cell.setPaddingBottom(8f);
//                    cell.setPaddingTop(-6f);
//                    table.addCell(cell);
//                    g=0;
//                }else {
//                    p8 = new Paragraph(ff,font);
//                    p8.setAlignment(Element.ALIGN_LEFT);
//                    cell.addElement(p8);
//                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//                    cell.setPaddingBottom(8f);
//                    cell.setPaddingTop(-6f);
//                    table.addCell(cell);
//                    g=1;
//                }
//            }


            ///#####################################(new)####################################
            int row=0;
            double[] All_bills_Double=databases.get_All_bills_double(String.valueOf(id_agent));
            for (int j=0;j<databases.read_The_bills_2(id_agent);j++){

                f.setSize(15);


                PdfPCell  cell = new PdfPCell();
                Phrase p;
                int g;
                Paragraph p8;

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setWidthPercentage(90);
                table.setSpacingBefore(50f);
                table.setSpacingAfter(0f);
                table.setPaddingTop(300f);

                font.setStyle("bold");
                font.setSize(12);

                //b     الصورة

                if ((databases.read_save_Market())>=1) {
                    Bitmap[] all_Bitmap = databases.get_ALLq_store_Blob_Market();

                    // @SuppressLint("UseCompatLoadingForDrawables") Drawable d = getResources().getDrawable(R.drawable.follow);
                    //BitmapDrawable bitDw = ((BitmapDrawable) d);
                    Bitmap bmp = all_Bitmap[0];
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = null;
                    try {
                        image = Image.getInstance(stream.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //document.add(image);
                    //PdfPCell cell7 = new PdfPCell(image,true);

                    cell.setFixedHeight(100);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                    ImageEvent imgEvent = new ImageEvent(image);
                    cell.setCellEvent(imgEvent);
                }else {
                    @SuppressLint("UseCompatLoadingForDrawables") Drawable d = mContext.getResources().getDrawable(R.drawable.img);
                    BitmapDrawable bitDw = ((BitmapDrawable) d);
                    Bitmap bmp = bitDw.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = null;
                    try {
                        image = Image.getInstance(stream.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //document.add(image);
                    //PdfPCell cell7 = new PdfPCell(image,true);

                    cell.setFixedHeight(100);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                    ImageEvent imgEvent = new ImageEvent(image);
                    cell.setCellEvent(imgEvent);
                }



                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setPaddingTop(-100f);
                // String[] k={"الاسم","Name","العنوان","Addres","رقم التلفون"};

                if ((databases.read_save_Market())>=1) {
                    String[] all_text=databases.get_ALL_store_save_Market();
                    Bitmap[] all_Bitmap=databases.get_ALLq_store_Blob_Market();

                    //objectImageView.setImageBitmap(all_Bitmap[0]);
                    String copy = "";
                    if (databases.read_store_number()>0){

                        String[] Settings = databases.get_ALLq_store_number();

                        for (int jj=0;jj<databases.read_store_number();jj++){
                            copy += "هاتف : "+Settings[jj];
                            copy +="\n";
                        }
                    }

                    k= new String[]{all_text[0], "Name", "العنوان : "+all_text[1], "Addres", copy};

                }else{  k= new String[]{"الاسم", "Name", "العنوان", "Addres", "رقم التلفون"}; }

                g=1;

                for (int i=0;i<= k.length-1;i++) {
                    String ff=k[i];
                    if (g==1){
                        p = new Phrase(ff, font);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                        g=0;
                    }else {
                        p8 = new Paragraph(ff,font);
                        p8.setAlignment(Element.ALIGN_RIGHT);
                        cell.addElement(p8);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        g=1;
                    }

                    cell.setPaddingBottom(-18f);/// cell.setPaddingBottom(8f);

                }

                cell.setPaddingBottom(20f);
                cell.setColspan(2);
                table.addCell(cell);

                Calendar calendar=Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(""+System.currentTimeMillis()));
                String timeAdded1="التاريخ : "+ DateFormat.format("dd/MM/yyyy",calendar)+"";

                cell = new PdfPCell();

                p8 = new Paragraph("بيع "+money_or_debt, font);
                p8.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p8);
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                cell.setColspan(2);
                cell.setPaddingBottom(8f);
                cell.setPaddingTop(-6f);
                table.addCell(cell);

                g=1;
                k= new String[]{timeAdded1, "رقم الفاتوره: " +id_bills.get(j), "العميل : "+ahent_name, "النوع : "+money_or_debt};
                for (int i=0;i<= k.length-1;i++) {

                    String ff=k[i];
                    cell = new PdfPCell();
                    if (g==1){
                        p = new Phrase(ff, font);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-6f);
                        table.addCell(cell);
                        g=0;
                    }else {
                        p8 = new Paragraph(ff,font);
                        p8.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(p8);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-6f);
                        table.addCell(cell);
                        g=1;
                    }
                }

                document.add(table);

                PdfPTable table2 = new PdfPTable(5);

                table2.setWidthPercentage(90);
                table2.setSpacingBefore(50f);
                table2.setSpacingAfter(0f);
                table2.setPaddingTop(300f);
                table2.setWidths(new int[]{2, 2, 1, 1, 3});


                String[] a={"الاجمالي","السعر","الكمية","الوحدة","أسم الصنف"};
                for (int i=0;i<=a.length-1;i++ ){
                    String ff=a[i];
                    p8 = new Paragraph(ff,f);
                    cell = new PdfPCell();
                    p8.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(p8);
                    cell.setPaddingRight(50f);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    if(!ff.equals("أسم الصنف"))
                    {
                        cell.setPaddingRight(5f);
                    }
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    //cell.setPadding(5.0f);
                    //cell.setPaddingRight(280f);
                    cell.setPaddingBottom(20f);
                    cell.setBackgroundColor(new BaseColor(111, 111, 111));
                    table2.addCell(cell);
                }

                String[] purchases=databases.get_one_products_bills(Integer.parseInt(id_bills.get(j)));
                double[] purchases_double=databases.get_one_products_bills_double(Integer.parseInt(id_bills.get(j)));
                int quantity_sum=0;
                int i = 0,y=0;

                for (int c=0;c<databases.get_one_products_bills_num(Integer.parseInt(id_bills.get(j)));c++) {

                    for (int q=0;q<1;q++){
                        cell = new PdfPCell();
                        p = new Phrase(theack_aggen(new DecimalFormat("#.00#").format(purchases_double[y]*Double.parseDouble(purchases[i+2]))), f);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-8f);
                        cell.setPaddingRight(30f);
                        table2.addCell(cell);
                    }
                    for (int w=0;w<1;w++){
                        cell = new PdfPCell();
                        p = new Phrase(theack_aggen(new DecimalFormat("#.00#").format(purchases_double[y])), f);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-8f);
                        cell.setPaddingRight(30f);
                        table2.addCell(cell);
                    }
                    for (int e=0;e<1;e++){
                        cell = new PdfPCell();
                        p = new Phrase(purchases[i+2], f);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-8f);
                        cell.setPaddingRight(30f);
                        table2.addCell(cell);
                        quantity_sum += Integer.parseInt(purchases[i+2]);
                    }
                    for (int t=0;t<1;t++){
                        cell = new PdfPCell();
                        p = new Phrase(purchases[i+3], f);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-8f);
                        cell.setPaddingRight(10f);
                        table2.addCell(cell);

                    }
                    for (int r=0;r<1;r++){
                        cell = new PdfPCell();
                        p = new Phrase(purchases[i], f);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-8f);
                        table2.addCell(cell);
                    }
                    i += 5;
                    y+=1;
                }

                ///الاجمالي
                g=1;

                for (int as=1;as<=3;as++) {
                    if (g==1){
                        cell = new PdfPCell();
                        p = new Phrase(theack_aggen(new DecimalFormat("#.00#").format(All_bills_Double[row]))+" ريال", f);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-8f);
                        cell.setPaddingRight(50f);
                        cell.setColspan(2);
                        table2.addCell(cell);
                        g++;
                    }else if (g==2){
                        cell = new PdfPCell();
                        p = new Phrase(String.valueOf(quantity_sum), f);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-8f);
                        cell.setPaddingRight(30f);
                        table2.addCell(cell);
                        g++;
                    }else if (g==3){
                        cell = new PdfPCell();
                        p = new Phrase("الاجمالي", f);
                        cell.addElement(p);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPaddingBottom(8f);
                        cell.setPaddingTop(-8f);
                        cell.setColspan(2);
                        table2.addCell(cell);
                        g++;
                    }
                }

                ///##############################  ARABASE

                ArabicTools arabicTools = new ArabicTools();
                arabicTools.isFeminine = true;
                String s = theack_aggen(new DecimalFormat("#.00#").format(All_bills_Double[row])) ,print;
                int size=s.length()-1;
                String[] parts = s.split("\\.");

                String part1 ;   //      :الجزء الأول سيحتوي على النص
                String part2 ;   //           :الجزء الثاني سيحتوي على النص

                if (s.contains(".")){

                    part1 = parts[0];
                    part2 = parts[1];
                    print =arabicTools.numberToArabicWords(part1)+
                            " ريال و "+  arabicTools.numberToArabicWords(part2)+" فلس ";

                }else {
                    print=arabicTools.numberToArabicWords(s)+"  ريال ";

                }

                cell = new PdfPCell();
                p = new Phrase(print, f);
                cell.addElement(p);
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                cell.setColspan(5);
                cell.setPaddingBottom(8f);
                cell.setPaddingTop(-6f);

                if (size<=2){
                    cell.setPaddingRight(270f);
                }else if (size<=3) {
                    cell.setPaddingRight(230f);
                }
                else if (size<=5) {
                    cell.setPaddingRight(180f);
                }
                else {
                    cell.setPaddingRight(10f);
                }

                table2.addCell(cell);
                document.add(table2);
                row +=2;
                document.newPage();


            }

            ///#####################################(3)######################################
            document.close();

            if (what.equals("1")){
                share();
            }else {open();}


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void share(){
        File file = new File(PATH);
        if (!file.exists()){
            Toast.makeText(mContext, "File doesn't exists", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("application/pdf");
        intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
        mContext.startActivity(Intent.createChooser(intentShare, "Share the file ..."));

    }

    public void open(){
        mContext.startActivity(new Intent(mContext, Openpdf.class).
                putExtra("key",PATH));

    }

    /////////////////n     خوارزمية تساعد لعملية عرض وادخال الارقام
    public String theack_aggen(@NonNull String s){
        StringBuilder ss= new StringBuilder();

        boolean b=false;
        for (int i = 0; i<= s.length()-1; i++){
            if (String.valueOf(s.charAt(i)).equals("٠")){
                ss.append("0");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٩")){
                ss.append("9");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("١")){
                ss.append("1");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٢")){
                ss.append("2");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٣")){
                ss.append("3");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٤")){
                ss.append("4");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٥")){
                ss.append("5");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٦")){///١٢٣٤٥٦٧٨٩٫٠٠٠
                ss.append("6");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٧")){
                ss.append("7");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٨")){
                ss.append("8");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٫")){
                ss.append(".");
                b=true;
            }
        }

        if (b){
            return ss.toString();
        }else {
            return s;
        }

    }
}
