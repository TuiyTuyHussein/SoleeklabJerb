package dev.m.hussein.jobtask.config;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import java.util.Locale;

/**
 * Created by Dev. M. Hussein on 11/6/2017.
 */

public class LocaleAlphabets {
    public static char[] getAlphabet(Resources resources){
        return getAlphabet(resources , false);
    }

    public static char[] getAlphabet(Resources resources , boolean flagToUpperCase){
        Locale locale = resources.getConfiguration().locale;
        LocaleLanguage language = LocaleLanguage.getLocalLanguage(locale);
        return getAlphabet(language, flagToUpperCase);
    }

    public static char[] getAlphabet(LocaleLanguage localeLanguage, boolean flagToUpperCase){
        if (localeLanguage == null)
            localeLanguage = LocaleLanguage.ENGLISH;

        char firstLetter = localeLanguage.getFirstLetter();
        char lastLetter = localeLanguage.getLastLetter();
        int alphabetSize = lastLetter - firstLetter + 1;

        char[] alphabet = new char[alphabetSize];

        for (int index = 0; index < alphabetSize; index++ ){
            alphabet[index] = (char) (index + firstLetter);
        }

        if (flagToUpperCase){
            alphabet = new String(alphabet).toUpperCase().toCharArray();
        }

        return alphabet;
    }

    public  enum LocaleLanguage{
        ARMENIAN(new Locale("hy"), 'ա', 'ֆ'),
        RUSSIAN(new Locale("ru"), 'а','я'),
        ENGLISH(new Locale("en"), 'a','z');

        private final Locale mLocale;
        private final char mFirstLetter;
        private final char mLastLetter;

        LocaleLanguage(Locale locale, char firstLetter, char lastLetter) {
            this.mLocale = locale;
            this.mFirstLetter = firstLetter;
            this.mLastLetter = lastLetter;
        }

        public Locale getLocale() {
            return mLocale;
        }

        public char getFirstLetter() {
            return mFirstLetter;
        }

        public char getLastLetter() {
            return mLastLetter;
        }

        public String getDisplayLanguage(){
            return getLocale().getDisplayLanguage();
        }

        public String getDisplayLanguage(LocaleLanguage locale){
            return getLocale().getDisplayLanguage(locale.getLocale());
        }

        @Nullable
        public static LocaleLanguage getLocalLanguage(Locale locale){
            if (locale == null)
                return LocaleLanguage.ENGLISH;

            for (LocaleLanguage localeLanguage : LocaleLanguage.values()){
                if (localeLanguage.getLocale().getLanguage().equals(locale.getLanguage()))
                    return localeLanguage;
            }

            return null;
        }
    }
}
