package com.bit.springboard.entity;

import lombok.Data;

import java.io.Serializable;

@Data
//IdClass는 항상 Serializable을 상속받아야 한다.
public class BoardFileId implements Serializable {
    //IdClass에서 참조할 키 변수명은 원본 엔티티의 키 변수명과 일치시킨다.
    //자료형은 테이블에 실제로 들어가 타입으로 지정
    private int board;
    private int boardFileNo;
}
