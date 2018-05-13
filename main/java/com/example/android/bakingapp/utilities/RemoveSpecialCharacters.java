package com.example.android.bakingapp.utilities;

/**
 * Created by Nikos on 05/09/18.
 * Attribution: found at:
 * https://stackoverflow.com/questions/21362484/remove-all-special-character-from-string-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 */
public class RemoveSpecialCharacters {

    private static boolean isSpecialCharacter(int b) {
//        if ((b >= 32 && b <= 47) || (b >= 58 && b <= 64) || (b >= 91 && b <= 96) || (b >= 123 && b <= 126) || b > 126)
        if ((b >= 127 && b <= 193) || b > 244)
            return true;
        return false;
    }
    public static String removeSpecialCharacters(String a) {
        StringBuffer s = new StringBuffer(a);


        int lenvar = s.length();
        String myString = "";
        for (int i = 0; i < lenvar; i++) {


            if (!isSpecialCharacter(s.charAt(i))) {
                myString += s.charAt(i);
            }
        }
        return myString;
    }
}