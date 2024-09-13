package com.tosi.tale.tale;

public class Josa {
    public String appendJosa(String word, String josa) {
        switch (josa) {
            case "이":
                return eNoE(word);
            case "가":
                return iGa(word);
            case "은":
            case "는":
                return eunNeun(word);
            case "아":
            case "야":
                return aYa(word);
            case "을":
            case "를":
                return eulReul(word);
            case "와":
            case "과":
                return gwaWa(word);
            case "로":
                return euroRo(word);
            case "님":
            case ",":
            case "!":
                return josa;
            default:
                return eNoE(word) + josa;
        }

    }

    /**
     * 종성 여부에 따라 '이' 또는 빈 문자열을 반환합니다.
     *
     * @param inputStr 입력 문자열
     * @return 종성 있으면 '이', 없으면 빈 문자열
     */
    public String eNoE(String inputStr) {
        return getJosa(inputStr, "이", "");
    }

    /**
     * 종성 여부에 따라 '이가' 또는 '가'를 반환합니다.
     *
     * @param inputStr 입력 문자열
     * @return 종성 있으면 '이가', 없으면 '가'
     */
    public String iGa(String inputStr) {
        return getJosa(inputStr, "이가", "가");
    }

    /**
     * 종성 여부에 따라 '이를' 또는 '를'을 반환합니다.
     *
     * @param inputStr 입력 문자열
     * @return 종성 있으면 '이를', 없으면 '를'
     */
    public String eulReul(String inputStr) {
        return getJosa(inputStr, "이를", "를");
    }

    /**
     * 종성 여부에 따라 '이는' 또는 '는'을 반환합니다.
     *
     * @param inputStr 입력 문자열
     * @return 종성 있으면 '이는', 없으면 '는'
     */
    public String eunNeun(String inputStr) {
        return getJosa(inputStr, "이는", "는");
    }

    /**
     * 종성 여부에 따라 '이와' 또는 '와'를 반환합니다.
     *
     * @param inputStr 입력 문자열
     * @return 종성 있으면 '이와', 없으면 '와'
     */
    public String gwaWa(String inputStr) {
        return getJosa(inputStr, "이와", "와");
    }

    /**
     * 종성 여부에 따라 '아' 또는 '야'를 반환합니다.
     *
     * @param inputStr 입력 문자열
     * @return 종성 있으면 '아', 없으면 '야'
     */
    public String aYa(String inputStr) {
        return getJosa(inputStr, "아", "야");
    }

    /**
     * 종성 여부에 따라 '으로' 또는 '로'를 반환합니다.
     *
     * @param inputStr 입력 문자열
     * @return 종성 있으면 '으로', 없으면 '로'
     */
    public String euroRo(String inputStr) {
        return getJosa(inputStr, "으로", "로");
    }

    /**
     * 종성 여부에 따라 적절한 조사를 반환합니다.
     *
     * @param inputStr       입력 문자열
     * @param jongSungJosa   종성이 있을 때 붙일 조사
     * @param noJongSungJosa 종성이 없을 때 붙일 조사
     * @return 종성 여부에 따라 선택된 조사
     */
    private String getJosa(String inputStr, String jongSungJosa, String noJongSungJosa) {
        return hasJongSung(inputStr) ? jongSungJosa : noJongSungJosa;
    }

    /**
     * 입력 문자열의 마지막 글자가 종성을 가지는지 확인합니다.
     *
     * @param inputStr 입력 문자열
     * @return 종성이 있으면 true, 없으면 false
     */
    private boolean hasJongSung(String inputStr) {
        // 문자열의 마지막 글자 추출
        String last = inputStr.substring(inputStr.length() - 1);

        // 한글의 종성 여부를 유니코드로 계산 (가 ~ 힇 사이의 글자)
        if (last.compareTo("가") >= 0 && last.compareTo("힇") <= 0)
            return (last.codePointAt(0) - 0xAC00) % 28 > 0;

        return false;
    }

}





