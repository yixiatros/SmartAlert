package com.example.backend;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DangerReviewer {

    private static final int MAX_DISTANCE = 70000;

    List<List<Integer>> ariaGroupList = new ArrayList<>();

    public List<List<Integer>> start(
            List<Double> longitudeList,
            List<Double> latitudeList,
            List<String> typeOfDangerList,
            List<LocalDateTime> timeOfEventList
    ) {

        ariaGroupList.add(new ArrayList<Integer>());
        ariaGroupList.get(0).add(0);

        for (int i = 1; i < longitudeList.size(); i++) {
            boolean newListFlag = true;
            for (List<Integer> integerList : ariaGroupList){
                boolean flag = false;
                for (Integer integer : integerList){
                    float[] distance = new float[1];
                    Location.distanceBetween(
                            latitudeList.get(i),
                            longitudeList.get(i),
                            latitudeList.get(integer),
                            longitudeList.get(integer),
                            distance
                    );

                    if (distance[0] < MAX_DISTANCE){
                        if (timeOfEventList.get(i).isAfter(timeOfEventList.get(integer).minusDays(1)) &&
                                timeOfEventList.get(i).isBefore(timeOfEventList.get(integer).plusDays(1))){
                            if (typeOfDangerList.get(i).equals(typeOfDangerList.get(integer))){
                                flag = true;
                                break;
                            }
                        }
                    }
                }

                if (flag) {
                    integerList.add(i);
                    newListFlag = false;
                    break;
                }
            }

            if (newListFlag){
                List<Integer> newIntegerList = new ArrayList<>();
                newIntegerList.add(i);
                ariaGroupList.add(newIntegerList);
            }
        }

        Collections.sort(ariaGroupList, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> t1, List<Integer> t2) {
                return Integer.valueOf(t2.size()).compareTo(t1.size());
            }
        });

        return ariaGroupList;
    }

    private void onDataChange() {
    }
}
