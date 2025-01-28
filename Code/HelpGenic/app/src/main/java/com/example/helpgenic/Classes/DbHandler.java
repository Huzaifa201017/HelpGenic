package com.example.helpgenic.Classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mysql.jdbc.CallableStatement;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DbHandler {

    private Connection connection = null;

    private CallableStatement cs = null;

    String url="jdbc:mysql://sda.mysql.database.azure.com:3306/helpgenic?useSSL=true&loginTimeout=30";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public boolean  connectToDb(Context ptr) {
        db = FirebaseFirestore.getInstance();
        return true;
    }

    public void closeConnection() throws SQLException {
        db = null;
    }





    public void insertPatientDetailsInDb(String id, String name , char gender , Date dob, String bloodGroup , String phNum){

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("gender",  String.valueOf(gender));
        user.put("dob", dob);
        user.put("bloodGroup", bloodGroup);
        user.put("phoneNum", phNum);
        user.put("type", "P");


        db.collection("Users").document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DbHandler: insertPatientDetailsInDb", "DocumentSnapshot (user) successfully written with ID: " + id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("DbHandler: insertPatientDetailsInDb", "Error writing document", e);
            }
        });


    }

    public void insertDoctorDetailsInDb(String id, String name , Date dob , String phNum, String specialization, char gender, boolean isSurgeon, byte [] degreeImage ) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String path = "degrees/" + id + ".jpg";
        StorageReference imageRef = storageRef.child(path);

        UploadTask uploadTask = imageRef.putBytes(degreeImage);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("DbHandler: insertDoctorDetailsInDb", "Image upload failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.d("DbHandler: insertDoctorDetailsInDb", "Image upload Success");

                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                user.put("gender", String.valueOf(gender));
                user.put("dob", dob);
                user.put("phoneNum", phNum);
                user.put("specialization", specialization);
                user.put("surgeon", isSurgeon);
                user.put("verified", false);
                user.put("rating", 4.3);
                user.put("ratingCount", 1);
                user.put("type", "D");


                db.collection("Users").document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DbHandler: insertDoctorDetailsInDb", "DocumentSnapshot (user) successfully written with ID: " + id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DbHandler: insertDoctorDetailsInDb", "Error writing document", e);
                    }
                });



            }
        });


    }

    public Task<DocumentSnapshot> getUserDetails(String uID) {

        TaskCompletionSource<DocumentSnapshot> taskCompletionSource = new TaskCompletionSource<>();
        DocumentReference docRef = db.collection("Users").document(uID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    Log.d("Db Handler: getUserDetails", "DocumentSnapshot data: " + document.getData());
                    taskCompletionSource.setResult(document);

                } else {
                    Log.d("Db Handler: getUserDetails", "No relevant document found");
                    taskCompletionSource.setResult(null);
                }
            } else {
                Log.d("Db Handler: getUserDetails", "get failed with ", task.getException());
                taskCompletionSource.setResult(null);
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<ArrayList<Doctor>> getUnVerifiedDocs() {

        TaskCompletionSource<ArrayList<Doctor>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Doctor> docList = new ArrayList<>();
        db.collection("Users").whereEqualTo("verified", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot userDocument : task.getResult()) {
                        String id = userDocument.getId();
                        String name = userDocument.getString("name");
                        String specialization = userDocument.getString("specialization");

                        docList.add(new Doctor(name,specialization,id));

                    }

                    taskCompletionSource.setResult(docList);
                } else {
                    Log.d("Db Handler: getUnVerifiedDocs", "Error getting documents: ", task.getException());
                    taskCompletionSource.setResult(null);
                }
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<byte[]> getDocument(String documentId) {

        TaskCompletionSource<byte[]> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String path = "degrees/" + documentId + ".jpg";
        StorageReference imageRef = storageRef.child(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                taskCompletionSource.setResult(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                taskCompletionSource.setResult(null);
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<Boolean> updateDoctorVerifyStatus(String documentId){

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        DocumentReference docRef = db.collection("Users").document(documentId);
        docRef.update("verified", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Db Handler: updateDoctorVerifyStatus", "DocumentSnapshot successfully updated!");
                taskCompletionSource.setResult(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Db Handler: updateDoctorVerifyStatus", "Error updating document", e);
                taskCompletionSource.setResult(false);
            }
        });

        return taskCompletionSource.getTask();

    }

    public Task<Boolean> removeDoctor(String documentId) {

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        try{
            db.collection("Users").document(documentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Db Handler: removeDoctor", "DocumentSnapshot successfully deleted!");

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    String path = "degrees/" + documentId + ".jpg";
                    StorageReference imageRef = storageRef.child(path);

                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Db Handler: removeDoctor", "Image successfully deleted!");
                            taskCompletionSource.setResult(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("Db Handler: removeDoctor", "Error deleting image", exception);
                            taskCompletionSource.setResult(false);

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Db Handler: removeDoctor", "Error deleting document", e);
                    taskCompletionSource.setResult(false);
                }
            });


        }catch (Exception e){
            Log.w("Db Handler: removeDoctor", "Error removing doctor", e);
            taskCompletionSource.setResult(false);
        }


        return taskCompletionSource.getTask();
    }

    public boolean isConnectionOpen(){
        return !Objects.equals(db, null);
    }

    public Task<ArrayList<Doctor>> getListOfDoctors(){

        TaskCompletionSource<ArrayList<Doctor>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Doctor> docList = new ArrayList<>();
        db.collection("Users").whereEqualTo("verified", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot userDocument : task.getResult()) {


                        String id = userDocument.getId();
                        String name = userDocument.getString("name");
                        String specialization = userDocument.getString("specialization");
                        char gender = Objects.requireNonNull(userDocument.getString("gender")).charAt(0);
                        float rating = Objects.requireNonNull(userDocument.getDouble("rating")).floatValue();
                        boolean isSurgeon = Objects.requireNonNull(userDocument.getBoolean("surgeon"));
                        int fee = Objects.requireNonNull(userDocument.getLong("fee")).intValue();
                        String email = userDocument.getString("email");

                        if(isSurgeon){
                            specialization += ", Surgeon";
                        }

                        docList.add(new Doctor(id ,name, specialization, gender , rating, isSurgeon, fee, email));

                    }

                    taskCompletionSource.setResult(docList);
                } else {
                    Log.d("Db Handler: getListOfDoctors", "Error getting documents: ", task.getException());
                    taskCompletionSource.setResult(null);
                }
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<Boolean> setDoctorAptScheduleDetails(Doctor d) {

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        DocumentReference docRef = db.collection("Users").document(d.getId());

        ArrayList<VirtualAppointmentSchedule> vAptSchedules = new ArrayList<>();
        ArrayList<PhysicalAppointmentSchedule> pAptSchedules = new ArrayList<>();

        Task<QuerySnapshot> vAptScheduleTask = docRef.collection("vApt Schedules").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot userDocument : task.getResult()) {

                        String id = userDocument.getId();
                        String day = userDocument.getString("day");
                        Time eTime = new Time(Objects.requireNonNull(userDocument.getDate("eTime")).getTime());
                        Time sTime = new Time(Objects.requireNonNull(userDocument.getDate("sTime")).getTime());

                        vAptSchedules.add(new VirtualAppointmentSchedule(id, day, sTime, eTime));

                    }

                    d.setVSch(vAptSchedules);

                } else {
                    Log.d("Db Handler: getUserDetails", "get failed with ", task.getException());
                }

            }
        });


        Task<QuerySnapshot> pAptScheduleTask = docRef.collection("pApt Schedules").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot userDocument : task.getResult()) {

                        String day = userDocument.getString("day");
                        Time eTime = new Time(Objects.requireNonNull(userDocument.getDate("eTime")).getTime());
                        Time sTime = new Time(Objects.requireNonNull(userDocument.getDate("sTime")).getTime());
                        String clinicName = userDocument.getString("clinicName");
                        String assistantPh = userDocument.getString("assistantPhNum");
                        Double latts = userDocument.getDouble("latts");
                        Double longs = userDocument.getDouble("longs");
                        String id = userDocument.getId();

                        pAptSchedules.add(new PhysicalAppointmentSchedule(id, clinicName,latts, longs, assistantPh, day, sTime, eTime));

                    }

                    d.setPSchedule(pAptSchedules);

                } else {
                    Log.d("Db Handler: getUserDetails", "get failed with ", task.getException());
                }

            }
        });


        // Wait for both tasks to complete
        Tasks.whenAll(vAptScheduleTask, pAptScheduleTask).addOnCompleteListener(allTasks -> {

            if (allTasks.isSuccessful()) {
                // Both tasks succeeded
                taskCompletionSource.setResult(true);
            } else {
                // At least one task failed
                Log.d("DbHandler", "One or both tasks failed", allTasks.getException());
                taskCompletionSource.setResult(false);
            }
        });

        return taskCompletionSource.getTask();
    }

    public ArrayList<Slot> getConsumedSlots(int docId , Date date ,Context context ){

        ArrayList<Slot> list = new ArrayList<>();

        String query = "select * from Appointment where docId= ? and `date`= ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);
//            stmt.setDate(2,date);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                Time sTime = rs.getTime("sTime");
                Time eTime = rs.getTime("eTime");

                list.add(new Slot(sTime , eTime ,null));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return list;
    }


    public Task<Boolean> checkDuplicateAppointment(String docId , String patientId , Date date){

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        taskCompletionSource.setResult(false);

        db.collection("Appointments")
                .whereEqualTo("docId", docId)
                .whereEqualTo("patientId", patientId)
                .whereEqualTo("date", date).get().addOnCompleteListener( task -> {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot ignored : task.getResult()) {
                            taskCompletionSource.setResult(true);
                        }
                    } else {
                        Log.d("Db Handler: getListOfDoctors", "Error getting documents: ", task.getException());
                        taskCompletionSource.setException(Objects.requireNonNull(task.getException()));
                    }
                });

        return taskCompletionSource.getTask();

    }

    public Task<Boolean> loadAppointmentToDb(Appointment newApt) {

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        try {

            Map<String, Object> appointment = new HashMap<>();
            appointment.put("docId", newApt.getDoc().getId());
            appointment.put("docName", newApt.getDoc().getName());
            appointment.put("docSpecialization", newApt.getDoc().getSpecialization());
            appointment.put("docSTime", newApt.getDoc().getvSchedule().get(0).getsTime());
            appointment.put("docETime", newApt.getDoc().getvSchedule().get(0).geteTime());
            appointment.put("patientId", newApt.getPatient().getId());
            appointment.put("date", newApt.getAppDate());
            appointment.put("sTime", newApt.getsTime());
            appointment.put("eTime", newApt.geteTime());


            db.collection("Appointments").document().set(appointment).addOnSuccessListener(aVoid -> {
                Log.d("DbHandler: loadAppointmentToDb", "DocumentSnapshot (Appointment) successfully written with ID: " );
                taskCompletionSource.setResult(true);
            }).addOnFailureListener(e -> {
                Log.w("DbHandler: loadAppointmentToDb", "Error writing document", e);
                taskCompletionSource.setResult(false);
            });


        } catch (Exception e) {
            Log.w("DbHandler: loadAppointmentToDb", "Error writing document", e);
            taskCompletionSource.setResult(false);
        }

        return taskCompletionSource.getTask();

    }

    public Task<ArrayList<Appointment>> getUpcommingAppointmentsForPatients(String pId,Context context){

        TaskCompletionSource<ArrayList<Appointment>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Appointment> upcomingApp = new ArrayList<>();

        try {

            // Fetching current date
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Timestamp currentTimestamp = new Timestamp(calendar.getTime());

            // Fetching current time
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String currentTimeString = timeFormat.format(new Date());
            Time currentTime = java.sql.Time.valueOf(currentTimeString);

            db.collection("Appointments")
                    .whereEqualTo("patientId", pId)
                    .whereGreaterThanOrEqualTo("date", currentTimestamp ).get().addOnCompleteListener( task -> {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {


                        Date eTimeDate = documentSnapshot.getDate("eTime");
                        String eTimeString = timeFormat.format(eTimeDate);
                        Time eTime = java.sql.Time.valueOf(eTimeString);

                        if (eTime.after(currentTime)) {

                            String id = documentSnapshot.getId();
                            String name = documentSnapshot.getString("docName");
                            String specialization = documentSnapshot.getString("docSpecialization");
                            Date aptDate = documentSnapshot.getDate("date");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String formattedDate = dateFormat.format(aptDate);

                            Date finalFormattedDate = null;
                            try {
                                finalFormattedDate = dateFormat.parse(formattedDate);
                            } catch (Exception e) {
                                Log.d("Db Handler: getListOfDoctors", "Error while formatting the date: ");
                            }

                            Time sTime = new Time(Objects.requireNonNull(documentSnapshot.getDate("sTime")).getTime());


                            Doctor d = new Doctor(name, specialization, id);
                            upcomingApp.add(new Appointment(finalFormattedDate, formattedDate, d, sTime, eTime, documentSnapshot.getId()));
                        }

                    }

                    taskCompletionSource.setResult(upcomingApp);
                } else {
                    Log.d("Db Handler: getListOfDoctors", "Error getting documents: ", task.getException());
                    taskCompletionSource.setResult(upcomingApp);
                }
            });


        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            taskCompletionSource.setResult(upcomingApp);
        }



        return taskCompletionSource.getTask();
    }


    public void updateAppointmentInDatabase(int appointmentId , Time sTime , Time eTime ,Context context){
        String query = "update Appointment set sTime=? , eTime=? where appId=? ";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {


            stmt.setTime(1,sTime);
            stmt.setTime(2,eTime);
            stmt.setInt(3,appointmentId);



            stmt.execute();

            Toast.makeText(context, "Updated Successfully !", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void InsertComment(int pId, int dId, String comment,Context context) {
        String query = "call insertComment(?,?,?)";

        try {

            if (connection != null) {
                cs = (CallableStatement) this.connection.prepareCall(query);
                cs.setInt(1, pId);
                cs.setInt(2, dId);
                cs.setString(3, comment);
                cs.executeQuery();
            }

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void updateRating( int docId ,  float rating , Context context){
        String query = "call updateRating(?,?)";

        try {

            if (connection != null) {
                cs = (CallableStatement) this.connection.prepareCall(query);
                cs.setFloat(1, rating);
                cs.setInt(2, docId);
                cs.executeQuery();
            }

        } catch (Exception e) {


            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void cancelAppointment(int appId , Context context){
        String query = "Delete from appointment where appid = ?";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,appId);
            stmt.execute();
            Toast.makeText(context, "Cancelled Successfully !", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public String loadDoctorDegreeImageFromDb(int docId , Context context){

        String query = "select degree from Doctor where docId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();
            String degree = "";

            while (rs.next()) {
                degree = rs.getString("degree");
            }

           return degree;

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public ArrayList<Appointment> getUpcommingAppointmentsForDoctor(int docId,Context context)
    {
        ArrayList<Appointment> upcomingApp = new ArrayList<>();
        String query = "select * from upcomingAppointmentsForDoctor where docId = ?";


        try ( PreparedStatement stmt = connection.prepareStatement(query) ) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {


                Appointment ap = new Appointment(rs.getDate("date")  , null,new Patient(rs.getString("pid"), rs.getString("name")) , rs.getTime("sTime") , rs.getTime("eTime") , 0);
                upcomingApp.add(ap);


            }


            return upcomingApp;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public Patient getPatientInfo(int patientId, Context context){
        String query = "select * from getPatientInfo where pid=?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,patientId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                return new Patient(rs.getString("name") ,rs.getString("email") , rs.getString("phoneNum") ,rs.getString("bloodGroup") ,rs.getInt("age")  ,rs.getBoolean("gender") );
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }


    public ArrayList<Appointment> getPreviousAppointmentInfo(int docId , int patientId , Context context){
        String query = "select * from getPreviousAppointmentsInfo where pid=? and docId = ?";

        ArrayList<Appointment> prevAppointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,patientId);
            stmt.setInt(2,docId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                prevAppointments.add(new Appointment(rs.getDate("date"), null , null,null,null,rs.getInt("appId")));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return prevAppointments;
    }

    public void loadPrescriptionToDb(int aptId ,Prescription p , Context context) {{
        String query = "insert into Prescripton values(?,?)";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,aptId);
            stmt.setInt(2,p.getDays());

            stmt.execute();



            String query1 = "insert into medicineDosage values(?,?,?,?,?,?)";

            try (PreparedStatement stmt1 = connection.prepareStatement(query1)) {
                stmt1.setInt(5,aptId);

                for (MedicineDosage m: p.getMedicines()){
                    stmt1.setString(1,m.getMedicineName());
                    stmt1.setInt(2,m.getMedicineDosage());
                    stmt1.setBoolean(3,m.isMorning());
                    stmt1.setBoolean(4,m.isEvening());
                    stmt1.setBoolean(6,m.isAfternoon());


                    stmt1.execute();
                }

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }}


    public ArrayList<Prescription> getPrescriptionInfo(int aptId , Context context){
        String query = "select * from Prescripton p join medicineDosage m on p.presId = m.presId where p.presId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,aptId);

            ResultSet rs = stmt.executeQuery();


            Prescription p = new Prescription();

            while (rs.next()) {

                p.addMedicine(new MedicineDosage(rs.getString("name") , rs.getInt("dosage"), rs.getBoolean("morning") , rs.getBoolean("afternoon"), rs.getBoolean("night")));
                p.setDays(rs.getInt("days"));

            }

            if(p.getMedicines().size() != 0){
                ArrayList<Prescription> temp = new ArrayList<>();
                temp.add(p);
                return temp;
            } else{
                return null;
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;

    }


    public void loadDocumentToDb(int aptId , String document, Context context){
        String query1 = "insert into Document (appId , documentBinary) values(?,?)";

        try (PreparedStatement stmt1 = connection.prepareStatement(query1)) {

            stmt1.setInt(1,aptId);
            stmt1.setString(2,document);
            stmt1.execute();


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Document> getDocumentsFromDb(int aptId , Context context){

        String query = "select * from Document where appId = ?";

        ArrayList<Document> documents = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,aptId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                documents.add(new Document(rs.getInt("documentId") , null));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return documents;
    }

    public String getPatientDocumentsFromDb(int documentId , Context context){

        String query = "select documentBinary from Document where documentId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,documentId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                return rs.getString("documentBinary");
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public ArrayList<Patient> getPreviousPatientsAttended(String docId , Context context){

        ArrayList<Patient> prevPatients = new ArrayList<>();

        String query = "select DISTINCT * from getPreviousPatients where docId = ?";


        try ( PreparedStatement stmt = connection.prepareStatement(query) ) {

            stmt.setString(1,docId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {


                Patient ap = new Patient(rs.getString("pid"), rs.getString("name"));
                prevPatients.add(ap);


            }


            return prevPatients;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public ArrayList<Doctor> getPreviousDoctorsMet(int patientID , Context context){

        ArrayList<Doctor> list = new ArrayList<>();

        String query = "select DISTINCT * from getPreviousDoctors  where pId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query) ) {


            stmt.setInt(1,patientID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String id = rs.getString("docId");
                String docName = rs.getString("name");
                String email = rs.getString("email");
                boolean isGender = rs.getBoolean("gender");
                float rating = rs.getFloat("rating");


                char gender;
                if (isGender){
                    gender = 'M';
                }else{
                    gender = 'F';
                }

                String specialization = rs.getString("specialization");
                boolean isSurgeon = rs.getBoolean("surgeon");

                if(isSurgeon){
                    specialization += ", Surgeon";
                }

                list.add(new Doctor(id , email,specialization ,isSurgeon , null ,docName, gender,null, rating));

            }

            return list;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        return list;

    }

    public Task<Boolean> updateFee(String docId , int fee,Context context){

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        DocumentReference docRef = db.collection("Users").document(docId);
        docRef.update("fee", fee).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                taskCompletionSource.setResult(true);
                Log.d("Db Handler: updateFee", "DocumentSnapshot successfully updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                taskCompletionSource.setResult(false);
                Log.w("Db Handler: updateFee", "Error updating document", e);
            }
        });

        return taskCompletionSource.getTask();

    }



    public ResultSet get_doctors_and_prev_patients(Context context){
        ResultSet rs =null;
        try{
            Statement st= connection.createStatement();
            rs = st.executeQuery("Select user.name,t.pAttended from user inner join (select distinct Docid,count(*) as pAttended from getpreviouspatients \n" +
                    "group by docid)as t on user.uid=t.docid");
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rs;
    }


    public ResultSet getAllDocRating(Context context){
        ResultSet rs=null;
        try {
            if(connection!=null){
                Statement statement = connection.createStatement();
                rs = statement.executeQuery("select user.name,doctor.rating from doctor inner join user on user.uid=doctor.docid");
            }
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rs;
    }


    public void setVerified(Context context,int docId) {

        try{
            if(connection!=null){
                cs = (CallableStatement) connection.prepareCall("call setVerified(?)");
                cs.setInt(1,docId);
                cs.executeQuery();
            }
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void removeDoctor(Context context,int docId){
        if(connection!=null){
            try {
                cs = (CallableStatement) connection.prepareCall("call deleteDoctor(?)");
                cs.setInt(1, docId);
                cs.executeQuery();
                Toast.makeText(context,"Deleted!",Toast.LENGTH_SHORT).show();
            }
            catch(SQLException e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public Doctor getDoctorComments(String docId , Context context){
        String query = "select  u.name , c.pComment from comments c join Patient p on c.pid = p.pid join `User` u on p.pid = u.uid\n" +
                "where c.docId = ?";

        Doctor d = new Doctor(null , null);

//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//
//            stmt.setInt(1,docId);
//
//            ResultSet rs = stmt.executeQuery();
//
//
//            while (rs.next()) {
//                d.setComment(rs.getString("name") , rs.getString("pComment"));
//            }
//
//
//        } catch (Exception e) {
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
        return d;
    }

    public void updateVirtualSchedule(int aptId , Time stime ,Time etime , String day, Context context){
        String query = "update appSchedule set sTime =? , eTime = ? , day= ? where aptId = ?";

        try (PreparedStatement stmt1 = connection.prepareStatement(query)) {

            stmt1.setTime(1,stime);
            stmt1.setTime(2,etime);
            stmt1.setString(3,day);
            stmt1.setInt(4,aptId);
            stmt1.execute();


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Task<Boolean> insertVAppSchedule(Context context,VirtualAppointmentSchedule appointmentSchedule, String docId){

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        try {

            Map<String, Object> vScheduleDocument = new HashMap<>();
            vScheduleDocument.put("day", appointmentSchedule.getDay());
            vScheduleDocument.put("sTime",  appointmentSchedule.getsTime());
            vScheduleDocument.put("eTime", appointmentSchedule.geteTime());


            db.collection("Users").document(docId).collection("vApt Schedules").document().set(vScheduleDocument).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("DbHandler: insertVAppSchedule", "DocumentSnapshot (vSchedule) successfully written " );
                    taskCompletionSource.setResult(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("DbHandler: insertVAppSchedule", "Error writing document", e);
                    taskCompletionSource.setResult(false);
                }
            });


        }
        catch(Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            taskCompletionSource.setResult(false);
        }

        return taskCompletionSource.getTask();
    }

    public Task<Boolean> insertPAppSchedule(Context context,PhysicalAppointmentSchedule physicalAppointmentSchedule,String docId){

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        try{

            Map<String, Object> vScheduleDocument = new HashMap<>();
            vScheduleDocument.put("day", physicalAppointmentSchedule.getDay());
            vScheduleDocument.put("sTime",  physicalAppointmentSchedule.getsTime());
            vScheduleDocument.put("eTime", physicalAppointmentSchedule.geteTime());
            vScheduleDocument.put("clinicName", physicalAppointmentSchedule.getClinicName());
            vScheduleDocument.put("latts", physicalAppointmentSchedule.getLatts());
            vScheduleDocument.put("longs", physicalAppointmentSchedule.getLongs());
            vScheduleDocument.put("assistantPhNum", physicalAppointmentSchedule.getAssistantPhNum());


            db.collection("Users").document(docId).collection("pApt Schedules").document().set(vScheduleDocument).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    taskCompletionSource.setResult(true);
                    Log.d("DbHandler: insertPAppSchedule", "DocumentSnapshot (vSchedule) successfully written " );
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    taskCompletionSource.setResult(false);
                    Log.w("DbHandler: insertPAppSchedule", "Error writing document", e);
                }
            });

        }
        catch (Exception e) {
            taskCompletionSource.setResult(false);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return taskCompletionSource.getTask();
    }


    public ResultSet getAllVirAppointments(Context context,int docId){
        ResultSet resultSet = null;
        try{
            cs = (CallableStatement) connection.prepareCall("call getAllAppointments(?)");
            cs.setInt(1,docId);
            resultSet = cs.executeQuery();
        }
        catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return resultSet;
    }

    public ResultSet getAllPhyAppointments(Context context,int docId){
        ResultSet resultSet=null;
        try{
            cs= (CallableStatement) connection.prepareCall("call getAllPhyAppointments(?)");
            cs.setInt(1,docId);
            resultSet= cs.executeQuery();
        }
        catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return resultSet;
    }

    public ResultSet getAllClinicNames(Context context, int docId) {
        ResultSet resultSet=null;
        try{
            cs = (CallableStatement) connection.prepareCall("call getAllClinicNames(?)");
            cs.setInt(1,docId);
            resultSet= cs.executeQuery();
        }
        catch (Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return  resultSet;
    }


    public Task<Boolean> removeVAptSchedule(String scheduleId, String doctorId, Context context){

        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        try{
            db.collection("Users").document(doctorId).collection("vApt Schedules").document(scheduleId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Db Handler: removeVAptSchedule", "DocumentSnapshot successfully deleted!");
                    taskCompletionSource.setResult(true);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Db Handler: removeVAptSchedule", "Error deleting document", e);
                    taskCompletionSource.setResult(false);
                }
            });


        }catch (Exception e){
            Log.w("Db Handler: removeVAptSchedule", "Error removing Document", e);
            taskCompletionSource.setResult(false);
        }


        return taskCompletionSource.getTask();
    }


    public ArrayList<PhysicalAppointmentSchedule> getPhysicalSchDetails(Context context){

        String query = "select u.name , clinicName, latts, longs  from appschedule ap join phyappschedule p on ap.aptId = p.aptId join `User` u on ap.docId = u.uid";

        ArrayList<PhysicalAppointmentSchedule> lst = new ArrayList<>();

        try ( PreparedStatement stmt = connection.prepareStatement(query) ) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lst.add(new PhysicalAppointmentSchedule(rs.getString("clinicName"), rs.getDouble("latts"), rs.getDouble("longs"), rs.getString("name")));

            }


            return lst;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return lst;
    }

    public ArrayList<Double> getLattsAndLongs(String clinicName , Context context){

        ArrayList<Double> coordinates = new ArrayList<>();
        String query = "select DISTINCT p.latts , p.longs from phyAppSchedule p where clinicName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1,clinicName);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                coordinates.add(rs.getDouble("latts"));
                coordinates.add(rs.getDouble("longs"));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return coordinates;
    }


}

