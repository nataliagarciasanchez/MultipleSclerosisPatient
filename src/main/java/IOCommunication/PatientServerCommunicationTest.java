/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import BITalino.BITalino;
import BITalino.Frame;
import Menu.Utilities.Utilities;
import POJOs.Bitalino;
import POJOs.Feedback;
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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to test all the method in the communication
 * @author maipa
 */
public class PatientServerCommunicationTest {
    
    public static PatientServerCommunication com; 
    public static PatientServerCommunication.Send send;
    public static PatientServerCommunication.Receive receive;
    public static String macAddress = "98:D3:41:FD:4E:E8";
    public static Role role;
    private static final float EMG_MIN = 0.1f;
    private static final float EMG_MAX = 5.0f;
         // Valores normales para ECG según género
    private static final float ECG_MIN_MALE = 0.6f;
    private static final float ECG_MAX_MALE = 3.5f;

    private static final float ECG_MIN_FEMALE = 0.4f;
    private static final float ECG_MAX_FEMALE = 2.8f;
    
    public static void main(String[] args) {
        com= new PatientServerCommunication("localhost", 9000);
        com.start();
        send= com.new Send();
        receive=com.new Receive();
        role=new Role();
        //register();
        //register1();
        //register2();
        //register3();
        //register4();
        //login();
        //logout();
        //viewSymptoms();
        //viewPersonalInfo();
        //updateInfo();
        //sendReport(); 
         // sendCompleteReport1();
       // sendCompleteReport();
       //testBitalino();
        //viewFeedbacks();
    }
    
   
    public static void register() {
        try {
            java.sql.Date dob = Utilities.convertString2SqlDate("31/10/2003");
            Patient maite = new Patient("maite", "gomez", "53993542F", dob, Gender.FEMALE, "609526931");
            User user=new User("maite@gmail.com", "Password123", role);
            maite.setUser(user);
            
            send.register(maite);
        } catch (ParseException ex) {
            Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void register1() {
        try {
            java.sql.Date dob = Utilities.convertString2SqlDate("31/10/2003");
            Patient patient = new Patient("Noelia", "Gomez", "51258525N", dob, Gender.FEMALE, "609526931");
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
            Patient patient = new Patient("Maria", "Fernandez", "51258523X", dob, Gender.FEMALE, "624789012");
            User user = new User("maria.fernandez@gmail.com", "MyPassword789", role);
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
       Patient patient=send.login("maite@gmail.com", "Password123");
       patient.getUser().setRole(role);
       System.out.println(patient);
       //send.logout();
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
            //send.updateInformation(user, patient1);
        }
        
        Patient patient2 = send.login("probando@gmail.com", "Password123");

    }
    
    public static void viewPersonalInfo(){
        Patient patient = send.login("noelia@gmail.com", "Password123");
        System.out.println(patient);
    }
    
    public static void viewSymptoms(){
        Patient patient = send.login("noelia@gmail.com", "Password123");
        List<Symptom> symptoms=receive.getSymptoms();
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
            Patient patient=send.login("noelia.gomez@gmail.com", "Password123");
            
            
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
            List<Symptom> symptoms = receive.getSymptoms();            
            List <Symptom> symptomsSend = new ArrayList<>();
            symptomsSend.add(symptoms.get(1));
            symptoms.add(new Symptom("Loss of balance"));
            symptoms.add(new Symptom("Muscle spasms"));
            symptoms.add(new Symptom("Numbness or abnormal sensation in any area"));
            
            Report report=new Report(date,patient,bitalinos,symptoms);
            send.sendReport(report);
            
        } catch (Throwable ex) {
            Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendCompleteReport1() {

        try {
            LocalDate r_date = LocalDate.now();
            Date date = Utilities.convertString2SqlDate2(r_date.toString());

            Patient patient = send.login("noelia.gomez@gmail.com", "Password123");

            Bitalino bitalinoEMG = new Bitalino(date, SignalType.EMG);
            List<Frame> emgFrames = generateSignalWithinRange(SignalType.EMG, EMG_MIN, EMG_MAX, 60000); // 60000 muestras para 1 minuto
            bitalinoEMG.setSignalValues(emgFrames, 0);

            float ecgMin = patient.getGender() == Gender.MALE ? ECG_MIN_MALE : ECG_MIN_FEMALE;
            float ecgMax = patient.getGender() == Gender.MALE ? ECG_MAX_MALE : ECG_MAX_FEMALE;

            Bitalino bitalinoECG = new Bitalino(date, SignalType.ECG);
            List<Frame> ecgFrames = generateSignalWithinRange(SignalType.ECG, ecgMin, ecgMax, 60000); // 60000 muestras para 1 minuto
            bitalinoECG.setSignalValues(ecgFrames, 1);

            List<Bitalino> bitalinos = new ArrayList<>();
            bitalinos.add(bitalinoEMG);
            bitalinos.add(bitalinoECG);

            List<Symptom> symptoms = receive.getSymptoms();
            List<Symptom> symptomsSend = new ArrayList<>();
            symptomsSend.add(symptoms.get(1));
            symptomsSend.add(symptoms.get(2));
            symptomsSend.add(symptoms.get(3));
            symptomsSend.add(symptoms.get(4));
            symptoms.add(new Symptom("Loss of balance"));
            symptoms.add(new Symptom("Numbness or abnormal sensation in any area"));

            Report report = new Report(date, patient, bitalinos, symptomsSend);
            send.sendReport(report);
            System.out.println("Report sent successfully!");

        } catch (ParseException ex) {
            System.out.println("Error while sending the report.");
        } catch (Throwable ex) {
            Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método auxiliar para generar señales dentro de un rango específico
    private static List<Frame> generateSignalWithinRange(SignalType signalType, float min, float max, int numSamples) {
        List<Frame> frames = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numSamples; i++) {
            Frame frame = new Frame(); 
            int[] channels = new int[6]; 

            float value = min + random.nextFloat() * (max - min);
            channels[0] = (int) (value * 1000); 

            // Asignar canales al frame
            frame.analog = channels;
            frames.add(frame);
        }

        return frames;
    }


    public static void sendCompleteReport() {
        try {
            LocalDate r_date = LocalDate.now();
            Date date = Utilities.convertString2SqlDate2(r_date.toString());

            Patient patient = send.login("noelia.gomez@gmail.com", "Password123");

            Bitalino bitalinoEMG = new Bitalino(date, SignalType.EMG);
            List<Frame> emgFrames = generateRandomSignal(SignalType.EMG, 60000); // 60000 muestras para 1 minuto
            bitalinoEMG.setSignalValues(emgFrames, 0);

            Bitalino bitalinoECG = new Bitalino(date, SignalType.ECG);
            List<Frame> ecgFrames = generateRandomSignal(SignalType.ECG, 60000); // 60000 muestras para 1 minuto
            bitalinoECG.setSignalValues(ecgFrames, 1);

            List<Bitalino> bitalinos = new ArrayList<>();
            bitalinos.add(bitalinoEMG);
            bitalinos.add(bitalinoECG);

            List<Symptom> symptoms = receive.getSymptoms();
            
            List <Symptom> symptomsSend = new ArrayList<>();
            symptomsSend.add(symptoms.get(1));
            symptomsSend.add(symptoms.get(2));
            symptomsSend.add(symptoms.get(3));
            symptomsSend.add(symptoms.get(4));
            symptoms.add(new Symptom("Loss of balance"));
            symptoms.add(new Symptom("Numbness or abnormal sensation in any area"));

            Report report = new Report(date, patient, bitalinos, symptomsSend);
            send.sendReport(report);
            System.out.println("Report sent successfully!");

        } catch (ParseException ex) {
            System.out.println("Error while sending the report.");
        } catch (Throwable ex) {
            Logger.getLogger(PatientServerCommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static List<Frame> generateRandomSignal(SignalType signalType, int numSamples) {
        List<Frame> frames = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numSamples; i++) {
            Frame frame = new Frame();
            frame.seq = i % 16; // Secuencia del frame (0-15)

            if (signalType == SignalType.EMG) {
                // Generar valores simulados de EMG en rango típico (0-1000)
                frame.analog[0] = random.nextInt(1000);
            } else if (signalType == SignalType.ECG) {
                // Generar valores simulados de ECG en rango típico (300-1200)
                frame.analog[1] = 300 + random.nextInt(900);
            }

            frames.add(frame);
        }

        return frames;
    }

    public static void viewFeedbacks(){
        Patient patient = send.login("noelia.gomez@gmail.com", "Password123");
        List<Feedback> feedbacks=receive.viewFeedbacks(patient);
        ListIterator <Feedback> it=feedbacks.listIterator();
        //prints list of all feedbacks of that patient (in chronological order)
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }


}
