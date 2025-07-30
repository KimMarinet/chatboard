package org.maengle.chatbot.repositories;

import org.maengle.chatbot.entities.ChatData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatDataRepository extends JpaRepository<ChatData, Long>, QuerydslPredicateExecutor<ChatData> {
}
