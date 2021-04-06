package com.aoa.mini_cashier;



import android.text.TextUtils;

import com.aoa.mini_cashier.DB.Databases;

import org.junit.Test;

import java.text.DecimalFormat;



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

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
      // System.out.println(" : "+To_double("19.0E12"));         //555525454.1234567
        //TimeOut();


        String s="",c="gg";
        boolean b=false,n=true;

        if (b&&c.equals("")){
            System.out.println("1");
        }if (n&& s.length()>0){
            System.out.println("1");
        }else {
            System.out.println("2");
        }

    }
    public void TimeOut(){
        int i=0;
        boolean b=true;
        while (b){
            i++;
            if (i==350000){
                b=false;
            }
        }
    }
    public String To_double(String s){

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

            for (int i=1;i<=size2;i++){
                text_2.append("0");
            }

            for (int i=1;i<=size1;i++){
                text_1.append("0");
            }

             d1=Float.parseFloat(part1);
             d2=Float.parseFloat(part2);

            d2=d1+d2;

            String bb= text_1.toString() +text_2;

            System.out.println("this is before formatting: "+d2);
             df = new DecimalFormat(bb);

            System.out.println("Value: " + df.format(d2));



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