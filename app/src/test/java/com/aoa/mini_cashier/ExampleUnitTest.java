package com.aoa.mini_cashier;


///١٢٣٤٥٦٧٨٩٫٠٠٠
import android.text.TextUtils;

import com.aoa.mini_cashier.DB.Databases;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.MessageFormat;

import static java.text.MessageFormat.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    DecimalFormat df ;
    @Test
    public void addition_isCorrect() {
       // assertEquals(4, 2 + 2);
//        int a=200525454;//+Double.parseDouble(c)
//        String c="0.6666666";
//        double d= Double.parseDouble("555525454.1234567");
//        float b=200525454.1f;//555525454.1234567
//        System.out.println(d);
//        System.out.println(a);
//        System.out.println(b);
//
//        BigDecimal bd = new BigDecimal(d);
//
//        System.out.println("engineering:\t\t\t\t" + bd.toEngineeringString());
//        // or a plain String (might look equal to the engineering String)
//        System.out.println("plain      :\t\t\t\t\t" + bd.toPlainString());
//
//        System.out.print("thes : ");
//        System.out.printf("%f",d);
//
//        System.out.println("\nnew : ");
        //creating instance of StringBuffer

//        StringBuilder stringBuffer = new StringBuilder();
//        stringBuffer.append(format("{0}", 999999999.12));
//        String numberAsString5 = stringBuffer.toString();
//        System.out.println("Convert using StringBuffer Example -> " + String.format("%.3f", 099999999.12));
//
//
//
//
//        double d = 999999999.123;
//        String str = DecimalFormat.getNumberInstance().format(d);
//        System.out.println(str); //str is '123.454'
////if you don't want formatting
//        str = new DecimalFormat("#.0#").format(d); // rounded to 2 decimal places
//        System.out.println(str); //str is '123.45'
//        str = new DecimalFormat("#.0#").format(999999999.1239); // rounded to 2 decimal places
//        System.out.println(str); //str is '123.46'
//
//
//             Double f= 9999999999d;
//
//             double c=0;
//
//             double cc=Double.parseDouble("535353538");
//             double ccc=c+cc;
//             System.out.println("cc "+ccc);

       String str = new DecimalFormat("#.000#").format(999999999.1236); // rounded to 2 decimal places

        System.out.println(" v : " + To_double(str));


        //System.out.println(Double.parseDouble(new DecimalFormat("#.00#").format(٣٣٣٣٣٫٩٦٦)));

//
//        System.out.println(" : " + To_double2(new DecimalFormat("#.00#").format(535353538)));١٢٣٤٥٦٧٨٩٫٠٠٠
             // 9999999999.123
        //TimeOut();


        System.out.println(" int : " + To_int("12.0"));

        int gg=6000;
        System.out.println(To_int("6000.00"));
        String tecack_1=" 5";
        int number= Integer.parseInt(tecack_1);
        System.out.println(" int : " +number);



    }


    public void set_nu(){
        String ss="",nu="999999999.1239";
        for (int i=0;i<=nu.length()-1;i++){
            if (String.valueOf(nu.charAt(i)).equals("٠")){
                ss+="0";
            }else if(String.valueOf(nu.charAt(i)).equals("٩")){
                ss+="9";
            }else if(String.valueOf(nu.charAt(i)).equals("١")){
                ss+="1";
            }else if(String.valueOf(nu.charAt(i)).equals("٢")){
                ss+="2";
            }else if(String.valueOf(nu.charAt(i)).equals("٣")){
                ss+="3";
            }else if(String.valueOf(nu.charAt(i)).equals("٤")){
                ss+="4";
            }else if(String.valueOf(nu.charAt(i)).equals("٥")){
                ss+="5";
            }else if(String.valueOf(nu.charAt(i)).equals("٦")){///١٢٣٤٥٦٧٨٩٫٠٠٠
                ss+="6";
            }else if(String.valueOf(nu.charAt(i)).equals("٧")){
                ss+="7";
            }else if(String.valueOf(nu.charAt(i)).equals("٨")){
                ss+="8";
            }else if(String.valueOf(nu.charAt(i)).equals("٫")){
                ss+=".";
            }
        }
        System.out.println(" nn : " +ss);
        System.out.println(" nn : " + nu.length());
    }


    public String To_double(String s){

        String[] parts = s.split("\\.");
        String part1 ,v  ;   //
        String part2 ;   //
        StringBuilder text_1= new StringBuilder();
        StringBuilder text_2= new StringBuilder(".");
        double d1,d2;
        DecimalFormat df ;
        if (s.contains(".")) {

            part1 = parts[0];
            part2 = parts[1];

            int size1=part1.length();
            int size2=part2.length();

            part2 ="0.";
            part2 += parts[1];

            for (int i=1;i<=size2;i++){
                text_2.append("0");
            }

            for (int i=1;i<=size1;i++){
                text_1.append("0");
            }

            String arr;
            arr=parts[1];

            if (String.valueOf(arr.charAt(0)).equals("0")&&String.valueOf(arr.charAt(1)).equals("0")){
                text_2= new StringBuilder(".");
                text_2.append("0");
            }

             d1=Double.parseDouble(part1);
             d2=Double.parseDouble(part2);

            d2=d1+d2;

            String bb= text_1.toString() +text_2;

            System.out.println("this is before formatting: "+d2);
             df = new DecimalFormat(bb);

            System.out.println("Value: " + df.format(d2));
            v=df.format(d2);

        }else {
            String par=s+".0";
           v= To_double(par);
        }
        return v ;
    }


    public String To_int(String s){
        String[] parts = s.split("\\.");
        String part1 ,v  ;
        if (s.contains(".")) {

            part1 = parts[0];
            v=part1;
        }else {
            v=s;
        }
        return v;
    }


    public String To_double2(String s){

        String[] parts = s.split("\\.");
        String part1 ,v ="" ;   //
        String part2 ;   //
        StringBuilder text_1= new StringBuilder();
        StringBuilder text_2= new StringBuilder(".");
        double d1,d2;
        DecimalFormat df ;
        if (s.contains(".")) {

            part1 = parts[0];
            part2 = parts[1];


            int size1=part1.length();
            int size2=part2.length();

            part2 ="0.";
            part2 += parts[1];

            for (int i=1;i<=size1;i++){
                text_1.append("0");
            }

            for (int i=1;i<=size2;i++){////   .00000000->
                text_2.append("0");
            }

            String arr;
            arr=parts[1];

            if (String.valueOf(arr.charAt(0)).equals("0")&&String.valueOf(arr.charAt(1)).equals("0")){
                text_2= new StringBuilder(".");
                text_2.append("0");
            }



            String bb= text_1.toString() +text_2;

            System.out.println("this is before formatting: "+bb);




        }else {
            String par=s+".0";
            v= To_double(par);
            //d("55555656656556565656");
        }

        return v ;
    }



    public void d(String s){
        String x=" ";
        double ddf =Double.parseDouble(s);
        String ss=String.valueOf(ddf);

        String[] parts9 = ss.split("\\.");
        String part1 ,v ="" ;   //
        String part2 ;   //
        StringBuilder text_1= new StringBuilder();
        StringBuilder text_2= new StringBuilder(".");
        double d1,d2;
        DecimalFormat df ;
        if (ss.contains(".")) {

            part1 = parts9[0];
            part2 = parts9[1];

            int size1 = part1.length();
            int size2 = part2.length();

            part2 = "0.";
            part2 += parts9[1];

            for (int i = 1; i <= size2; i++) {
                text_2.append("0");
            }

            for (int i = 1; i <= size1; i++) {
                text_1.append("0");
            }

            d1 = Double.parseDouble(part1);
            d2 = Double.parseDouble(part2);

            d2 = d1 + d2;
            String bb = text_1.toString() + text_2;

            System.out.println("this is before formatting: " + d2);
            df = new DecimalFormat(bb);

             x=df.format(d2);

            //System.out.println("Value: " + df.format(d2));
        }else {
            String par=s+".0";
            x= To_double(par);
        }
        System.out.println(x);
    }
    //Lnumber=Double.parseDouble(part1);
    //part1=String.format ("%.0f", dd);
    // Lnumber=Double.parseDouble(part1);

//            Rnumber=Double.parseDouble(part2);
//            Lnumber=Lnumber+Rnumber;
//            Rnumber=Double.parseDouble(part2);
//            int size1=part2.length();

}