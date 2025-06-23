# PNU Guide 프로젝트 제안서

## 1. 프로젝트 개요

- **앱 이름**: PNU Guide
- **목표**: Pusan National University(부산대학교) 캠퍼스를 효과적으로 탐색하고, 다양한 정보(코스 안내, 이벤트, 식단, 영상 콘텐츠 등)를 제공하는 종합 가이드 앱 개발
- **차별점**: AR-lite 기반 위치 오버레이, GPT 챗봇, QR 코드/클러스터 스탬프 기능 등을 결합하여 사용자 몰입도와 참여도를 극대화

## 2. 주요 기능

1. **비회원 모드 및 외부 DB 저장**
   - 로그인/회원가입 없이 앱 내 모든 기능 사용 가능
   - 사용자 데이터(즐겨찾기, 스탬프, 퀴즈 기록 등)는 Firebase Firestore 익명 인증(Anonymous Auth) 기반 외부 DB에 저장 및 동기화
2. **코스 추천 기능**
   - 인기/테마별(역사·문화·휴식·학습) 캠퍼스 투어 코스 생성
   - 시작·종료 지점, 예상 소요 시간, 경로 안내
3. **ML 클러스터링 기반 스탬프 수집 기능**
   - 사용자가 직접 스마트폰 카메라로 장소 사진 촬영
   - TensorFlow Lite 클러스터링 모델로 촬영 사진을 사전에 수집된 스팟 이미지와 유사도 비교
   - 유사도 임계치 초과 시 해당 스팟의 디지털 도장(스탬프) 자동 부여 및 Firestore 동기화
4. **스팟별 퀴즈 모드**
   - O/X 퀴즈 및 주관식 문제 제공
   - 정답 제출 후 즉시 피드백 및 추가 스탬프 보상
   - 퀴즈 결과를 Firestore에 저장하여 사용자 학습 히스토리 관리
5. **영상 콘텐츠 임베디드**
   - 각 단과대학·건물별 소개 영상 YouTube 링크 삽입 및 재생
6. **지도 & 길찾기**
   - Google Maps SDK로 캠퍼스 주요 시설 위치 표시
   - Navigation API(구글 또는 카카오)로 실시간 경로 안내
7. **대화형 챗봇**
   - GPT-4 API 기반 챗봇 UI 구현
   - "학교 건물 위치", "학사 일정", "동아리 정보" 등 자연어 질의응답 지원

## 3. 기술 스택

- **프론트엔드(Android)**: Kotlin, Android Studio, Kotlin Coroutines (Retrofit + Coroutines)
- **Jetpack Library**: RecyclerView(코스 목록, 퀴즈 목록), Fragment(탭 UI), DrawerLayout(사이드 내비게이션)
- **지도 & 네비게이션**: Google Maps SDK, Google Directions API
- **데이터 저장**: Firebase Firestore(익명 사용자 데이터 외부 DB)
- **네트워킹**: Retrofit + Coroutines (YouTube Data API, OpenAI GPT API)
- **머신러닝**: TensorFlow Lite 클러스터링 모델 (스팟 사진 클러스터링 및 유사도 비교)
- **영상 임베디드**: YouTube 앱 Intent 또는 WebView 내장 플레이어
- **API**: Google Maps & Google Directions, OpenAI GPT-4

## 4. Activity & Intent 설계

1. **MainActivity**: 앱 랜딩 / 메뉴 선택
2. **CourseActivity**: 테마별·인기 코스 리스트 표시 (RecyclerView)
3. **MapActivity**: 코스별 지도 표시 및 길찾기 (Google Maps + Navigation Intent)
4. **SpotDetailActivity**: QR/이미지 인식 스팟 상세 정보 + 퀴즈 (O/X, 주관식)
5. **QuizActivity**(옵션): 심화 퀴즈 UI
   - **Intent 예시**:
       - `MainActivity -> CourseActivity`: 선택한 테마 또는 시작 지점 데이터 전달
       - `CourseActivity -> MapActivity`: 선택한 코스 ID 전달, 예상 경로 표시
       - `MapActivity -> SpotDetailActivity`: 터치한 POI의 spotId 전달
       - `SpotDetailActivity -> MainActivity`: 퀴즈 결과 전달 (스탬프 획득 피드백)
