package com.example.mypage;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class WatchDto implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String contId; // 콘텐츠 ID
    private String ty1Code; // 콘텐츠 타입 코드 (AR : AR 콘텐츠, VR : VR 콘텐츠, LB : 라이브 콘텐츠)
    private String contNm; // 콘텐츠 이름
    private String pchrgFreeCode; // 유료 코드 (F: 무료, C: 유료)
    private int price; // 가격
    private boolean isAdultCont; // 성인 콘텐츠 여부 (true : 성인콘텐츠, false : 일반콘텐츠)
    private String posterUrl; // 썸네일 url
    private boolean checkState; // 체크박스 상태값 체크 (true : 체크된 상태, false : 체크헤제 상태)

    public WatchDto(@NonNull String contId, String ty1Code, String contNm, String pchrgFreeCode, int price, boolean isAdultCont, String posterUrl) {
        this.contId = contId;
        this.ty1Code = ty1Code;
        this.contNm = contNm;
        this.pchrgFreeCode = pchrgFreeCode;
        this.price = price;
        this.isAdultCont = isAdultCont;
        this.posterUrl = posterUrl;
        checkState = false;
    }

    @NonNull
    public String getContId() {
        return contId;
    }
    public String getTy1Code() {
        return ty1Code;
    }
    public String getContNm() {
        return contNm;
    }
    public String getPchrgFreeCode() {
        return pchrgFreeCode;
    }
    public int getPrice() {
        return price;
    }
    public boolean getIsAdultCont() {
        return isAdultCont;
    }
    public String getPosterUrl() {
        return posterUrl;
    }
    public Boolean getCheckState() { return checkState; }

    public void setContId(@NonNull String contId) {
        this.contId = contId;
    }
    public void setTy1Code(String ty1Code) {
        this.ty1Code = ty1Code;
    }
    public void setContNm(String contNm) {
        this.contNm = contNm;
    }
    public void setPchrgFreeCode(String pchrgFreeCode) {
        this.pchrgFreeCode = pchrgFreeCode;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setIsAdultCont(boolean isAdultCont) {
        this.isAdultCont = isAdultCont;
    }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    public void setCheckState(Boolean checkState) { this.checkState = checkState; }

    @Override
    public String toString() {
        return "WatchDto{" +
                "contId='" + contId + '\'' +
                ", ty1Code='" + ty1Code + '\'' +
                ", contNm='" + contNm + '\'' +
                ", pchrgFreeCode='" + pchrgFreeCode + '\'' +
                ", price=" + price +
                ", isAdultCont=" + isAdultCont +
                ", posterUrl='" + posterUrl + '\'' +
                ", checkState=" + checkState +
                '}';
    }
}
