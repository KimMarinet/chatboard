package org.maengle.board.exceptions;

import org.maengle.global.exceptions.NotFoundException;

public class BoardDataNotFoundException extends NotFoundException {
    public BoardDataNotFoundException() {
        super("NotFound.boardData");
    }
}
