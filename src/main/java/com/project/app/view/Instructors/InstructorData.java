package Instructors;

import javax.swing.*;
import java.util.ArrayList;

public class InstructorData {
    String Introduction="강의 소개글";
    String imagepath="src/Instructors/Person.png";
    String name="홍길동";
    String subject="수학";
    double star=5.0;

    String[] lectureList={"[기초] 확률과 통계", "수능 수학 기초 다지기", "[심화] 확률과 통계","강의평"
            ,"강의평","강의평","강의평","강의평","강의평","강의평","강의평"};

//    public InstructorData(String Introduction, String imagepath,String name, double star){
//        this.Introduction=Introduction;
//        this.imagepath=imagepath;
//        this.name=name;
//        this.star=star;
//    }
    public String getIntroduction(){
        return Introduction;
    }

    public ImageIcon getImage(){
        return new ImageIcon(imagepath);
    }
    public String getName(){
        return name;
    }
    public double getStar(){
        return star;
    }
    public String[] getLectureList(){
        return lectureList;
    }
}
