package com.piltong.modudoc.common.network;

import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.document.DocumentSummary;
import com.piltong.modudoc.common.operation.Operation;

import java.net.Socket;
import java.util.List;


// 서버 네트워크 핸들러 측에서 서비스 로직에 접근하기 위한 인터페이스.
// 서버에서 서비스 로직 <-> 네트워크 핸들러 접속을 위한 인터페이스를 정의한다.
// 예를 들어, 서버가 클라로부터 OperationDTO를 전송받으면, 서버의 네트워크 핸들러는 서버의 서비스 로직에서 구현한 onOperationReceived 메소드를 호출한다.
// 이후 서비스 로직 측에서 Operation 입력을 받았을 때의 로직을 처리한다.
public interface ServerNetworkListener {


    // 클라이언트 접속/종료. 네트워크 핸들러는 네트워크 레벨을 추상화하므로, 소켓 관련 메소드는 필요하지 않음.
//    void onClientConnected(Socket client); // 클라이언트 연결 성공 시(필요 시, 소켓 정보 넘김)
//    void onClientDisConnected(Socket client); // 클라이언트 연결 종료 시(필요 시, 소켓 정보 넘김)

    // 문서 관련 CRUD 메소드. 클라로부터 수신한 CRUD 명령에 대해 처리한다.
    DocumentSummary handleCreateDocument(); // 문서 생성 메소드. 생성한 문서의 요약 객체를 리턴한다. 실패 시, null을 리턴한다.
    Document handleReadDocument(String readId); // 문서 읽기 메소드. ID가 일치하는 문서를 리턴한다. 실패 시, null을 리턴한다.
    DocumentSummary handleUpdateDocument(DocumentSummary updated); // 문서 수정 메소드. 수정한 문서의 요약 객체를 리턴한다. 실패 시, null을 리턴한다.
    String handleDeleteDocument(String deleteId); // 문서 삭제 메소드. ID가 일치하는 문서를 삭제한다. 삭제한 문서의 ID를 리턴한다. 실패 시, null을 리턴한다.


    // Operatino 관련 메소드. 클라로부터 수신한 Operation 리턴 명령에 대해 처리한다.
    void handleOperation(Operation op); // 클라로부터 Operation을 수신 수신받을 때 실행하는 메소드. 중앙의 문서를 수정한다. 브로드캐스팅은 네트워크 핸들러가 자동으로 수행한다.


    // 에러 관련 메소드
    void onNetworkError(Throwable t); // 네트워크 에러 발생 시, 실행하는 메소드


}

