package com.example.todolist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

// 할 일 데이터 클래스 작성
open class Todo(
    @PrimaryKey var id: Long = 0, var title: String = "", var date: Long = 0    // id는 유일한 값이 되어야 하기 때문에 기본키 제약 주석으로 추가
) : RealmObject(){

}

