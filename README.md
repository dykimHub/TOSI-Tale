# :rabbit: TOSI(The Only Story In the world)를 소개합니다. :tada:

<div align="center">
<img src="assets/tosi.png" width="" height="150"></img>

**토씨는 어린이들이 혼자서도 재미있게 독서할 수 있는 인터랙티브 동화 구연 서비스입니다.** <br>
**TOSI-Tale**은 **TOSI 서비스**에서 동화와 관련된 기능을 **동화 서비스**로 분리한 프로젝트 입니다.

</div>

# :sparkle: 서비스 목표

동화 **등장인물의 이름을 아이의 이름으로 바꿔** TTS로 읽어주며,
아이 혼자서도 동화를 즐길 수 있어서서 미디어 소비 대신 독서 습관 형성을 돕습니다.

동화가 끝나면 OpenAI API를 활용해 **원하는 등장인물과 채팅**하면서,
이야기에 능동적으로 참여하여 더 몰입감 있는 독서 경험을 제공합니다.

**원하는 키워드와 배경**으로 OpenAI API를 활용해 **커스텀 동화를 제작**하면서,
매번 새로운 이야기와 삽화로 아이의 상상력을 자극합니다.

</div>

# :date: 기간

_SSAFY 10기 공통 프로젝트_  
**2024.01.03 - 2024.02.16 (7주)**  
 우수상🏆

_1차 리팩토링_  
**24.09.09 - 24.10.18**

_2차 리팩토링_  
**25.02.06 - 25.02.23**

## :computer: Team. 먼똑귀

| 이름         | 역할          | 기능                                                                                                                                                                                    |
| ------------ | ------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 천우진(팀장) | BE, FE        | 일반 동화 관리, 메인페이지 UI 구성                                                                                                                                                      |
| 우지민       | BE, FE        | TTS 재생/정지/배속/볼륨 적용, TTS 제어 UI 구성                                                                                                                                          |
| 김다윤       | BE, FE        | 등장인물을 회원 이름으로 변경, 회원 관심 동화 관리, 이름 선택 및 동화책 UI 구성 <br> 1차 리팩토링: MSA 마이그레이션 & 쿠버네티스 도입<br> 2차 리팩토링: MSA 공통 로직 설계 & Redis 도입 |
| 양성주       | BE, FE        | 커스텀 동화 생성 및 저장, (비)공개 커스텀 동화 관리, 커스텀 동화 제작 UI 구성                                                                                                           |
| 이아진       | BE, FE, INFRA | 등장인물과의 채팅, 채팅방 UI 구성, AWS EC2 배포                                                                                                                                         |
| 김소연       | BE, FE        | JWT 기반 회원 인증 및 인가, 로그인/회원가입/마이페이지 UI 구성                                                                                                                          |

## :pushpin: TOSI 서비스 아키텍처

<img src="/assets/tosi_msa_tale.drawio.png" alt="System Architecture" width="700"/>

## :deciduous_tree: TOSI-Tale 기술 스택

| Section      | Stack                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| ------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Backend**  | ![Spring Boot](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white) ![querydsl](https://img.shields.io/badge/QueryDSL-007ACC.svg?style=for-the-badge&logo=&logoColor=white)                                                                                                                                                                                                                                    |
| **Database** | ![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-DC382D.svg?style=for-the-badge&logo=redis&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| **Infra**    | ![Docker](https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white) ![Kubernetes](https://img.shields.io/badge/kubernetes-%23326CE5.svg?style=for-the-badge&logo=kubernetes&logoColor=white) ![Amazon RDS](https://img.shields.io/badge/Amazon%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white) ![Amazon S3](https://img.shields.io/badge/Amazon%20S3-569A31.svg?style=for-the-badge&logo=amazons3&logoColor=white) ![Route 53](https://img.shields.io/badge/Route%2053-6A34D1.svg?style=for-the-badge&logo=amazonroute53&logoColor=white) ![AWS EKS](https://img.shields.io/badge/AWS%20EKS-%23FF9900.svg?style=for-the-badge&logo=amazoneks&logoColor=white) |
| **DevTool**  | ![GitHub](https://img.shields.io/badge/GitHub-181717.svg?style=for-the-badge&logo=github&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF.svg?style=for-the-badge&logo=githubactions&logoColor=white) ![OpenAPI](https://img.shields.io/badge/OpenAPI-85EA2D.svg?style=for-the-badge&logo=swagger&logoColor=black) ![IntelliJ](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white) ![VSCode](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=&logoColor=white)                                                                                                                     |

## :art: TOSI-Tale 관련 서비스 화면

<table>
  <tr>
    <th width="50%">메인페이지</th>
    <th width="50%">동화 목록</th>
  </tr>
  <tr>
    <td><img src="/assets/main.gif" width="100%"></td>
    <td><img src="/assets/booklist.gif" width="100%"></td>
  </tr>
  <tr>
    <td>- 비회원용 메인페이지에서는 토씨의 기능에 대해서 음성으로 설명합니다. <br> - 로그인에 성공하면 토씨의 기능을 사용할 수 있습니다.</td>
    <td>- 동화를 인기순, 이름순, 랜덤으로 정렬합니다. <br> - 동화를 제목의 일부로 검색할 수 있습니다. <br> - 동화 수에 따라 페이지네이션이 가능합니다.</td>
  </tr>
</table>
<table>
  <tr>
    <th width="50%">동화 상세</>
    <th width="50%">동화 구연</th>
  </tr>
  <tr>
    <td><img src="./assets/bookdetail.gif" width="100%"></td>
    <td><img src="./assets/bookread.gif" width="100%"></td>
  </tr>
  <tr>
    <td>- 동화 찜 버튼을 누르면 나의 책장에 추가 됩니다. <br> - 이름을 바꾸길 원하는 등장인물과 어린이의 이름을 선택할 수 있습니다. 이미 선택된 등장인물은 클릭할 수 없습니다. <br> - 동화를 읽어주길 원하는 목소리를 선택할 수 있습니다. <br> - 스피커를 누르면 목소리를 들어볼 수 있습니다.</td>
    <td>- 등장인물의 이름이 어린이의 이름으로 바뀌고 알맞은 조사가 붙어서 출력됩니다. <br> - TTS로 글을 다 읽히면 자동으로 다음 페이지로 넘어갑니다. 지정한 페이지에서 삽화가 바뀝니다.  <br> - 화살표를 누르면 이전 / 다음 페이지로 넘어갈 수 있습니다. <br> - 하단에서 재생 제어, 볼륨 조절, 속도 조절이 가능합니다. </td>
  </tr>
</table>
