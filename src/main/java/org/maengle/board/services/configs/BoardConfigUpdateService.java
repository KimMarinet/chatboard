package org.maengle.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.board.controllers.RequestBoard;
import org.maengle.board.entities.Board;
import org.maengle.board.repositories.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardConfigUpdateService {
    private final ModelMapper mapper;
    private final BoardRepository boardRepository;

    public void process(RequestBoard form) {

        Board item = mapper.map(form, Board.class);
        boardRepository.saveAndFlush(item);
    }
}