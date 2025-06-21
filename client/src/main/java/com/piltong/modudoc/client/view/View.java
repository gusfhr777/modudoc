package com.piltong.modudoc.client.view;


// 상위 뷰, 하위 뷰 모두 이 인터페이스에 속한다.
// 생명 주기
// 생성자 : 초기화, 뷰 주입 등 실행 준비
// 시작 : 컨트롤러 동작 시작
// 끝 : 컨트롤러 동작 끝
// 종료 : 컨트롤러 종료.
public interface View {

    public void start();

    public void end();

    public void shutdown();
}
