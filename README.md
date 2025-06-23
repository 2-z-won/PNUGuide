# PNU Guide

부산대학교(PNU) 캠퍼스를 쉽고 재미있게 탐험할 수 있도록 돕는 모바일 가이드 앱입니다.
투어 코스, 스탬프 수집, 영상 라이브러리, 챗봇, 지도 네비게이션 기능을 하나로 통합했습니다.

---

## 주요 기능

### 🎓 Interactive Courses
- 캠퍼스 내 인기·테마별 코스를 단계별로 안내
- 각 스팟의 진행 상태(완료/미완료)를 시각화
- 검색/정렬 기능으로 원하는 코스를 빠르게 찾기

### 📸 Stamp Collection
- 카메라로 사진 촬영 후 ML 클러스터링으로 스팟 자동 인식
- 스팟별 퀴즈(O/X, 객관식) 완료 시 스탬프 지급
- 획득한 스탬프는 뱃지 북에서 관리

### 🎥 Campus Video Library
- 홈 화면에 건물·단과대학 소개 영상 하이라이트
- 더보기 화면에서 YouTube 목록, 검색/정렬 지원
- YouTube 앱 연동으로 바로 재생

### 💬 Chatbot
- ChatGPT API와 연동된 대화형 챗봇
- 캠퍼스 정보, 길찾기 팁, 행사 일정 등 실시간 응대
- Coroutine 기반 비동기 처리로 부드러운 UX

### 🗺️ Map & Navigation
- Google Maps SDK 기반 캠퍼스 지도 제공
- 현위치 마커와 경로 탐색(네비게이션) 지원
- 스팟 터치 시 사진 촬영과 퀴즈 옵션으로 연결

### ⚙️ Settings & Profile
- 익명 UUID 기반 로그인으로 간편 시작
- 앱 정보 확인 및 로그아웃/탈퇴 기능 제공
- 최소한의 개인정보만 요구하여 부담 감소

---

## 기술 스택

| 구분 | 기술 및 라이브러리 |
| --- | --- |
| 언어 | Kotlin |
| UI | XML 레이아웃, RecyclerView, Fragment |
| 비동기 처리 | Kotlin Coroutines |
| 머신러닝 | TensorFlow Lite, 커스텀 클러스터링 |
| 지도 | Google Maps Android SDK |
| HTTP & API | Retrofit, OkHttp, ChatGPT API |
| 데이터베이스 | Firebase Firestore |
| 외부 앱 연동 | Camera Intent, YouTube Intent |

---

## 아키텍처

- **MVVM** 패턴
- **Repository** 레이어로 데이터 소스를 추상화
- **ViewModel**에서 Coroutine을 활용한 비동기 로직 처리
- **LiveData / StateFlow**로 UI 업데이트

---

## 설치 및 실행

1. 저장소 클론  
   ```bash
   git clone https://github.com/your-org/pnu-guide.git
   ```
2. Android Studio로 프로젝트 열기
3. Google Maps API 키와 Firebase 설정 파일(`google-services.json`) 추가
4. `./gradlew assembleDebug` 또는 IDE에서 실행

---

## 사용 가이드

1. 앱 실행 후 **익명 로그인**
2. **홈**에서 추천 코스와 인기 스팟 확인
3. **Courses**에서 코스 선택 후 지도/퀴즈 진행
4. **Stamps** 메뉴에서 사진 촬영 후 스탬프 수집
5. **Chat**에서 챗봇으로 궁금증 해결
6. 헤더 아이콘을 통해 **지도**와 **설정** 화면 진입

---

## 기여 방법

1. Fork 후 브랜치 생성 (`git checkout -b feature/my-feature`)
2. 변경 사항 커밋
3. Push 후 Pull Request 제출

---

*PNU Guide와 함께 캠퍼스를 더욱 스마트하게 탐험해 보세요!*
