package com.piltong.modudoc.client.network;

import java.net.*;
import java.io.*;
import java.util.List;

import com.piltong.modudoc.client.model.*;
import com.piltong.modudoc.common.model.*;
import com.piltong.modudoc.common.network.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// 클라이언트에서 네트워크 로직을 처리하는 클래스
public class ClientNetworkHandler implements Runnable{
    private static final Logger log = LogManager.getLogger(ClientNetworkHandler.class);
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final ClientNetworkListener listener;


    /**
     *
     * @param listener 네트워크 이벤트를 처리할 {@link ClientNetworkListener} 구현체
     */
    public ClientNetworkHandler(String host, int port, ClientNetworkListener listener) {
        try {
            this.socket = new Socket(host, port);
            this.listener = listener;
        } catch (UnknownHostException e) {
            String msg = "Unknown Hostname Detected.";
            log.error(msg, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            String msg = "IOException occured.";
            log.error(msg, e);
            throw new RuntimeException(e);
        }

        log.info("Network Handler Initialized.");
    }



    // 클라이언트 네트워크 핸들러의 메인 로직
    // 서버로부터 데이터 송신을 대기하고, 송신을 받으면 그에 따른 로직을 처리한다.
    // 스레드를 통해 실행할 때, 처음 실행되는 지점이다.
    @Override
    public void run() {
        try {
            // 스트림 초기화 순서 주의 : out 다음 in을 해야한다.
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            String msg = "Client Handler Thread Error occured.";
            log.error(msg, e);
            throw new RuntimeException(e);
        }


        try {
            // 인터럽트 받기 전까지 무한 반복
            while (!Thread.currentThread().isInterrupted()) {
                Object msg = in.readObject(); // 오브젝트 읽기

                // 메시지가 ResponseCommandDto 형식일 경우
                if (msg instanceof ResponseCommandDto<?> dto) {

                    ClientCommand command = dto.getCommand();


                    // 실패 응답
                    if (!dto.isSuccess()) {
                        listener.onCommandFailure(command, dto.getErrorMsg());
                        continue;
                    }

                    // 성공 응답
                    switch (command) {

                        // 문서 생성 명령
                        case CREATE_DOCUMENT:
                            listener.onCommandSuccess(command, DocMapper.toEntity((DocumentDto) dto.getPayload()));
                            break;

                        // 단일 문서 조회 명령
                        case READ_DOCUMENT:
                            listener.onCommandSuccess(command, DocMapper.toEntity((DocumentDto) dto.getPayload()));
                            break;

                        // 문서 수정 명령
                        case UPDATE_DOCUMENT:
                            listener.onCommandSuccess(command, null);
                            break;

                        // 문서 삭제 명령
                        case DELETE_DOCUMENT:
                            listener.onCommandSuccess(command, null);
                            break;

                        // 문서 요약 리스트 조회 명령
                        case READ_DOCUMENT_LIST:
                            listener.onCommandSuccess(command, DocMapper.toEntity((List<DocumentDto>) dto.getPayload()));
                            break;

                        // Operation 전파 명령
                        case PROPAGATE_OPERATION:
                            listener.onCommandSuccess(command, null);
                            break;

                        // 이외 명령
                        default:
                            String errMsg = "Unkown Command Detected.";
                            log.error(errMsg);
                            throw new RuntimeException();
                    }

                } else {
                    listener.onNetworkError(new IllegalArgumentException("Unknown DTO Received: "+msg.getClass()));

                }

            }
        } catch (IOException | ClassNotFoundException e) {
            String errMsg = "Client Thread Receive Failed.";
            log.error(errMsg, e);
            throw new RuntimeException(e);

        } finally { // 핸들러 처리 이후
            shutdown(); // 종료
        }

    }


    // 명령어를 서버 측으로 전송한다.
    public <T> void sendCommand(ClientCommand command, T payload) {
        Serializable payloadDto = null;

        try {
            // payload가 Serializable을 구현한 객체인지 확인한다.
            if (payload instanceof Serializable) {
                payloadDto = (Serializable) payload;
            } else {
                if (payload instanceof Document) {
                    payloadDto = Document.toDto((Document) payload);
                } else if (payload instanceof Operation) {
                    payloadDto = OperationMapper.toDto((Operation) payload);
                }
            }

            // RequestCommandDto 생성 및 서버 측에 전송
            RequestCommandDto<Serializable> dto = new RequestCommandDto<>(command, payloadDto);
            out.writeObject(dto);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // Operation 요청 함수
    public Operation requestOperation() {
        try {
            return OperationMapper.toEntity((OperationDto) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    // Operation 전송 함수
    public void sendOperation(Operation operation) {
        try {
            out.writeObject(OperationMapper.toDto(operation)); // 전송 버퍼에 로드
            out.flush(); // 전송
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Document 요청 함수
    public Document requestDocument() {
        try {
            return DocMapper.toEntity((DocumentDto) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    // 핸들러 종료 함수
    public void shutdown() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException ignored) {}
    }
}
