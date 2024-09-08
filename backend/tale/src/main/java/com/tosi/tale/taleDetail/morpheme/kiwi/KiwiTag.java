package com.tosi.tale.taleDetail.morpheme.kiwi;

public class KiwiTag {
    final static public byte unknown = 0,
            nng = 1, nnp = 2, nnb = 3,
            vv = 4, va = 5,
            mag = 6,
            nr = 7, np = 8,
            vx = 9,
            mm = 10, maj = 11,
            ic = 12,
            xpn = 13, xsn = 14, xsv = 15, xsa = 16, xsm = 17, xr = 18,
            vcp = 19, vcn = 20,
            sf = 21, sp = 22, ss = 23, sso = 24, ssc = 25, se = 26, so = 27, sw = 28, sb = 29,
            sl = 30, sh = 31, sn = 32,
            w_url = 33, w_email = 34, w_mention = 35, w_hashtag = 36, w_serial = 37,
            jks = 38, jkc = 39, jkg = 40, jko = 41, jkb = 42, jkv = 43, jkq = 44, jx = 45, jc = 46,
            ep = 47, ef = 48, ec = 49, etn = 50, etm = 51,
            z_coda = 52,
            user0 = 53, user1 = 54, user2 = 55, user3 = 56, user4 = 57,
            p = 58,
            max = 59,
            pv = p,
            pa = (byte) (p + 1),
            irregular = -128,

    vvi = (byte) (vv | irregular),
            vai = (byte) (va | irregular),
            vxi = (byte) (vx | irregular),
            xsai = (byte) (xsa | irregular),
            pvi = (byte) (pv | irregular),
            pai = (byte) (pa | irregular);

    public static String toString(byte tag) {
        switch (tag) {
            case unknown:
                return "UNK"; // 알 수 없음
            case nng:
                return "NNG"; // 일반 명사
            case nnp:
                return "NNP"; // 고유 명사
            case nnb:
                return "NNB"; // 의존 명사
            case vv:
                return "VV"; // 동사
            case va:
                return "VA"; // 형용사
            case mag:
                return "MAG"; // 일반 부사
            case nr:
                return "NR"; // 수사
            case np:
                return "NP"; // 대명사
            case vx:
                return "VX"; // 보조 용언
            case mm:
                return "MM"; // 관형사
            case maj:
                return "MAJ"; // 접속 부사
            case ic:
                return "IC"; // 감탄사
            case xpn:
                return "XPN"; // 체언 접두사
            case xsn:
                return "XSN"; // 명사 파생 접미사
            case xsv:
                return "XSV"; // 동사 파생 접미사
            case xsa:
                return "XSA"; // 형용사 파생 접미사
            case xsm:
                return "XSM"; // 부사 파생 접미사
            case xr:
                return "XR"; // 어근
            case vcp:
                return "VCP"; // 긍정 지시사(이다)
            case vcn:
                return "VCN"; // 부정 지시사(아니다)
            case sf:
                return "SF"; // 종결 부호
            case sp:
                return "SP"; // 구분 부호
            case ss:
                return "SS"; // 인용 부호 및 괄호
            case sso:
                return "SSO"; // 여는 부호
            case ssc:
                return "SSC"; // 닫는 부호
            case se:
                return "SE"; // 줄임표
            case so:
                return "SO"; // 붙임표
            case sw:
                return "SW"; // 기타 특수 문자
            case sb:
                return "SB"; // 순서 있는 글머리
            case sl:
                return "SL"; // 알파벳
            case sh:
                return "SH"; // 한자
            case sn:
                return "SN"; // 숫자
            case w_url:
                return "W_URL"; // URL 주소
            case w_email:
                return "W_EMAIL"; // 이메일 주소
            case w_mention:
                return "W_MENTION"; // 멘션
            case w_hashtag:
                return "W_HASHTAG"; // 해시태그
            case w_serial:
                return "W_SERIAL"; // 일련번호
            case jks:
                return "JKS"; // 주격 조사
            case jkc:
                return "JKC"; // 보격 조사
            case jkg:
                return "JKG"; // 관형격 조사
            case jko:
                return "JKO"; // 목적격 조사
            case jkb:
                return "JKB"; // 부사격 조사
            case jkv:
                return "JKV"; // 호격 조사
            case jkq:
                return "JKQ"; // 인용격 조사
            case jx:
                return "JX"; // 보조사
            case jc:
                return "JC"; // 접속 조사
            case ep:
                return "EP"; // 선어말 어미
            case ef:
                return "EF"; // 종결 어미
            case ec:
                return "EC"; // 연결 어미
            case etn:
                return "ETN"; // 명사형 전성 어미
            case etm:
                return "ETM"; // 관형형 전성 어미
            case z_coda:
                return "Z_CODA"; // 덧붙은 받침
            case user0:
                return "USER0"; // 사용자 정의 태그 0
            case user1:
                return "USER1"; // 사용자

        }
        return null;

    }

}
