package com.piltong.modudoc.common.network;

import com.piltong.modudoc.common.document.DocumentSummary;
import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.common.document.Document;

import java.util.List;


// 클라이언트 네트워크 핸들러 측에서 서비스 로직에 접근하기 위한 인터페이스.
// 서비스 로직 <-> 네트워크 핸들러 접속을 위한 인터페이스를 정의한다.
// 예를 들어, 클라에서 OperationDTO를 서버로부터 전송받으면, 클라이언트 네트워크 핸들러는 클라의 서비스 로직에서 구현한 onOperationReceived 메소드를 호출한다.
// 이후 서비스 로직 측에서 Operation 입력을 받았을 때의 로직을 처리한다.
public interface ClientNetworkListener {


    // 클라이언트가 실행한 명령이 성공했을 때 수행하는 메소드
//    void onDocumentSummariesReadSuccess(List<DocumentSummary> summaries); // READ_DOCUMENT_SUMMARIES 성공.
//    void onOperationPropagationSuccess(); // PROPAGATE_OPERATION 성공

    // 문서에 대한 CRUD(Create, Read, Update, Delete) 메소드. 서버측으로부터 송신받는다.
//    void onDocumentCreateSuccess(DocumentSummary created); // CREATE_DOCUMENT 성공. 생성한 문서의 요약 객체를 가져온다.
//    void onDocumentReadSuccess(Document doc); // READ_DOCUMENT 성공. 문서를 가져온다.
//    void onDocumentUpdateSuccess(DocumentSummary updated); // UPDATE_DOCUMENT 성공. 업데이트한 문서의 요약 객체를 가져온다.
//    void onDocumentDeleteSuccess(String deletedId); // DELETE_DOCUMENT 성공. 삭제한 문서의 아이디를 가져온다.


    // PROPAGATE_OPERATION에 의해 다른 클라이언트가 Operation을 수신했을 때 수행하는 메소드

    

    <T> void onCommandSuccess(ClientCommand command, T payload);
    void onOperationReceived(Operation op); //
    void onCommandFailure(ClientCommand command, String errorMessage); // 명령 실패 시, 실행하는 메소드.
    void onNetworkError(Throwable t); // 네트워크 에러 발생 시, 실행하는 메소드

}




