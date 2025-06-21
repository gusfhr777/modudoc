package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.view.View;

// 상위 컨트롤러, 하위 컨트롤러 모두 이 인터페이스에 속한다.
// 생명 주기
// 생성자 : 초기화, 뷰 주입 등 실행 준비
// 시작 : 컨트롤러 동작 시작
// 끝 : 컨트롤러 동작 끝
// 종료 : 컨트롤러 종료.
public interface Controller {

    public void setView(View view); // 뷰 의존성 주입
    public void start(); // 컨트롤러 시작. 이 하위 컨트롤러가 동작중이다.
    public void end(); // 컨트롤러 끝. 이 하위 컨트롤러가 끝이다.
    public void shutdown(); // 컨트롤러
}
