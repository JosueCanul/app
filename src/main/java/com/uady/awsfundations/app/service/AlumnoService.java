package com.uady.awsfundations.app.service;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uady.awsfundations.app.dto.AlumnoDto;
import com.uady.awsfundations.app.mapper.AlumnoMapper;
import com.uady.awsfundations.app.model.Alumno;
import com.uady.awsfundations.app.repository.AlumnoRepository;

import jakarta.persistence.EntityNotFoundException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class AlumnoService {

    @Value("${aws.sns.topic.arn}")
    private String topicArn;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private AlumnoMapper alumnoMapper;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private SnsClient snsClient;

    public List<Alumno> findAll(){
        return alumnoRepository.findAll();
    }

    public Alumno findById(Integer id){
        return alumnoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Alumno save(AlumnoDto alumnoDto) {
        Alumno alumnoEntity = new Alumno();
        this.alumnoMapper.toEntity(alumnoEntity, alumnoDto);
        return alumnoRepository.save(alumnoEntity);
    }

    public Alumno update(Integer id, AlumnoDto alumnoDto) {
        Alumno alumnoToUdate = this.findById(id);
        this.alumnoMapper.toEntity(alumnoToUdate, alumnoDto);
        return alumnoRepository.save(alumnoToUdate);
    }

    public void delete(Integer id) {
        Alumno existingAlumno = this.findById(id);
        alumnoRepository.delete(existingAlumno);
    }
    
    public String uploadProfilePicture(Integer id, MultipartFile profileImage) throws IOException {
        Alumno alumno = this.findById(id);
        String ext = FilenameUtils.getExtension(profileImage.getOriginalFilename());
        String Key = "profile_image_" + alumno.getId() + ext;
        String profileImgUrl = s3Service.uploadFile(profileImage, Key);
        alumno.setPerfilImgURL(profileImgUrl);
        alumnoRepository.save(alumno);
        return profileImgUrl;
    }

    public String notificarCalificaciones(Integer alumnoId) {
        Alumno alumno = this.findById(alumnoId); 
        String nombres = alumno.getNombres();
        String apellidos = alumno.getApellidos();
        Double promedio = alumno.getPromedio();
        String mensaje = String.format(
            "Reporte de Calificaciones\n" +
            "-------------------------\n" +
            "Alumno: %s %s\n" +
            "Calificación Final: %.2f\n" +
            "-------------------------\n" +
            "Enviado desde el sistema de la UADY.",
            nombres, apellidos, promedio
        );

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(mensaje)
                .subject("Alerta Académica: " + nombres + " " + apellidos) // Asunto del correo
                .build();

        PublishResponse result = snsClient.publish(request);

        return result.messageId();
    }
}
