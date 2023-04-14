package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalServiceImplTest {
    String UUID = "b383ebaa-d716-47c6-94ae-d0a9a37577ae";
    LocalDate birthday = LocalDate.of(1942, 02, 13);
    BloodPressure bloodPressure = new BloodPressure(140,90);
    HealthInfo healthInfo = new HealthInfo(BigDecimal.valueOf(36.6), bloodPressure);
    PatientInfo patientInfo = new PatientInfo(UUID,"John", "Dow", birthday, healthInfo);

    @Test
    void checkBloodPressureNormal() {
        BloodPressure currentBloodPressure = new BloodPressure(140, 90);

        PatientInfoRepository patientInfoRepositoryMock = mock();
        when(patientInfoRepositoryMock.getById(UUID)).thenReturn(patientInfo);
        when(patientInfoRepositoryMock.add(patientInfo)).thenReturn(UUID);
        SendAlertService sendAlertServiceMock = mock();
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, sendAlertServiceMock);
        medicalService.checkBloodPressure(UUID, currentBloodPressure);
        verify(sendAlertServiceMock, times(0)).send(Mockito.anyString());
    }

    @Test
    void checkBloodPressureAbnormal() {
        BloodPressure currentBloodPressure = new BloodPressure(150, 91);

        PatientInfoRepository patientInfoRepositoryMock = mock();
        when(patientInfoRepositoryMock.getById(UUID)).thenReturn(patientInfo);
        when(patientInfoRepositoryMock.add(patientInfo)).thenReturn(UUID);
        SendAlertService sendAlertServiceMock = mock();
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, sendAlertServiceMock);
        medicalService.checkBloodPressure(UUID, currentBloodPressure);
        verify(sendAlertServiceMock, times(1)).send(argumentCaptor.capture());
        assertEquals("Warning, patient with id: b383ebaa-d716-47c6-94ae-d0a9a37577ae, need help",
                argumentCaptor.getValue());
    }

    @Test
    void checkTemperatureNormal() {
        BigDecimal currentTemperature = BigDecimal.valueOf(36.6);

        PatientInfoRepository patientInfoRepositoryMock = mock();
        when(patientInfoRepositoryMock.getById(UUID)).thenReturn(patientInfo);
        when(patientInfoRepositoryMock.add(patientInfo)).thenReturn(UUID);
        SendAlertService sendAlertServiceMock = mock();
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, sendAlertServiceMock);
        medicalService.checkTemperature(UUID, currentTemperature);
        verify(sendAlertServiceMock, times(0)).send(Mockito.anyString());
    }

    @Test
    void checkTemperatureAbnormal() {
        BigDecimal currentTemperature = BigDecimal.valueOf(0);

        PatientInfoRepository patientInfoRepositoryMock = mock();
        when(patientInfoRepositoryMock.getById(UUID)).thenReturn(patientInfo);
        when(patientInfoRepositoryMock.add(patientInfo)).thenReturn(UUID);
        SendAlertService sendAlertServiceMock = mock();
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, sendAlertServiceMock);
        medicalService.checkTemperature(UUID, currentTemperature);
        verify(sendAlertServiceMock, times(1)).send(argumentCaptor.capture());
        assertEquals("Warning, patient with id: b383ebaa-d716-47c6-94ae-d0a9a37577ae, need help",
                argumentCaptor.getValue());
    }
}