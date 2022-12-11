package com.risk.riskcontrol.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nevio
 * on 2022/2/10
 */
public class StringUtil {

//    private static final Pattern numPattern = Pattern.compile("[0-9]*");
//    private static final Pattern letterPattern = Pattern.compile("[a-zA-Z]");
//    private static final Pattern characterPattern = Pattern.compile("[\u4e00-\u9fa5]");
//    private static final Pattern upperPattern = Pattern.compile("[A-Z]");
//    private static final Pattern lowerPattern = Pattern.compile("[a-z]");
    private static final Pattern phoneNumPattern = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
//    private static final Pattern emailEnPattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
//    private static final Pattern emailCnPattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");



    public static boolean isPhoneNumCn(String phoneNum){
        Matcher m = phoneNumPattern.matcher(phoneNum);
        return m.matches();
    }

    /**
     * 只允许英文字母、数字、下划线、英文句号、以及中划线、@符号组成
     */
//    public static boolean isEmailEn(String email){
//        Matcher m = emailEnPattern.matcher(email);
//        return m.matches();
//    }
//
//    /**
//     * 名称允许汉字、字母、数字，下划线，中划线，域名只允许英文域名
//     */
//    public static boolean isEmailCn(String email){
//        Matcher m = emailCnPattern.matcher(email);
//        return m.matches();
//    }

    public static String fix(String str){
        if (TextUtils.isEmpty(str) || "null".equals(str)){
            return "";
        }
        return str;
    }

    public static boolean equal(String str1, String str2){
        if (str1 == null || str2 == null){
            return false;
        }
        return str1.equals(str2);
    }


    public static void setEmptyText(TextView textView, String str) {
        if (textView == null) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            textView.setText("/");
        } else {
            textView.setText(str);
        }
    }

    public static void setEmptyText(TextView[] textViewArr, String[] strArr) {
        if (textViewArr == null || textViewArr.length == 0) {
            return;
        }
        if (strArr == null || strArr.length == 0) {
            for (TextView textView : textViewArr) {
                textView.setText("/");
            }
        } else {
            for (int i = 0; i < textViewArr.length; i++) {
                if (i < strArr.length && !TextUtils.isEmpty(strArr[i])) {
                    textViewArr[i].setText(strArr[i]);
                } else {
                    textViewArr[i].setText("/");
                }
            }
        }
    }

    public static void setEmptyText(TextView textView, String str, String front) {
        if (textView == null) {
            return;
        }
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(front)) {
            textView.setText("/");
        } else if (TextUtils.isEmpty(str)) {
            textView.setText(front.concat("/"));
        } else if (TextUtils.isEmpty(front)) {
            textView.setText(str);
        } else {
            textView.setText(front.concat(str));
        }
    }

    /**
     * TextView部分字体变色
     * @param charSequence textView.getText()
     * @param startIndex   需变色字体的起始角标
     * @param endIndex     需变色字体的结束角标
     */
    @Nullable
    public static Spannable getSpannable(Context context, CharSequence charSequence,
                                         @ColorRes int colorResId, int startIndex, int endIndex) {
        if (context == null) {
            return null;
        }
        Spannable spannable = new SpannableString(charSequence);
        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorResId)),
                startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    @Nullable
    public static Spannable getSpannable(Context context, CharSequence charSequence,
                                         @ColorRes int colorResId, int startIndex) {
        if (context == null) {
            return null;
        }
        return getSpannable(context, charSequence, colorResId, startIndex, charSequence.length());
    }

    public static String formatFileLength(long originLength) {
        if (originLength < 1024) {
            return originLength + "B";
        } else if (originLength < 1024 * 1024) {
            return originLength / 1024 + "KB";
        } else if (originLength < 1024 * 1024 * 1024) {
            return originLength / 1024 / 1024 + "MB";
        } else {
            return originLength / 1024 / 1024 / 1024 + "G";
        }
    }

    /**
     * 从下载连接中解析出文件名
     */
    public static String getNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 是否只包含数字
     */
    public static boolean isDigitOnly(String inputStr) {
        if (TextUtils.isEmpty(inputStr)) {
            return false;
        }
        for (int i = 0; i < inputStr.length(); i++) {
            if (!Character.isDigit(inputStr.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否只包含字母
     */
    public static boolean isLetterOnly(String inputStr) {
        if (TextUtils.isEmpty(inputStr)) {
            return false;
        }
        for (int i = 0; i < inputStr.length(); i++) {
            if (!Character.isLetter(inputStr.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否只包含字母和数字
     */
    public static boolean isLetterAndDigitOnly(String inputStr) {
        if (TextUtils.isEmpty(inputStr)) {
            return false;
        }
        boolean isLetter = false;
        boolean isDigit = false;
        boolean isOther = false;
        for (int i = 0; i < inputStr.length(); i++) {
            if (Character.isLetter(inputStr.charAt(i))) {
                isLetter = true;
            } else if (Character.isDigit(inputStr.charAt(i))) {
                isDigit = true;
            } else {
                isOther = true;
            }
        }

        if (isLetter && isDigit && !isOther){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否只包含小写
     */
    public static boolean isLowerCaseOnly(String inputStr) {
        if (TextUtils.isEmpty(inputStr)) {
            return false;
        }
        for (int i = 0; i < inputStr.length(); i++) {
            if (!Character.isLowerCase(inputStr.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否包含字母
     */
    public static boolean isContainLetter(String inputStr) {
        if (TextUtils.isEmpty(inputStr)) {
            return false;
        }
        for (int i = 0; i < inputStr.length(); i++) {
            if (Character.isLetter(inputStr.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 必须包含小写字母和数字
     */
    public static boolean isLowerAndDigit(String inputStr) {
        if (TextUtils.isEmpty(inputStr)) {
            return false;
        }
        boolean isLower = false;
        boolean isDigit = false;
        for (int i = 0; i < inputStr.length(); i++) {
            if (Character.isLowerCase(inputStr.charAt(i))) {
                isLower = true;
            } else if (Character.isDigit(inputStr.charAt(i))) {
                isDigit = true;
            }
        }
        return isLower && isDigit;
    }


    /**
     * 必须包含字母和数字
     */
    public static boolean isLetterAndDigit(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        boolean isLetter = false;
        boolean isDigit = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                isLetter = true;
            } else if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        return isLetter && isDigit && str.matches(regex);
    }

    /**
     * 至少包含大小写字母及数字中的一种
     */
    public static boolean isLetterOrDigit(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        //定义一个boolean值，用来表示是否包含字母或数字
        boolean isLetterOrDigit = false;
        for (int i = 0; i < str.length(); i++) {
            //用char包装类中的判断数字的方法判断每一个字符
            if (Character.isLetterOrDigit(str.charAt(i))) {
                isLetterOrDigit = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        return isLetterOrDigit && str.matches(regex);
    }

    /**
     * 至少包含大小写字母及数字中的两种
     */
    public static boolean isLetterDigit(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        //定义一个boolean值，用来表示是否包含数字
        boolean isDigit = false;
        //定义一个boolean值，用来表示是否包含字母
        boolean isLetter = false;
        for (int i = 0; i < str.length(); i++) {
            //用char包装类中的判断数字的方法判断每一个字符
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
                //用char包装类中的判断字母的方法判断每一个字符
            } else if (Character.isLetter(str.charAt(i))) {
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        return isDigit && isLetter && str.matches(regex);
    }

    /**
     * 必须同时包含大小写字母及数字
     */
    public static boolean isContainAll(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        //定义一个boolean值，用来表示是否包含数字
        boolean isDigit = false;
        //定义一个boolean值，用来表示是否包含字母
        boolean isLowerCase = false;
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            //用char包装类中的判断数字的方法判断每一个字符
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {
                //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        return isDigit && isLowerCase && isUpperCase && str.matches(regex);
    }


}
