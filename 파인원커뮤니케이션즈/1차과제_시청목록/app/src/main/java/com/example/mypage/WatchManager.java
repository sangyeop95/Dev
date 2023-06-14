package com.example.mypage;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class WatchManager {
    private ArrayList<WatchDto> list = new ArrayList<>();

    private static class WatchManagerHolder {
        public static final WatchManager INSTANCE = new WatchManager();
    }

    public static WatchManager getInstance() {
        return WatchManager.WatchManagerHolder.INSTANCE;
    }

    public ArrayList<WatchDto> getListByPage(int page, int pageSize) {

        ArrayList<WatchDto> result = new ArrayList<>();

        if (page == 0 || pageSize == 0 || ((page - 1) * pageSize) >= list.size())
            return result;

        int startIndex = (page - 1) * pageSize;
        int endIndex = (list.size() % (page * pageSize)) == list.size() ? list.size() : (page * pageSize);

        for (int i = startIndex; i < endIndex; i++) {
            WatchDto watchDto = list.get(i);
            result.add(new WatchDto(
                    watchDto.getContId(),
                    watchDto.getTy1Code(),
                    watchDto.getContNm(),
                    watchDto.getPchrgFreeCode(),
                    watchDto.getPrice(),
                    watchDto.getIsAdultCont(),
                    watchDto.getPosterUrl()));
        }

        return result;
    }

    public ArrayList<WatchDto> getList() {
        ArrayList<WatchDto> result = new ArrayList<>();
        for (WatchDto watchDto : list) {
            result.add(new WatchDto(
                    watchDto.getContId(),
                    watchDto.getTy1Code(),
                    watchDto.getContNm(),
                    watchDto.getPchrgFreeCode(),
                    watchDto.getPrice(),
                    watchDto.getIsAdultCont(),
                    watchDto.getPosterUrl()));
        }

        return result;
    }

    public boolean deleteById(String contId) {
        return list.removeIf(watchDto -> TextUtils.equals(watchDto.getContId(), contId));
    }

    public boolean deleteAll() {
        list.clear();
        return true;
    }

    public void initList() {
        list.clear();
        list.add(new WatchDto("PR2019030400020001", null, "3대 폭포 제목길게~제목길게~제목길게~제목길게~제목길게~제목길게~제목길", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/ETC/202111/20211115174747802_0.jpg"));
        list.add(new WatchDto("CO20215000001791", null, "AR Sniper", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/ETC/202112/20211213135158867_0.png"));
        list.add(new WatchDto("CG20215000000066", "AR", "AR 묶음 컨텐츠 그룹", "F", 0, true, "https://d1szs72hycmioq.cloudfront.net/service/CONT_GRP/202111/20211118105328973_0.png"));
        list.add(new WatchDto("CG20225000000223", "AR", "AR 묶음 혼재", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT_GRP/202202/20220223175043078_0.jpeg"));
        list.add(new WatchDto("M0120AU156PPV00", "VR", "EXO 카이의 Vacation", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/MIG/BC/poster/M0120AU156PPV00MD130.png"));
        list.add(new WatchDto("CO20225000003762", "VR", "IVE - ELEVEN (더쇼 284회)_언어테스트", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/VR/202201/2022012514243254705_1.jpeg"));
        list.add(new WatchDto("CO20225000008982", "VR", "IVE - LOVE DIVE (DRM X)", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/VR/202205/2022053009245744759_0.png"));
        list.add(new WatchDto("CO20215000000911", "VR", "NCT127 버킷리스트 - 태용/마크", "C", 1000, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/VR/202111/2021110513244329018_0.jpg"));
        list.add(new WatchDto("M0120AQ259PPV00", "VR", "SUPER 3 Lamborghini Huracan 1화", "C", 700, true, "https://d1szs72hycmioq.cloudfront.net/service/MIG/BC/poster/M0120AQ259PPV00MD130.png"));
        list.add(new WatchDto("CO20215000000230", null, "WEB링크형테스트이거제목길게~길게~길게~길게~길게~길게~길게~길게~길게~", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/ETC/202108/20210819204113201_8.png"));
        list.add(new WatchDto("CO20225000003465", null, "[여행이온다]맛이온다1편", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/VRVP/202201/2022012016243857955_2.png"));
        list.add(new WatchDto("WT2020031200020009", null, "[유미의세포들(완결)]신순록편6", "F", 0, true, "https://d1szs72hycmioq.cloudfront.net/service/MIG/VR/service/WT/WT2020031200020009.IMG.1.jpg"));
        list.add(new WatchDto("CO20215000000944", null, "극한캐치볼", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/ETC/202111/20211115110548827_0.png"));
        list.add(new WatchDto("PR2019030400090001", null, "네팔 에베레스트", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/ETC/202111/20211115180617540_0.jpg"));
        list.add(new WatchDto("CO13076626312634", "AR", "돌아온 홀맨 감정티콘 5", "F", 0, true, "https://d1szs72hycmioq.cloudfront.net/service/CONT/AR/202203/20220317163840653_0.png"));
        list.add(new WatchDto("M01194B540PPV00", "VR", "모모랜드 - I`m so hot  (더쇼 182회)", "C", 15000, false, "https://d1szs72hycmioq.cloudfront.net/service/MIG/BC/poster/M01194B540PPV00M6230.png"));
        list.add(new WatchDto("CO20215000002073", "LB", "방송시작 전_무료(유료로 잠시만)", "C", 200, true, "https://d1szs72hycmioq.cloudfront.net/service/CONT/LBVR/202112/20211221163029779_5.jpeg"));
        list.add(new WatchDto("M012163584PPV00", "VR", "브레이브걸스-운전만해(더쇼259회)", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/MIG/BC/poster/M012163584PPV00MD130.png"));
        list.add(new WatchDto("M01217N165PPV00", "VR", "브룩에버스의촬영장비하인드", "F", 0, true, "https://d1szs72hycmioq.cloudfront.net/service/MIG/BC/poster/M01217N165PPV00MD130.png"));
        list.add(new WatchDto("CO20225000005559", "VR", "비투비영상테스트", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/VR/202203/2022031510241220058_7.png"));
        list.add(new WatchDto("CO13063623128242", "AR", "뽀로로와공룡여행3", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/AR/202203/20220329131551406_0.png"));
        list.add(new WatchDto("M01193L183PPV00", "VR", "사랑스런신재은1화", "F", 0, true, "https://d1szs72hycmioq.cloudfront.net/service/MIG/BC/poster/M01193L183PPV00MD230.png"));
        list.add(new WatchDto("M01213J224PPV00", "VR", "서울스프링실내악콘서트3화", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/MIG/BC/poster/M01213J224PPV00MD130.png"));
        list.add(new WatchDto("CG20215000000064", null, "손나은스타데이트시리즈", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT_GRP/202111/20211116202348913_0.jpg"));
        list.add(new WatchDto("CG20225000000233", null, "영상종류별로시리즈물", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT_GRP/202203/20220317142003703_0.png"));
        list.add(new WatchDto("WT2019030400040001", null, "옥수역 귀신 제목 길게제목 길게제목 길게제목 길게제목 길게제목 길게제", "F", 0, true, "https://d1szs72hycmioq.cloudfront.net/service/CONT/ETC/202112/20211230103830902_0.jpeg"));
        list.add(new WatchDto("CO20215000002193", "LB", "유료1 (인앱)", "C", 1200, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/LBVR/202112/20211224112322812_3.jpg"));
        list.add(new WatchDto("CO20215000000361", "VR", "유료1~제목길게~제목길게~제목길게~제목길게~제목길게~제목길게(인앱 only)~~", "C", 900, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/VR/202109/2021090315241901112_2.jpg"));
        list.add(new WatchDto("CO20215000002197", "LB", "유료4 (복합) 제목변경", "C", 2300, true, "https://d1szs72hycmioq.cloudfront.net/service/CONT/LBVR/202112/20211224112900665_4.jpg"));
        list.add(new WatchDto("PR2019030400050001", null, "이집트", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/ETC/202203/20220323111013140_0.jpeg"));
        list.add(new WatchDto("CO20215000002079", "LB", "인앱과쿠폰", "C", 1000, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/LBVR/202112/20211221175532231_7.jpg"));
        list.add(new WatchDto("M012032275PPV00", "VR", "청하, 청하를 말하다 2화", "F", 0, true, "https://d1szs72hycmioq.cloudfront.net/service/MIG/BC/poster/M012032275PPV00MD130.png"));
        list.add(new WatchDto("WT201908190001", null, "타인은 지옥이다(완결) 2줄로 넘어가게 이름 길게길게 하겠습니다. 타인은", "F", 0, true, "https://d1szs72hycmioq.cloudfront.net/service/CONT_GRP/202111/20211119181000716_0.jpg"));
        list.add(new WatchDto("CO20215000001265", "AR", "통통이 댄스댄스댄스", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT/AR/202112/2021120615245040805_5.png"));
        list.add(new WatchDto("CG20225000000213", null, "파노라마 묶음", "F", 0, false, "https://d1szs72hycmioq.cloudfront.net/service/CONT_GRP/202201/20220116150548998_0.jpg"));
    }
}
