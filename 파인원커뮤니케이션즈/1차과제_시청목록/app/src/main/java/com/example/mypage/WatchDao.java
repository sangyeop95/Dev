package com.example.mypage;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WatchDao {
    @Insert(onConflict = REPLACE) // onConflict : id 가 같은 것이 있으면 데이터 덮어쓰기
    void insert(WatchDto watchDto);

    @Query("SELECT * FROM WatchDto") // 데이터 전체를 리스트<Object : WatchDto>형태로 출력
    List<WatchDto> getAll();

    @Query("SELECT contNm FROM WatchDto") // 데이터 전체를 리스트<String : 콘텐츠이름>형태로 출력
    List<String> getAllContentName();

    @Query("DELETE FROM WatchDto WHERE contNm = :contentName") // 콘텐츠이름과 동일한 데이터 삭제
    void deleteContentByContNm(String contentName);

    @Query("SELECT * FROM WatchDto WHERE ty1Code = :contentType") // 콘텐츠타입과 동일한 데이터를 리스트형태로 출력
    List<WatchDto> searchByContentType(String contentType);

    @Query("SELECT * FROM WatchDto WHERE isAdultCont = 0 AND ty1Code = :contentType") // 콘텐츠타입과 동일한 데이터를 리스트형태로 출력 (성인타입 콘텐츠 X)
    List<WatchDto> searchByContentTypeIsNotAdult(String contentType);

    @Query("SELECT * FROM WatchDto WHERE isAdultCont = :isAdult") // 성인(boolean)타입과 동일한 데이터를 리스트<Object : WatchDto>형태로 출력 -> 0 = false, 1 = true
    List<WatchDto> searchByIsAdult(String isAdult);

    @Query("SELECT contNm FROM WatchDto WHERE isAdultCont = :isAdult") // 성인(boolean)타입과 동일한 데이터를 리스트<String : 콘텐츠이름>형태로 출력 -> 0 = false, 1 = true
    List<String> searchAdultContentName(String isAdult);

}
