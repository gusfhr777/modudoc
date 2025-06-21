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
public class NetworkHandler implements Runnable{
    private static final Logger log = LogManager.getLogger(NetworkHandler.class);
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final networkHandlerListener listener;


    /**
     *
     * @param listener 네트워크 이벤트를 처리할 {@link networkHandlerListener} 구현체
     */
    public NetworkHandler(String host, int port, networkHandlerListener listener)  {
        log.info("Network Handler Initializing...");
        this.listener = listener;

        final int MAX_ATTEMPTS = 10; // 최대 연결 시도
        final int CONNECTION_TIMEOUT = 5000; // Timeout 대기 시간
        final int WAIT_PER_ATTEMPT = 2000; // 시도 이후 대기 시간

        Socket tmpSocket = null;
        try {
            for(int i=0; i < MAX_ATTEMPTS; i++) {
                tmpSocket = new Socket();
                try {
                    tmpSocket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT);
                    break;
                } catch (UnknownHostException e) {
                    log.error("Unknown host : {}", host);
                    throw new RuntimeException("Unknown Host Detected.", e);
                } catch (SocketTimeoutException e) {
                    String msg = "Connection timed out after 5 seconds.";
                    log.error(msg, e);
                    throw new RuntimeException(msg, e);
                } catch (ConnectException e) {
                    log.error("connection failed : {} retry..", e.getMessage());
                } catch (SocketException e) {
                    log.fatal("Socket Related failed..", e);
                    throw new RuntimeException("Socket Related failed..", e);
                } catch (IOException e) {
                    String errMsg = "Socket IO Excpetion.";
                    log.error(errMsg, e);
                    throw new RuntimeException(errMsg, e);
                }
                Thread.sleep(WAIT_PER_ATTEMPT);
                if (i==(MAX_ATTEMPTS-1)) {
                    String errMsg = "Connection to host failed. Maybe server not working.";
                    log.fatal(errMsg);
                    throw new RuntimeException(errMsg);
                }
            }
        } catch (InterruptedException e) {
            log.fatal("Thread Interrupted while Initializing Networkhandler.");
            throw new RuntimeException(e);
        }


        try {
            this.socket = tmpSocket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            log.info("Connection established with {}:{}", host, port);
        } catch (IOException e) {
            String msg = "I/O error during connection to " + host + ":" + port;
            log.fatal(msg, e);
            throw new RuntimeException(msg, e);
        }

    }



    // 클라이언트 네트워크 핸들러의 메인 로직
    // 서버로부터 데이터 송신을 대기하고, 송신을 받으면 그에 따른 로직을 처리한다.
    // 스레드를 통해 실행할 때, 처음 실행되는 지점이다.
    @Override
    public void run() {
        log.info("Client Thread Running...");



        while (!Thread.currentThread().isInterrupted()) {
            log.info("message Receiving...");  // 대기

            Object msg = null;

            try {
                msg = in.readObject(); // 오브젝트 읽기
            } catch (EOFException e) {
                String errMsg;
                if (e.getMessage() == null) errMsg = "Socket closed from server.";
                else errMsg ="readObject() failed";

                log.fatal(errMsg, e);
                throw new RuntimeException(e);
            } catch (Exception e) {
                String errMsg = "Socket error Received.";
                log.fatal(errMsg, e);
                throw new RuntimeException(e);
            }

            log.info("message Received : " + msg);  // Debugging the received object

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
                        listener.onCommandSuccess(command, DocMapper.toEntity((DocumentDto) dto.getPayload()));
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

                    case LOGIN:
                        listener.onCommandSuccess(command, UserMapper.toEntity((UserDto) dto.getPayload()));
                        break;

                    // 이외 명령
                    default:
                        String errMsg = "Unkown Command Detected.";
                        log.error(errMsg);
                        listener.onNetworkError(new IllegalArgumentException(errMsg));
                }

            } else {
                String errMsg = "Unknown DTO Received.";
                log.error(errMsg);
                listener.onNetworkError(new IllegalArgumentException(errMsg));

            }
        }
        log.info("Client Network Thread Interrupted.");
        shutdown();


    }


    // 명령어를 서버 측으로 전송한다.
    public <T> void sendCommand(ClientCommand command, T payload) {
        log.info("send Command to Server {} : {}", command, payload);



        Serializable payloadDto = null;

        try {
            // payload가 Serializable을 구현한 객체인지 확인한다.
            if (payload instanceof Serializable) {
                payloadDto = (Serializable) payload;
            } else {
                if (payload instanceof Document) {
                    log.info(payload);
                    payloadDto = DocMapper.toDto((Document) payload);
                } else if (payload instanceof Operation) {
                    payloadDto = OperationMapper.toDto((Operation) payload);
                } else if (payload instanceof LoginRequest) {
                    payloadDto = LoginRequestMapper.toDto((LoginRequest) payload);
                }
            }

            // RequestCommandDto 생성 및 서버 측에 전송
            RequestCommandDto<Serializable> dto = new RequestCommandDto<>(command, payloadDto);
            out.writeObject(dto);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    // Operation 요청 함수
//    public Operation requestOperation() {
//        try {
//            return OperationMapper.toEntity((OperationDto) in.readObject());
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    // Operation 전송 함수
//    public void sendOperation(Operation operation) {
//        try {
//            out.writeObject(OperationMapper.toDto(operation)); // 전송 버퍼에 로드
//            out.flush(); // 전송
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


    // Document 요청 함수
//    public Document requestDocument() {
//        try {
//            return DocMapper.toEntity((DocumentDto) in.readObject());
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }


    // 핸들러 종료 함수
    public void shutdown() {
        log.info("Network handler Shutting down.");
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException ignored) {}
    }
}
