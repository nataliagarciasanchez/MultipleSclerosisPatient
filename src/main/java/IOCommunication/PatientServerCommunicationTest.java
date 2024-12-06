/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import BITalino.BITalino;
import BITalino.Frame;
import Menu.Utilities.Utilities;
import POJOs.Bitalino;
import POJOs.Gender;
import POJOs.Patient;
import POJOs.Report;
import POJOs.Role;
import POJOs.SignalType;
import POJOs.Symptom;
import POJOs.User;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to test all the method in the communication
 * @author maipa
 */
public class PatientServerCommunicationTest {
    
    public static PatientServerCommunication com; 
    public static PatientServerCommunication.Send send;
    public static String macAddress = "98:D3:41:FD:4E:E8";
    public static Role role;
    
    public static void main(String[] args) {
        com= new PatientServerCommunication("localhost", 9000);
        com.start();
        send= com.new Send();
        role=new Role();
        //register();
        //register1();
        //register2();
       //register3();
        //register4();
        //login();
        //logout()
        //viewSymptoms();
        //viewPersonalInfo();
        //updateInfo();
        //sendReport();  
    }
    
   
    public static void register() {
        try {
            java.sql.Date dob = Utilities.convertString2SqlDate("31/10/2003");
            Patient maite = new Patient("noelia", "gomez", "05459423F", dob, Gender.FEMALE, "609526931");
            User user=new User("probando@gmail.com", "Password123", role);
            maite.setUser(user);
            
            send.register(maite);
        } catch (ParseException ex) {
            Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void register1() {
    try {
        java.sql.Date dob = Utilities.convertString2SqlDate("31/10/2003");
        Patient patient = new Patient("Noelia", "Gomez", "05459423F", dob, Gender.FEMALE, "609526931");
        User user = new User("noelia.gomez@gmail.com", "Password123", role);
        patient.setUser(user);
        send.register(patient);
    } catch (ParseException ex) {
        Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public static void register2() {
    try {
        java.sql.Date dob = Utilities.convertString2SqlDate("15/05/1990");
        Patient patient = new Patient("Carlos", "Lopez", "12345678Z", dob, Gender.MALE, "617234567");
        User user = new User("carlos.lopez@gmail.com", "SecurePass456", role);
        patient.setUser(user);
        send.register(patient);
    } catch (ParseException ex) {
        Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public static void register3() {
    try {
        java.sql.Date dob = Utilities.convertString2SqlDate("20/11/1985");
        Patient patient = new Patient("Maria", "Fernandez", "87654321A", dob, Gender.FEMALE, "624789012");
        User user = new User("maria.fernandez@gmail.com", "MyPass789", role);
        patient.setUser(user);
        send.register(patient);
    } catch (ParseException ex) {
        Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public static void register4() {
    try {
        java.sql.Date dob = Utilities.convertString2SqlDate("01/01/1995");
        Patient patient = new Patient("David", "Martinez", "56789012B", dob, Gender.MALE, "632987654");
        User user = new User("david.martinez@gmail.com", "DavidPass456", role);
        patient.setUser(user);
        send.register(patient);
    } catch (ParseException ex) {
        Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    
    public static void login(){
       Patient patient=send.login("probando@gmail.com", "Password123");
       patient.getUser().setRole(role);
       System.out.println(patient);
       send.logout();
    }
    
    public static void updateInfo() {
        Patient patient1 = send.login("probando@gmail.com", "Password456");
        System.out.println(patient1);
        User user = patient1.getUser();
        user.setRole(role);
        System.out.println("user\n" + user);
        String newPass = "Password123";
        if (Utilities.isValidPassword(newPass)) {
            user.setPassword(newPass);
            String new_name="Noelia";
            patient1.setName(new_name);
            System.out.println("This is to check if the setter is working correctly: " + user.getPassword());
            send.updateInformation(user, patient1);
        }
        
        Patient patient2 = send.login("probando@gmail.com", "Password123");

    }
    
    public static void viewPersonalInfo(){
        Patient patient = send.login("noelia@gmail.com", "Password123");
        System.out.println(patient);
    }
    
    public static void viewSymptoms(){
        Patient patient = send.login("noelia@gmail.com", "Password123");
        List<Symptom> symptoms=send.getSymptoms();
        ListIterator it=symptoms.listIterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    public static void sendReport(){
        try {
            BITalino bitalinoDevice=new BITalino();
            
            bitalinoDevice.open(macAddress);
            LocalDate r_date=LocalDate.now();
            Date date = null;
            try {
                date = Utilities.convertString2SqlDate2(r_date.toString());
            } catch (ParseException ex) {
                Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            Patient patient=send.login("noelia@gmail.com", "Password456");
            
            
            Bitalino bitalinoEMG=new Bitalino(date,SignalType.EMG);
            List<Frame> emgFrames=bitalinoEMG.storeRecordedSignals(bitalinoDevice, SignalType.EMG);
            bitalinoEMG.setSignalValues(emgFrames, 0);
            System.out.println("Strings EMG:"+bitalinoEMG.getSignalValues());
            
            Bitalino bitalinoECG=new Bitalino(date,SignalType.ECG);
            List<Frame> ecgFrames=bitalinoECG.storeRecordedSignals(bitalinoDevice, SignalType.ECG);
            bitalinoECG.setSignalValues(ecgFrames, 1);
            System.out.println("Strings ECG: "+bitalinoECG.getSignalValues());
            
            List<Bitalino> bitalinos = new ArrayList();
            bitalinos.add(bitalinoEMG);
            bitalinos.add(bitalinoECG);
            List<Symptom> symptoms = new ArrayList();
            symptoms.add(new Symptom("Loss of balance"));
            symptoms.add(new Symptom("Muscle spasms"));
            symptoms.add(new Symptom("Numbness or abnormal sensation in any area"));
            
            Report report=new Report(date,patient,bitalinos,symptoms);
            send.sendReport(report);
            
        } catch (Throwable ex) {
            Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
