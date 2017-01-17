package sds.auto.plate.utility;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

public class PlateNumberUtils {

	Context mContext;

	public PlateNumberUtils(Context context) {
		this.mContext = context;
	}
	

	public String correctNumber(String source)
	{
		char[] sourceArray = source.toCharArray();
		for(int index = 0; index < sourceArray.length; index++)
		{

            /***
             *

            [S,9,L,0,Z,5,2,H,Y,U,3,F,K,1,V,T,6,P,G,M,D,7,R,8,X,4,E,C,N,B,A]

[A,B,E,K,M,H,O,P,C,T,Y,X]

			if(sourceArray[index] == 'Z')
				sourceArray[index] = '2';
			else if (sourceArray[index] == 'S')
				sourceArray[index] = '5';
			else if (sourceArray[index] == 'C')
				sourceArray[index] = '0';
            else if (sourceArray[index] == 'T')
                sourceArray[index] = '1';
            else if (sourceArray[index] == 'Y')
                sourceArray[index] = '1';
            else if (sourceArray[index] == 'V')
                sourceArray[index] = '9';
            else if (sourceArray[index] == 'B')
                sourceArray[index] = '8';

             */

        //    [S,9,L,0,Z,5,2,H,Y,U,3,F,K,1,V,T,6,P,G,M,D,7,R,8,X,4,E,C,N,B,A]


            switch ( sourceArray[index] )  {

                case 'Z': sourceArray[index] = '2'; break;
                case 'S': sourceArray[index] = '5'; break;
                case 'C': sourceArray[index] = '0'; break;
                case 'T': sourceArray[index] = '1'; break;
                case 'Y': sourceArray[index] = '1'; break;
                case 'B': sourceArray[index] = '8'; break;
                case 'D': sourceArray[index] = '0'; break;
                case 'R': sourceArray[index] = '8'; break;
                case 'X': sourceArray[index] = '8'; break;
                case 'G': sourceArray[index] = '6'; break;
                case 'A': sourceArray[index] = '8'; break;
                case 'F': sourceArray[index] = '5'; break;
                case 'H': sourceArray[index] = '8'; break;
                case 'V': sourceArray[index] = '9'; break;
                case 'E': sourceArray[index] = '6'; break;

            }
        }
				
		return String.valueOf(sourceArray);
	}

    public String correctLetter(String source)
    {
        char[] sourceArray = source.toCharArray();
        for(int index = 0; index < sourceArray.length; index++)
        {

            switch ( sourceArray[index] )  {

                case '0': sourceArray[index] = 'O'; break;
                case '1': sourceArray[index] = 'T'; break;
                case '3': sourceArray[index] = 'B'; break;
                case '6': sourceArray[index] = 'B'; break;
                case '7': sourceArray[index] = 'T'; break;
                case '8': sourceArray[index] = 'B'; break;
                case '9': sourceArray[index] = 'B'; break;

                case 'Z': sourceArray[index] = ' '; break;
                case 'N': sourceArray[index] = ' '; break;
                case 'S': sourceArray[index] = ' '; break;
                case 'V': sourceArray[index] = 'Y'; break;
                case 'U': sourceArray[index] = 'O'; break;
                case 'D': sourceArray[index] = 'O'; break;
                case 'R': sourceArray[index] = 'P'; break;
                case 'F': sourceArray[index] = 'E'; break;




            }
        }

        return String.valueOf(sourceArray);
    }

	public String formatRusPlateNumber(String source) {

        // 8 ли 9 цифр в формате L DDD LL ?DD

        String result;
        source = source.replaceAll("L", ""); //  убираем мусорные символы

        if(source.length() == 8)
            result = correctLetter( source.substring(0, 1) ) +   correctNumber(source.substring(1, 4))  +
                     correctLetter( source.substring(4, 6) ) + " " + correctNumber(source.substring(6, 8));
        else if (source.length() == 9) {
                            result = correctLetter(source.substring(0, 1)) +   correctNumber(source.substring(1, 4)) +
                         correctLetter(source.substring(4, 6)) + " " +  correctNumber(source.substring(6, 9));
        }

        else
            result = "";

        return result;

    }



    public static SpannableString convertToPlate(String plate) {
        String res = "";
        char[] sourceArray = plate.toCharArray();
        for(int index = 0; index < sourceArray.length; index++)
        {
            switch ( index ) {
                case 0: res = cоnvertToRus ( String.valueOf(sourceArray[index])); break;
                case 1: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 2: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 3: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 4: res += cоnvertToRus( String.valueOf(sourceArray[index])); break;
                case 5: res += cоnvertToRus( String.valueOf(sourceArray[index])); break;
                case 6: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 7: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 8: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
            }
        }

        SpannableString ss =  new SpannableString(res);

        ss.setSpan(new RelativeSizeSpan(1.5f), 0,1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE); // устанавливаем размер
        ss.setSpan(new RelativeSizeSpan(2f), 1, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE); // устанавливаем размер
        ss.setSpan(new RelativeSizeSpan(1.5f), 4, 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE); // устанавливаем размер
        ss.setSpan(new RelativeSizeSpan(1.9f), 6, res.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE); // устанавливаем размер

        return ss;
    }

    public static String convertForBD(String plate) {
        String res = "";
        plate = plate.toUpperCase();

        char[] sourceArray = plate.toCharArray();
        for(int index = 0; index < sourceArray.length; index++)
        {
            switch ( index ) {
                case 0: res = cоnvertToEng(String.valueOf(sourceArray[index])); break;
                case 1: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 2: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 3: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 4: res += cоnvertToEng(String.valueOf(sourceArray[index])); break;
                case 5: res += cоnvertToEng(String.valueOf(sourceArray[index])); break;
                case 6: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 7: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
                case 8: res += cоnvertToDig( String.valueOf(sourceArray[index])); break;
            }
        }
        return res;
    }

    public static String cоnvertToRus(String c) {
        if (!c.matches("^\\D*$")) {       //  если цифра - добавлям напрямую
            return "";
        } else {
            // [S,9,L,0,Z,5,2,H,Y,U,3,F,K,1,V,T,6,P,G,M,D,7,R,8,X,4,E,C,N,B,A]
            if ( c.equals("A") ) return "А";
            if ( c.equals("B") ) return "В";
            if ( c.equals("C") ) return "С";
            if ( c.equals("E") ) return "Е";
            if ( c.equals("X") ) return "Х";
            if ( c.equals("M") ) return "М";
            if ( c.equals("P") ) return "Р";
            if ( c.equals("T") ) return "Т";
            if ( c.equals("K") ) return "К";
            if ( c.equals("Y") ) return "У";
            if ( c.equals("H") ) return "Н";
            if ( c.equals("O") ) return "О";


            if ( c.equals("А") || c.equals("В")  || c.equals("С")  || c.equals("Е")
                    || c.equals("Х")  || c.equals("М")  || c.equals("Р") || c.equals("Т")
                    || c.equals("К")  || c.equals("У") || c.equals("Н")  || c.equals("О")
                    ) return c.toUpperCase();
            else return "";
        }
    }

    public static String cоnvertToEng (String c ) {
        if (!c.matches("^\\D*$")) {       //  если цифра - убираем
            return "";
        } else {
            // [S,9,L,0,Z,5,2,H,Y,U,3,F,K,1,V,T,6,P,G,M,D,7,R,8,X,4,E,C,N,B,A]
            if ( c.equals("А") ) return "A";
            if ( c.equals("В") ) return "B";
            if ( c.equals("С") ) return "C";
            if ( c.equals("Е") ) return "E";
            if ( c.equals("Х") ) return "X";
            if ( c.equals("М") ) return "M";
            if ( c.equals("Р") ) return "P";
            if ( c.equals("Т") ) return "T";
            if ( c.equals("К") ) return "K";
            if ( c.equals("У") ) return "Y";
            if ( c.equals("Н") ) return "H";
            if ( c.equals("О") ) return "O";

            return c;
        }
    }




    public static String cоnvertToDig(String c) {
        if (!c.matches("^\\D*$")) {       //  если цифра - добавлям напрямую
            return c;
        } else return "";
    }


}
