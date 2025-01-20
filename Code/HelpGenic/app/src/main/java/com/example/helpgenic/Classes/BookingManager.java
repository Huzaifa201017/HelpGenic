package com.example.helpgenic.Classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

public class BookingManager {

    DbHandler db;

    public void setDb(DbHandler db) {
        this.db = db;
    }


    public ArrayList<Slot> getAvailableSlots(String docId , Date dateSelected , String day , ArrayList<Slot> slots,Context context){

//        ArrayList<Slot> consumedSlots = db.getConsumedSlots(docId , dateSelected, context);
        ArrayList<Slot> consumedSlots =  new ArrayList<>();

        ArrayList<Slot> availableSlots = new ArrayList<>();

        boolean isPresent, onSameDay = false;


        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);

        Time t = new Time(date.getTime());


        if(Objects.equals(dateSelected.toString(),date.toString())){
            Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();
            onSameDay = true;
        }

        for (Slot slot: slots ){

            isPresent = false;

            if(onSameDay){
                if( this.compareTo(slot.sTime,t) <= 0){
                    continue;
                }
            }

            for (Slot cSlot: consumedSlots) {


                if (Objects.equals(slot.sTime , cSlot.sTime) && Objects.equals(slot.eTime ,cSlot.eTime) ){
                    isPresent = true;
                    break;
                }

            }
            if(!isPresent) {

                slot.day = day;
                availableSlots.add(slot);

            }


        }
        return availableSlots;
    }

    public ArrayList<Slot> makeSlots( Time sTime , Time eTime , String day){

        return func(sTime , eTime , day);
    }

    private Time add30ToTime(String time){
        int mm = Integer.parseInt(time.substring(3,5));
        int hh = Integer.parseInt(time.substring(0,2));


        mm += 30;
        if (mm == 60){
            mm = 0;
            hh += 1;
            if (hh == 24){
                hh = 0;
            }

        }
        Time t = Time.valueOf(hh +":"+ mm+":" +"00");
        return t;
    }

    private ArrayList<Slot> func(Time t1, Time t2 , String day) {

        ArrayList<Slot> slots = new ArrayList<>();
        Time temp;

        while(true){
            temp = add30ToTime(t1.toString());
            slots.add(new Slot(t1,temp , day));
            t1 = temp;
            if(Objects.equals(t1,t2)){
                break;
            }
        }


        return slots;
    }

    private int compareTo (Time t1 , Time t2) {

        int mm1 =  Integer.parseInt(t1.toString().substring(3,5));
        int hh1 = Integer.parseInt(t1.toString().substring(0,2));
        int mm2 =  Integer.parseInt(t2.toString().substring(3,5));
        int hh2 = Integer.parseInt(t2.toString().substring(0,2));

        if(hh1 > hh2){
            return 1;
        }else if (hh1 < hh2){
            return -1;

        }else {

            if(mm1 > mm2){
                return 1;
            }else if (mm1 < mm2){
                return -1;
            }else {
                return 0;
            }
        }



    }




    public Task<Boolean> confirmAppointment(String patientId , String docId , Date selectedDate, Slot slot, Context context) {

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        if(Objects.equals(slot , null)) {

            Toast.makeText(context, "No Slot selected !", Toast.LENGTH_SHORT).show();
            taskCompletionSource.setResult(false);

        } else {

            db.checkDuplicateAppointment(patientId , docId, selectedDate).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful() ){

                        if (task.getResult()){

                            Toast.makeText(context, "You have already got an appointment on this date !", Toast.LENGTH_SHORT).show();
                            taskCompletionSource.setResult(false);

                        }else{

                            db.loadAppointmentToDb(docId , patientId , selectedDate , slot.sTime , slot.eTime ).addOnCompleteListener(
                                    task2 -> {
                                        if (task2.isSuccessful() && task2.getResult() ){
                                            Toast.makeText(context, "Appointment Confirmed", Toast.LENGTH_SHORT).show();
                                            taskCompletionSource.setResult(true);
                                        }else{
                                            Toast.makeText(context, "Operation Failed, please try again later !", Toast.LENGTH_SHORT).show();
                                            taskCompletionSource.setResult(false);
                                        }
                                    }
                            );
                        }

                    }else{
                        taskCompletionSource.setResult(false);
                    }
                }
            );
        }

        return taskCompletionSource.getTask();

    }



    }
