package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {
    val realm = Realm.getDefaultInstance()           // 인스턴스 얻기
    val calendar: Calendar = Calendar.getInstance()  // 오늘 날짜로 캘린터 객체 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // 업데이트 조건
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            insertMode()    // id가 -1이면 추가모드
        } else {
            updateMode(id)  // 아니면 수정모드
        }

        // 캘린더 뷰의 날짜를 선택했을 때 Calendar 객체에 설정
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    // 추가 모드 초기화
    private fun insertMode() {
        deleteFab.visibility = View.GONE    // 삭제 버튼 감추기

        doneFab.setOnClickListener {        // 완료 버튼 클릭하면 추가
            insertTodo()
        }
    }

    // 수정 모드 초기화
    private fun updateMode(id: Long) {
        // id에 해당하는 객체를 화면에 표시
        val todo = realm.where<Todo>().equalTo("id", id).findFirst()!!
        todoEditText.setText(todo.title)
        calendarView.date = todo.date

        doneFab.setOnClickListener {    // 완료 버튼을 클릭하면 수정
            updateTodo(id)
        }
        deleteFab.setOnClickListener {    // 삭제 버튼을 클릭하면 삭제
            deleteTodo(id)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()   // 인스턴스 해제
    }



    // 날짜를 Long형으로 반환
    // val time: Long = calendar.timeInMillis


    // 할 일 추가
    private fun insertTodo(){
        realm.beginTransaction()    // 트랜젝션 시작

        val newItem = realm.createObject<Todo>(nextId())    // 새 객체 생성
        newItem.title = todoEditText.text.toString()
        newItem.date = calendar.timeInMillis

        realm.commitTransaction()   // 트랜직션 종료 반영

        alert("내용이 추가되었습니다."){
            yesButton{finish()}
        }.show()
    }

    // 다음 id를 반환
    private fun nextId(): Int{
        val maxId = realm.where<Todo>().max("id")
        if(maxId!=null){
            return maxId.toInt() + 1
        }
        return 0
    }


    // 할 일 수정
    private fun updateTodo(id: Long) {
        realm.beginTransaction()    // 트랜젝션 시작

        val updateItem = realm.where<Todo>().equalTo("id", id).findFirst()    // "id"컬럼에 id값이 있다면 첫 번째 데이터 반환

        // 값 수정
        updateItem!!.title = todoEditText.text.toString()
        updateItem.date = calendar.timeInMillis

        realm.commitTransaction()   // 트랜직션 종료 반영

        alert("내용이 변경되었습니다."){
            yesButton{finish()}
        }.show()
    }


    // 할 일 삭제
    private fun deleteTodo(id: Long) {
        realm.beginTransaction()    // 트랜젝션 시작

        val deleteTItem = realm.where<Todo>().equalTo("id", id).findFirst()    // "id"컬럼에 id값이 있다면 첫 번째 데이터 반환

        deleteTItem?.deleteFromRealm()   //삭제
        realm.commitTransaction()   // 트랜직션 종료 반영

        alert("내용이 삭제되었습니다."){
            yesButton{finish()}
        }.show()
    }
}
