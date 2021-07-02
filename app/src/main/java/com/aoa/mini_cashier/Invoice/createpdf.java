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
import android.view.View;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class createpdf {

    //private static final String PATH = Environment.getExternalStorageDirectory() + "/Mini Cashier Invoice/itexdemo.pdf";

    //private  String PATH = Environment.getExternalStorageDirectory()  + "Mini Cashier Invoice" + File.separator + "/itexdemo.pdf";
     Context mContext;
   public String PATH;
    public Databases databases ;

    Font font = FontFactory.getFont("assets/Aljazeera.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font f = FontFactory.getFont("assets/Aljazeera.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
   public static String[] k;
    String oooo;

    public void onbdf(ArrayList<String>name_prod,String total_price,ArrayList<String>selling_price,ArrayList<String>quantity,
                      ArrayList<String>quantity_type,ArrayList<String>total_price_ofOne,String money_or_debt,String c_name,Databases db,
                      Context context,String what) {
        mContext=context;
        databases=db;
        String ahent_name;
        int size_data = name_prod.size();
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

                    for (int j=0;j<databases.read_store_number();j++){
                        copy += "هاتف : "+Settings[j];
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
            k= new String[]{timeAdded1, "رقم الفاتوره: ***", "العميل : "+ahent_name, "النوع : "+money_or_debt};
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


            ///#####################################(1)######################################

            PdfPTable table2 = new PdfPTable(5);
            //table2.setHorizontalAlignment(Element.ALIGN_LEFT);///Table LEFT

//            table2.setTotalWidth(100);
//            table2.setLockedWidth(true);

            table2.setWidthPercentage(90);
            table2.setSpacingBefore(50f);
            table2.setSpacingAfter(0f);
            table2.setPaddingTop(300f);
            table2.setWidths(new int[]{2, 2, 1, 1, 3});


            String[] a={"الاجمالي","السعر","الكمية","الوحدة","أسم الصنف"};//{"أسم الصنف","الكمية","السعر","الاجمالي"};
            for (int i=0;i<=a.length-1;i++ ){
                String ff=a[i];
                //f.setColor(111, 111, 111);
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


//            int n=0;
//            String[] kk={"100","50","2","جبن","100","50","1","زبادي"};
//
//            for (int i=0;i<= kk.length-1;i++) {
//                String ff=kk[i];
//                if (n==3){
//                    cell = new PdfPCell();
//                    p = new Phrase(ff, f);
//                    cell.addElement(p);
//                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
////                     cell.setPadding(0.0f);
//                    cell.setPaddingBottom(8f);
//                    cell.setPaddingTop(-8f);
//                    //  cell.setPaddingRight(80f);
//                    table2.addCell(cell);
//                    n=0;
//                }else {
//                    cell = new PdfPCell();
//                    p = new Phrase(ff, f);
//                    cell.addElement(p);
////                   cell.setUseAscender(false);
////                   cell.setUseDescender(false);
////                   cell.setNoWrap(true);
////                   cell.setFixedHeight(36f);
//                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
////              cell.setPadding(0.0f);
//                    cell.setPaddingBottom(8f);
//                    cell.setPaddingTop(-8f);
//                    cell.setPaddingRight(30f);
//                    table2.addCell(cell);
//                    n++;
//                }
//            }

///C/////////////////////////////////////////////////////////////////////////ءءءءXXXXXK
            int row=0,quantity_sum=0;
            for (int i=0;i<size_data;i++) {
                for (int q=0;q<1;q++){
                    cell = new PdfPCell();
                    p = new Phrase(total_price_ofOne.get(row), f);
                    cell.addElement(p);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    cell.setPaddingBottom(8f);
                    cell.setPaddingTop(-8f);
                    cell.setPaddingRight(30f);
                    table2.addCell(cell);
                }
                for (int w=0;w<1;w++){
                    cell = new PdfPCell();
                    p = new Phrase(selling_price.get(row), f);
                    cell.addElement(p);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    cell.setPaddingBottom(8f);
                    cell.setPaddingTop(-8f);
                    cell.setPaddingRight(30f);
                    table2.addCell(cell);
                }
                for (int e=0;e<1;e++){
                    cell = new PdfPCell();
                    p = new Phrase(quantity.get(row), f);
                    cell.addElement(p);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    cell.setPaddingBottom(8f);
                    cell.setPaddingTop(-8f);
                    cell.setPaddingRight(30f);
                    table2.addCell(cell);
                    quantity_sum += Integer.parseInt(quantity.get(row));
                }
                for (int t=0;t<1;t++){
                    cell = new PdfPCell();
                    p = new Phrase(quantity_type.get(row), f);
                    cell.addElement(p);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    cell.setPaddingBottom(8f);
                    cell.setPaddingTop(-8f);
                    cell.setPaddingRight(10f);
                    table2.addCell(cell);

                }
                for (int r=0;r<1;r++){
                    cell = new PdfPCell();
                    p = new Phrase(name_prod.get(row), f);
                    cell.addElement(p);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    cell.setPaddingBottom(8f);
                    cell.setPaddingTop(-8f);
                    table2.addCell(cell);
                }
                row++;
            }

            ///الاجمالي
            g=1;
            for (int i=1;i<=3;i++) {
                if (g==1){
                    cell = new PdfPCell();
                    p = new Phrase(total_price+" ريال", f);
                    cell.addElement(p);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//                  cell.setPadding(0.0f);
                    cell.setPaddingBottom(8f);
                    cell.setPaddingTop(-8f);
                    //  cell.setPaddingRight(80f);
                    cell.setPaddingRight(50f);
                    cell.setColspan(2);
                    table2.addCell(cell);
                    g++;
                }else if (g==2){
                    cell = new PdfPCell();
                    p = new Phrase(String.valueOf(quantity_sum), f);
                    cell.addElement(p);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//                  cell.setPadding(0.0f);
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
//                  cell.setPadding(0.0f);
                    cell.setPaddingBottom(8f);
                    cell.setPaddingTop(-8f);
                    cell.setColspan(2);
                    //cell.setPaddingRight(30f);
                    table2.addCell(cell);
                    g++;
                }
            }

            ///##############################  ARABASE

            ArabicTools arabicTools = new ArabicTools();
            arabicTools.isFeminine = true;
            String s = total_price ,print;
            int size=s.length()-1;
            String[] parts = s.split("\\.");

            String part1 ;   //      :الجزء الأول سيحتوي على النص
            String part2 ;   //           :الجزء الثاني سيحتوي على النص

            if (s.contains(".")){

                part1 = parts[0];
                part2 = parts[1];
                print =arabicTools.numberToArabicWords(part1)+
                        " ريال و "+  arabicTools.numberToArabicWords(part2)+" فلس ";

//                System.out.println("76521351.61 = "+ arabicTools.numberToArabicWords(part1)+
//                        " ريال و "+  arabicTools.numberToArabicWords(part2)+" فلس ");
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

//            document.newPage();
//            document.add(table);
            ///#####################################(3)######################################
            document.close();

            if (what.equals("1")){
            share();
            }else {open();}


//            ColumnText canvas = new ColumnText(writer.getDirectContent());
//            canvas.setSimpleColumn(36, 750, 559, 780);
//            canvas.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
//            canvas.addElement(p);
//            canvas.go();

           // document.close();

            // Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static String reverseString(String str){/////عكس
        StringBuilder sb = new StringBuilder(str);
        String result = sb.reverse().toString();
        return result;
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

}