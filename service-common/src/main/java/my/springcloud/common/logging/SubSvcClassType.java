package my.springcloud.common.logging;

public enum SubSvcClassType {

	// GS 홈쇼핑 및 Default 규격 스케줄 조회
	A01
	// GS 마이샵 스케줄 조회
	, A02
	// CJ 스케줄 조회
	, A03
	// CJ+ 스케줄 조회
	, A04
	// 롯데홈쇼핑 스케줄 조회
	, A05
	// 롯데OneTv 스케줄 조회
	, A06
	// 현대 스케줄 조회
	, A07
	// 현대홈쇼핑+ 스케줄 조회
	, A08
	// 신세계TV쇼핑 스케줄 조회
	, A09
	// W홈쇼핑 스케줄 조회
	, A10
	// 홈앤쇼핑 스케줄 조회
	, A11
	// MD 카테고리 조회( F2)
	, B01
	// MD 전체 데이터 조회( F5)
	, B02
	// MD 데이터 조회( F7)
	, B03
	// 방송정보이벤트발행
	, C01
	// 편성정보이벤트발생
	, C02
	// 방송조회 - 방송정보 이벤트 구독
	, C03
	// 방송조회 - 편성정보 이벤트 구독
	, C04
	// 개인화 - 방송정보 이벤트 구독
	, C05
	// 문자공유 이벤트발행
	, C06
	// 문자공유 이벤트 구독
	, C07
	// PUSH 발송 요청 이벤트발행
	, C08
	// PUSH 발송 요청 이벤트 구독
	, C09
	// 가입자정보보회
	, D01
	// PUSH 발송
	, E01
	// SMS 발송
	, E02
	// MMS 발송
	, E03
	// 인기TOP EDP 적재
	, F01
	// 최근본상품  EDP 파일 적재
	, F02
	// 서비스인증
	, G01
	// 예약목록조회
	, G02
	// 예약하기
	, G03
	// 예약취소
	, G04
	// 이미지노출설정
	, G05
	// 이미지노출 ON
	, G06
	// 이미지노출 OFF
	, G07
	// 문자발송요청
	, G08
	// 튜토리얼정보 조회
	, G09
	// 앱메뉴조회
	, G10
	// 컨테이너조회
	, G11
	// 지금하는방송조회
	, G12
	// 메뉴별상품조회
	, G13
	// 컨테이너별상품조회
	, G14
	// 홈쇼핑채널EPG편성조회
	, G15
	// 튜토리얼정보 등록
	, H01
	// 튜토리얼정보 수정
	, H02
	// 튜토리얼정보 삭제
	, H03
	// 튜토리얼정보 단건 조회
	, H04
	// 튜토리얼정보 목록 조회
	, H05
	// 쇼핑채널 목록 조회 for admin
	, H06
	// 쇼핑채널 목록 조회 -> List<쇼핑채널 ID> // 수정) 쇼핑채널 단건 조회
	, H07
	// 쇼핑채널 등록
	, H08
	// 쇼핑채널 수정
	, H09
	// 쇼핑채널 삭제
	, H10
	// 상품 목록 조회 -> List<메인상품 ID>
	, H11
	// 상품 목록 조회 -> 날짜기준 모든 상품
	, H12
	// 상품 목록 조회 for admin
	, H13
	// 인기방송상품 조회
	, H14
	// 앱 메뉴 등록
	, H15
	// 앱 메뉴 수정
	, H16
	// 앱 메뉴 삭제
	, H17
	// 앱 메뉴 단건 조회
	, H18
	// 앱 메뉴  메뉴순서적용
	, H19
	// 앱 메뉴 목록 조회
	, H20
	// 컨테이너 등록
	, H21
	// 컨테이너 수정
	, H22
	// 컨테이너 삭제
	, H23
	// 컨테이너 단건 조회
	, H24
	// 컨테이너 조회
	, H25
	// 인증 요청
	, H26
	// 인증 SMS 발송
	, H27
	// 관리자 로그인
	, H28
	// SMS 인증번호 요청
	, H29
	// 인증번호 확인
	, H30
	// 패스워드 변경
	, H31
	// 계정 등록
	, H32
	// 계정 수정
	, H33
	// 계정 삭제
	, H34
	// 계정 단건 조회
	, H35
	// 계정 목록 조회
	, H36
	// 권한 등록
	, H37
	// 권한 수정
	, H38
	// 권한 단건 조회
	, H39
	// 권한 목록 조회
	, H40
	// 권한 메뉴 조회
	, H41
	// 메뉴 조회
	, H42
	// 계정 중복 체크
	, H43
	// 추가) 카테고리 단건 조회
	, H44
	// 추가) 카테고리 레벨별 조회
	, H45
	// 추가) 풀카테고리 조회
	, H46
	// 추가) 상품 목록 조회 -> 편성 상품 조회
	, H47
	// 앱 메뉴 관리용 컨테이너 조회
	, H48
	// 계정 차단
	, H49
	// 계정 승인
	, H50
	// 쇼핑채널수집 배치
	, I01
	// 방송상품수집 배치
	, I02
	// 수동 편성상품 컨테이너용 상품수집 배치
	, I03
	// 자동 - 상품평점(고정) 컨테이너용 상품수집 배치
	, I04
	// 자동 - 무료상품 컨테이너용 상품수집 배치
	, I05
	// 자동 - 쇼호스트 컨테이너용 상품수집 배치
	, I06
	// 자동 - 인기프로그램 컨테이너용 상품수집 배치
	, I07
	// 자동 - 방송사명 컨테이너용 상품수집 배치
	, I08
	// 자동 - 채널번호 컨테이너용 상품수집 배치
	, I09
	// 자동 - 브랜드 컨테이너용 상품수집 배치
	, I10
	// 자동 - 가격 컨테이너용 상품수집 배치
	, I11
	// 자동 - 할인율 컨테이너용 상품수집 배치
	, I12
	// 자동 - 상품평점 컨테이너용 상품수집 배치
	, I13
	// 자동 - 무이자개월수 컨테이너용 상품수집 배치
	, I14
	// 자동 - 카테고리 컨테이너용 상품수집 배치
	, I15

	// 지금하는 방송 미리보기
	, J01
	// 앱메뉴 미리보기
	, J02
	// 수동 컨테이너 미리보기
	, J03
	// 자동 컨테이너 미리보기
	, J04;

}
