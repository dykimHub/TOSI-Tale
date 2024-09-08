package com.tosi.tale.taleDetail.morpheme;

import java.util.regex.Pattern;

public class Josa {
    private final Pattern reT;
    private final Pattern reF;

    public Josa() {
        reT = Pattern.compile("[0136-8L-NRㄱ-ㅎ\uFFA1-\uFFBE\u3165-\u3186\u1100-\u115E\u11A8-\u11FF]");
        reF = Pattern.compile("[2459A-KO-QS-Zㅏ-ㅣ\uFFC2-\uFFC7\uFFCA-\uFFCF\uFFD2-\uFFD7\uFFDA-\uFFDC\u3187-\u318E\u1161-\u11A7]");
    }

    public boolean jong(String inputStr) {
        String last = inputStr.substring(inputStr.length() - 1);
        if (last.compareTo("가") >= 0 && last.compareTo("힇") <= 0) {
            return (last.codePointAt(0) - 0xAC00) % 28 > 0;
        } else if (reT.matcher(last).find()) {
            return true;
        } else if (reF.matcher(last).find()) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    public String eNoE(String inputStr) { // 종성 있으면 ~이 없으면 안 붙임
        boolean j = jong(inputStr);
        return j ? "이" : "";
    }

    public String iGa(String inputStr) { // 종성 있으면 ~이가 없으면 가
        boolean j = jong(inputStr);
        return j ? "이가" : "가";
    }

    public String eulReul(String inputStr) { // 종성 있으면 ~을 -> 이를 없으면 를
        boolean j = jong(inputStr);
        return j ? "이를" : "를";
    }

    public String eunNeun(String inputStr) { // 종성 있으면 ~은 -> 이는 없으면 는
        boolean j = jong(inputStr);
        return j ? "이는" : "는";
    }

    public String gwaWa(String inputStr) { // 종성 있으면 ~과 -> 이와 없으면 와
        boolean j = jong(inputStr);
        return j ? "이와" : "와";
    }

    public String aYa(String inputStr) { // 종성있으면 ~아 없으면 ~야 없으면 야
        boolean j = jong(inputStr);
        return j ? "아" : "야";
    }

    public String euroRo(String inputStr) { // 종성 있으면 ~으로 없으면 로
        boolean j = jong(inputStr);
        return j ? "으로" : "로";
    }

}