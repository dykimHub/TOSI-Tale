package com.tosi.tale.taleDetail.morpheme;

// import com.ssafy.tosi.taleDetail.morpheme.kiwi.KiwiTag;
// import kr.pe.bab2min.Kiwi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NameChanger {
    //    private static final String modelPath = "src/main/java/com/ssafy/tosi/taleDetail/morpheme/kiwi/ModelGenerator";
    private static final Josa josa = new Josa();
    //    private static Kiwi kiwi = null;
//    private static KiwiTag kiwiTag;
    private static StringBuilder changingContent;


//    Kiwi getKiwi() throws Exception {
//        if (kiwi == null) {
//            kiwi = Kiwi.init(modelPath);
//        }
//        return kiwi;
//    }

    public String changeName(String content, Map<String, String> nameMap) throws Exception {
        List<String> cnames = new ArrayList<>();
        for (String key : nameMap.keySet()) {
            if (!key.equals(nameMap.get(key))) // 이름 바뀌었을 때만 추가
                cnames.add(key);
        }

        for (int i = 0; i < cnames.size(); i++) {
            content = content.replace(cnames.get(i), i + "*");
        }

        changingContent = new StringBuilder();
        String[] sentences = content.split("\n");

        for (String sent : sentences) {
            char[] sent2char = sent.toCharArray();
            for (int i = 0; i < sent2char.length; i++) {

                changingContent.append(sent2char[i]);

                if (sent2char[i] == '*') {
                    String word = nameMap.get(cnames.get(Character.getNumericValue(sent2char[i - 1])));
                    String myjosa = appendJosa(word, sent2char[i + 1]);
                    changingContent.append(myjosa);
                    i++;
                }


            }

            changingContent.append("\n");

//            Kiwi.Token[] tokens = getKiwi().tokenize(sent, Kiwi.Match.allWithNormalizing); // 형태소 분석
//            System.out.println(Arrays.toString(tokens));
//
//
//            for (int i = tokens.length - 3; i >= 0; i--) {
//                String word = "";
//
//                if (!tokens[i].form.equals("*"))
//                    continue;
//
//                if (tokens[i + 2].form.equals("님") || (tokens[i + 2].form.equals("니") && tokens[i + 3].form.equals("ᆷ")))
//                    continue;
//
//                word = nameMap.get(cnames.get(Integer.parseInt(tokens[i + 1].form)));
//                String myjosa = appendJosa(tokens[i + 2], word);
//
//                int start, end;
//                start = tokens[i + 2].position;
//                end = start + tokens[i + 2].length;
//                sentCopy = sentCopy.substring(0, start) + myjosa + sentCopy.substring(end);
//
//                if (KiwiTag.toString(tokens[i + 2].tag).equals("VCP")) {
//                    System.out.print("토큰" + tokens[i + 4]);
//                    System.out.println(tokens[i + 4].form.equals("ᆷ"));
//
//                    start = tokens[i + 2].position;
//                    end = start + tokens[i + 3].length + 1;
//                    if (tokens[i + 3].form.equals("아") || tokens[i + 3].form.equals("야")) {
//                        myjosa = josa.aYa(word);
//                    } else if (KiwiTag.toString(tokens[i + 3].tag).equals("ETM") || KiwiTag.toString(tokens[i + 3].tag).equals("EF") || KiwiTag.toString(tokens[i + 3].tag).equals("EP") || KiwiTag.toString(tokens[i + 3].tag).equals("JX")) {
//                        myjosa = josa.eNoE(word) + tokens[i + 3].form;
//                    } else if (tokens[i + 3].form.equals("님") || (tokens[i + 3].form.equals("니") && tokens[i + 4].form.equals("ᆷ"))) {
//                        myjosa = "님";
//                    } else {
//                        myjosa = josa.eNoE(word);
//                    }
//                    sentCopy = sentCopy.substring(0, start) + myjosa + sentCopy.substring(end);
//
//                }
//
//            }

        }

        String changedContent = changingContent.toString();

        for (int i = 0; i < cnames.size(); i++) {
            changedContent = changedContent.replace(i + "*", nameMap.get(cnames.get(i)));
        }

        return changedContent;
    }

    public String appendJosa(String word, char myjosa) {
        switch (myjosa) {
            case '이':
                return josa.eNoE(word);
            case '가':
                return josa.iGa(word);
            case '은':
            case '는':
                return josa.eunNeun(word);
            case '아':
            case '야':
                return josa.aYa(word);
            case '을':
            case '를':
                return josa.eulReul(word);
            case '와':
            case '과':
                return josa.gwaWa(word);
            case '로':
                return josa.euroRo(word);
            case '님':
            case ',':
            case '!':
                return String.valueOf(myjosa);
            default:
                return josa.eNoE(word) + myjosa;
        }

    }


//    public String appendJosa(Kiwi.Token nextToken, String word) {
//        String form = nextToken.form;
//        String nextTag = KiwiTag.toString(nextToken.tag);
//
//        switch (nextTag) {
//            case "JKS":
//            case "ETM":
//                return form.equals("이") || form.equals("가") ? josa.iGa(word) : josa.eunNeun(word);
//            case "MM":
//                if (form.equals("이") || form.equals("가"))
//                    return josa.iGa(word);
//            case "JX":
//                if (form.equals("아") || form.equals("야")) {
//                    return josa.aYa(word);
//                } else if (form.equals("은") || form.equals("는")) {
//                    return josa.eunNeun(word);
//                } else if (form.equals("이나") || form.equals("나")) {
//                    return josa.eNoE(word) + "나";
//                } else {
//                    return josa.eNoE(word);
//                }
//            case "JKO":
//                return josa.eulReul(word);
//            case "JKC":
//                return josa.iGa(word);
//            case "JKB":
//                if (form.equals("로") || form.equals("으로")) {
//                    return josa.euroRo(word);
//                } else if (form.equals("와") || form.equals("과")) {
//                    return josa.gwaWa(word);
//                } else {
//                    return josa.eNoE(word) + form;
//                }
//            case "JKV":
//                return josa.aYa(word);
//            case "JC":
//                return josa.gwaWa(word);
//            case "XSN":
//            case "JKG":
//                return josa.eNoE(word) + form;
//            case "NNG":
//            case "NNB":
//                if (form.equals("와") || form.equals("과")) {
//                    return josa.gwaWa(word);
//                } else if (form.equals("은") || form.equals("는")) {
//                    return josa.eunNeun(word);
//                } else if (form.equals("을") || form.equals("를")) {
//                    return josa.eulReul(word);
//                } else if (form.equals("도")) {
//                    return josa.eNoE(word) + form;
//                } else return form;
//            default:
//                return form;
//
//        }
//    }

}
