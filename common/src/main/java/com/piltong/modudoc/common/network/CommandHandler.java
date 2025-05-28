package com.piltong.modudoc.common.network;

/**
 * 하나의 ClientCommand를 처리하고 결과를 반환하는 핸들러.
 * @param <T> 이 핸들러가 반환하는 결과 타입
 */
public interface CommandHandler<T> {
    T handle(CommandRequest request) throws CommandException;
}
